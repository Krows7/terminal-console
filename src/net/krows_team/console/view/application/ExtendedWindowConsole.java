package net.krows_team.console.view.application;

import javax.swing.JTextPane;

import net.krows_team.console.view.ExtendedConsoleGUI;

/**
 * 
 * ExtendedWindowConsole is extending version of {@link AbstractWindowConsole} for interacting between user and console.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
public class ExtendedWindowConsole extends AbstractWindowConsole {

/**
 * 
 * Creates new window with console.
 * 
 * @param arguments Arguments for window setup.
 *
 */
	public ExtendedWindowConsole() {

		super(new ExtendedConsoleGUI(new JTextPane()));
	}
}