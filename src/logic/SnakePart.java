package logic;

/**
 * Diese Klasse repr‰sentiert einen Teil (ein Feld) der Schlange.
 * Die Schlange ist in der Hauptklasse eine LinkedList welche Objekte dieser
 * Klasse speichert.
 * 
 * Das Schlangenteil merkt ich seinen Vorder- und Hintermann und in welcher Reihe / Spalte
 * es sich gerade befindet. Falls es der Schwanz der Schlange ist wird dieses Feld n‰mlich
 * einfach weiﬂ gef‰rbt.
 * 
 * @author Benjamin Seber
 * @version 2009/07/18
 */

import java.io.Serializable;


@SuppressWarnings("serial")
public class SnakePart implements Serializable {
	
	private int row;
	private int col;
	private SnakePart parent;
	private SnakePart child;
	
	public SnakePart(int row, int col) {
		this.row = row;
		this.col = col;
		parent = null;
		child = null;
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public SnakePart getParent() {
		return parent;
	}
	
	public SnakePart getChild() {
		return child;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public void setParent(SnakePart parent) {
		this.parent = parent;
	}
	
	public void setChild(SnakePart child) {
		this.child = child;
	}
}
