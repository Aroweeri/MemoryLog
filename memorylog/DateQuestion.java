package memorylog;

import java.util.ArrayList;
import java.time.LocalDate;

public class DateQuestion extends Question {
	private int addThis;      /* the number of days to add to the reviewOn date after the user has answered this question correctly. */
	private OurDate reviewOn; /* the date that the program will present this question to the user. */

	//*****************************************************************************************
	// Default constructor
	//*****************************************************************************************
	public DateQuestion() {
		super();
		addThis=0;
		reviewOn = null;
	}

	//*****************************************************************************************
	// copy constructor
	//*****************************************************************************************
	public DateQuestion(DateQuestion dateQuestion) {
		super(dateQuestion.answers, dateQuestion.question);
		this.addThis = dateQuestion.addThis;
		this.reviewOn = new OurDate(dateQuestion.reviewOn);
	}

	//*****************************************************************************************
	// constructor takes a string that is delimited by recordDelimiter (should be)
	//*****************************************************************************************
	public DateQuestion(String record, String recordDelimiter) {
		answers = new ArrayList<String>();
		String[] fields = record.split("\t");
		addThis = Integer.parseInt(fields[0]);
		reviewOn = new OurDate(Integer.parseInt(fields[3]), Integer.parseInt(fields[2]), Integer.parseInt(fields[1]));
		question = fields[4];
		for(int i = 5; i<fields.length;i++) {
			answers.add(fields[i]);
		}
	}

	//*****************************************************************************************
	// constructor takes a Question object and adds today's date and default addThis.
	//*****************************************************************************************
	public DateQuestion(Question q) {
		LocalDate today;
		today = LocalDate.now();

		reviewOn = new OurDate(today.getDayOfMonth(), today.getMonthValue(), today.getYear());
		question = q.question;
		answers = q.answers;
		addThis = 0;
	}

	//*****************************************************************************************
	// Getter for reviewOn
	//*****************************************************************************************
	public OurDate getReviewOn() {
		return this.reviewOn;
	}
	
	//*****************************************************************************************
	// Setter for reviewOn
	//*****************************************************************************************
	public void setReviewOn(OurDate date) {
		this.reviewOn = new OurDate(date);
	}

	//*****************************************************************************************
	// adjusts an addThis value to help even out questions to complete to prevent days with lots.
	// It accomplishes this by finding a range of days that the question could be placed in based
	// on the passed addThis value. The algorithm is x +- (x * 0.25). So an add this of 8 could
	// change to anywhere between 6 and 10. It will choose the day that has the fewest
	// questions in that range of days surrounding the original addThis. */
	//*****************************************************************************************
	public int refineAddThis(OurDate today, ArrayList<DateQuestion> questions, int addThis, boolean messages) {
		int min = addThis;
		int max = addThis;
		int change = 0;
		int updatedAddThis = addThis;
		int numItemsOnTestDay = 0;
		int minItemsOnTestDay = 0;
		int day = today.getDay();
		int month = today.getMonth();
		int year = today.getYear();

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
		if(messages) System.out.print("QOD: ");
		for(int i = min;i<max+1;i++) {
			numItemsOnTestDay = 0;
			today.setDay(day);
			today.setMonth(month);
			today.setYear(year);
			for(int j = 0;j<i;j++) {
				today.addOne();
			}
			for(int j = 0;j<questions.size();j++) {
				if(questions.get(j).getReviewOn().calcDays() == today.calcDays()) {
					numItemsOnTestDay++;
				}
			}

			if (messages) {
				if(i == addThis) 
					System.out.printf("|%02d| ", numItemsOnTestDay);
				else 
					System.out.printf("%02d ", numItemsOnTestDay);
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
			System.out.print("\nRefined addThis is " + updatedAddThis + " (was " + addThis + ")");
		}
		System.out.println();

		return updatedAddThis;
	}

	//*****************************************************************************************
	// Executed when the user successfully answers a question. Increases the addThis value
	// based on what it is currently, then runs it through the refineAddThis() method.
	//*****************************************************************************************
	public void increasePeriod(OurDate today, ArrayList<DateQuestion> questions) {
		int refinedAddThis = 0;
		if(addThis==0) {
			addThis=1;
			refinedAddThis = 1;
		} else if (addThis%2 == 0) {
			addThis = (int)(addThis + addThis*0.5);
			refinedAddThis = refineAddThis(new OurDate(today), questions, addThis, true);
		} else {
			addThis = 1+(int)(addThis + addThis*0.5);
			refinedAddThis = refineAddThis(new OurDate(today), questions, addThis, true);
		}

		reviewOn = new OurDate(today);

		for(int i = 0;i<refinedAddThis;i++) {
			reviewOn.addOne();
		}
	}

	//*****************************************************************************************
	// Always sets the addThis to one. Executed when the user fails a question and has to
	// start over their progress on it.
	//*****************************************************************************************
	public void decreasePeriod(OurDate today) {
		reviewOn = new OurDate(today);
		addThis = 1;
		reviewOn.addOne();
	}

	//*****************************************************************************************
	// Returns a string that is written to a file to be read later.
	//*****************************************************************************************
	public String toRecord(String recordDelimiter) {
		StringBuilder sb = new StringBuilder();
		//Write addThis
		sb.append(addThis + recordDelimiter);

		//Write date
		sb.append(reviewOn.getYear() + recordDelimiter);
		sb.append(reviewOn.getMonth() + recordDelimiter);
		sb.append(reviewOn.getDay() + recordDelimiter);

		//Write the question.
		sb.append(question + recordDelimiter);

		//Write each answer that is in the answers ArrayList.
		for (int i = 0;i<answers.size();i++) {
			sb.append(answers.get(i) + recordDelimiter);
		}

		return sb.toString();
	}
}
