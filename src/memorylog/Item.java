package memorylog;

import java.util.ArrayList;

public class Item {

	private int addThis;        /* The number of days to add to the date. */
	private OurDate reviewOn;   /* The date on which the item needs to be reviewed. */
	private String title;       /* The action that the user should take to push the item further down the list. */
	private boolean toggleable; /* If true, the item's action changes per review period (ie. review slides 1st/2nd of 1-10, 11-20). */
	private ArrayList<String> modifiers;       /* Holds the possible split actions that change each review period. */
	private ArrayList<Integer> addThisHistory; /* Used to keep track of history of changes to alert user of progress. */
	private int modifierIdentifier; /* Holds the current item from modifiers that user should next perform (ie. 1st, 2nd, etc.). */
	private boolean recurring;      /* Set if this item will never change it's addThis. */

	//*****************************************************************************************
	// default constructor
	//*****************************************************************************************
	public Item() {
		this(null, 0, null, null, false, null, 1, false);
	}

	//*****************************************************************************************
	// initial constructor
	//*****************************************************************************************
	public Item(ArrayList<Integer> addThisHistory, int addThis, OurDate reviewOn,
	            String title, boolean toggleable, ArrayList<String> modifiers,
	            int modifierIdentifier, boolean recurring) {
		this.addThisHistory = addThisHistory;
		this.addThis = addThis;
		this.reviewOn = reviewOn;
		this.title = title;
		this.toggleable = toggleable;
		this.modifiers = modifiers;
		this.modifierIdentifier = modifierIdentifier;
		this.recurring = recurring;
	}

	//*****************************************************************************************
	// copy constructor
	//*****************************************************************************************
	public Item(Item item) {
		this.addThisHistory = item.addThisHistory;
		this.addThis = item.addThis;
		this.reviewOn = new OurDate();
		this.reviewOn.setDay(item.reviewOn.getDay());
		this.reviewOn.setMonth(item.reviewOn.getMonth());
		this.reviewOn.setYear(item.reviewOn.getYear());
		this.title = item.title;
		this.toggleable = item.toggleable;
		this.modifiers = new ArrayList<String>();
		for (int i = 0;i<item.modifiers.size();i++) {
			this.modifiers.add(item.modifiers.get(i).toString());
		}
		this.modifierIdentifier = item.modifierIdentifier;
		this.recurring = item.recurring;
	}

