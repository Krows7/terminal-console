import net.coretools.core.Launcher;
import net.krows_team.console.core.Console;
import net.krows_team.console.io.IO;
import net.krows_team.console.view.application.ExtendedWindowConsole;

public class MainLauncher extends Launcher {

/**
 * 
 * User console.
 * 
 */
	private Console console;
	
	public MainLauncher(String... args) {
		
		for(String line : args) {
			
			switch(line) {
			
			case "/GUI" :
				
				console = new ExtendedWindowConsole();
				console.echo("<a href=It&nbsp;Works!>Test Hyperlink</a>");
				
				break;
			}
		}
		
		if(console == null) console = new Console(new IO(System.in, System.out));
	}
	
/**
 * 
 * The main method.
 * 
 * @param args Arguments for program.
 * 
 */
	public static void main(String... args) {
		
		Launcher launcher = new MainLauncher(args);
		launcher.launch();
	}

	@Override
	public void launch() {
		
	}
}