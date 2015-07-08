package gui;

/**
 * Dies ist ein JDialog der aufgerufen wird, wenn der Spieler ein eigenes Labyrinth erstellen möchte.
 * Das Labyrinth kann in einer Datei gespeichert und / oder gleich gespielt werden.
 * 
 * TODO Dateien werden noch nicht überprüft und einfach überschrieben...
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import logic.*;

@SuppressWarnings("serial")
public class BuilderDialog extends JDialog {

	private static final int HORIZONTAL_WALL = -1;
	private static final int VERTICAL_WALL = -2;

	private JPanel container;
	private JPanel buttonPanel;
	private JPanel labyrinthPanel;
	private JPanel infoPanel;
	private JLabel infoLabel;
	private JButton[][] labyrinth; // Feld auf dem der Benutzer das Labyrinth erstellt
	private int[][] labyrinthLogic; // Logisches Spielfeld zum speichern

	private JToggleButton fieldButton; // einfaches Feld
	private JToggleButton wallButton; // Wand
	private JToggleButton horWallButton; // horizontale Wand
	private JToggleButton verWallButton; // vertikale Wand
	private JToggleButton snakeButton; // Schlange setzen
	private JButton abortButton;
	private JButton justPlayButton;
	private JButton saveButton;
	private JButton resetButton;

	private JFileChooser fileChooser;
	private MainWindow parent;

	/*
	 * ==============================================
	 *   K O N S T R U K T O R   &   C O
	 */

	/**
	 * Konstruktor erstellt einen JDialog um ein eigenes Labyrinth zu basteln
	 * 
	 * @param frame
	 *            JFrame von dem aus der JDialog aufgerufen wurde
	 * @param title
	 *            Titel des Dialoges
	 * @param arg
	 *            Ob der JFrame so lange blockiert werden soll
	 */
	BuilderDialog(MainWindow frame, String title, boolean arg) {

		super(frame, title, arg);
		parent = frame;

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
		}

		JPanel border = new JPanel();
		border.setBorder(BorderFactory.createLineBorder(
				MainWindow.BORDER_COLOR, MainWindow.BORDER_WIDTH));

		container = initContainer();
		border.add(container);
		add(border);

		setLocationByPlatform(true);
		setResizable(false);
		pack();
	}

	/**
	 * Initiiert das Hauptpanel und gibt es zurück. Enthalten sind Buttons zum
	 * Wählen des Objektes was gesetzt werden soll, das Spielfeld was bearbeitet
	 * wird und Buttons zum speichern etc.
	 */
	private JPanel initContainer() {

		GridBagConstraints c;
		JPanel panel = new JPanel(new GridBagLayout());

		buttonPanel = initButtonsPanel();
		c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(buttonPanel, c);

		labyrinthPanel = initPlayground();
		drawPlayground();
		c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 1;
		c.insets = new Insets(10, 0, 10, 10);
		panel.add(labyrinthPanel, c);

		infoPanel = initInfoPanel();
		c = new GridBagConstraints();
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(infoPanel, c);

		//TODO Dateien werden noch nicht geprüft und einfach überschrieben...
		JLabel label = new JLabel(
				"!!! Falls eine Datei ausgewählt wird, wird diese überschrieben !!!");
		label.setForeground(Color.RED);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 2, 5);
		panel.add(label, c);

		JLabel label2 = new JLabel("wird noch geändert... irgendwann...");
		label2.setForeground(Color.RED);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 5, 5, 5);
		panel.add(label2, c);
		// -->

		return panel;
	}

	/**
	 * Initiiert das Panel mit den Buttons zum Wählen des Objektes was gesetzt
	 * werden soll und gibt es zurück
	 */
	private JPanel initButtonsPanel() {

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c;

		// Buttons zum Wählen des Objektes welches gesetzt werden soll
		fieldButton = new JToggleButton("normales Feld");
		wallButton = new JToggleButton("Mauer");
		wallButton.setSelected(true);
		horWallButton = new JToggleButton("horizontale Mauer");
		verWallButton = new JToggleButton("vertikale Mauer");
		snakeButton = new JToggleButton("Schlange");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(fieldButton);
		buttonGroup.add(wallButton);
		buttonGroup.add(horWallButton);
		buttonGroup.add(verWallButton);
		buttonGroup.add(snakeButton);

		// Button zum Zurücksetzen des Spielfeldes
		resetButton = new JButton("zurücksetzen");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetPlayground();
				drawPlayground();
			}
		});

		// Platzierungen der Buttons
		c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(fieldButton, c);

		c = new GridBagConstraints();
		c.gridy = 1;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 0, 0, 0);
		panel.add(wallButton, c);

		c = new GridBagConstraints();
		c.gridy = 2;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 0, 0, 0);
		panel.add(horWallButton, c);

		c = new GridBagConstraints();
		c.gridy = 3;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 0, 0, 0);
		panel.add(verWallButton, c);

		c = new GridBagConstraints();
		c.gridy = 4;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(30, 0, 0, 0);
		panel.add(snakeButton, c);

		c = new GridBagConstraints();
		c.gridy = 5;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(30, 0, 0, 0);
		panel.add(resetButton, c);

		return panel;
	}

	/**
	 * Initiiert ein Panel mit dem Spielfeld was vom Benutzer bearbeitet wird
	 * und gibt es zurück
	 */
	private JPanel initPlayground() {

		JPanel panel = new JPanel(new GridLayout(Game.ROWS, Game.COLS));

		// Logisches Spielfeld
		labyrinthLogic = new int[Game.ROWS][Game.COLS];
		for (int row = 0; row < Game.ROWS; row++)
			for (int col = 0; col < Game.COLS; col++)
				labyrinthLogic[row][col] = Game.EMPTY_FIELD;

		// Optisches Spielfeld
		labyrinth = new JButton[Game.ROWS][Game.COLS];
		for (int row = 0; row < Game.ROWS; row++)
			for (int col = 0; col < Game.COLS; col++) {
				labyrinth[row][col] = new JButton();
				labyrinth[row][col].setPreferredSize(Playground.DIMENSION);
				labyrinth[row][col].addMouseListener(new MyMouseListener(row,
						col) {

					public void mouseReleased(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					/**
					 * Zeichne das Spielfeld neu
					 */
					public void mouseExited(MouseEvent e) {
						drawPlayground();
					}

					/**
					 * Falls das Objekt hier gesetzt werden kann zeige es an
					 */
					public void mouseEntered(MouseEvent e) {

						if (fieldButton.isSelected()) {
							if (isPlacable(row, col, Game.EMPTY_FIELD))
								// leeres Feld zeichnen
								drawObject(row, col, Game.EMPTY_FIELD);
						} else if (wallButton.isSelected()) {
							if (isPlacable(row, col, Game.WALL_FIELD))
								// Wand zeichnen
								drawObject(row, col, Game.WALL_FIELD);
						} else if (horWallButton.isSelected()) {
							if (isPlacable(row, col, HORIZONTAL_WALL))
								// horizontale Wand zeichnen
								drawObject(row, col, HORIZONTAL_WALL);
						} else if (verWallButton.isSelected()) {
							if (isPlacable(row, col, VERTICAL_WALL))
								// vertikale Wand zeichnen
								drawObject(row, col, VERTICAL_WALL);
						} else if (snakeButton.isSelected()) {
							// Schlange zeichnen
							if (isPlacable(row, col, Game.SNAKE_FIELD))
								drawObject(row, col, Game.SNAKE_FIELD);
						}
					}

					/**
					 * Setze das Objekt
					 */
					public void mouseClicked(MouseEvent e) {

						if (fieldButton.isSelected()) {
							if (isPlacable(row, col, Game.EMPTY_FIELD))
								// leeres Feld setzen
								setObject(row, col, Game.EMPTY_FIELD);
						} else if (wallButton.isSelected()) {
							if (isPlacable(row, col, Game.WALL_FIELD))
								// Wand setzen
								setObject(row, col, Game.WALL_FIELD);
						} else if (horWallButton.isSelected()) {
							if (isPlacable(row, col, HORIZONTAL_WALL))
								// horizontale Wand setzen
								setObject(row, col, HORIZONTAL_WALL);
						} else if (verWallButton.isSelected()) {
							if (isPlacable(row, col, VERTICAL_WALL))
								// vertikale Wand setzen
								setObject(row, col, VERTICAL_WALL);
						} else if (snakeButton.isSelected()) {
							if (isPlacable(row, col, Game.SNAKE_FIELD)) {
								/*
								 * Wenn die Schlange gesetzt ist, kann gespielt
								 * bzw gespeichert werden
								 */
								setObject(row, col, Game.SNAKE_FIELD);
								infoLabel.setText("- Schlange gesetzt -");
								snakeButton.setEnabled(false);
								wallButton.setSelected(true);
								justPlayButton.setEnabled(true);
								saveButton.setEnabled(true);
							}
						}
					}
				});
				panel.add(labyrinth[row][col]);
			}

		return panel;
	}

	/**
	 * Initiiert ein Panel mit den Buttons zum speichern etc. und gibt es zurück
	 */
	private JPanel initInfoPanel() {

		GridBagConstraints c;
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());

		// kurze Info über den aktuellen Status
		infoLabel = new JLabel("- Schlange setzen zum Spielen bzw. Speichern -");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 0, 5, 0);
		panel.add(infoLabel, c);

		// Button zum abbrechen
		abortButton = new JButton("Beenden");
		abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO Prüfung falls noch nicht gespeichert
				dispose();
			}
		});
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(5, 5, 5, 5);
		panel.add(abortButton, c);

		// Button um das Labyrinth einfach nur zu spielen
		justPlayButton = new JButton("Spielen");
		justPlayButton.setEnabled(false);
		justPlayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * In den Einstellungen wird gepseichert, dass das eigene
				 * Labyrinth geladen werden soll, das Spiel wird mit diesem neu
				 * gestartet und der Dialog verschwindet.
				 */
				Settings settings = parent.getSettings();
				settings.setModus(Settings.OWN_LABYRINTH);
				parent.getOwnLabButton().setEnabled(true);
				parent.getOwnLabButton().setSelected(true);
				Game.setOwnPlayground(labyrinthLogic);
				parent.resetGame();
				dispose();
			}
		});
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		panel.add(justPlayButton, c);

		// Button um das Labyrinth zu speichern
		saveButton = new JButton("Speichern");
		saveButton.setEnabled(false);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (saveLabyrinth())
						infoLabel.setText("- Labyrinth wurde gespeichert -");
					else
						infoLabel.setText("- Fehler beim Speichern -");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		panel.add(saveButton, c);

		return panel;
	}
	
	/*
	 * ==============================================
	 *   L O G I K
	 */

	/**
	 * Setzt das Spielfeld zurück
	 */
	private void resetPlayground() {

		for (int row = 0; row < Game.ROWS; row++)
			for (int col = 0; col < Game.COLS; col++)
				// Logisches Spielfeld wird mit leeren Feldern gefüllt
				labyrinthLogic[row][col] = Game.EMPTY_FIELD;

		// und das optische für den Benutzer neu gedruckt
		drawPlayground();

		infoLabel.setText("- Schlange setzen zum Spielen bzw. Speichern -");
		// Schlange muss erst wieder gesetzt werden zum speichern
		saveButton.setEnabled(false);
		justPlayButton.setEnabled(false);
		snakeButton.setEnabled(true);
	}

	/**
	 * Zeichnet das Spielfeld
	 */
	private void drawPlayground() {

		for (int row = 0; row < Game.ROWS; row++)
			for (int col = 0; col < Game.COLS; col++) {

				if (labyrinthLogic[row][col] == Game.WALL_FIELD)
					labyrinth[row][col].setBackground(Settings.WALL_COLOR);

				else if (labyrinthLogic[row][col] == Game.SNAKE_FIELD)
					labyrinth[row][col].setBackground(Settings.SNAKE_COLOR);

				else
					labyrinth[row][col].setBackground(Settings.EMPTY_COLOR);
			}

	}

	/**
	 * Prüft, ob das gewählte Objekt am übergebenen Ort platziert werden kann.
	 * 
	 * @param row
	 *            Reihe in der sich der Mauszeiger befindet
	 * @param col
	 *            Spalte in der sich der Mauszeiger befindet
	 * @param objectToSet
	 *            EMPTY_FIELD, SIMPLE_WALL, HOTIZONTAL_WALL, VERTICAL_WALL oder
	 *            SNAKE
	 * @return true: Objekt kann gesetzt werden, ansonsten false
	 */
	private boolean isPlacable(int row, int col, int objectToSet) {

		if (objectToSet == Game.EMPTY_FIELD || objectToSet == Game.WALL_FIELD) {
			if (labyrinthLogic[row][col] == Game.SNAKE_FIELD) {
				// Schlange im Weg
				return false;
			}
		} else if (objectToSet == HORIZONTAL_WALL) {
			for (int x = 0; x < Game.COLS; x++)
				if (labyrinthLogic[row][x] == Game.SNAKE_FIELD)
					// Schlange im Weg
					return false;
		} else if (objectToSet == VERTICAL_WALL) {
			for (int y = 0; y < Game.ROWS; y++)
				if (labyrinthLogic[y][col] == Game.SNAKE_FIELD)
					// Schlange im Weg
					return false;
		} else if (objectToSet == Game.SNAKE_FIELD) {
			if (col == 0)
				// zu weit links
				return false;
			else if (labyrinthLogic[row][col] == Game.WALL_FIELD
					|| labyrinthLogic[row][col - 1] == Game.WALL_FIELD)
				// Wand im Weg
				return false;
		}

		// Objekt kann gesetzt werden
		return true;
	}

	/**
	 * Setzt das Objekt aufs Spielfeld
	 * 
	 * @param row
	 *            Reihe in der sich der Mauszeiger befindet
	 * @param col
	 *            Spalte in der isch der Mauszeiger befindet
	 * @param objectToSet
	 *            EMPTY_FIELD, SIMPLE_WALL, HOTIZONTAL_WALL, VERTICAL_WALL oder
	 *            SNAKE
	 */
	private void setObject(int row, int col, int objectToSet) {

		if (objectToSet == Game.EMPTY_FIELD) {
			// leeres Feld setzen
			labyrinthLogic[row][col] = Game.EMPTY_FIELD;
		} else if (objectToSet == Game.WALL_FIELD) {
			// Wand setzen
			labyrinthLogic[row][col] = Game.WALL_FIELD;
		} else if (objectToSet == HORIZONTAL_WALL) {
			// horizontale Wand setzen
			for (int x = 0; x < Game.COLS; x++)
				labyrinthLogic[row][x] = Game.WALL_FIELD;
		} else if (objectToSet == VERTICAL_WALL) {
			// vertikale Wand setzen
			for (int y = 0; y < Game.ROWS; y++)
				labyrinthLogic[y][col] = Game.WALL_FIELD;
		} else if (objectToSet == Game.SNAKE_FIELD) {
			// Schlange setzen
			labyrinthLogic[row][col] = Game.SNAKE_FIELD;
			labyrinthLogic[row][col - 1] = Game.SNAKE_FIELD;
		}

	}

	/**
	 * Zeichnet eine Linie abhängig der Richtung vertikal oder horinzontal bzw.
	 * lüscht sie wieder
	 * 
	 * @param row
	 *            Reihe in der sich der Mauszeiger befindet
	 * @param col
	 *            Spalte in der sich der Mauszeiger befindet
	 * @param objectToDraw
	 *            EMPTY_FIELD, SIMPLE_WALL, HOTIZONTAL_WALL, VERTICAL_WALL oder
	 *            SNAKE
	 */
	private void drawObject(int row, int col, int objectToDraw) {

		if (objectToDraw == Game.EMPTY_FIELD) {
			// leeres Feld zeichnen
			labyrinth[row][col].setBackground(Settings.EMPTY_COLOR);
		} else if (objectToDraw == Game.WALL_FIELD) {
			// Wand zeichnen
			labyrinth[row][col].setBackground(Settings.WALL_COLOR);
		} else if (objectToDraw == HORIZONTAL_WALL) {
			// Horizontale Wand zeichnen
			for (int j = 0; j < Game.COLS; j++)
				labyrinth[row][j].setBackground(Settings.WALL_COLOR);
		} else if (objectToDraw == VERTICAL_WALL) {
			// Vertikale Wand Zeichnen
			for (int i = 0; i < Game.ROWS; i++)
				labyrinth[i][col].setBackground(Settings.WALL_COLOR);
		} else if (objectToDraw == Game.SNAKE_FIELD) {
			// Schlange zeichnen
			labyrinth[row][col].setBackground(Settings.SNAKE_COLOR);
			labyrinth[row][col - 1].setBackground(Settings.SNAKE_COLOR);
		}

	}

	/**
	 * Speichert das erstellte Labyrinth in einer Datei
	 * 
	 * @throws IOException
	 */
	private boolean saveLabyrinth() throws IOException {
		//TODO Prüfung der gewählten Datei

		fileChooser = new JFileChooser("Speichern unter");
		// nur Dateien mit der Endung ".snake" werden angezeigt
		fileChooser.setFileFilter(new MySnakeFileFilter());
		int returnVal = fileChooser.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// Labyrinth kann gespeichert werden
			File target = fileChooser.getSelectedFile().getAbsoluteFile();
			LabyrinthWriter writer = new LabyrinthWriter(target, labyrinthLogic);
			writer.savePlayground();

			return true;
		}

		return false;
	}

}
