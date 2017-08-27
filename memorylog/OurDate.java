package memorylog;

//OurDate is a date entity.
public class OurDate {
	
	//Data fields that hold the day, month, and year of the date.
	private int day;
	private int month;
	private int year;
	
	//months serves as a reference to see how many days exist in each month.
	private int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	//Creates a default OurDate object.
	public OurDate() {
		day = 1;
		month = 1;
		year = 2000;
	}
	
	public OurDate(OurDate ourDate) {
		this(ourDate.day, ourDate.month, ourDate.year);
	}

	//Creates an OurDate object using inputted information.
	public OurDate(int dayHolder, int monthHolder, int yearHolder) {
		day = dayHolder;
		month = monthHolder;
		year = yearHolder;
	}

	//Used to return the OurDate object to original values.
	public void resetObject() {
		day = 1;
		month = 1;
		year = 2000;
		months[1] = 28;
	}
	
	public int getDay() {return day;}
	public int getMonth() {return month;}
	public int getYear() {return year;}

	//receives an integer and sets it as the object's day.
	public void setDay(int dayHolder) {
		if (dayHolder < 1 || dayHolder > months[month-1]) {
			dayHolder = 1;
		}
		day = dayHolder;
	}

	//receives an integer and sets it as the object's month.
	public void setMonth (int monthHolder) {
		if (monthHolder < 1 || monthHolder > 12) {
			monthHolder = 1;
		}
		month = monthHolder;
	}

	//receives an integer and sets it as the object's year.
	public void setYear (int yearHolder) {
		if (yearHolder < 2000) {
			yearHolder = 2000;
		}
		year = yearHolder;
	}

	//Determines if the given OurDate object is equal to another.
	public boolean isEqual (OurDate dateHolder) {
		if (dateHolder.calcDays() == this.calcDays()) 
			return true;
		else
			return false;
	}

	public boolean isLesser (OurDate dateHolder) {
		if(dateHolder.calcDays() >= this.calcDays())
			return true;
		else return false;
	}

	//Adds one 'day' to the date.
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

	//returns a (DD/MM/YYYY) representation of the date.
	public String toString() {
		String displayDate = new String();
		displayDate = month + "/" + day + "/" + year;
		return displayDate;
	}

	//calculates the number of days that exist beginning from 0 of the current object.
	/*
	 * This method could be fully functional if it was made so that it would count 
	 * how many leap years there were in between the object's value and 1,1,2000
	 * because keep in mind that some years have 366 days and not 365.
	 */
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

	//determines if the given date is a leap year.
	public boolean isLeapYear(int year) {
		if((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//augments the months array to leap year settings.
	public void setLeapYear() {
		months[1] = 29;
	}
} 
