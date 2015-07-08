package logic;

/**
 * "Gehirn" des Spieles.
 * Hier läuft die komplette Logik ab... In welche Richtung die Schlange laufen soll,
 * wann das Spiel zu Ende ist etc pp.
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.util.LinkedList;


public class Game {
	
	public static final int ROWS = 17;
	public static final int COLS = 17;
	
	public static final int EMPTY_FIELD = 0;
	public static final int WALL_FIELD  = 1;
	public static final int SNAKE_FIELD = 2;
	public static final int FOOD_FIELD  = 3;
	
	private static int[][] ownPlayground;
	
	private Settings settings;
	private int[][] playground;
	private LinkedList<SnakePart> snake;
	private int newHeadRow, newHeadCol;
	private int score;
	
	private char direction;
	private char oppositeDirection;
	
	
	/*
	 * ==============================================
	 *   K O N S T R U K T O R   &   C O
	 */

	/**
	 * Es wird ein Spiel mit den übergebenen Regeln erstellt
	 * 
	 * @param settings
	 *            Regeln (Geschwindigkeit, Steuerung etc.)
	 */
	public Game(Settings settings) {
		this.settings = settings;
		initGame();
	}
	
	/**
	 * Das Spielfeld (abhängig des gewählten Labyrinths)
	 * inklusive der Schlange werden erstellt
	 */
	private void initGame() {
		
		score = 0;
		
		if (settings.getModus() != Settings.OWN_LABYRINTH)
			playground = new int[Game.ROWS][Game.COLS];
		
		switch (settings.getModus()) {
		case Settings.CLASSIC:
			initClassic();
			initSnake(Game.ROWS / 2, Game.COLS / 2);
			break;

		case Settings.LABYRINTH_A:
			initClassic();
			initLabyrinthA();
			initSnake(Game.ROWS / 2, Game.COLS / 2);
			break;

		case Settings.LABYRINTH_B:
			initClassic();
			initLabyrinthB();
			initSnake(Game.ROWS / 2, Game.COLS / 2);
			break;

		case Settings.LABYRINTH_C:
			initLabyrinthC();
			initSnake((Game.ROWS / 2) - 4, (Game.COLS / 2) - 4);
			break;
			
		case Settings.OWN_LABYRINTH:
			initOwnLabyrinth();
			break;
		}
		
		setNewMeal(-1, -1);
	}

	/**
	 * Rahmen um das komplette Spielfeld
	 */
	private void initClassic() {
		
		// Reihe oben
		for (int col = 0; col < COLS; col++)
			playground[0][col] = WALL_FIELD;
		// Reihe unten
		for (int col = 0; col < COLS; col++)
			playground[ROWS-1][col] = WALL_FIELD;
		// Spalte links
		for (int row = 0; row < ROWS; row++)
			playground[row][0] = WALL_FIELD;
		// Spalte rechts
		for (int row = 0; row < Game.ROWS; row++)
			playground[row][COLS-1] = WALL_FIELD;
	}

	/**
	 * In jeder Ecke ist nochmal ein Eck
	 * und in der Mitte ein Durchgang
	 */
	private void initLabyrinthA() {
		
		// "L" links oben
		for (int i=2; i <= 4; i++ )
			// reihe
			playground[2][i] = WALL_FIELD;
		for (int i=3; i <= 4; i++)
			// spalte
			playground[i][2] = WALL_FIELD;
		
		// "L" rechts oben
		for (int i=12; i <= 14; i++)
			// reihe
			playground[2][i] = WALL_FIELD;
		for (int i=3; i <= 4; i++)
			// spalte
			playground[i][14] = WALL_FIELD;
		
		// "L" links unten
		for (int i=2; i <= 4; i++ )
			// reihe
			playground[14][i] = WALL_FIELD;
		for (int i=12; i <= 13; i++)
			// spalte
			playground[i][2] = WALL_FIELD;
		
		// "L" rechts unten
		for (int i=12; i <= 14; i++ )
			// reihe
			playground[14][i] = WALL_FIELD;
		for (int i=12; i <= 13; i++)
			// spalte
			playground[i][14] = WALL_FIELD;
		
		// Durchgang in der Mitte des Spielfeldes
		for (int i=6; i <=10; i++)
			playground[7][i] = WALL_FIELD;
		for (int i=6; i <=10; i++)
			playground[9][i] = WALL_FIELD;
	}

	/**
	 * Zwei Diagonalen nach rechts oben mitten auf dem Spielfeld
	 */
	private void initLabyrinthB() {
		
		// 1. Diagonale (nach rechts oben)
		for (int row = 8, col = 2; row >= 2; row--, col++)
			playground[row][col] = WALL_FIELD;
		
		// 2. Diagonale (auch nach rechts oben)
		for (int row = 14, col = 8; row >= 8; row--, col++)
			playground[row][col] = WALL_FIELD;
	}

	/**
	 * Kein Rahmen,
	 * der vierte Quadrant (rechts unten) ist abgegrenzt.
	 */
	private void initLabyrinthC() {
		
		for (int row = ROWS / 2; row < ROWS; row++)
			// mittlere spalte
			playground[row][COLS / 2] = WALL_FIELD;
		
		for (int col = COLS / 2; col < COLS; col++)
			// mittlere Reihe
			playground[ROWS / 2][col] = WALL_FIELD;
		
	}
	
	/**
	 * Initialisiert ein vom Benutzer erstelltes Labyrinth. Die Schlange ist
	 * bereits auf dem Feld, wird gesucht und der Liste der Schlange hinzugefŸgt
	 */
	private void initOwnLabyrinth() {
		
		// setze das Spielfeld
		playground = copyArray(ownPlayground);
		
		// von der rechten Spalte nach link nach dem Schlangenkopf suchen
		// Schlange ist zwei Felder groß und horizontal ausgerichtet
		boolean snakeFound = false;
		int row = 0;
		int col = COLS - 1;
		try {
			while(!snakeFound) {
				
				if (playground[row][col] == SNAKE_FIELD) {
					// Schlange gefunden
					snake = new LinkedList<SnakePart>();
					snake.addFirst(new SnakePart(row, col));
					snake.addLast(new SnakePart(row, col-1));
					newHeadRow = row;
					newHeadCol = col;
					snakeFound = true;
				}
				
				if (row + 1 == ROWS) {
					// nächste Spalte prüfen
					row = 0;
					col--;
				}
				else
					// Reihe erhöhen
					row++;
				
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// Schlange konnte nichtgefunden werden
			e.printStackTrace();
		}
	}

	/**
	 * Schlange wird erstellt und auf dem Spielfeld registriert
	 * 
	 * @param headRow
	 *            Reihe des Kopfes der Schlange
	 * @param headCol
	 *            Spalte des Kopfes der Schlange
	 */
	private void initSnake(int headRow, int headCol) {
		
		snake = new LinkedList<SnakePart>();
		SnakePart head = new SnakePart(headRow, headCol);
		SnakePart tail = new SnakePart(headRow, headCol-1);
		snake.addLast(head);
		snake.addLast(tail);
		
		newHeadRow = headRow;
		newHeadCol = headCol;
		
		playground[headRow][headCol] = SNAKE_FIELD;
		playground[headRow][headCol-1] = SNAKE_FIELD;	
	}
	
	/**
	 * Kopiert den übergebenen Array und gibt den neuen zurück
	 */
	private int[][] copyArray(int[][] array) {
		
		int rows = array.length;
		int cols = array[0].length;
		int[][] newArray = new int[rows][cols];
		
		for (int i=0; i < rows; i++)
			for (int j=0; j < cols; j++)
				newArray[i][j] = array[i][j];
		
		return newArray;		
	}

	
	
	
	/*
	 * ==============================================
	 *   S P I E L - L O G I K
	 */
	
	/**
	 * @return true falls das Spiel beendet ist, ansonsten false
	 */
	public boolean isGameOver() {
		
		setNewHead();
		
		if (playground[newHeadRow][newHeadCol] == WALL_FIELD)
			// Kopf knallt an eine Wand
			return true;
		
		else if (playground[newHeadRow][newHeadCol] == SNAKE_FIELD)
			// Schlange beist sich selbst
			return true;
			
		else
			// ansonsten läuft das Spiel weiter
			return false;
	}

	/**
	 * Erzeugt den neuen Kopf der Schlange abhängig der Richtung
	 * 
	 * @param currentHead
	 *            Aktueller Kopf und dessen Koordinaten werden benötigt um den
	 *            Ort des neuen Kopfes zu berechen
	 */
	private void setNewHead() {
		
		if (direction == settings.getUp()) {
			if (newHeadRow - 1 >= 0)
				// Schlange bewegt sich ein Feld nach oben
				newHeadRow--;
			else
				// Schlange kommt unten wieder raus
				newHeadRow = ROWS - 1;	
		}
		else if (direction == settings.getLeft()) {
			if (newHeadCol - 1 >= 0)
				// Schlange bewegt sich ein Feld nach links
				newHeadCol--;
			else
				// Schlange kommt rechts wieder raus
				newHeadCol = COLS - 1;
		}
		else if (direction == settings.getDown()) {
			if (newHeadRow + 1 < ROWS)
				// Schlange bewegt sich ein Feld nach unten
				newHeadRow++;
			else
				// Schlange kommt oben wieder raus
				newHeadRow = 0;
		}
		else if (direction == settings.getRight()) {
			if (newHeadCol + 1 < COLS)
				// Schlange bewegt sich ein Feld nach rechts
				newHeadCol++;
			else
				// Schlange kommt links wieder raus
				newHeadCol = 0;
		}
		
		snake.addFirst(new SnakePart(newHeadRow, newHeadCol));
	}
	
	/**
	 * Platziert eine neue Mahlzeit auf das Spielfeld
	 */
	private void setNewMeal(int newHeadRow, int newHeadCol) {
		
		int row, col;
		do {
			// zufällige Reihe / Spalte suchen
			// bis ein freies Feld gefunden wurde
			row = (int) (Math.random() * (ROWS - 1));
			col = (int) (Math.random() * (COLS - 1));
	
		} while ((row == newHeadRow && col == newHeadCol)
				|| playground[row][col] == SNAKE_FIELD
				|| playground[row][col] == WALL_FIELD);
	
		// registriere auf dem Spielfeld
		playground[row][col] = FOOD_FIELD;
	}
	
	/**
	 * Der nächste Schritt des Spieles wird berechnet
	 */
	public void nextStep() {
		
		// Koordinaten des aktuellen Schwanzes speichern
		// um dieses Feld wieder zurückzusetzen
		SnakePart oldTail = snake.getLast();
		int oldTailRow = oldTail.getRow();
		int oldTailCol = oldTail.getCol();
		// und lösche den Schwanz
		snake.removeLast();
		
		if (playground[newHeadRow][newHeadCol] == Game.FOOD_FIELD) {
			// Falls eine Mahlzeit genommen wurde
			// vergrößere die Schlange und erzeuge eine neue Mahlzeit
			snake.addLast(new SnakePart(oldTailRow, oldTailCol));
			setNewMeal(newHeadRow, newHeadCol);
			score++;
			
		}
		
		// setze den neuen Kopf
		playground[newHeadRow][newHeadCol] = SNAKE_FIELD;
		// lösche den alten Schwanz
		playground[oldTailRow][oldTailCol] = EMPTY_FIELD;
		// (eine mögliche Vergrößerung der Schlange wird erst im nächsten Schritt gedruckt)
		
	}
	
	/**
	 * @return	true wenn das Spiel pausiert ist, ansonsten false
	 */
	public boolean isPaused() {
		if (direction == settings.getPause())
			return true;
		else
			return false;
	}
	
	/**
	 * Setzt die entgegengesetzte Richtung.
	 * Falls diese nun gedrückt wird, läuft die Schlange normal weiter
	 * (soe beist sich nicht selbst)
	 */
	private void setOppositeDirection() {
		
		if (direction == settings.getDown())
			oppositeDirection = settings.getUp();
		
		else if (direction == settings.getUp())
			oppositeDirection = settings.getDown();
		
		else if (direction == settings.getLeft())
			oppositeDirection = settings.getRight();
		
		else if (direction == settings.getRight())
			oppositeDirection = settings.getLeft();
	}
	
	
	
	
	/*
	 * ==============================================
	 *   G E T T E R   &   S E T T E R
	 */
	
	/**
	 * Setzt das vom Benutzer erstellte Labyrinth
	 */
	public static void setOwnPlayground(int[][] array) {
		ownPlayground = array;
	}
	
	/**
	 * Die neue Richtung der Schlange wird gesetzt
	 * (sofern es nicht die entgegengesetzte Richtung der aktuellen ist)
	 */
	public void setDirection(char direction) {
		if (direction != oppositeDirection) {
			this.direction = direction;
			setOppositeDirection();
		}
	}
	
	/**
	 * @return	Spielfeld
	 */
	public int[][] getPlayground() {
		return playground;
	}
	
	/**
	 * @return	Anzahl gegessener Mahlzeiten
	 */
	public int getScore() {
		return score;
	}
	
	public static void printArray(int[][] array) {
		
		for (int i=0; i < array.length; i++) {
			for (int j=0; j < array[i].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
		
	}
}
