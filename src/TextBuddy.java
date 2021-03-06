import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * TextBuddy is a Command Line Interface (CLI) program which assists the user to
 * write and save text onto a file saved to the disk. TextBuddy also helps to
 * delete specific lines of text from the file and clears the file's contents
 * when instructed to. The instructions are delivered in the form of commands,
 * namely, "display", "add", "delete", "clear", "exit".
 * The command format is given by the interaction below:
 	Welcome to TextBuddy. mytextfile.txt is ready for use
 	command: add This is TextBuddy!
 	added to mytextfile.txt: "This is TextBuddy!"
 	command: display
 	1. This is TextBuddy!
 	command: add TextBuddy is Great!
 	added to mytextfile.txt: "TextBuddy is Great!"
 	command: display
 	1. This is TextBuddy!
 	2. TextBuddy is Great!
 	command: delete 1
 	deleted from mytextfile.txt: "This is TextBuddy!"
 	command: display
 	1. TextBuddy is Great!
	command: clear
	all content deleted from mytextfile.txt
	command: display
	mytextfile.txt is empty
	command: add hello world!
	added to mytextfile.txt: "hello world!"
	command: search world
	The following lines contain: world
	1. hello world
	command: add apples are great
	added to mytextfile.txt: "apples are great"
	command sort
	mytextfile.txt has been sort alphabetically.
	command: display
	1. apples are great
	2. hello world!
	command: exit
 * Notes to user:
 * 1. This program assumes that the user input is valid, especially
 * 	  for file handling related functions. When an invalid file or error during
 * 	  file writing or reading occurs, the program will throw an IOException.
 * 2. This program assumes that the line number corresponding to each line of
 * 	  text need not be written to the text file itself. The program will reflect
 * 	  the line number during the operation of TextBuddy only.
 * @author Venkatesan Harish, A0121828H
 * 
 */

public class TextBuddy {

	// List of Output Messages
	private final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	private final String MESSAGE_ERROR_INVALID_FILE_NAME = "File name provided is invalid. TextBuddy will exit now.";
	private final String MESSAGE_ADDED = "added to %1$s: \"%2$s\""; 
	private final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private final String MESSAGE_CLEARED = "all content deleted from %s";
	private final String MESSAGE_ERROR_INVALID_COMMAND = "You have provided an invalid command. Please try again.";
	private final String MESSAGE_ERROR_INVALID_LINE_TO_DELETE = "You have provided an invalid line number.";
	private final String MESSAGE_ERROR_INVALID_DELETE_COMMAND = "You have provided an invalid/incomplete delete command.";
	private final String MESSAGE_SORT_SUCCESS = "%s has been sorted alphabetically.";
	private final String MESSAGE_SORT_EMPTY_FILE = "%s has nothing to sort.";
	private final String MESSAGE_SEARCH_KEYWORD_SUCCESS = "The following lines contain: %s";
	private final String MESSAGE_SEARCH_KEYWORD_FAILED = "There are no lines containing: %s";
	private final String MESSAGE_SEARCH_EMPTY_FILE = "%s is empty. There are no lines to search.";
	private final String MESSAGE_ERROR_INVALID_SEARCH_COMMAND = "You have provided an invalid search command.";
	private final String MESSAGE_PROMPT_USER = "command: ";
	private final String MESSAGE_FILE_EMPTY = "%s is empty";
	private final String MESSAGE_EXIT_PROGRAM = "TextBuddy is closing...";

	// List of Commands
	enum CommandType {
		ADD, DELETE, DISPLAY, CLEAR, SORT, SEARCH, INVALID, EXIT
	};

	// List of integer constants used in parsing/manipulating file
	// The numbers refer to the index that the relevant number/name starts from
	private final int INDEX_OF_FILE_NAME = 0;
	private final int INDEX_OF_USER_COMMAND = 0;
	private final int INDEX_OF_LINE_NUMBER = 7;
	private final int INDEX_OF_LINE_TO_ADD = 4;
	private final int INDEX_OF_SEARCH_WORD = 7;
	
	// Integer constant used to reflect empty files/lines
	private final int EMPTY = 0;
	
