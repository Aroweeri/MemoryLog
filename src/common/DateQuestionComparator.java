package common;

import java.util.Comparator;

public class DateQuestionComparator implements Comparator<DateQuestion> {

	//*****************************************************************************************
	// Used to compare the DateQuestion objects and sort them.
	//*****************************************************************************************
	public int compare(DateQuestion d1, DateQuestion d2) {
		return Integer.compare(d1.getReviewOn().calcDays(), d2.getReviewOn().calcDays());
	}
}
