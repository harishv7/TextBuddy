import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TextBuddyCE1Test {
	private static final String fileName = "mytextfile.txt";
	
	@Before
	public void initialiseProgram() throws IOException {
		TextBuddy.setFileName(fileName);
		TextBuddy.getFileReady();
	}
	
	@Test
	public void testAdd() throws IOException {
		assertEquals("added to mytextfile.txt: \"This is TextBuddy\"", TextBuddy.runCommand(TextBuddy.CommandType.ADD,"add This is TextBuddy"));
	}
}