	// List of constants used to reflect error or successful exit
	private final int SYSTEM_EXIT_SUCCESS = 0;
	private final int SYSTEM_EXIT_WITH_ERROR = -1;
	private final int NUMBER_FORMAT_EXCEPTION_ERROR = -1;
	
	// List of static class variables
	private File textFile;
	private String fileName;
	private Scanner scanner;
	private boolean shouldExitProgram = false;
	
	// This array stores the lines of text in a file
	private ArrayList<String> textStorage;
	
	/*
	 * This is the constructor for TextBuddy. This constructor
	 * instantiates a TextBuddy object with the filename provided,
	 * displays a welcome message and gets the file for storage ready.
	 * 
	 * @param args	This is the argument provided by user.
	 */
	public TextBuddy(String[] args) throws IOException {
		validateArguments(args);
		printWelcomeMessage();
		getFileReady();
	}

	public static void main(String[] args) throws IOException {
		TextBuddy myTextBuddy = new TextBuddy(args);
		myTextBuddy.getInputUntilUserExits();
	}

	/*
	 * This operation checks if the given argument if a valid file name. 
	 * If invalid, that is, no file name is provided, TextBuddy will exit.
	 * 
	 * @param args This is the argument as provided by the user when starting
	 * 			   TextBuddy.
	 */
	private void validateArguments(String[] args) {
		if (args.length == EMPTY) {
			displayMessage(MESSAGE_ERROR_INVALID_FILE_NAME);
			System.exit(SYSTEM_EXIT_WITH_ERROR);
		} else {
			fileName = args[INDEX_OF_FILE_NAME];
		}
	}

	private void printWelcomeMessage() {
		System.out.println(String.format(MESSAGE_WELCOME, fileName));
	}

	/*
	 * This operation gets the system ready by initializing the text file,
	 * scanner object and the arraylist to store the text.
	 * If the text file does not already exist, it creates a new file.
	 */
	 private void getFileReady() throws IOException {
		textFile = new File(fileName);
		if (!textFile.exists()) {
			textFile.createNewFile();
		}
		scanner = new Scanner(System.in);
		textStorage = new ArrayList<String>();
		loadTextFromFile();
	}

