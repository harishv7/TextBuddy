import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TextBuddyCE1Test {
	private static final String[] fileNameArray = {"mytextfile.txt"};
	private static final String fileName = fileNameArray[0];
	private static TextBuddy myTextBuddy;
	
	@Before
	public void initialiseProgram() throws IOException {
		myTextBuddy = new TextBuddy(fileNameArray);
	}
	
	@Test
	public void testRunCommands() throws IOException {
		// test add function
		assertEquals("added to " + fileName + ": \"This is TextBuddy\"", myTextBuddy.runCommand(TextBuddy.CommandType.ADD,"add This is TextBuddy"));
		assertEquals("added to " + fileName + ": \"TextBuddy is Great!\"", myTextBuddy.runCommand(TextBuddy.CommandType.ADD,"add TextBuddy is Great!"));
		assertEquals("added to " + fileName + ": \"TextBuddy is amazing\"", myTextBuddy.runCommand(TextBuddy.CommandType.ADD,"add TextBuddy is amazing"));
		assertEquals("added to " + fileName + ": \"With Great Power Comes Great Responsibility\"", myTextBuddy.runCommand(TextBuddy.CommandType.ADD,"add With Great Power Comes Great Responsibility"));
		assertEquals("added to " + fileName + ": \"Patience is Virtue\"", myTextBuddy.runCommand(TextBuddy.CommandType.ADD,"add Patience is Virtue"));
		
		// test display function
		myTextBuddy.runCommand(TextBuddy.CommandType.DISPLAY, "display");
		assertEquals("added to " + fileName + ": \"Hello World\"", myTextBuddy.runCommand(TextBuddy.CommandType.ADD,"add Hello World"));
		myTextBuddy.runCommand(TextBuddy.CommandType.DISPLAY, "display");
		
		// test clear function
		assertEquals("all content deleted from " + fileName, myTextBuddy.runCommand(TextBuddy.CommandType.CLEAR, "clear"));
		
		// test deleting from an empty file
		myTextBuddy.runCommand(TextBuddy.CommandType.CLEAR, "clear");
		assertEquals(fileName + " is empty", myTextBuddy.runCommand(TextBuddy.CommandType.DELETE, "delete 1"));
		
		// test deleting an invalid line
		myTextBuddy.runCommand(TextBuddy.CommandType.ADD, "add Performing unit testing now");
		assertEquals("You have provided an invalid line number.", myTextBuddy.runCommand(TextBuddy.CommandType.DELETE, "delete 2"));
		
		// test deleting a valid line
		myTextBuddy.runCommand(TextBuddy.CommandType.ADD, "add Adding another line");
		myTextBuddy.runCommand(TextBuddy.CommandType.ADD, "add Adding a third line");
		assertEquals("deleted from " + fileName + ": \"Performing unit testing now\"", myTextBuddy.runCommand(TextBuddy.CommandType.DELETE, "delete 1"));
		assertEquals("deleted from " + fileName + ": \"Adding a third line\"", myTextBuddy.runCommand(TextBuddy.CommandType.DELETE, "delete 2"));
	}
}
