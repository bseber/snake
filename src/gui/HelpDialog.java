package gui;

/**
 * JDialog für das Spiel Snake welcher eine Hilfe zeigt.
 * Im Konstruktor wird übergeben welche Hilfe angezeigt werden soll.
 * Entweder zur Steuerung des Spiels oder zum erstellen eines eigenen Labyrinthes.
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HelpDialog extends JDialog {

	public static final int GAMEHELP = 0;
	public static final int CONSTRUCTORHELP = 1;

	private JPanel panel;
	private ImageIcon image;

	HelpDialog(JFrame parent, String title, boolean arg, int type) {

		super(parent, title, arg);
		setResizable(false);
		setLocationRelativeTo(parent);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);

		if (type == GAMEHELP)
			image = new ImageIcon(getClass().getResource("/gameHelp.gif"));

		else if (type == CONSTRUCTORHELP)
			image = new ImageIcon(getClass().getResource("/constructHelp.gif"));

		// Label mit dem Bild füŸr die entsprechende Hilfe
		JLabel label = new JLabel();
		label.setIcon(image);
		label.setSize(image.getIconWidth(), image.getIconHeight());
		panel.add(label);

		add(panel);
		pack();
	}

}
