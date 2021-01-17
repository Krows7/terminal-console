package net.krows_team.console.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * IO class represents the set of Input/Output channels for interacting with console.
 * 
 * @since 1.0.0
 *
 * @author Krows
 * 
 */
public class IO {

/**
 * 
 * Input channel.
 * 
 */
	protected InputStream input;
	
/**
 * 
 * Output channel.
 * 
 */
	protected OutputStream output;
	
/**
 * 
 * Creates new I/O set.
 * 
 * @param input Input channel of console.
 * @param output Output channel of console.
 *
 */
	public IO(InputStream input, OutputStream output) {
		
		this.input = input;
		
		this.output = output;
	}
	
/**
 * 
 * Creates new empty I/O set.
 * 
 */
	IO() {
		
	}
	
/**
 * 
 * Returns output channel of console.
 * 
 * @return Output channel of console.
 *
 */
	public OutputStream getOutputStream() {

		return output;
	}
	
/**
 * 
 * Returns input channel of console.
 * 
 * @return Input channel of console.
 *
 */
	public InputStream getInputStream() {

		return input;
	}
}