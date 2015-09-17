import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
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
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	private static final String MESSAGE_ERROR_INVALID_FILE_NAME = "File name provided is invalid. TextBuddy will exit now.";
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\""; 
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEARED = "all content deleted from %s";
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "You have provided an invalid command. Please try again.";
	private static final String MESSAGE_ERROR_INVALID_LINE_TO_DELETE = "You have provided an invalid line number.";
	private static final String MESSAGE_ERROR_INVALID_DELETE_COMMAND = "You have provided an invalid/incomplete delete command.";
	private static final String MESSAGE_PROMPT_USER = "command: ";
	private static final String MESSAGE_FILE_EMPTY = "%s is empty";
	private static final String MESSAGE_EXIT_PROGRAM = "TextBuddy is closing...";

	// List of Commands
	enum CommandType {
		ADD, DELETE, DISPLAY, CLEAR, INVALID, EXIT
	};

	// List of constants used to extract the correct parts of a string
	private static final int INDEX_OF_FILE_NAME = 0;
	private static final int INDEX_OF_USER_COMMAND = 0;
	private static final int INDEX_OF_LINE_NUMBER = 7;
	private static final int INDEX_OF_LINE_TO_ADD = 4;
	
	// List of constants used to reflect error or successful exit
	private static final int SYSTEM_EXIT_SUCCESS = 0;
	private static final int SYSTEM_EXIT_WITH_ERROR = -1;
	private static final int NUMBER_FORMAT_EXCEPTION_ERROR = -1;
	
	// List of static class variables
	private static File textFile;
	private static String fileName;
	private static Scanner scanner;
	private static boolean shouldExitProgram = false;
	
	// This array stores the lines of text in a file
	private static ArrayList<String> storeText;
	
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
	private static void validateArguments(String[] args) {
		if (args.length == 0) {
			displayMessage(MESSAGE_ERROR_INVALID_FILE_NAME);
			System.exit(SYSTEM_EXIT_WITH_ERROR);
		} else {
			fileName = args[INDEX_OF_FILE_NAME];
		}
	}

	private static void printWelcomeMessage() {
		System.out.println(String.format(MESSAGE_WELCOME, fileName));
	}

	/*
	 * This operation gets the system ready by initializing the text file,
	 * scanner object and the arraylist to store the text.
	 * If the text file does not already exist, it creates a new file.
	 */
	static void getFileReady() throws IOException {
		textFile = new File(fileName);
		if (!textFile.exists()) {
			textFile.createNewFile();
		}
		scanner = new Scanner(System.in);
		storeText = new ArrayList<String>();
		loadTextFromFile();
	}

	/* 
	 * This operation loads the existing text from the file into storeText arraylist.
	 */
	private static void loadTextFromFile() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(textFile));
		String line;
		while ((line = reader.readLine()) != null) {
			storeText.add(line);
		}
		reader.close();
	}

	private static void displayMessage(String message) {
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

	private static void promptUserForInput() {
		System.out.print(MESSAGE_PROMPT_USER);
	}

	private static String getUserInput() {
		String userInput = scanner.nextLine();
		return userInput.trim();
	}

	private static CommandType getUserCommand(String userInput) {
		String[] userInputStringArray = userInput.split(" ");
		String command = userInputStringArray[INDEX_OF_USER_COMMAND];
		switch (command){
		  case "add" :
			  return CommandType.ADD;
		  case "delete" :
			  return CommandType.DELETE;
		  case "clear" :
			  return CommandType.CLEAR;
		  case "display" :
			  return CommandType.DISPLAY;
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
	 * @param userCommand  is the first word of the user's input command
	 * @param userInput	   is the whole string representing the user's entire command
	 * @throws IOException when there is a problem in manipulating the file 
	 */
	static String runCommand(CommandType userCommand, String userInput) throws IOException {
		switch (userCommand) {
		  case ADD :
			  return addToFile(userInput);
		  case DELETE :
			  return deleteFromFile(userInput);
		  case CLEAR :
			  return clearFile();
		  case DISPLAY :
			  return displayFileContents();
		  case EXIT :
			  return exitTextBuddy();
		  case INVALID :
			  return errorInCommand();
		  default :
			  return "";
		}
	}

	/*
	 * This operation deletes the user-specified line of text from the file.
	 * 
	 * @param userInput is the entire line of input as entered by the user
	 * @throws IOException when there is a problem in manipulating/saving the file.
	 */
	private static String deleteFromFile(String userInput) throws IOException {
		if (isDeleteCommandValid(userInput)) {
			int lineToDelete = getLineNumberToDelete(userInput);
			return deleteLine(lineToDelete);
		} else {
			return displayInvalidDeleteMessage();
		}
	}

	private static boolean isDeleteCommandValid(String userInput) throws StringIndexOutOfBoundsException {
		try {
			Integer.parseInt(userInput.substring(INDEX_OF_LINE_NUMBER));
			return true;
		} catch (StringIndexOutOfBoundsException exception) {
			return false;
		}
	}

	private static String displayInvalidDeleteMessage() {
		return MESSAGE_ERROR_INVALID_DELETE_COMMAND;
	}

	private static String deleteLine(int lineToDelete) throws IOException {
		if (isLineToDeleteValid(lineToDelete)) {
			String lineDeleted = storeText.remove(lineToDelete - 1);
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
	 * @param userInput is the entire line of input as entered by the user
	 * @throws NumberFormatException when the user's input cannot be parsed into an integer.
	 */
	private static int getLineNumberToDelete(String userInput) throws NumberFormatException {
		try {
			return Integer.parseInt(userInput.substring(INDEX_OF_LINE_NUMBER));
		} catch (NumberFormatException exception) {
			return NUMBER_FORMAT_EXCEPTION_ERROR;
		}
	}

	private static boolean isLineToDeleteValid(int lineToDelete) {
		if ((lineToDelete > 0) && (lineToDelete <= storeText.size())) {
			return true;
		}
		return false;
	}

	private static String displayDeleteErrorMessage() {
		if (storeText.isEmpty()) {
			return displayFileEmptyMessage();
		} else {
			return displayInvalidLineMessage();
		}
	}

	private static String displayInvalidLineMessage() {
		return MESSAGE_ERROR_INVALID_LINE_TO_DELETE;
	}

	private static String displayDeleteSuccessMessage(String lineDeleted) {
		return String.format(MESSAGE_DELETED, fileName, lineDeleted);
	}

	private static String clearFile() throws IOException {
		storeText.clear();
		saveFile();
		return displayFileClearedMessage();
	}

	private static String displayFileClearedMessage() {
		return String.format(MESSAGE_CLEARED, fileName);
	}

	private static String errorInCommand() {
		return MESSAGE_ERROR_INVALID_COMMAND;
	}

	private static String exitTextBuddy() {
		shouldExitProgram = true;
		return MESSAGE_EXIT_PROGRAM;
	}

	private static String addToFile(String userInput) throws IOException {
		String lineToAdd = "";
		lineToAdd = userInput.substring(INDEX_OF_LINE_TO_ADD);
		storeText.add(lineToAdd);
		saveFile();
		return displayAddSuccessMessage(lineToAdd);
	}

	private static String displayAddSuccessMessage(String lineAdded) {
		return String.format(MESSAGE_ADDED, fileName, lineAdded);
	}

	private static void saveFile() throws IOException {
		FileWriter writer = new FileWriter(textFile);
		for (int i = 0; i < storeText.size(); i++) {
			writer.write(storeText.get(i));
			if (i != storeText.size() - 1) {
				writer.write("\n");
			}
			writer.flush();
		}		
		writer.close();
	}

	private static String displayFileContents() {
		if (storeText.size() == 0) {
			return displayFileEmptyMessage();
		} else {
			String fileContent = "";
			for(int i = 0; i < storeText.size(); i++) {
				int lineNumber = i+1;
				fileContent += lineNumber + ". " + storeText.get(i) + "\n";
			}
			return fileContent;
		}
	}

	private static String displayFileEmptyMessage() {
		return String.format(MESSAGE_FILE_EMPTY, fileName);
	}
	
	public static void setFileName(String name) {
		fileName = name;
	}
}