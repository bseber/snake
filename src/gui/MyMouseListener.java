package gui;

/**
 * MouseListener für das Orten der Maus beim Labyrinth erstellen...
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.awt.event.MouseListener;

public abstract class MyMouseListener implements MouseListener {
	
	protected int row;
	protected int col;
	
	MyMouseListener (int row, int col) {
		this.row = row;
		this.col = col;
	}

	

}
