package net.krows_team.console.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import net.coretools.core.OnDevelopment;
import net.coretools.core.Script;
import net.coretools.core.ThreadTools;
import net.coretools.core.argument.Argument;
import net.coretools.core.argument.VariableDescription;
import net.coretools.core.file.FileTools;
import net.krows_team.console.character.CharacterConstants;
import net.krows_team.console.io.IO;

/**
 * 
 * Console class is main project class which contains custom console system. 
 * Also in the project contains implementation of command line by using this console.
 * 
 * @since 1.0.0
 *
 * @author Krows
 *
 */
public class Console implements CharacterConstants {
	
/**
 * 
 * Start message types at first in output.
 * 
 */
	private static final File START_MESSAGE_FILE = new File("res/start.msg");
	
/**
 * 
 * Default root message of user ready status.
 * 
 */
	protected static final String ROOT_NAME = "root";
	
/**
 * 
 * Message of user ready status.
 * 
 */
	protected String message = ROOT_NAME + " > ";
	
/**
 * 
 * I/O channel for interaction with programs.
 * 
 */
	protected IO io;
	
/**
 * 
 * Map contains commands and its scripts for executing.
 * 
 */
	protected Map<VariableDescription, Script> scriptMap;
	
/**
 * 
 * Scanner which contains all the current command parameters.
 * 
 */
	protected Scanner commandScanner;
	
/**
 * 
 * Script executes when appears request for executing of command.
 * 
 */
	protected Script commandRequest;
	
/**
 * 
 * Indicates whether user can write commands.
 * 
 */
	protected boolean ready;
	
/**
 * 
 * Creates new Console and IO system by specified {@link IO} object.
 * 
 * @param io IO for maintaining I/O system between console and the program.
 * 
 */
	@OnDevelopment
	public Console(IO io) {
		
		this.io = io;
		
		scriptMap = new HashMap<>();
		scriptMap.put(get("exit", "Closes current console."), () -> System.exit(0));
		scriptMap.put(get("time", "Shows current machine time."), () -> echo0(LocalTime.now().toString()));
		scriptMap.put(get("help", "Shows all available console commands."), () -> scriptMap.keySet().forEach(command -> echo0(command.toString())));
		
		setDefaultCommandRequest();
	}
	
/**
 * 
 * Starts interacting with program.
 *
 */
	@OnDevelopment
	public void start() {
		
		if(START_MESSAGE_FILE.exists()) echo(FileTools.getText(START_MESSAGE_FILE));
		else echo("Terminal Console.");
		
		ThreadTools.createThread(() -> {
			
			while(true) {
				
				try(BufferedReader bufferReader = new BufferedReader(new InputStreamReader(io.getInputStream()))) {
					
					commandScanner = new Scanner(bufferReader.readLine());
					
					commandRequest.execute();
					
					if(!ready) echoRoot();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}, new Argument("-START"));
	}
	
/**
 * 
 * Creates new {@link VariableDescription} object of specified command and its description. 
 * This method usually is used for putting new command into command list. 
 * If description contains empty string or even is null, then is better to use {@link #empty(String)} method.
 * 
 * @param command Name of command.
 * @param desc String description of specified command.
 * 
 * @return VariableDescription object of specified command and its description.
 *
 */
	protected VariableDescription get(String command, String desc) {
		
		return new VariableDescription(command, desc) {
			
			@Override
			public String toString() {
			
				return String.format("%s - %s", var, description);
			}
		};
	}
	
/**
 * 
 * Creates new {@link VariableDescription} object with empty command description. This method usually is used for putting new command into command list.
 * It's important to know that description is contains empty string, but not null.
 * 
 * @param command Name of command.
 * 
 * @return VariableDescription object of specified command with empty description.
 *
 */
	protected VariableDescription empty(String command) {
		
		return new VariableDescription(command, "") {
			
			@Override
			public String toString() {
			
				return String.format("%s", var);
			}
		}; 
	}
	
/**
 * 
 * Makes "echo" command of root message.
 * 
 */
	private void echoRoot() {
		
		echo0(message, false);
		userReady();
	}
	
/**
 * 
 * Makes "echo" command of specified string.
 * 
 * @param string String for sending.
 *
 */
	protected void echo0(String string) {
		
		echo0(string, true);
	}
	
/**
 * 
 * Makes "echo" command of specified string.
 * 
 * @param string String for sending.
 * @param newLine Indicates whether it needs to make new line.
 *
 */
	private void echo0(String string, boolean newLine) {
		
		try {
			
			io.getOutputStream().write(string.concat(newLine ? NEW_LINE : "").getBytes());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
/**
 * 
 * Makes "echo" command of specified string and sets "user ready" status.
 * 
 * @param string String for sending.
 *
 */
	public void echo(String string) {
		
		echo0(string);
		echoRoot();
	}
	
/**
 * 
 * Sets ready status to "true".
 *
 */
	protected void userReady() {
		
		ready = true;
	}
	
/**
 * 
 * Sets as user ready message the default root message.
 * 
 */
	protected void setRoot() {
		
		setMessage(ROOT_NAME);
	}
	
/**
 * 
 * Sets as user ready message the specified String message.
 * 
 * @param msg String message of user ready status.
 *
 */
	protected void setMessage(String msg) {
		
		this.message = msg + " > ";
	}
	
/**
 * 
 * Sets script for executing when appears request for executing the command.
 * 
 * @param script Script for executing when appears request for executing the command.
 * 
 */
	protected void setCommandRequest(Script script) {
		
		this.commandRequest = script;
	}
	
/**
 * 
 * Sets default script for executing when appears request for executing the command.
 * 
 */
	protected void setDefaultCommandRequest() {
		
		this.commandRequest = () -> {
			
			boolean isCommand = false;
		
			ready = false;
			
			if(commandScanner.hasNext()) {
				
				String command = commandScanner.next();
				
				try {
				
					for(VariableDescription command0 : scriptMap.keySet()) {
						
						if(command0.getVariable().contentEquals(command)) {
							
							isCommand = true;
							
							scriptMap.get(command0).execute();
						}
					}
				} catch(NoSuchElementException e) {
					
					echo(String.format("\"%s\" command needs more parameters.", command));
				} catch(NumberFormatException e) {
					
					echo(String.format("Entered wrong parameters for \"%s\" command.", command));
				}
				
				if(!isCommand) {
					
					if(command.startsWith(HYPERLINK_PREFIX)) echo(command.substring(HYPERLINK_PREFIX.length()));
					else echo0(String.format("\"%s\" is not a console command.", command));
				}
			}
		};
	}
}