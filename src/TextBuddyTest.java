import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {
	private static final String[] fileNameArray = {"mytestfile.txt"};
	private static final String fileName = fileNameArray[0];
	private static TextBuddy myTextBuddy;
	
	@Before
	public void initialiseProgram() throws IOException {
		myTextBuddy = new TextBuddy(fileNameArray);
	}
	
	@Test
	public void testexecuteCommands() throws IOException {
		// test add function
		assertEquals("added to " + fileName + ": \"This is TextBuddy\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.ADD,"add This is TextBuddy"));
		assertEquals("added to " + fileName + ": \"TextBuddy is Great!\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.ADD,"add TextBuddy is Great!"));
		assertEquals("added to " + fileName + ": \"TextBuddy is amazing\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.ADD,"add TextBuddy is amazing"));
		assertEquals("added to " + fileName + ": \"With Great Power Comes Great Responsibility\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.ADD,"add With Great Power Comes Great Responsibility"));
		assertEquals("added to " + fileName + ": \"Patience is Virtue\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.ADD,"add Patience is Virtue"));
		
		// test clear function
		assertEquals("all content deleted from " + fileName, 
				myTextBuddy.executeCommand(TextBuddy.CommandType.CLEAR, "clear"));
		
		// test display function
		assertEquals("added to " + fileName + ": \"Hello World\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.ADD,"add Hello World"));
		assertEquals("1. Hello World", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.DISPLAY, "display"));
		
		// test deleting from an empty file
		myTextBuddy.executeCommand(TextBuddy.CommandType.CLEAR, "clear");
		assertEquals(fileName + " is empty", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.DELETE, "delete 1"));
		
		// test deleting an invalid line
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Performing unit testing now");
		assertEquals("You have provided an invalid line number.", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.DELETE, "delete 2"));
		
		// test deleting a valid line
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Adding another line");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Adding a third line");
		assertEquals("deleted from " + fileName + ": \"Performing unit testing now\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.DELETE, "delete 1"));
		assertEquals("deleted from " + fileName + ": \"Adding a third line\"", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.DELETE, "delete 2"));
		
		// Test Driven Development (TDD) for CE2
		// test sorting an empty file
		myTextBuddy.executeCommand(TextBuddy.CommandType.CLEAR, "clear");
		assertEquals(fileName + " has nothing to sort.", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.SORT, "sort"));
		
		// test sorting a non-empty file according to alphabetical order
		// add sample lines
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Camel");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Penguin");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Buffalo");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Hippopotamus");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Meerkat");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Reindeer");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Zebra");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Shark");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Gorilla");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Rabbit");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Koala");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Flamingo");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Elephant");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Mouse");
				
		assertEquals(fileName + " has been sorted alphabetically.", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.SORT, "sort"));
		
		// test sort - if the contents have also been sorted properly
		assertEquals("1. Buffalo\n2. Camel\n3. Elephant\n4. Flamingo\n5. Gorilla\n6. "
				+ "Hippopotamus\n7. Koala\n8. Meerkat\n9. Mouse\n10. Penguin\n11. " 
				+ "Rabbit\n12. Reindeer\n13. Shark\n14. Zebra", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.DISPLAY, "display"));
		
		// test sort - sort lines with mixed cases
		myTextBuddy.executeCommand(TextBuddy.CommandType.CLEAR, "clear");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Flamingo");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add zebra");
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add elephants");
		assertEquals(fileName + " has been sorted alphabetically.", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.SORT, "sort"));
		assertEquals("1. elephants\n2. Flamingo\n3. zebra", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.DISPLAY, "display"));
		
		// test search method - on a non-empty file
		myTextBuddy.executeCommand(TextBuddy.CommandType.ADD, "add Elephants are huge!");
		assertEquals("The following lines contain: Elephants\n4. Elephants are huge!", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.SEARCH, "search Elephants"));
		
		// test search method - search word does not exist in file
		assertEquals("There are no lines containing: aeroplanes", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.SEARCH, "search aeroplanes"));
		
		// test search method - on an empty file
		myTextBuddy.executeCommand(TextBuddy.CommandType.CLEAR, "clear");
		assertEquals(fileName + " is empty. There are no lines to search.", 
				myTextBuddy.executeCommand(TextBuddy.CommandType.SEARCH, "search elephants"));
	}
}