	/* 
	 * This operation loads the existing text from the file into textStorage arraylist.
	 */
	private void loadTextFromFile() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(textFile));
		String line;
		while ((line = reader.readLine()) != null) {
			textStorage.add(line);
		}
		reader.close();
	}

	private void displayMessage(String message) {
		System.out.println(message);
	}

	private void getInputUntilUserExits() throws IOException {
		String userInput;
		while (!shouldExitProgram){
			promptUserForInput();
			userInput = getUserInput();
			CommandType userCommand = getUserCommand(userInput);
			String textToDisplay = runCommand(userCommand, userInput);
			displayMessage(textToDisplay);
		}
		System.exit(SYSTEM_EXIT_SUCCESS);
	}

	private void promptUserForInput() {
		System.out.print(MESSAGE_PROMPT_USER);
	}

	private String getUserInput() {
		String userInput = scanner.nextLine();
		return userInput.trim();
	}

	private CommandType getUserCommand(String userInput) {
		String[] userInputStringArray = userInput.split(" ");
		String command = userInputStringArray[INDEX_OF_USER_COMMAND];
		switch (command) {
		  case "add" :
			  return CommandType.ADD;
		  case "delete" :
			  return CommandType.DELETE;
		  case "clear" :
			  return CommandType.CLEAR;
		  case "display" :
			  return CommandType.DISPLAY;
		  case "sort" :
			  return CommandType.SORT;
		  case "search" : 
			  return CommandType.SEARCH;
		  case "exit" :
			  return CommandType.EXIT;
		  default :
			  return CommandType.INVALID;
		}
	}

	/*
	 * This operation determines what the user wishes to execute and calls the relevant
	 * methods to manipulate the text file.
	 * 
	 * @param userCommand  is the first word of the user's input command.
	 * @param userInput	   is the whole string representing the user's entire command.
	 * @throws IOException when there is a problem in manipulating the file. 
	 */
	 String runCommand(CommandType userCommand, String userInput) throws IOException {
		switch (userCommand) {
		  case ADD :
			  return addToFile(userInput);
		  case DELETE :
			  return deleteFromFile(userInput);
		  case CLEAR :
			  return clearFile();
		  case DISPLAY :
			  return displayFileContents();
		  case SORT :
			  return sortFileContents();
		  case SEARCH : 
			  return searchFileContents(userInput);
		  case EXIT :
			  return exitTextBuddy();
		  case INVALID :
			  return errorInCommand();
		  default :
			  return "";
		}
	}
	 
	 /* 
	  * This method searches the file/storage to check for the search word and
	  * returns a string containing the lines with the search word. It returns an
	  * appropriate error message if the command is incomplete or no lines were found.
	  * The search word is case sensitive. (i.e.: Apples ≠ apples)
	  * 
	  * @param userInput	is the user given input containing the details of the search word. 
	  */
	 private String searchFileContents(String userInput) {
		 try {
	         String searchWord = userInput.substring(INDEX_OF_SEARCH_WORD);
			 String linesContainingSearchWord = "";
			 boolean isSearchWordFound = false;

			 for (int i = 0; i < textStorage.size(); i++) {
				 if (textStorage.get(i).contains(searchWord)) {
					 linesContainingSearchWord += (i+1) + ". " + textStorage.get(i) + "\n";
					 isSearchWordFound = true;
				 }
			 }
			 if (isSearchWordFound) {
				 int lengthOfLinesWithSearchWord = linesContainingSearchWord.length();
				 return displaySearchSuccessMessage(searchWord) + "\n" 
						 + linesContainingSearchWord.substring(0, lengthOfLinesWithSearchWord-1);
			 } else {
				 return displaySearchFailedMessage(searchWord);
			 }
		 } catch (StringIndexOutOfBoundsException exception) {
			 return displayInavlidSearchMessage();
		 }
	 }

	private String displayInavlidSearchMessage() {
		return MESSAGE_ERROR_INVALID_SEARCH_COMMAND;
	}

	private String displaySearchFailedMessage(String searchWord) {
		if (textStorage.isEmpty()) {
			return displaySearchEmptyFileMessage();
		} else {
			return displaySearchNotFoundMessage(searchWord);
		}
	}

	private String displaySearchNotFoundMessage(String searchWord) {
		return String.format(MESSAGE_SEARCH_KEYWORD_FAILED, searchWord);
	}

	private String displaySearchSuccessMessage(String searchWord) {
		return String.format(MESSAGE_SEARCH_KEYWORD_SUCCESS, searchWord);
	}
	
	private String displaySearchEmptyFileMessage() {
		return String.format(MESSAGE_SEARCH_EMPTY_FILE, fileName);
	}
	
	/* 
	 * This method sorts the file contents in alphabetical order and employs
	 * a case-insensitive manner while sorting.
	 * 
	 * @throws IOException if there are problems in manipulating the textfile.
	 */
	private String sortFileContents() throws IOException {
		if (textStorage.isEmpty()) {
			return displayEmptyFileSortMessage();
		} else {
			Collections.sort(textStorage, String.CASE_INSENSITIVE_ORDER);
			saveFile();
			return displaySuccessfulSortMessage();
		}
	}

	private String displayEmptyFileSortMessage() {
		return String.format(MESSAGE_SORT_EMPTY_FILE, fileName);
	}

	private String displaySuccessfulSortMessage() {
		return String.format(MESSAGE_SORT_SUCCESS, fileName);
	}

	/*
	 * This operation deletes the user-specified line of text from the file.
	 * 
	 * @param userInput is the entire line of input as entered by the user.
	 * @throws IOException when there is a problem in manipulating/saving the file.
	 */
	private String deleteFromFile(String userInput) throws IOException {
		if (isDeleteCommandValid(userInput)) {
			int lineNumToDelete = getLineNumberToDelete(userInput);
			return deleteLine(lineNumToDelete);
		} else {
			return displayInvalidDeleteMessage();
		}
	}

	private boolean isDeleteCommandValid(String userInput) throws StringIndexOutOfBoundsException {
		try {
			Integer.parseInt(userInput.substring(INDEX_OF_LINE_NUMBER));
			return true;
		} catch (StringIndexOutOfBoundsException exception) {
			return false;
		}
	}

	private String displayInvalidDeleteMessage() {
		return MESSAGE_ERROR_INVALID_DELETE_COMMAND;
	}
	
	/* 
	 * This method deletes the specified line number from the storage/textfile
	 * 
	 * @param lineNumToDelete	is the line number as indicated by the user to delete.
	 * @throws IOException	when there are problems in manipulating/saving the external file.
	 */
	private String deleteLine(int lineNumToDelete) throws IOException {
		if (islineNumToDeleteValid(lineNumToDelete)) {
			String lineDeleted = textStorage.remove(lineNumToDelete - 1);
			saveFile();
			return displayDeleteSuccessMessage(lineDeleted);
		} else {
			return displayDeleteErrorMessage();
		}
	}
	
	/*
	 * This operation retrieves the line number as provided by the user.
	 * In the case of an exception where the input cannot be formatted, it
	 * returns NUMBER_FORMAT_EXCEPTION_ERROR which represents -1.
	 * 
	 * @param userInput is the entire line of input as entered by the user.
	 * @throws NumberFormatException when the user's input cannot be parsed into an integer.
	 */
	private int getLineNumberToDelete(String userInput) throws NumberFormatException {
		try {
			return Integer.parseInt(userInput.substring(INDEX_OF_LINE_NUMBER));
		} catch (NumberFormatException exception) {
			return NUMBER_FORMAT_EXCEPTION_ERROR;
		}
	}

	private boolean islineNumToDeleteValid(int lineNumToDelete) {
		if ((lineNumToDelete > 0) && (lineNumToDelete <= textStorage.size())) {
			return true;
		}
		return false;
	}

	private String displayDeleteErrorMessage() {
		if (textStorage.isEmpty()) {
			return displayFileEmptyMessage();
		} else {
			return displayInvalidLineMessage();
		}
	}

	private String displayInvalidLineMessage() {
		return MESSAGE_ERROR_INVALID_LINE_TO_DELETE;
	}

	private String displayDeleteSuccessMessage(String lineDeleted) {
		return String.format(MESSAGE_DELETED, fileName, lineDeleted);
	}

	private String clearFile() throws IOException {
		textStorage.clear();
		saveFile();
		return displayFileClearedMessage();
	}

	private String displayFileClearedMessage() {
		return String.format(MESSAGE_CLEARED, fileName);
	}

	private String errorInCommand() {
		return MESSAGE_ERROR_INVALID_COMMAND;
	}

	private String exitTextBuddy() {
		shouldExitProgram = true;
		return MESSAGE_EXIT_PROGRAM;
	}

	private String addToFile(String userInput) throws IOException {
		String lineToAdd = "";
		lineToAdd = userInput.substring(INDEX_OF_LINE_TO_ADD);
		textStorage.add(lineToAdd);
		saveFile();
		return displayAddSuccessMessage(lineToAdd);
	}

	private String displayAddSuccessMessage(String lineAdded) {
		return String.format(MESSAGE_ADDED, fileName, lineAdded);
	}

	private void saveFile() throws IOException {
		FileWriter writer = new FileWriter(textFile);
		for (int i = 0; i < textStorage.size(); i++) {
			writer.write(textStorage.get(i));
			if (i != textStorage.size() - 1) {
				writer.write("\n");
			}
			writer.flush();
		}		
		writer.close();
	}

	private String displayFileContents() {
		if (textStorage.size() == EMPTY) {
			return displayFileEmptyMessage();
		} else {
			String fileContent = "";
			for (int i = 0; i < textStorage.size(); i++) {
				int lineNumber = i+1;
				fileContent += lineNumber + ". " + textStorage.get(i);
				
				// Only add a newline character when it is not the last line to display
				if (!isAddingLastLine(i)) {
					fileContent += "\n";
				}
			}
			return fileContent;
		}
	}

	private boolean isAddingLastLine(int i) {
		if (i == textStorage.size() - 1) {
			return true;
		} else {
			return false;
		}
	}

	private String displayFileEmptyMessage() {
		return String.format(MESSAGE_FILE_EMPTY, fileName);
	}
}