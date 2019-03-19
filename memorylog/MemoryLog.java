package memorylog;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;

class MemoryLog {

	ArrayList<Item> entries;
	File itemList;
	int historySize = 10;

	//Used to determine what day is today. Used with viewTodaysEntries().
	LocalDate date;

	public MemoryLog() throws java.io.FileNotFoundException {
		entries = new ArrayList<Item>();
		itemList = new File("memorylog/auto_memory_log.txt");
		date = LocalDate.now();
		loadEntries();
	}

	//Takes the information from a file and populates the ArrayList with Item objects from it.
	public void loadEntries() {
		Scanner s = null;
		boolean noExceptionThrown = true;

		//Remove all current entries.
		while (entries.size() != 0) {
			entries.remove(0);
		}

		//Open the input stream.
		try {
			s = new Scanner(itemList);
		}
		catch (java.io.FileNotFoundException e) {
			System.out.println("Could not load " + itemList.getAbsolutePath());
			noExceptionThrown = false;
		}

		if (noExceptionThrown) {

			//Create temporary holders for all of the values in each line of the file.
			Quiz tempQuiz = null;
			int tempAddThis = 0;
			int tempYear = 0;
			int tempMonth = 0;
			int tempDay = 0;
			String tempTitle = null;
			ArrayList<Integer> tempHistory = new ArrayList<Integer>();
			boolean tempToggleable = false;
			ArrayList<String> tempModifiers = new ArrayList<String>();
			int tempModifierIdentifier = 0;
			boolean tempRecurring = false;
			int tempRecurringInt = 0;
			String record = null;

			//While there is still information in the file.
			while (s.hasNextLine()) {
				record = s.nextLine();
				
				//Allow full-line comments
				while (record.charAt(0) == '#') {
					record = s.nextLine();
				}
				
				StringBuilder sb = new StringBuilder();
				int offset = 0;

				//Get the addThis value.
				for(;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				tempAddThis = Integer.parseInt(sb.toString());
				offset++;
				sb = new StringBuilder();

				//Get the history of this entry
				for(;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				String[] history;
				if (!sb.toString().equals("null")) {
					history = sb.toString().split(","); 
					if (history.length > historySize) {
						throw new java.lang.ArrayIndexOutOfBoundsException();
					}
					for (int i = 0;i<history.length;i++) {
						tempHistory.add(Integer.parseInt(history[i]));
					}
				}
				offset++;
				sb = new StringBuilder();

				//Get the year from the file.
				for (;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				tempYear = Integer.parseInt(sb.toString());
				offset++;
				sb = new StringBuilder();

				//Get the month from the file.
				for (;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				tempMonth = Integer.parseInt(sb.toString());
				offset++;
				sb = new StringBuilder();

				//Get the day from the file.
				for (;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				tempDay = Integer.parseInt(sb.toString());
				offset++;
				sb = new StringBuilder();

				//Get the title from the file.
				for (;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				tempTitle = sb.toString();
				offset++;
				sb = new StringBuilder();

				//Get the toggleable from file
				for (;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				if (sb.toString().equals("1")) {
					tempToggleable = true;
				}
				else {
					tempToggleable = false;
				}
				offset++;
				sb = new StringBuilder();

				//Get the modifiers from the file.
				for (;record.charAt(offset) != '.';offset++) {
					for (;record.charAt(offset) != '\t';offset++) {
						sb.append(record.charAt(offset));
					}
					tempModifiers.add(sb.toString());
					sb = new StringBuilder();
				}
				offset+=2;
				sb = new StringBuilder();

				//Get the modifierIdentifier from the file
				for (;record.charAt(offset) != '\t';offset++) {
					sb.append(record.charAt(offset));
				}
				tempModifierIdentifier = Integer.parseInt(sb.toString());
				offset++;
				sb = new StringBuilder();

				//Get the recurring flag from the file
				while(offset < record.length()) {
					sb.append(record.charAt(offset));
					offset++;
				}
				tempRecurringInt = Integer.parseInt(sb.toString());
				tempRecurring = tempRecurringInt == 0 ? false : true;

				entries.add(new Item(tempQuiz, tempHistory, tempAddThis,
				            new OurDate(tempDay, tempMonth, tempYear), tempTitle,
				            tempToggleable, tempModifiers, tempModifierIdentifier,
				            tempRecurring));
				tempModifiers = new ArrayList<String>();
				tempHistory = new ArrayList<Integer>();
			}
		}
	}

	/*
	 * Takes the information stored in the entries ArrayList and writes it to a file to be read
	 * by loadEntries or by a user on the computer.
	 */
	public void saveEntries() {
		if (entries.size() > 0) {
			//Arrange the entries in the proper order before saving.
			Collections.sort(entries, new DateComparator());
			PrintWriter p = null;
			try {
				p = new PrintWriter(itemList);
			}
			catch (java.io.FileNotFoundException e) {
				System.out.println("There was a problem when writing to" +
						   " the file.");
			}

			for (int i = 0;i<entries.size();i++) {
				if (i == entries.size()-1) {
					p.printf("%s", entries.get(i).toRecord());	
				}
				else {
					p.printf("%s\n", entries.get(i).toRecord());
				}
			}
			p.close();
		}
		else {
			System.out.println("No entries to save.");
		}
	}

	//Changes an entry, adding days to the reviewOn and incrementing the modifier identifier.
	public void processIndex(Item passedItem, int addThis, boolean messages, boolean isRecurring) {
		int refinedAddThis = 0;

		/* Only refine addThis if it is not recurring. */
		if(!isRecurring) {
			refinedAddThis = refineAddThis(addThis, messages);
			passedItem.setAddThis(refinedAddThis);
		}
		
		//Set date to current date, start to add days.
		OurDate today = new OurDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
		passedItem.setReviewOn(today);
		for (int i = 0;i<passedItem.getAddThis();i++) {
			passedItem.getReviewOn().addOne();
		}

		//reset the addThis to what it should have been originally - so that the refinement
		//doesn't keep shrinking it each time.
		passedItem.setAddThis(addThis);
		
		//Change modifierIdentifier if entry has modifiers.
		if (passedItem.getToggleable()) {
			int holder = passedItem.getModifierIdentifier();
			if (holder < passedItem.getModifiers().size()) {
				passedItem.setModifierIdentifier(holder+1);
			}
			else {
				passedItem.setModifierIdentifier(1);
			}
		}
		
	}

	/* adjusts an addThis value to help even out items to complete to prevent days with lots.
	 * It accomplishes this by finding a range of days that the entry could be placed in based
	 * on the passed addThis value. The algorithm is x +- (x * 0.25). So an add this of 8 could
	 * change to anywhere between 6 and 10. It will choose the day that has the fewest
	 * entries in that range of days surrounding the original addThis. */
	public int refineAddThis(int addThis, boolean messages) {
		int min = addThis;
		int max = addThis;
		int change = 0;
		int updatedAddThis = addThis;
		int numItemsOnTestDay = 0;
		int minItemsOnTestDay = 0;
		OurDate today = new OurDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
		Item testItem = new Item();
		testItem.setReviewOn(today);

		/* decide what the min and max should be, accounting for special cases that are too
		 * small for the algorithm to make sense. */
		switch(addThis) {
			case 1:
				min = 1;
				max = 1;
				break;
			case 2:
				min = 2;
				max = 3;
				break;
			case 3:
				min = 2;
				max = 4;
				break;
			default:
				change = (int)((float)addThis * 0.25);
				min = addThis-change;
				max = addThis+change;
		}

		/* Try each date in the range and find the one with the least number of items. */
		for(int i = min;i<max+1;i++) {
			numItemsOnTestDay = 0;
			today.setDay(date.getDayOfMonth());
			today.setMonth(date.getMonthValue());
			today.setYear(date.getYear());
			for(int j = 0;j<i;j++) {
				today.addOne();
			}
			for(int j = 0;j<entries.size();j++) {
				if(entries.get(j).getReviewOn().calcDays() == today.calcDays()) {
					numItemsOnTestDay++;
				}
			}

			if(i == min) {
				minItemsOnTestDay = numItemsOnTestDay;
				updatedAddThis = i;
			} else if(numItemsOnTestDay < minItemsOnTestDay) {
				minItemsOnTestDay = numItemsOnTestDay;
				updatedAddThis = i;
			}
		}
		if(messages && updatedAddThis != addThis) {
			System.out.println("Refined addThis is " + updatedAddThis);
		}

		return updatedAddThis;
	}

	/*
	* Main part of the program, offers a menu that the user can choose options from. Allows
	* the user to view their entries, move them, and exit.
	* returns 1 on error, 0 otherwise.
	*/
	public int run(String[] args) {

		if(args.length < 1) {
			System.out.println("Wrong number of arguments.");
			return 1;
		}

		switch(args[0]) {
		case "add":
			if(add(args) != 0) {
				return 1;
			}
			break;
		case "delete":
			if(delete(args) != 0) {
				return 1;
			}
			break;
		case "process":
			if(process(args) != 0) {
				return 1;
			}
			break;
		case "show":
			if(show(args) != 0) {
				return 1;
			}
			break;
		}
		return 0;
	}

	/*
	* Takes a list of argements from main(). Parses arguments for applicable arguments to the
	* add command and adds a new entry into the entries ArrayList. Returns 1 on error,
	* 0 otherwise.
	*/
	int add(String[] args) {
		//possible command line arguments
		String title = null;
		OurDate reviewDate = null;
		int addThis = 0;
		boolean isRecurring = false;
		String modifiersString = null;
		ArrayList<String> modifiers = new ArrayList<String>();
		int startModifier = 0;
		boolean toggleable = false;
		ArrayList<Integer> history;

		Item item;

		/* check required number of arguments. */
		if(args.length < 2) {
			System.out.println("Not enough arguments for add command.");
			return 1;
		}

		//parse all arguments
		for(int i = 0;i<args.length;i++) {
			if(args[i].equals("--title")) {
				if(args.length > i) {
					title = args[i+1];
				}
			}
			else if(args[i].equals("--review-date")){
				if(args.length > i) {
					reviewDate = OurDate.convertFromString(args[i+1]);
					if(reviewDate == null) {
						System.out.println("Invalid date supplied for --review-date.");
						return 1;
					}
				} else {
					System.out.println("No argument supplied for --review-date");
					return 1;
				}
			}
			else if(args[i].equals("--addThis")){
				if(args.length > i) {
					try {
						addThis = Integer.parseInt(args[i+1]);
					} catch (java.lang.NumberFormatException e) {
						System.out.println("--addThis value not an integer.");
						return 1;
					}
				} else {
					System.out.println("No argument supplied for --addThis.");
					return 1;
				}
			}
			else if(args[i].equals("-r")){
				isRecurring = true;
			}
			else if(args[i].equals("--modifiers")){
				if(args.length > i) {
					modifiers = Item.convertModifiersFromString(args[i+1]);
					if(modifiers == null) {
						System.out.println("Invalid format for --modifiers argument.");
						return 1;
					}
					startModifier = 1;
					toggleable = true;
				} else {
					System.out.println("No argument supplied for --modifiers.");
					return 1;
				}
			}
			else if(args[i].equals("--start-modifier")){
				if(args.length > i) {
					try {
						startModifier = Integer.parseInt(args[i+1]);
					} catch (java.lang.NumberFormatException e) {
						System.out.println("--start-modifier value not an integer.");
						return 1;
					}
				} else {
					System.out.println("No argument supplied for --start-modifier.");
					return 1;
				}
			}
		}

		/* ensure title is set. */
		if(title == null) {
			System.out.println("Must include --title");
			return 1;
		}

		/* check that user supplied a valid --start-modiier value */
		if(modifiers.size() != 0 && (startModifier < 1 || startModifier > modifiers.size())) {
			System.out.println("--start-modifier value not valid given value for --modifiers");
			return 1;
		}

		/* if date not supplied, use today's date. */
		if(reviewDate == null) {
			reviewDate = new OurDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
		}


		history = new ArrayList<Integer>();
		history.add(addThis);

		/* create new item and save it to disk. */
		item = new Item(null, history, addThis, reviewDate, title, toggleable, modifiers, startModifier, isRecurring);
		entries.add(item);
		saveEntries();
		return 0;
	}

	/*
	* Takes a list of arguments from main(). Parses arguments for applicable arguments to the
	* delete command and deletes the entry in entries based on the -i argument's value. Returns
	* 1 on error, 0 otherwise.
	*/
	int delete(String[] args) {
		//possible command line arguments
		int index = -1;

		/* parse arguments */
		for(int i = 1;i<args.length;i++) { //start at one to skip command argument

			/* -i argument. */
			if(args[i].equals("-i") && args.length >= i+1) {
				try {
					index = Integer.parseInt(args[i+1]);
				} catch (java.lang.NumberFormatException e) {
					System.out.println("Error when parsing -i value.");
					return 1;
				}
				i++;
			}
		}

		/* ensure that index value is valid. */
		if(index < 0 || index > entries.size()-1) {
			System.out.println("Invalid index value.");
			return 1;
		}

		/* ensure there is at least one entry to delete. */
		if(entries.size() == 0) {
			System.out.println("No entries to delete.");
			return 1;
		}

		/* delete entry and save. */
		System.out.println(index);
		entries.remove(index);
		saveEntries();
		return 0;
	}

	/*
	* Takes a list of arguments from main(). Parses arguments for applicable arguments to the
	* process command and processes an entry in the entry list. Calls processIndex() to do the
	* actual calculations.
	* Returns 1 on error, 0 otherwise.
	*/
	int process(String[] args) {
		boolean confirm = false; //by default we only show what will happen, not actually do it.
		int id = -1;             //id of entry to process
		int addThis = 0;         //number of days until next review/appearance in today's entries.

		for(int i = 1;i<args.length;i++) { //start at one to skip command argument
			/* -c argument. */
			if(args[i].equals("-c")) {
				confirm = true;
			}
			/* -i argument. */
			if(args[i].equals("-i") && args.length >= i+1) {
				try {
					id = Integer.parseInt(args[i+1]);
					//Now that we have been given id, set addThis to the current addThis of that entry.
					addThis = entries.get(id).getAddThis();
				} catch (java.lang.NumberFormatException e) {
					System.out.println("Error when parsing -i value.");
					return 1;
				}
				i++;
			}
			/* -a argument. */
			if(args[i].equals("-a") && args.length >= i+1) {
				try {
					addThis = Integer.parseInt(args[i+1]);
				} catch (java.lang.NumberFormatException e) {
					System.out.println("Error when parsing -a value.");
					return 1;
				}
				i++;
			}
		}

		/* validate id argument, also catch missing -i flag (default -1) */
		if(id < 0 || id > entries.size()-1) {
			System.out.println("Invalid value for id to process (-i)");
			return 1;
		}

		//user specified -c option, they don't mean to test results
		if(confirm == true) {
			if(entries.get(id).isRecurring()) {
				processIndex(entries.get(id), addThis, false, true);
			} else {
				processIndex(entries.get(id), addThis, false, false);
			}

			entries.get(id).updateHistory(addThis,historySize); //Add addThis to the history of this entry.
			Collections.sort(entries, new DateComparator());    //sort the entries based on review date.
			saveEntries();
		} else {
			//Create an item to display what the entry will be changed to before it happens.
			Item tempItem = new Item(entries.get(id));

			/* pass flag to only refine addThis of entry that is not recurring. */
			if(entries.get(id).isRecurring()) {
				processIndex(tempItem, addThis, true, true);
			} else {
				processIndex(tempItem, addThis, true, false);
			}

			//Show new record information
			System.out.println("History: " + entries.get(id).showHistory());
			System.out.println(String.format("\n%10s", "Previous: ") + entries.get(id).toString());
			System.out.println(String.format("%10s", "New: ") + tempItem.toString());
		}
		return 0;
	}

	/*
	* Displays a list of entries. Either all entries or only up to day based on arguments.
	* Return 1 on error, 0 otherwise.
	*/
	int show(String[] args) {

		//if set instructs to print all entries, not only those with dates up to today.
		boolean allFlag = false; 
		boolean hasTodayEntry = false;
		ArrayList<Integer> indexes = new ArrayList<Integer>();

		//while there are still arguments
		for(int i = 1;i<args.length;i++) { //start at one to skip command argument

			//--all argument
			if(args[i].equals("--all")) {
				allFlag = true;
			}
		}

		if(allFlag) {
			//User wants to show all indexs. Add them all into the indexes ArrayList.
			for(int i = 0;i<entries.size();i++) {
				indexes.add(i);
			}
		} else {
			//fill entires with entries that have the date of today or earlier.
			OurDate today = new OurDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
			for(int i = 0;i<entries.size();i++) {
				if(entries.get(i).getReviewOn().calcDays() <= today.calcDays()) {
					indexes.add(i);
					hasTodayEntry = true;
				}
			}
		}

		if(allFlag == false && hasTodayEntry == false) {
			System.out.println("No entries today.");
			return 0;
		}
		if(allFlag == true && indexes.size() == 0) {
			System.out.println("No entries.");
			return 0;
		}

		//actually print out the entries requested.
		for (int i = 0;i<indexes.size();i++) {
			System.out.println( String.format("%03d",i) + ": " + entries.get(indexes.get(i)).toString());
		}
		return 0;
	}


	/*
	* Prints the usage for the MemoryLog program.
	*/
	public static void printUsage() {
		String usage = 
		"java memorylog.MemoryLog <command> [args]\n" +
		"commands:\n" +
		"    process <-i> <index>\n" +
		"        -c\n" +
		"            by default process only shows what will happen. Use this option to actually do it.\n" +
		"        -a <addThis>\n" +
		"            set the new addThis for this entry. Default is current.\n" +
		"    delete  <-i> <index>\n" +
		"    add     <--title> <\"title\">\n" +
		"        --review-date <date>\n" +
		"            Specify the first date that this entry will appear on. Default is current day. Format is \"YYYY-MM-DD\"\n" +
		"        --addThis <addthis>\n" +
		"            Specify the addThis for this new entry, or the number of days between review times.\n" +
		"        -r\n" +
		"            Instruct command that this entry is recurring, meaning that the addThis is locked, and you don't have to specify it with the process command.\n" +
		"        --modifiers <modifiers>\n" +
		"            Set the entry to toggle between a list of string modifiers each process time. Argument format: \"option1,option2\"\n" +
		"        --start-modifier <int>\n" +
		"            set which modifier should be used first. id for first modifier is 1.\n" +
		"    show\n" +
		"        --all : show all entries, not just those up to today.\n";

		System.out.println(usage);
	}

	//Main method just runs the run method in MemoryLog class.
	public static void main(String[] args) {
		try {
			MemoryLog log = new MemoryLog();
			if(log.run(args) != 0) {
				printUsage();
			}
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Failed to load quizzes: could not find config file/perhaps a quiz is missing?");
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException: Unable to read auto_memory_log.txt.");
		} catch (java.lang.NumberFormatException e) {
			System.out.println("NumberFormatException: Unable to read auto_memory_log.txt.");
		}
	}
}
