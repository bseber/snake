package logic;

/**
 * Diese Klasse ist verantwortlich für das Speichern eines selbst erstellten Labyrinthes.
 * Zuerst wird eine Info in die Datei geschrieben, dass es sich hier um ein Labyrinth für dieses
 * Spiel handelt, dann folgt das logische Labyrinth bestehend aus
 * 0 - leeres Feld
 * 1 - Wand
 * 2 - Schlange
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LabyrinthWriter {
	
	public static final String ARGUMENT = "# SNAKE_LABYRINTH";
	private static final String POSTFIX = ".snake";
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private BufferedWriter bw;
	private FileWriter fw;
	private int[][] playground;
	private String filename;
	
	public LabyrinthWriter(File input, int[][] playground) throws IOException {
		
		filename = input.getPath();
		
		if (filename.endsWith(POSTFIX))
			fw = new FileWriter(input);
		else
			fw = new FileWriter(new File(filename + POSTFIX));
		
		bw = new BufferedWriter(fw);
		this.playground = playground;
	}
	
	public void savePlayground() throws IOException {
		
		bw.write("//" + NEWLINE);
		bw.write("// Diese Zeile sagt dem Programm, dass es sich um ein Labyrinth fŸr das Spiel handelt" + NEWLINE);
		bw.write("//" + NEWLINE);
		bw.write(NEWLINE);
		bw.write(ARGUMENT + NEWLINE);
		bw.write(NEWLINE);
		
		bw.write("//" + NEWLINE);
		bw.write("// Die Grš§e des Spielfeldes muss exakt " + Game.ROWS + "*" + Game.COLS + " Felder betragen." + NEWLINE);
		bw.write("// " + Game.EMPTY_FIELD + ": Leeres Feld" + NEWLINE);
		bw.write("// " + Game.WALL_FIELD + ": Mauer" + NEWLINE);
		bw.write("// " + Game.SNAKE_FIELD + ": Schlange (die erste 2 die von rechts gefunden wird zŠhlt als Kopf)" + NEWLINE);
		bw.write("//" + NEWLINE);
		bw.write(NEWLINE);
		
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				bw.write(playground[row][col] + " ");
				if (col + 1 == Game.COLS)
					bw.write(NEWLINE);
			}
		}
		
		bw.close();
		
	}

}
