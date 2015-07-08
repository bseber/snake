package logic;

/**
 * Filter für den JFileChooser
 * Es sollen nur Dateien gezeigt werden, die auf ".snake" enden.
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MySnakeFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		return f.getName().toLowerCase().endsWith(".snake")
				|| f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "Snake Labyrinth";
	}

}
