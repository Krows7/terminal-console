package net.krows_team.console.view;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * 
 * ConsoleGUIModule interface is an abstract interacting console text GUI. Interacting occurs through {@link #getComponent()} method.
 * 
 * @since 1.0.0
 *
 * @author Krows
 *
 */
public interface ConsoleGUIModule {
	
/**
 * 
 * Returns number of first available offset to edit by user.
 * 
 * @return First available offset for user.
 *
 */
	public int getUserPositionStart();
	
/**
 * 
 * Main method allows to interact with console by using text component. Returns {@link JTextComponent} object of current console window.
 * 
 * @return JTextComponent of window of the console.
 * 
 */
	public JTextComponent getComponent();
	
/**
 * 
 * Sets user offset to the new position.
 *
 */
	public void userReady();
	
/**
 * 
 * Appends text in to the text component of console window.
 * 
 * @param str String text to append.
 *
 */
	public default void append(String str) {
		
		Document doc = getComponent().getDocument();
		
        if (doc != null) {
        	
            try {
            	
                doc.insertString(doc.getLength(), str, null);
            } catch (BadLocationException e) {
            	
            }
        }
	}
}