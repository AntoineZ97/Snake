package snake;

/*
 * Class snake qui permet de stocker un bloc du serpent avec ses positions X et Y
 */

public class Snake {
	private int _x;
	private int _y;
	
	public Snake(int x, int y) {
		this._x = x;
		this._y = y;
	}
	
	public int getX() {
		return this._x;
	}
	
	public int getY() {
		return this._y;
	}
	
	public void setX(int newValue) {
		this._x = newValue;
	}
	
	public void setY(int newValue) {
		this._y = newValue;
	}
	
	public void changeX(int valueToAdd) {
		this._x += valueToAdd;
	}
	
	public void changeY(int valueToAdd) {
		this._y += valueToAdd;
	}
}
