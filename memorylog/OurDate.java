package memorylog;

//OurDate is a date entity.
public class OurDate {
	
	private static final int MIN_YEAR = 2000;

	//Data fields that hold the day, month, and year of the date.
	private int day;
	private int month;
	private int year;

	//months serves as a reference to see how many days exist in each month.
	private int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	//*****************************************************************************************
	// default constructor
	//*****************************************************************************************
	public OurDate() {
		day = 1;
		month = 1;
		year = 2000;
	}

	//*****************************************************************************************
	// copy constructor
	//*****************************************************************************************
	public OurDate(OurDate ourDate) {
		this(ourDate.day, ourDate.month, ourDate.year);
	}

	//*****************************************************************************************
	// initial constructor
	//*****************************************************************************************
	public OurDate(int dayHolder, int monthHolder, int yearHolder) {
		day = dayHolder;
		month = monthHolder;
		year = yearHolder;
	}

	//*****************************************************************************************
	// getter for day
	//*****************************************************************************************
	public int getDay() {
		return day;
	}

	//*****************************************************************************************
	// getter for month
	//*****************************************************************************************
	public int getMonth() {
		return month;
	}

	//*****************************************************************************************
	// getter for year
	//*****************************************************************************************
	public int getYear() {
		return year;
	}

	//*****************************************************************************************
	// setter for day. checks for valid day.
	//*****************************************************************************************
	public void setDay(int dayHolder) {
		if (dayHolder < 1 || dayHolder > months[month-1]) {
			dayHolder = 1;
		}
		day = dayHolder;
	}

	//*****************************************************************************************
	// setter for month. checks for valid month
	//*****************************************************************************************
	public void setMonth (int monthHolder) {
		if (monthHolder < 1 || monthHolder > 12) {
			monthHolder = 1;
		}
		month = monthHolder;
	}

	//*****************************************************************************************
	// setter for year. checks for valid year.
	//*****************************************************************************************
	public void setYear (int yearHolder) {
		if (yearHolder < MIN_YEAR) {
			yearHolder = MIN_YEAR;
		}
		year = yearHolder;
	}

	//*****************************************************************************************
	// Determines if the given OurDate object is equal to another.
	//*****************************************************************************************
	public boolean isEqual (OurDate dateHolder) {
		if (dateHolder.calcDays() == this.calcDays()) 
			return true;
		else
			return false;
	}

	//*****************************************************************************************
	// determines if this is lesser than the OurDate object passed as parameter.
	//*****************************************************************************************
	public boolean isLesser (OurDate dateHolder) {
		if(dateHolder.calcDays() >= this.calcDays())
			return true;
		else return false;
	}

	//*****************************************************************************************
	// Adds one day to the date. Updates the month and year if the day was the end of the month or year
	//*****************************************************************************************
	public void addOne() {
		if (day == months[month-1]) {
			if (month < 12) {
				day = 1;
				month++;
			}
			else if (month == 12) {
				day = 1;
				month = 1;
				year++;
			}
		}
		else if (day < months[month-1]) {
			day++;
		}
	}

	//*****************************************************************************************
	// returns a (DD/MM/YYYY) representation of the date.
	//*****************************************************************************************
	public String toString() {
		String displayDate = new String();
		displayDate = month + "/" + day + "/" + year;
		return displayDate;
	}

	//*****************************************************************************************
	// calculates the number of days that exist beginning from 0 of the current object.
	//*****************************************************************************************
	public int calcDays() {
		int numDay = 0;
		if (month > 1) {
			for (int i = 0;i<month-1;i++) {
				numDay+=months[i];
			}
			numDay+= day + (year*365);
		}
		else {
			numDay = day + (year*365);
		}
		return numDay;
	}

	//*****************************************************************************************
	// determines if the given date is a leap year.
	//*****************************************************************************************
	public boolean isLeapYear(int year) {
		if((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
			return true;
		}
		else {
			return false;
		}
	}

	//*****************************************************************************************
	// augments the months array to leap year settings.
	//*****************************************************************************************
	public void setLeapYear() {
		months[1] = 29;
	}

	//*****************************************************************************************
	// Takes a string as parameter which represents a date in the form of "1970-01-01". The year,
	// month, and day are extracted and used to create a new OurDate object, which is returned.
	//*****************************************************************************************
	public static OurDate convertFromString(String date) {
		OurDate returnDate = null;
		String[] parts;
		int day;
		int month;
		int year;

		parts = date.split("-");

		if(parts.length != 3) {
			return null;
		}
		try {
			year = Integer.parseInt(parts[0]);
			month = Integer.parseInt(parts[1]);
			day = Integer.parseInt(parts[2]);
		} catch (java.lang.NumberFormatException e) {
			return null;
		}

		returnDate = new OurDate(day, month, year);
		if(returnDate.hasValidValues()) {
			return returnDate;
		} else return null;
	}


	//*****************************************************************************************
	// Evaluates if the values for day, month, and year are valid. Ex. day is 30 if month is Feb.
	// Returns true if so and false otherwise.
	//*****************************************************************************************
	public boolean hasValidValues() {

		int daysInMonth = 0;

		/* check year */
		if(year < MIN_YEAR) {
			return false;
		}

		/* check month */
		if(month < 1 || month > 12) {
			return false;
		}

		/* check day */
		daysInMonth = months[month-1];
		if(day < 1 || day > daysInMonth) {
			return false;
		}
		
		return true;
	}
} 
