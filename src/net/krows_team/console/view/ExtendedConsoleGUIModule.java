package net.krows_team.console.view;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * 
 * ExtendedConsoleGUIModule interface extends {@link ConsoleGUIModule} for restructuring the module to working with extended text components.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
public interface ExtendedConsoleGUIModule extends ConsoleGUIModule {
	
/**
 * 
 * Main method allows to interact with console by using extended text component. Returns {@link JEditorPane} or extended to him object of current console window.
 * 
 * @return JEditorPane of window of the console.
 * 
 */
	@Override
	public JEditorPane getComponent();
	
	@Override
	public default void append(String str) {
		
		if(str == null || str.equals("")) return;
		
		try {
			
            Document doc = getComponent().getDocument();
            
            getComponent().getEditorKit().read(new StringReader(str), doc, doc.getLength());
            
            doc.remove(doc.getLength() - 1, 1);
            doc.insertString(doc.getLength(), " ", null);
        } catch (IOException e) {
            
        	e.printStackTrace();
        } catch (BadLocationException e) {
  
        	e.printStackTrace();
        }
	}
}