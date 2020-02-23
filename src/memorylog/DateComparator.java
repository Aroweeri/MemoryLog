package memorylog;

import java.util.Comparator;

public class DateComparator implements Comparator<Item> {

	//*****************************************************************************************
	// used to sort the review dates/OurDate objects.
	//*****************************************************************************************
	public int compare(Item i1, Item i2){
		return Integer.compare(i1.getReviewOn().calcDays(), i2.getReviewOn().calcDays());
	}

}
