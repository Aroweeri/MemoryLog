package memorylog;

import java.util.ArrayList;

public class Item {

	//Quiz that is associated
	private Quiz quiz;

	//The number of days to add to the date.
	private int addThis;

	//The date on which the item needs to be reviewed.
	private OurDate reviewOn;

	//The action that the user should take to push the item further down the list.
	private String title;

	//If true, the item's action changes per review period (ie. review slides 1st/2nd
	//of 1-10, 11-20).
	private boolean toggleable;

	//Holds the possible split actions that change each review period.
	private ArrayList<String> modifiers;

	//Used to keep track of history of changes to alert user of progress.
	private ArrayList<Integer> addThisHistory;

	//Holds the current item from modifiers that user should next perform (ie. 1st, 2nd, etc.).
	private int modifierIdentifier;

	//Set if this item will never change it's addThis.
	private boolean recurring;

	public Item() {
		this(null, null, 0, null, null, false, null, 1, false);
	}

	public Item(Quiz quiz, ArrayList<Integer> addThisHistory, int addThis, OurDate reviewOn,
	            String title, boolean toggleable, ArrayList<String> modifiers,
	            int modifierIdentifier, boolean recurring) {
		this.quiz = quiz;
		this.addThisHistory = addThisHistory;
		this.addThis = addThis;
		this.reviewOn = reviewOn;
		this.title = title;
		this.toggleable = toggleable;
		this.modifiers = modifiers;
		this.modifierIdentifier = modifierIdentifier;
		this.recurring = recurring;
	}
	
	public Item(Item item) {
		this.quiz = item.quiz;
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

	//Returns a string representation of the object to be used in viewEntries() in MemoryLog.java.
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

	//Stores the information in the object in a record that can be written and read from a file.
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

	//Show the contents of the history variable.
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

	public void updateHistory(int addThis, int max) {
		if(addThisHistory.size() >= max) {
			addThisHistory.remove(0);	
		}		
		addThisHistory.add(addThis);
	}

	public float questionsPerDay() {
		return (float)quiz.getQuestions().size()/(float)addThis;
	}
	
	//Getter for quiz.
	public Quiz getQuiz() {
		return quiz;
	}

	//Getter for addThis.
	public int getAddThis() {
		return addThis;
	}

	//Setter for addThis.
	public void setAddThis(int addThis) {
		this.addThis = addThis;
	}

	//Getter for reviewOn.
	public OurDate getReviewOn() {
		return reviewOn;
	}

	//Setter for reviewOn.
	public void setReviewOn(OurDate reviewOn) {
		this.reviewOn = reviewOn;
	}

	//Getter for title.
	public String getTitle() {
		return title;
	}

	//Setter for title.
	public void setTitle(String title) {
		this.title = title;
	}

	//Getter for toggleable.
	public boolean getToggleable() {
		return toggleable;
	}

	//Setter for toggleable.
	public void setToggleable(boolean toogleable) {
		this.toggleable = toggleable;
	}

	//Getter for modifiers.
	public ArrayList<String> getModifiers() {
		return modifiers;
	}

	//Setter for modifiers.
	public void setModifiers(ArrayList<String> modiiers) {
		this.modifiers = modifiers;
	}
	
	//Getter for modifierIdentifier
	public int getModifierIdentifier() {
		return modifierIdentifier;
	}
	
	//Setter for modifierIdentifier
	public void setModifierIdentifier(int a) {
		modifierIdentifier = a;
	}

	public boolean isRecurring() {
		return recurring;
	}
}