	//*****************************************************************************************
	// Returns a string representation of the object to be used in viewEntries() in MemoryLog.java.
	//*****************************************************************************************
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(reviewOn.getYear() + "-" + String.format("%02d", reviewOn.getMonth()) + "-" + String.format("%02d", reviewOn.getDay()));
		sb.append("(" + String.format("%03d", addThis) + ") ");
		if(recurring) {
			sb.append("LOCKED ");
		} else {
			sb.append("       ");
		}
		sb.append(title);
		if (toggleable == true) {
			sb.append(" [Option " + modifierIdentifier + " of ");
			for (int i = 0;i<modifiers.size();i++) {
				if (i == modifiers.size() - 1) {
					sb.append(modifiers.get(i) + "]");
				}
				else {
					sb.append(modifiers.get(i) + ", ");
				}
			}
		}
		return sb.toString();
	}

	//*****************************************************************************************
	// Stores the information in the object in a record that can be written and read from a file.
	//*****************************************************************************************
	public String toRecord() {
		StringBuilder sb = new StringBuilder();
		sb.append(addThis + "\t");
		if (addThisHistory.size() == 0)
			sb.append("null\t");
		else {
			for (int i = 0;i<addThisHistory.size();i++) {
				sb.append(addThisHistory.get(i));
				if(i<addThisHistory.size()-1)
					sb.append(",");
				else sb.append("\t");
			}
		}
		sb.append(reviewOn.getYear() + "\t" + reviewOn.getMonth() + "\t" + reviewOn.getDay() + "\t");
		sb.append(title + "\t");
		sb.append(toggleable ? "1\t" : "0\t");
		for (int i = 0;i<modifiers.size();i++) {
			sb.append(modifiers.get(i).toString() + "\t");
		}
		sb.append(".\t" + modifierIdentifier + "\t");
		sb.append(recurring ? "1" : "0");
		return sb.toString();
	}

	//*****************************************************************************************
	// Show the contents of the history variable.
	//*****************************************************************************************
	public String showHistory() {
		if(recurring) {
			return "Recurring (LOCKED) item";
		}
		if(addThisHistory.size() == 0) {
			return "None.";
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<addThisHistory.size();i++) {
			if (i == addThisHistory.size()-1) 
				sb.append(addThisHistory.get(i));
			else
				sb.append(addThisHistory.get(i) + ",");
		}
		return sb.toString();
	}

	//*****************************************************************************************
	// add a new integer value to the history ArrayList up to a maximum number of values.
	//*****************************************************************************************
	public void updateHistory(int addThis, int max) {
		if(addThisHistory.size() >= max) {
			addThisHistory.remove(0);	
		}		
		addThisHistory.add(addThis);
	}

	//*****************************************************************************************
	// Getter for addThis.
	//*****************************************************************************************
	public int getAddThis() {
		return addThis;
	}

	//*****************************************************************************************
	// Setter for addThis.
	//*****************************************************************************************
	public void setAddThis(int addThis) {
		this.addThis = addThis;
	}

	//*****************************************************************************************
	// Getter for reviewOn.
	//*****************************************************************************************
	public OurDate getReviewOn() {
		return reviewOn;
	}

	//*****************************************************************************************
	// Setter for reviewOn.
	//*****************************************************************************************
	public void setReviewOn(OurDate reviewOn) {
		this.reviewOn = reviewOn;
	}

	//*****************************************************************************************
	// Getter for title.
	//*****************************************************************************************
	public String getTitle() {
		return title;
	}

	//*****************************************************************************************
	// Setter for title.
	//*****************************************************************************************
	public void setTitle(String title) {
		this.title = title;
	}

	//*****************************************************************************************
	// Getter for toggleable.
	//*****************************************************************************************
	public boolean getToggleable() {
		return toggleable;
	}

	//*****************************************************************************************
	// Setter for toggleable.
	//*****************************************************************************************
	public void setToggleable(boolean toogleable) {
		this.toggleable = toggleable;
	}

	//*****************************************************************************************
	// Getter for modifiers.
	//*****************************************************************************************
	public ArrayList<String> getModifiers() {
		return modifiers;
	}

	//*****************************************************************************************
	// Setter for modifiers.
	//*****************************************************************************************
	public void setModifiers(ArrayList<String> modiiers) {
		this.modifiers = modifiers;
	}

	//*****************************************************************************************
	// Getter for addThisHistory.
	//*****************************************************************************************
	public ArrayList<Integer> getAddThisHistory() {
		return addThisHistory;
	}

	//*****************************************************************************************
	// setter for addThisHistory.
	//*****************************************************************************************
	public void setAddThisHistory(ArrayList<Integer> addThisHistory) {
		this.addThisHistory = addThisHistory;
	}

	//*****************************************************************************************
	// Getter for modifierIdentifier
	//*****************************************************************************************
	public int getModifierIdentifier() {
		return modifierIdentifier;
	}
	
	//*****************************************************************************************
	// Setter for modifierIdentifier
	//*****************************************************************************************
	public void setModifierIdentifier(int a) {
		modifierIdentifier = a;
	}

	//*****************************************************************************************
	// Getter for recurring.
	//*****************************************************************************************
	public boolean getRecurring() {
		return recurring;
	}

	//*****************************************************************************************
	// setter for recurring
	//*****************************************************************************************
	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}

	//*****************************************************************************************
	// takes a string in the form of "modfier1,modifier2" (with quotes) and returns an arraylist
	// containing each field delimited by comma. 
	//*****************************************************************************************
	public static ArrayList<String> convertModifiersFromString(String modifiers) {
		ArrayList<String> m = new ArrayList<String>();
		String[] values;

		if(modifiers.isEmpty()) {
			return null;
		}

		values = modifiers.split(",");
		for(int i = 0;i<values.length;i++) {
			m.add(values[i]);
		}

		return m;
	}
}
