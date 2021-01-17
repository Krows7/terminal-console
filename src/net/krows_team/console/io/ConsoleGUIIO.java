package net.krows_team.console.io;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.text.BadLocationException;

import net.coretools.core.OnDevelopment;
import net.coretools.core.SystemTools;
import net.krows_team.console.character.CharacterConstants;
import net.krows_team.console.view.BasicConsoleGUI;
import net.krows_team.console.view.ConsoleGUIModule;

/**
 * 
 * ConsoleGUIIO class is extending of {@link IO} for working and interacting with {@link BasicConsoleGUI} system.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
public class ConsoleGUIIO extends IO {
	
/**
 * 
 * Creates new I/O set for working with specified {@link BasicConsoleGUI}.
 * 
 * @param gui {@link BasicConsoleGUI} object to create its I/O.
 *
 */
	public ConsoleGUIIO(BasicConsoleGUI gui) {
		
		try(ConsoleGUIInputStream input = new ConsoleGUIInputStream(gui); ConsoleGUIOutputStream output = new ConsoleGUIOutputStream(gui)) {
			
			this.input = input;
			
			this.output = output;
		}
	}

/**
 * 
 * Format specified message to hyperlink message.
 * 
 * @param string String message to format.
 *
 */
	public void enterHyperlink(String string) {
		
		((ConsoleGUIInputStream) input).enterHyperlink(string);
	}
}

/**
 * 
 * ConsoleGUIInputStream class is an implementation of {@link InputStream} for working with GUI of console.
 * 
 * @since 1.0.0
 *
 * @author Krows
 *
 */
@OnDevelopment
class ConsoleGUIInputStream extends InputStream {
	
/**
 * 
 * Queue of entered commands.
 * 
 */
    final BlockingQueue<String> queue;
    
/**
 * 
 * Current reading command.
 * 
 */
    private String message;
    
/**
 * 
 * Index of current reading symbol of {@link #message}.
 * 
 */
    private int index;
    
/**
 * 
 * Creates new input channel with GUI module.
 * 
 * @param module Console GUI module to connect input channel.
 *
 */
    public ConsoleGUIInputStream(ConsoleGUIModule module) {
    	
        queue = new LinkedBlockingQueue<>();
        
        module.getComponent().addKeyListener(new KeyAdapter() {
        	
        	@Override
        	public void keyPressed(KeyEvent e) {

        		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        			
        			try {
        				
        				String s = module.getComponent().getText(module.getUserPositionStart(), module.getComponent().getDocument().getLength() - module.getUserPositionStart()) + CharacterConstants.NEW_LINE;
        				
        				queue.add(s);
        				
        				module.userReady();
        			} catch (BadLocationException e1) {
        				
        				e1.printStackTrace();
        			}
        		}
        	}
		});
    }

    @Override
    public int read() {
    	
        while(message == null || index >= message.length()) {
        	
            try {
            	
                message = queue.take();
                
                index = 0;
            } catch(InterruptedException e) {

            	e.printStackTrace();
            }
        }
        
        return message.charAt(index++);
    }
    
    @Override
	public int available() {
		
		int num = 0;
		
		if(message != null) num += message.length() - index;
		
		for(String s : queue) num += s.length();
		
		return num < 0 ? 0 : num;
	}
    
    @Override
	public synchronized void reset() {
		
		message = null;
		
		index = 0;
		
		queue.clear();
	}

    @Override
    public boolean markSupported() {
    	
        return false;
    }
    
    @Override
    public int read(byte[] b, int off, int len) {
    	
        int bytes_copied = 0;
        
        while(bytes_copied < 1) {
        	
            while(message == null || index >= message.length()) {
            	
                try {
                	
                    if(message != null) index -= message.length();
                    
                    message = queue.take();
                    
                } catch(InterruptedException e) {

                	e.printStackTrace();
                }
            }
            
            int bytes_to_copy = len < message.length() - index ? len : message.length() - index;
            
            System.arraycopy(message.getBytes(), index, b, off, bytes_to_copy);
            
            index += bytes_to_copy;
            
            bytes_copied += bytes_to_copy;
        }
        
        return bytes_copied;
    }

    @Override
    public int read(byte[] b) {
    	
        return read(b, 0, b.length);
    }
    
	@Override
	public void close() {
		
	}
    
/**
 * 
 * Adds in to queue the formatted to hyperlink message from specified string.
 * 
 * @param string String message to format.
 *
 */
	void enterHyperlink(String string) {
		
		queue.add(CharacterConstants.HYPERLINK_PREFIX + string + CharacterConstants.NEW_LINE);
	}
}

/**
 * 
 * ConsoleGUIOutputStream class is an implementation of {@link OutputStream} for working with GUI of console.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
class ConsoleGUIOutputStream extends OutputStream {
	
/**
 * 
 * Module which contains main GUI component to interact.
 * 
 */
    private ConsoleGUIModule module;
    
/**
 * 
 * Creates new output channel with GUI module.
 * 
 * @param module Console GUI module to connect output channel.
 *
 */
    public ConsoleGUIOutputStream(ConsoleGUIModule module) {
    	
        this.module = module;
    }

    @Override
    public void write(int b) {
       
    	write(new byte[] {(byte) b}, 0, 1);
    }
    
    @Override
    public void write(byte[] b, int off, int len) {
    	
        module.append(new String(b,off,len));
        module.getComponent().setCaretPosition(module.getComponent().getDocument().getLength());
        module.userReady();
    }

    @Override
    public void write(byte[] b) {
    	
        write(b, 0, b.length);
    }
    
    @Override
    public void close() {
    	
    }
}