package net.krows_team.console.view.application;

import javax.swing.JTextArea;

import net.krows_team.console.view.BasicConsoleGUI;

/**
 * 
 * BasicWindowConsole is basic window for interacting between user and console.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
public class BasicWindowConsole extends AbstractWindowConsole {
	
/**
 * 
 * Creates new window with console.
 * 
 * @param arguments Arguments for window setup.
 *
 */
	public BasicWindowConsole() {

		super(new BasicConsoleGUI(new JTextArea()));
	}
}