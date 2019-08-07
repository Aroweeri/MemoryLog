package memorylog;

//OurDate is a date entity.
public class OurDate {
	
	private static final int MIN_YEAR = 2000;
	private static final int DEF_YEAR = 2000;
	private static final int DEF_MONTH= 1;
	private static final int DEF_DAY  = 1;

	//Data fields that hold the day, month, and year of the date.
	private int day;
	private int month;
	private int year;

	//months serves as a reference to see how many days exist in each month.
	private static final int[] daysPerMonthNoLeapYear  = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private static final int[] daysPerMonthLeapYear  = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private int[] daysPerMonth = daysPerMonthNoLeapYear;

	//*****************************************************************************************
	// default constructor
	//*****************************************************************************************
	public OurDate() {
		day = DEF_DAY;
		month = DEF_MONTH;
		year = DEF_YEAR;
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
	public OurDate(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;

		/* set default values if the ones provided don't work. */
		if(!validValues(year, month, day)) {
			this.day = DEF_DAY;
			this.month = DEF_MONTH;
			this.year = DEF_YEAR;
		}
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
	public void setDay(int day) {
		if (day < 1 || day > daysPerMonth[month-1]) {
			day = 1;
		}
		this.day = day;
	}

	//*****************************************************************************************
	// setter for month. checks for valid month
	//*****************************************************************************************
	public void setMonth (int month) {
		if (month < 1 || month > 12) {
			month = 1;
		}
		this.month = month;
	}

	//*****************************************************************************************
	// setter for year. checks for valid year.
	//*****************************************************************************************
	public void setYear (int year) {
		if (year < MIN_YEAR) {
			year = MIN_YEAR;
		}
		this.year = year;
	}

	//*****************************************************************************************
	// Determines if the given OurDate object is equal to another.
	//*****************************************************************************************
	public boolean isEqual (OurDate date) {
		if (date.calcDays() == this.calcDays()) 
			return true;
		else
			return false;
	}

	//*****************************************************************************************
	// determines if this is lesser than the OurDate object passed as parameter.
	//*****************************************************************************************
	public boolean isLesser (OurDate date) {
		if(date.calcDays() >= this.calcDays())
			return true;
		else return false;
	}

	//*****************************************************************************************
	// Adds one day to the date. Updates the month and year if the day was the end of the month or year
	//*****************************************************************************************
	public void addOne() {
		if (day == daysPerMonth[month-1]) {
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
		else if (day < daysPerMonth[month-1]) {
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
				numDay+=daysPerMonth[i];
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
	public static boolean isLeapYear(int year) {
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
		daysPerMonth = daysPerMonthLeapYear;
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
		if(validValues(year, month, day)) {
			return returnDate;
		} else return null;
	}

	//*****************************************************************************************
	// Evaluates if the values for day, month, and year are valid. Ex. day is 30 if month is Feb.
	// Returns true if so and false otherwise.
	//*****************************************************************************************
	private static boolean validValues(int year, int month, int day) {


		/* check year */
		if(year < MIN_YEAR) {
			return false;
		}

		/* check month */
		if(month < 1 || month > 12) {
			return false;
		}

		/* check day */
		if(isLeapYear(year)) {
			if(day < 1 || day > daysPerMonthLeapYear[month-1])
				return false;
		} else {
			if(day < 1 || day > daysPerMonthNoLeapYear[month-1])
				return false;
		}

		return true;
	}

} 
