package logic;

/**
 * Einstellungen für das Spiel "Snake"
 * Enthällt Konstanten zur Steuerung, zur Wahl des Labyrinthes
 * und die Geschwindigkeit etc.
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.awt.Color;

public class Settings {
	
	/*
	 * Farben fŸr die Schlange etc.
	 */
	public static final Color EMPTY_COLOR = Color.white;
	public static final Color SNAKE_COLOR = Color.gray;
	public static final Color FOOD_COLOR  = Color.orange;
	public static final Color WALL_COLOR  = Color.black;
	
	/*
	 * Geschwindigkeit der Schlange
	 * leicht:  180 ms
	 * normal:  120 ms
	 * schwer:   80 ms
	 * wahnsinn: 50 ms
	 */
	public static final int EASY	= 180;
	public static final int NORMAL	= 100;
	public static final int HARD	= 50;
	public static final int INSANE	= 30;
	private int currentSpeed;
	
	/*
	 * Steuerung der Schlange per Tastatur
	 * (standard: w,a,s,d)
	 */
	private char up    = 'w';
	private char down  = 's';
	private char left  = 'a';
	private char right = 'd';
	private char pause = 'p';
	
	/*
	 * Spielmodus
	 * 0: Classic
	 * 1: Labyrinth 1
	 * 2: Labyrinth 2
	 * 3: Labyrinth 3
	 */
	public static final int CLASSIC = 0;
	public static final int LABYRINTH_A = 1;
	public static final int LABYRINTH_B = 2;
	public static final int LABYRINTH_C = 3;
	public static final int OWN_LABYRINTH = 4;
	private int modus;
	
	
	
	/*
	 * ======================================
	 * K O N S T R U K T O R E N
	 * 
	 */
	
	public Settings() {
		currentSpeed = NORMAL;
		modus = CLASSIC;
	}
	
	public Settings(int speed, int modus) {
		this.currentSpeed = speed;
		this.modus = modus;
	}
	
	
	
	/*
	 * ======================================
	 * G E T T E R  &  S E T T E R
	 * 
	 */

	public char getUp() {
		return up;
	}

	public char getDown() {
		return down;
	}

	public char getLeft() {
		return left;
	}

	public char getRight() {
		return right;
	}

	public char getPause() {
		return pause;
	}

	public int getCurrentSpeed() {
		return currentSpeed;
	}

	public int getModus() {
		return modus;
	}

	public void setUp(char up) {
		this.up = up;
	}

	public void setDown(char down) {
		this.down = down;
	}

	public void setLeft(char left) {
		this.left = left;
	}

	public void setRight(char right) {
		this.right = right;
	}

	public void setPause(char pause) {
		this.pause = pause;
	}

	public void setCurrentSpeed(int speed) {
		this.currentSpeed = speed;
	}

	public void setModus(int modus) {
		this.modus = modus;
	}
}
