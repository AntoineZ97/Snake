package snake;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	
	private Stage currentStage = null;
	private static Stage stageGame = null;
	private Canvas canvasGame = null;
	private AnimationTimer animationTimer = null;
	private static int score = 0;
	private int xFruit = 0;
	private int yFruit = 0;
	private java.util.Random random = new java.util.Random(System.currentTimeMillis());
	private  List<Snake> snake  = new ArrayList<Snake>();
	private static Boolean paused = false; // ON met les deux variables en statiques pour que l'animationTimer puisse récuperer la modification
	private static Printer printer = null;
	private enum direction {
		_RIGHT,
		_LEFT,
		_UP,
		_DOWN,
	};
	private static direction _direction = direction._RIGHT; // d'une des deux variables en dehors de lui

	
	public Controller() {
	}

	@FXML
	private void initialize() {
	}
	
	
	public void launchGame() throws Exception {
		Stage dialogStage = new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("gameLayout.fxml"));
		AnchorPane gameLayout= (AnchorPane) loader.load();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(currentStage);
			
		Scene scene = new Scene(gameLayout);
		dialogStage.setTitle("Snake");
		dialogStage.setScene(scene);
		dialogStage.show();
		stageGame = dialogStage;
		canvasGame = (Canvas) scene.lookup("#canvasGame");
		
		paused = false;
		score = 0;
		printer = new Printer(canvasGame.getGraphicsContext2D(), (Text) scene.lookup("#textLost"), (Text) scene.lookup("#textLost"));
		createSnake();
		createFruit();
		loopGame();
	}
	
	@FXML
	private void createSnake() {
		snake.clear();
		Snake bloc1 = new Snake(200,150); // Création du premier bloc en passant en paramètres x et y où l'on veut qu'il soit affiché
		Snake bloc2 = new Snake(190,150);// Création du deuxième  bloc en passant en paramètres x et y où l'on veut qu'il soit affiché
		snake.add(bloc1); // Ajout du bloc 1 au tableau de classe
		snake.add(bloc2); // Ajout du bloc 2 au tableau de classe
	}
	
	// pour chaque entrée du clavier, on associe une valeur à la variable direction pour simplifier le programme
	@FXML
	public void keyPressedEvent(KeyEvent event) {
		if (event.getCode().equals(KeyCode.RIGHT))
			_direction = direction._RIGHT;
		else if (event.getCode().equals(KeyCode.LEFT))
			_direction = direction._LEFT;
		else if (event.getCode().equals(KeyCode.UP))
			_direction = direction._UP;
		else if (event.getCode().equals(KeyCode.DOWN))
			_direction = direction._DOWN;
	}
	
	@FXML
	private void loopGame() {
		// (URL:https://docs.oracle.com/javase/8/javafx/api/javafx/animation/AnimationTimer.html) 
		if (animationTimer == null) {
		animationTimer = new AnimationTimer() {
			long oldDate = 0;
			@Override
			public void handle(long now) {
				if (now - oldDate > 1000000000 / (5 + score )) { // on compare le temps sauvegardé avec le temps actuel pour pouvoir mettre un délai avant de bouger le serpent
					oldDate = now; // On sauvegarde le temps actuel
					move();
				}
			}
		};
		}		
		animationTimer.start();
	}
	
	@FXML
	private void move() {
		// on met à jour les positions des blocs
		if (paused == false) {
		for (int i = snake.size() - 1; i > 0; i--) {
			 snake.get(i).setX(snake.get(i - 1).getX());
			 snake.get(i).setY(snake.get(i - 1).getY());
		}
		// le snake va à droite
		if (_direction == direction._RIGHT) {
			snake.get(0).changeX(10);
		}
		// le snake va à gauche
		if (_direction == direction._LEFT) {
			snake.get(0).changeX(-10);
		}
		// le snake va en haut
		if (_direction == direction._UP) {
			snake.get(0).changeY(-10);
		}
		// le snake va en bas
		if (_direction == direction._DOWN) {
			snake.get(0).changeY(10);
		}
		}
		printer.printMap(snake, xFruit, yFruit);;
		collisionSnake();
	}
	
	@FXML
	private void collisionSnake() {
		int i = 0;
		// on parcout le tableau pour vérifier que le 1er bloc n'a pas la même position qu'un des blocs suivants
		for (Snake blocs : snake) {
			if (snake.get(0).getX() == blocs.getX() && snake.get(0).getY() == blocs.getY() && i != 0) // Si oui, cela veut dire que le serpent s'est mangé lui-même
				pauseGame();
			i++;
		}
		// on vérifie que le 1er bloc ne touche pas les bords du jeu
		if (snake.get(0).getX() + 10 > 400 || snake.get(0).getX() < 0)
			pauseGame();
		if (snake.get(0).getY() + 10 > 300 || snake.get(0).getY() <0 )
			pauseGame();
		// on vérifie si le 1er bloc a la même position que le fruit
		if (snake.get(0).getX() == xFruit && snake.get(0).getY() == yFruit) { // si oui, cela veut dire que le serpent a mangé le fruit
			createFruit();
			addBlocSnake();
		}
	}
	
	@FXML
	private void addBlocSnake() {
		// On ajoute à la liste de classes un nouveau bloc à la position 0 puis on décale le reste dans le tableau
		List<Snake> tmpSnake  = new ArrayList<Snake>();
		// on crée une liste temporaire pour recréer la vraie liste
		Snake newBloc = new Snake(snake.get(0).getX(), snake.get(0).getY());
		tmpSnake.add(newBloc);
		for (Snake blocs : snake) {
			tmpSnake.add(blocs);
		}
		snake.clear();
		snake = tmpSnake; // on ajoute la liste temporaire à la variable globale
		updateScore();
	}

	@FXML
	private void createFruit() {	
		xFruit = random.nextInt(400);
		yFruit = random.nextInt(300);
		// on arrondit la position du fruit pour qu'il tombe toujours sur un multiple de 10, ce qui permet au snake de toucher correctement le fruit
		if (xFruit < 10)
			xFruit = 10;
		if (yFruit < 10)
			yFruit = 10;
		xFruit = Math.round(xFruit / 10) * 10;
		yFruit = Math.round(yFruit / 10) * 10;
	}
	
	@FXML
	private void updateScore() {
		score = score + 1;
		printer.printScore(score);
	}
	
	@FXML
	private void pauseGame() {
		paused = true;
		animationTimer.stop();
		printer.printDefeat();
	}
	
	@FXML
	public void closeGame() {
		stageGame.close();
	}
	
	public void setStage(Stage stage) {
		currentStage = stage;
	}
}
