package gui;

/**Das Hauptfenster für das Spiel "Snake"
 * Beinhaltet ein Menü zum einstellen des Schwierigkeitsgrades,
 * zur Auswahl verschiedener Labyrinthe oder zum erstellen / laden eines Labyrinthes.
 * Und natürlich einen Thread der das eigentliche Spielfeld (Labyrinth) mit
 * der Schlange repräsentiert. (Siehe <i>Playground.class</i>)
 * 
 * TODO Grafiken anstatt der simplen Farben...
 * TODO Sounds
 * TODO Kommentare vielleicht xD
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import logic.*;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements KeyListener {
	
	// Konstanten
	public static final Color BORDER_COLOR = Color.DARK_GRAY;
	public static final int   BORDER_WIDTH	= 20;
	
	// Spiel Attribute
	private Settings settings;
	private Game game;
	private Playground gameThread;
	
	// JFrame Attribute
	private JPanel container;
	private JPanel playgroundPanel;
	private JLabel infoLabel;
	private JButton resetButton;
	private JDialog helpDialog;
	
	// Menü Buttons
	private JRadioButtonMenuItem easyButton;
	private JRadioButtonMenuItem normalButton;
	private JRadioButtonMenuItem hardButton;
	private JRadioButtonMenuItem insaneButton;
	private JMenuItem createLabButton;
	private JMenuItem loadLabButton;
	private JRadioButtonMenuItem classicButton;
	private JRadioButtonMenuItem labAButton;
	private JRadioButtonMenuItem labBButton;
	private JRadioButtonMenuItem labCButton;
	private JRadioButtonMenuItem ownLabButton;
	private JMenuItem gameHelpButton;
	private JMenuItem createLabHelpButton;
	
	MainWindow() {
		
		super("Snake by Seebaer ;-)");
		initFrameSettings();
		
		settings = new Settings();
		game = new Game(settings);
		
		// Menü hinzufügen
		setJMenuBar(initMenuBar());
		
		container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, BORDER_WIDTH));
		container.add(initInfo(), BorderLayout.SOUTH);
		playgroundPanel = initPlayground();
		container.add(playgroundPanel, BorderLayout.CENTER);
		container.addKeyListener(this);
		container.setFocusable(true);
		
		add(container);
		pack();
	}
	
	private void initFrameSettings() {
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {}
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setResizable(false);
	}
	
	private JMenuBar initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(initDifficultyMenu());
		menuBar.add(initLabyrinthMenu());
		menuBar.add(initHelpMenu());
		return menuBar;
	}
	
	private JMenu initDifficultyMenu() {
		JMenu menu = new JMenu("Schwierigkeit");
		
		easyButton = new JRadioButtonMenuItem("leicht");
		easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setCurrentSpeed(Settings.EASY);
				resetGame();
			}
		});
		normalButton = new JRadioButtonMenuItem("normal");
		normalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setCurrentSpeed(Settings.NORMAL);
				resetGame();
			}
		});
		hardButton = new JRadioButtonMenuItem("schwer");
		hardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setCurrentSpeed(Settings.HARD);
				resetGame();
			}
		});
		insaneButton = new JRadioButtonMenuItem("wahnsinnig");
		insaneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setCurrentSpeed(Settings.INSANE);
				resetGame();
			}
		});
		
		// Buttons dem Menü hinzufügen
		menu.add(easyButton);
		menu.add(normalButton);
		normalButton.setSelected(true);
		menu.add(hardButton);
		menu.add(insaneButton);
		
		// ButtonGroup
		ButtonGroup bg = new ButtonGroup();
		bg.add(easyButton);
		bg.add(normalButton);
		bg.add(hardButton);
		bg.add(insaneButton);
		
		return menu;
	}

	private JMenu initLabyrinthMenu() {
		
		JMenu menu = new JMenu("Labyrinth");
		
		createLabButton = new JMenuItem("Labyrinth erstellen");
		createLabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showBuilderDialog();
			}
		});
		loadLabButton = new JMenuItem("Labyrinth laden");
		loadLabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int[][] labyrinth = loadLabyrinth();
					if (labyrinth != null) {
						// wenn das Labyrinth erfolgreich geladen wurde
						// erstelle ein neues Spiel damit
						Game.setOwnPlayground(labyrinth);
						settings.setModus(Settings.OWN_LABYRINTH);
						ownLabButton.setEnabled(true);
						ownLabButton.setSelected(true);
						resetGame();
					}
					else
						infoLabel.setText("Fehler beim Laden der Datei...");
				} catch (IOException e) {
					infoLabel.setText("Fehler beim Laden der Datei...");
				}
			}
		});
		classicButton = new JRadioButtonMenuItem("Klassik");
		classicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setModus(Settings.CLASSIC);
				resetGame();
			}
		});
		labAButton = new JRadioButtonMenuItem("Labyrinth A");
		labAButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setModus(Settings.LABYRINTH_A);
				resetGame();
			}
		});
		labBButton = new JRadioButtonMenuItem("Labyrinth B");
		labBButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setModus(Settings.LABYRINTH_B);
				resetGame();
			}
		});
		labCButton = new JRadioButtonMenuItem("Labyrinth C");
		labCButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setModus(Settings.LABYRINTH_C);
				resetGame();
			}
		});
		ownLabButton = new JRadioButtonMenuItem("Eigenes Labyrinth");
		ownLabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setModus(Settings.OWN_LABYRINTH);
				resetGame();
			}
		});
		
		// Buttons dem Menü hinzufügen
		menu.add(createLabButton);
		menu.add(loadLabButton);
		menu.add(new JSeparator());
		menu.add(classicButton);
		classicButton.setSelected(true);
		menu.add(labAButton);
		menu.add(labBButton);
		menu.add(labCButton);
		menu.add(ownLabButton);
		ownLabButton.setEnabled(false);
		
		// ButtonGroup
		ButtonGroup bg = new ButtonGroup();
		bg.add(classicButton);
		bg.add(labAButton);
		bg.add(labBButton);
		bg.add(labCButton);
		bg.add(ownLabButton);
		
		return menu;
	}

	private JMenu initHelpMenu() {
		
		JMenu menu = new JMenu("Hilfe");
		
		gameHelpButton = new JMenuItem("Steuerung");
		gameHelpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelp(HelpDialog.GAMEHELP);
			}
		});
		createLabHelpButton = new JMenuItem("Labyrinth erstellen");
		createLabHelpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelp(HelpDialog.CONSTRUCTORHELP);
			}
		});
		
		menu.add(gameHelpButton);
		menu.add(createLabHelpButton);
		
		return menu;
	}
	
	private JPanel initPlayground() {
		gameThread = new Playground(game, settings.getCurrentSpeed(), infoLabel);
		return gameThread.getPlaygroundPanel();
	}
	

	
	private JPanel initInfo() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		panel.setBackground(BORDER_COLOR);
		
		infoLabel = new JLabel("Punkte: 0");
		infoLabel.setForeground(Color.WHITE);
		panel.add(infoLabel, BorderLayout.CENTER);
		
		resetButton = new JButton("Neustart");
		resetButton.setBackground(Color.WHITE);
		resetButton.setFocusable(false);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetGame();
			}
		});
		panel.add(resetButton, BorderLayout.EAST);
		
		return panel;
	}
	
	/**
	 * Zeigt einen JDialog mit der entsprechenden Hilfe
	 * 
	 * @param type
	 *            Bestimmt welche Hilfe angezeigt wird. Mit HelpDialog.GAMEHELP
	 *            wird die Hilfe zum Spiel agezeigt, mit
	 *            HelpDialog.CONSTRUCTORHELP die Hilfe wie ein eigenes Labyrinth
	 *            erstellt wird.
	 */
	private void showHelp(int type) {
		
		if (type == HelpDialog.GAMEHELP)
			helpDialog = new HelpDialog(this, "Hilfe zum Spiel", true, type);
		
		else if (type == HelpDialog.CONSTRUCTORHELP)
			helpDialog = new HelpDialog(this, "Hilfe zum Labyrinth erstellen", true, type);
		
		helpDialog.setVisible(true);
	}
	
	/**
	 * Startet ein neues Spiel
	 */
	public void resetGame() {
		
		game = new Game(settings);
		
		container.remove(playgroundPanel);
		
		playgroundPanel = initPlayground();
		container.add(playgroundPanel, BorderLayout.CENTER);
		container.validate();
		
		infoLabel.setText("Punkte: 0");
		
		resetButton.setFocusable(false);
		container.setFocusable(true);
	}
	
	/**
	 * Zeigt einen Dialog um ein eigenes Labyrinth zu erstellen
	 */
	private void showBuilderDialog() {
		JDialog mapBuilder = new BuilderDialog(this, "Eigenes Labyrinth erstellen", true);
		mapBuilder.setVisible(true);
	}
	
	/**
	 * L�d ein Labyrinth des Benutzers
	 * @throws IOException
	 */
	private int[][] loadLabyrinth() throws IOException {
		
		JFileChooser fileChooser = new JFileChooser("Labyrinth laden");
		fileChooser.setFileFilter(new MySnakeFileFilter());
		int returnVal = fileChooser.showOpenDialog(this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			LabyrinthLoader loader = new LabyrinthLoader(
					fileChooser.getSelectedFile().getAbsoluteFile());
			
			if (loader.isValidFile())
				// falls es ein Labyrinth f�r dieses Spiel ist
				// �bergebe dem Spiel dieses
				return loader.getLabyrinth();
			else
				return null;
		}
		
		return null;
	}
	

	public void keyPressed(KeyEvent arg0) {	}
	public void keyReleased(KeyEvent arg0) { }

	/**
	 * Setzt die die Richtung der Schlange bzw ob das Spiel pausiert werden soll
	 */
	public void keyTyped(KeyEvent arg0) {
		
		char key = arg0.getKeyChar();
		
		if (key == settings.getDown()
				|| key == settings.getLeft()
				|| key == settings.getRight()
				|| key == settings.getUp()
				|| key == settings.getPause())
		{
			game.setDirection(key);
			
			if (!gameThread.isAlive() && key != settings.getLeft())
				// falls das Spiel noch nicht l�uft starte es
				System.out.println("bla");
				gameThread.start();
				
		}
	}
	
	Settings getSettings() {
		return settings;
	}
	
	JRadioButtonMenuItem getOwnLabButton() {
		return ownLabButton;
	}
	
	
	
	public static void main(String[] args) {
		new MainWindow().setVisible(true);
	}
	
}