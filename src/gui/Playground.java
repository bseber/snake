package gui;

/**
 * Das eigentliche Spielfeld der Schlange.
 * Von Game.class wird sich der Status des Spiels besorgt
 * und dementsprechend dem Benutzer auf den Bildschirm gebracht.
 * 
 * Jedesmal wenn ein neues Spiel gestartet wird, wird diese Klasse neu instanziiert.
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import logic.Game;
import logic.Settings;

public class Playground extends Thread implements Runnable {

	public static final Dimension DIMENSION = new Dimension(18, 18);

	JLabel infoLabel;
	Game game;
	int speed;

	private JPanel playgroundPanel;
	private JPanel[][] playgroundArray;

	/**
	 * Konstruktor erstellt das Spielfeld
	 * 
	 * @param game
	 *            Logische Spiel Klasse zum Steuern des Ablaufes
	 * @param speed
	 *            Geschwindigkeit der Schlange (Settings.EASY, Settings.NORMAL,
	 *            Settings.HARD, Settings.INSANE)
	 * @param infoLabel
	 *            Info Label vom Hauptfenster um den Spielstatus zu drucken
	 */
	Playground(Game game, int speed, JLabel infoLabel) {
		this.infoLabel = infoLabel;
		this.speed = speed;
		this.game = game;
		playgroundPanel = initPlayground();
	}

	/**
	 * Initiiert das Spielfeld und gibt es zurück
	 */
	private JPanel initPlayground() {

		playgroundArray = new JPanel[Game.ROWS][Game.COLS];
		JPanel panel = new JPanel(new GridLayout(Game.ROWS, Game.COLS));

		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				playgroundArray[row][col] = new JPanel();
				playgroundArray[row][col].setBorder(BorderFactory
						.createEtchedBorder(EtchedBorder.LOWERED));
				playgroundArray[row][col].setPreferredSize(DIMENSION);
				panel.add(playgroundArray[row][col]);
			}
		}

		draw(game.getPlayground());

		return panel;
	}

	/**
	 * Zeichnet das Spielfeld neu
	 * 
	 * @param array
	 *            "Logisches" Labyrinth aus der Game Klasse
	 */
	private void draw(int[][] array) {

		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {

				if (array[row][col] == Game.EMPTY_FIELD)
					playgroundArray[row][col]
							.setBackground(Settings.EMPTY_COLOR);

				else if (array[row][col] == Game.WALL_FIELD)
					playgroundArray[row][col]
							.setBackground(Settings.WALL_COLOR);

				else if (array[row][col] == Game.SNAKE_FIELD)
					playgroundArray[row][col]
							.setBackground(Settings.SNAKE_COLOR);

				else if (array[row][col] == Game.FOOD_FIELD)
					playgroundArray[row][col]
							.setBackground(Settings.FOOD_COLOR);
			}
		}
	}

	public JPanel getPlaygroundPanel() {
		return playgroundPanel;
	}

	/**
	 * Lässt die Schlange rennen bis der Arzt kommt...
	 */
	@Override
	public void run() {

		try {
			while (!interrupted()) {

				// Endlosschleife solange das Spiel pausiert ist
				while (game.isPaused())
					;

				if (!game.isGameOver()) {

					game.nextStep();

					draw(game.getPlayground());
					infoLabel.setText("Punkte: " + game.getScore());

					sleep(speed);
				} else {
					// Spiel ist zu Ende
					infoLabel.setText("Punkte: " + game.getScore()
							+ "          GAME OVER");
					interrupt();
				}
			}
		} catch (InterruptedException e) {
			interrupt();
		}

	}

}
