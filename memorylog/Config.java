package memorylog;

import java.io.File;
import java.util.Scanner;

public class Config {

	private int MEMORYLOGMAXBACKUPS = 10; /* currently not implemented anywhere. */
	private boolean loadingSuccess = true;/* used to indicate whether or not the loading succeeded. */

	public Config(String filename) {
		loadConfiguration(filename);
	}

	//*****************************************************************************************
	// given a filename, load all relavent fields with the values found in the config file.
	//*****************************************************************************************
	public void loadConfiguration(String filename) {
		File f = new File(filename);
		Scanner s = null;
		String line; //current line read from file
		int indexOfEqualsSign = 0;
		int currentLine = 0;
		String option = "";
		String value = "";

		try {
			s = new Scanner(f);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Unable to open config file " + f.getAbsolutePath() + ", no such file.");
			loadingSuccess = false;
			return;
		}

		while(s.hasNextLine()) {
			line = s.nextLine();

			//skip if commented
			if(line.charAt(0) == '#') {
				continue;
			}

			//ensure line structure is correct
			indexOfEqualsSign = line.indexOf("=");
			if(line.length() < 3) { //minimum length
				System.out.println("Error parsing " + f.getAbsolutePath() + " on line " + currentLine + ":line length < 3");
				loadingSuccess = false;
				return;
			}
			if(indexOfEqualsSign == -1) { //equals sign missing
				System.out.println("Error parsing " + f.getAbsolutePath() + " on line " + currentLine + ":missing equals sign");
				loadingSuccess = false;
				return;
			}
			if(indexOfEqualsSign == 0 || indexOfEqualsSign == line.length()-1) { // equals sign not in middle somewhere of string
				System.out.println("Error parsing " + f.getAbsolutePath() + " on line " + currentLine + ":equals sign not in middle of string");
				loadingSuccess = false;
				return;
			}
			currentLine++;

			//determine option and set value
			option = line.subSequence(0, indexOfEqualsSign-1).toString();
			value = line.subSequence(indexOfEqualsSign+1, line.length()-1).toString();

			if(option.equals("MEMORYLOGMAXBACKUPS")) {
				try {
					MEMORYLOGMAXBACKUPS = Integer.parseInt(value);
				} catch (java.lang.NumberFormatException e) {
					System.out.println("Error parsing " + f.getAbsolutePath() + " on line " + currentLine + ":failed to parse integer value from string value");
					loadingSuccess = false;
					return;
				}
			}

		}
	}

	//*****************************************************************************************
	// Getter for loadingSuccess variable.
	//*****************************************************************************************
	public boolean loadingSuccess() {
		return loadingSuccess;
	}

	//*****************************************************************************************
	// Getter for MEMORYLOGMAXBACKUPS variable.
	//*****************************************************************************************
	public int MEMORYLOGMAXBACKUPS() {
		return MEMORYLOGMAXBACKUPS;
	}
}
