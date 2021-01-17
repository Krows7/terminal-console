package net.krows_team.console.view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import net.krows_team.console.io.ConsoleGUIIO;

/**
 * 
 * ExtendedConsoleGUI is extended console GUI which can works with HTML.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
public class ExtendedConsoleGUI extends BasicConsoleGUI implements ExtendedConsoleGUIModule {
	
/**
 * 
 * Indicates whether text component is editable or not at current time.
 * 
 */
	boolean editable;
	
/**
 * 
 * Creates new extended console GUI with interacting tool is specified text editor pane.
 * 
 * @param comp JEditorPane object for interacting between user and console.
 *
 */
	public ExtendedConsoleGUI(JEditorPane comp) {
		
		super(comp);
		
		editable = true;
		
		comp.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
		comp.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		comp.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {

				if(e.getKeyCode() == KeyEvent.VK_ALT) {
					
					if(editable) {
						
						editable = false;
						
						comp.setEditable(editable);
					}
				} else if(e.getKeyCode() == KeyEvent.VK_V && e.getModifiers() == InputEvent.CTRL_MASK) {
					
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					
					try {
						
						String message = (String) clipboard.getData(DataFlavor.stringFlavor);
						message.replace("\r", "");
						message.replace("\n", "");
						
						StringSelection selection = new StringSelection(message);
						
						clipboard.setContents(selection, selection);
					} catch(IOException e1) {
						
						e1.printStackTrace();
					} catch(UnsupportedFlavorException e2) {
						
						e2.printStackTrace();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {

				if(e.getKeyCode() == KeyEvent.VK_ALT) {
					
					editable = true;
					
					comp.setEditable(editable);
				}
			}
		});
		comp.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {

				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					
					((ConsoleGUIIO) io).enterHyperlink(e.getDescription());
				}
			}
		});
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
	public JEditorPane getComponent() {
		
		return (JEditorPane) super.getComponent();
	}
}