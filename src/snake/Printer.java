package snake;

import snake.Snake;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Printer {
	
	private GraphicsContext _map;
	private Text _textField;
	private Text _textScore;
	
	//Couleurs par défaut.
	private Color _colorMap = Color.BLACK;
	private Color _colorSnake = Color.GREEN;
	private Color _colorFruit = Color.RED;
	
	private String _defeatString = "TU AS PERDU !";
	private String _scoreString = "Score : ";
	
	Printer(GraphicsContext map, Text textField, Text textScore){
		this._map = map;
		this._textField = textField;
		this._textScore = textScore;
	}
	// 2e constructeur pour set differentes couleurs
	Printer(GraphicsContext map, Color colorMap, Color colorSnake, 
			Color colorFruit, Text textField, Text textScore){
		this._map = map;
		this._colorMap = colorMap;
		this._colorSnake = colorSnake;
		this._colorFruit = colorFruit;
		this._textField = textField;
		this._textScore = textScore;
	}
	
	private void printSnake(List<Snake> snake) {
		this._map.setFill(this._colorSnake);
		 // on parcout le tableau pour récuperer chaque classe qui constitue le serpent et on récupère grâce à elles la position x et y
		for (Snake bloc : snake) {
			this._map.fillRect(bloc.getX(), bloc.getY(), 10, 10); // on crée un cube de 10x10 à la position récupérée pour créer le corps du serpent
		}
	}
	
	private void printFruit(int xFruit, int yFruit) {
		this._map.setFill(this._colorFruit);
		this._map.fillRect(xFruit , yFruit , 10, 10);
	}
	
	public void printMap(List<Snake> snake, int xFruit, int yFruit)
	{
		 this._map.setFill(this._colorMap);
		 this._map.fillRect(0, 0, 400, 300);
		 printSnake(snake);
		 printFruit(xFruit, yFruit);
	}
	
	public void printScore(int score) {
		this._textScore.setFill(Color.BLACK);
		this._textScore.setText(this._scoreString + Integer.toString(score));
	}
	
	public void printDefeat() {
		this._textField.setFill(Color.WHITE);
		this._textField.setText(this._defeatString);
	}
	
}
