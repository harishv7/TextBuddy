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
		assertEquals("added to " + fileName + ": \"This is TextBuddy\"", TextBuddy.runCommand(TextBuddy.CommandType.ADD,"add This is TextBuddy"));
		assertEquals("added to " + fileName + ": \"TextBuddy is Great!\"", TextBuddy.runCommand(TextBuddy.CommandType.ADD,"add TextBuddy is Great!"));
		assertEquals("added to " + fileName + ": \"TextBuddy is amazing\"", TextBuddy.runCommand(TextBuddy.CommandType.ADD,"add TextBuddy is amazing"));
		assertEquals("added to " + fileName + ": \"With Great Power Comes Great Responsibility\"", TextBuddy.runCommand(TextBuddy.CommandType.ADD,"add With Great Power Comes Great Responsibility"));
		assertEquals("added to " + fileName + ": \"Patience is Virtue\"", TextBuddy.runCommand(TextBuddy.CommandType.ADD,"add Patience is Virtue"));
	}
	
	@Test
	public void testDisplay() throws IOException {
		TextBuddy.runCommand(TextBuddy.CommandType.DISPLAY, "display");
		assertEquals("added to " + fileName + ": \"Hello World\"", TextBuddy.runCommand(TextBuddy.CommandType.ADD,"add Hello World"));
		TextBuddy.runCommand(TextBuddy.CommandType.DISPLAY, "display");
	}
	
	@Test
	public void testDelete() throws IOException {
		// delete from an empty file
		TextBuddy.runCommand(TextBuddy.CommandType.CLEAR, "clear");
		assertEquals(fileName + " is empty", TextBuddy.runCommand(TextBuddy.CommandType.DELETE, "delete 1"));
		
		// delete an invalid line
		TextBuddy.runCommand(TextBuddy.CommandType.ADD, "add Performing unit testing now");
		assertEquals("You have provided an invalid line number.", TextBuddy.runCommand(TextBuddy.CommandType.DELETE, "delete 2"));
	}
}
