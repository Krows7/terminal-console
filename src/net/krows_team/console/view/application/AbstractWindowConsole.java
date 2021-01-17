package net.krows_team.console.view.application;

import java.awt.Color;
import java.util.NoSuchElementException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import net.krows_team.console.core.Console;
import net.krows_team.console.view.BasicConsoleGUI;

/**
 * 
 * AbstractWindowConsole class is an window implementation of {@link Console}. By default it stylized like default system terminal.
 * 
 * @since 1.0.0
 *
 * @author Krows
 *
 */
public class AbstractWindowConsole extends Console {

/**
 * 
 * Interacting GUI of console.
 * 
 */
	protected BasicConsoleGUI area;
	
/**
 * 
 * Window of console.
 * 
 */
	protected JFrame window;
	
/**
 * 
 * Creates new Windowed console.
 * 
 * @param area Interacting GUI of console.
 * @param arguments Arguments for window setup.
 *
 */
	public AbstractWindowConsole(BasicConsoleGUI area) {
		
		super(area.getIO());
		
		this.area = area;
		
		window = new JFrame("Terminal Console");
		window.setSize(1280, 720);
		window.add(new JScrollPane(area.getComponent()));
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		scriptMap.put(get("color", "Edits background color by name."), () -> {
			
			try {
				
				area.getComponent().setBackground((Color) Color.class.getField(commandScanner.next()).get(null));
			} catch(NoSuchElementException e) {
				
				echo("This command needs more parameters.");
			} catch(Exception e) {
				
				echo("Typed wrong parameter. Please, check parameters for correct and try again.");
			}
		});
		
		start();
	}
	
	@Override
	protected void userReady() {
		
		super.userReady();
		
		area.userReady();
	}
	
/**
 * 
 * Returns interacting console GUI.
 * 
 * @return Interacting console GUI.
 *
 */
	public BasicConsoleGUI getGUI() {
		
		return area;
	}
}