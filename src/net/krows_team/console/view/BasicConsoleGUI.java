package net.krows_team.console.view;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import net.krows_team.console.io.ConsoleGUIIO;
import net.krows_team.console.io.IO;

/**
 * 
 * BasicConsoleGUI class is basic implementation of {@link ConsoleGUIModule}.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
public class BasicConsoleGUI implements ConsoleGUIModule {

/**
 * 
 * IO for interacting with console GUI.
 * 
 */
	protected IO io;
	
/**
 * 
 * Text component for interacting between user and console.
 * 
 */
	protected JTextComponent comp;
	
/**
 * 
 * Number of first available offset to edit by user.
 * 
 */
	protected int userPositionStart;
	
/**
 * 
 * Creates new console GUI with interacting tool is specified text component.
 * 
 * @param comp Text component for interacting between user and console.
 *
 */
	public BasicConsoleGUI(JTextComponent comp) {
		
		this.comp = comp;
		
		this.io = new ConsoleGUIIO(this);
		
		comp.setBackground(Color.BLACK);
		comp.setForeground(Color.GREEN);
		comp.setCaretColor(Color.GREEN);
		
		((AbstractDocument) comp.getDocument()).setDocumentFilter(new DocumentFilter() {
			
			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

				if(offset >= userPositionStart) super.remove(fb, offset, length);
			}
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				
				if(offset >= userPositionStart) super.replace(fb, offset, length, text, attrs);
			}
		});
	}
	
	@Override
	public void userReady() {
		
		userPositionStart = comp.getDocument().getLength();
	}
	
	@Override
	public int getUserPositionStart() {
		
		return userPositionStart;
	}
	
/**
 * 
 * Returns the line number which caret is located at current time.
 * 
 * @return The line number which caret is located at current time.
 *
 */
	public int getCurrentSelectedLine() {
		
		return comp.getDocument().getDefaultRootElement().getElementIndex(comp.getCaretPosition());
	}
	
/**
 * 
 * Returns first available offset of the current selected line which caret is located at current time.
 * 
 * @return Start offset of current selected line.
 *
 */
	public int getStartOffsetOfCurrentSelectedLine() {
		
        return comp.getDocument().getDefaultRootElement().getElement(getCurrentSelectedLine()).getStartOffset();
	}
	
/**
 * 
 * Returns last available offset of the current selected line which caret is located at current time.
 * 
 * @return End offset of current selected line.
 *
 */
	public int getEndOffsetOfCurrentSelectedLine() {
		
        int endOffset = comp.getDocument().getDefaultRootElement().getElement(getCurrentSelectedLine()).getEndOffset();
        
        return ((getCurrentSelectedLine() == comp.getDocument().getDefaultRootElement().getElementCount() - 1) ? (endOffset - 1) : endOffset);
	}
	
/**
 * 
 * Returns length of symbols of the current selected line which caret is located at current time.
 * 
 * @return Length of the current selected line.
 *
 */
	public int getLengthOfCurrentSelectedLine() {
		
		return getEndOffsetOfCurrentSelectedLine() - getStartOffsetOfCurrentSelectedLine();
	}
	
/**
 * 
 * Returns IO object for interacting with console GUI.
 * 
 * @return IO object for interacting with console GUI.
 *
 */
	public IO getIO() {
		
		return io;
	}
	
/**
 * 
 * Returns {@link OutputStream} object of console.
 * 
 * @return OutputStream object of console.
 *
 */
	public OutputStream getOutputStream() {

		return io.getOutputStream();
	}
	
/**
 * 
 * Returns {@link InputStream} object of console.
 * 
 * @return InputStream object of console.
 *
 */
	public InputStream getInputStream() {

		return io.getInputStream();
	}

	@Override
	public JTextComponent getComponent() {

		return comp;
	}
}