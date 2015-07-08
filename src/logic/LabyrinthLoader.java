package logic;

/**
 * Diese Klasse ist verantwortlich für das Laden eines Labyrinthes.
 * Es muss zuerst geprüft werden, ob die Datei ein Labyrinth für dieses
 * Spiel enthällt, dann kann es erfolgreich geladen und zurückgegeben werden.
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LabyrinthLoader {
	
	private BufferedReader br;
	private FileReader fr;
	
	private String textline;

	public LabyrinthLoader(File input) throws FileNotFoundException {
		fr = new FileReader(input);
		br = new BufferedReader(fr);
	}
	
	/**
	 * PrŸft ob es sich bei der Datei um ein Labyrinth handelt oder nicht
	 * @throws IOException 
	 */
	public boolean isValidFile() throws IOException {
		
		while (br.ready()) {
			textline = br.readLine();
			if (textline.startsWith(LabyrinthWriter.ARGUMENT))
				return true;
		}
		
		br.close();
		return false;
	}
	
	/**
	 * Liest das Labyrinth aus der Datei und gibt dieses zurŸck
	 * @throws IOException 
	 */
	public int[][] getLabyrinth() throws IOException {
		
		int[][] array = new int[Game.ROWS][Game.COLS];
		int row = 0, col;
		char c;
		
		while (br.ready()) {
			
			textline = br.readLine();
			col = 0;
			
			try {
				if (isInteger(textline.charAt(0))) {
					
					for (int i = 0; i < textline.length(); i++) {
						
						c = textline.charAt(i);
						
						if (isInteger(c)) {
							// speicher zahl im array
							array[row][col] = c - '0';
							
							if (col + 1 < Game.COLS)
								col++;
						}
					}
					
					row++;
				}
			} catch (StringIndexOutOfBoundsException e) {
				// leere Zeilen Ÿberspringen
			}
		}
		
		br.close();
		return array;
	}
	
	/**
	 * PrŸft ob der Ÿbergeben char eine Ziffer ist
	 */
	private boolean isInteger(char c) {
		if (c >= 48 && c <= 57)
			return true;
		else
			return false;
	}



}
