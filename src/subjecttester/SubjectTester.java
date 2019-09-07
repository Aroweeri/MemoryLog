package subjecttester;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Collections;
import common.*;

public class SubjectTester {

	ArrayList<DateQuestion> questions; /* questions in the subject test file. */
	String recordDelimiter = "\t";

	//******************************************************************************************
	// default constructor
	//******************************************************************************************
	public SubjectTester() {
		questions = new ArrayList<DateQuestion>();
	}

	//******************************************************************************************
	// fill the questions ArrayList with questions found in the subject file located at path.
	//
	// return true on success and false on failure.
	//******************************************************************************************
	public boolean load(String path) {
		File f = new File(path);
		Scanner s = null;
		try {
			s = new Scanner(f);
			while(s.hasNextLine()) {
				questions.add(new DateQuestion(s.nextLine(), recordDelimiter));
			}
			s.close();
			return true;
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Unable to read " + f.getAbsolutePath());
			return false;
		}
	}

	//******************************************************************************************
	// save the questions in the questions ArrayList to the file located at path.
	//******************************************************************************************
	public boolean save(String path) {
		File f = new File(path);
		PrintWriter p = null;
		try {
			p = new PrintWriter(f);
			for(int i = 0;i<questions.size();i++) {
				p.println(questions.get(i).toRecord("\t"));
			}
			p.close();
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Unable to write to " + f.getAbsolutePath());
		}
		return false;
	}

	//******************************************************************************************
	// Ask the user the passed question and determine if they answered correctly.
	// Returns 0 on correct answer, 1 on incorrect answer, and 2 if the user desires to delete
	// the question from the file.
	//******************************************************************************************
	public int ask(DateQuestion question, Scanner input) {
		String userConfirm = "default";
		final int DEFAULT =-1;
		final int FAILURE = 0;
		final int SUCCESS = 1;
		final int DELETE  = 2;

		System.out.println(question.getQuestion());	
		System.out.print("> ");
		String answer = input.nextLine();
		boolean answerFound = false;

		/* check if the user's answer exists in the answers. */
		for (int i = 0;i<question.getAnswers().size();i++) {
			if(answer.equals(question.getAnswers().get(i))) 
				return SUCCESS;
		}

		/* if the answer was not found */
		/* print out correct answers. */
		for(int i = 0;i<question.getAnswers().size();i++) {
			System.out.println("  " + question.getAnswers().get(i));
		}

		System.out.print("Confirm (1-Wrong,2-Actually Correct,3-Delete Question): ");

		while(userConfirm.equals("default")) {
			userConfirm = input.nextLine();
			switch(userConfirm) {
			case "1":
				return FAILURE;
			case "2":
				return SUCCESS;
			case "3":
				return DELETE;
			default:
				userConfirm = "default";
				System.out.print("> ");
			}
		}
		/* never executes */
		return SUCCESS;
	}

	//******************************************************************************************
	// return question that matches the parameter.
	//******************************************************************************************
	public DateQuestion match(DateQuestion question) {
		for(int i = 0;i<questions.size();i++) {
			if(questions.get(i).getQuestion().equals(question.getQuestion())) {
				return questions.get(i);
			}
		}
		return null;
	}

	//******************************************************************************************
	// run a given Subject file, asking all the questions and updating the values as the user answers them.
	//******************************************************************************************
	public void run(String path, Scanner input) {
		LocalDate date;
		date = LocalDate.now();
		OurDate today = new OurDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
		ArrayList<DateQuestion> todayQuestions = new ArrayList<DateQuestion>();
		ArrayList<DateQuestion> missedQuestions = new ArrayList<DateQuestion>();
		boolean previouslyWrong = false;
		boolean questionsToAnswer = false;

		if(load(path)) {

			//find questions to do today	
			for(int i = 0;i<questions.size();i++) {
				if(questions.get(i).getReviewOn().isLesser(today)) {
					todayQuestions.add(new DateQuestion(questions.get(i)));
					questionsToAnswer = true;
				}
			}

			if(questionsToAnswer) {

				Collections.shuffle(todayQuestions);

				//ask questions to do today
				while(todayQuestions.size() > 0) {
					previouslyWrong = false;
					System.out.printf("%d left.\n", todayQuestions.size());

					//as each question is asked, update it in the real list.
					for(int i = 0;i<missedQuestions.size();i++) {
						if(todayQuestions.get(0).getQuestion().equals(missedQuestions.get(i).getQuestion())) {
							previouslyWrong = true;
						}
					}
					switch(ask(todayQuestions.get(0), input)) {
						case 0:
							match(todayQuestions.get(0)).decreasePeriod(today);
							todayQuestions.get(0).decreasePeriod(today);

							if(previouslyWrong == false)
								missedQuestions.add(todayQuestions.get(0));

							todayQuestions.add(new DateQuestion(todayQuestions.get(0)));
							todayQuestions.remove(0);
							break;
						case 1:
							if(previouslyWrong == false) 
								match(todayQuestions.get(0)).increasePeriod(new OurDate(today), questions);	
							todayQuestions.remove(0);
							break;
						case 2:
							System.out.println("\nDeleted question.");
							questions.remove(match(todayQuestions.get(0)));
							todayQuestions.remove(0);
							break;
					}
				}
				
				//sort real list
				Collections.sort(questions, new DateQuestionComparator());
				
				//write to disk
				save(path);
			} else {
				System.out.println("No questions to answer today.");
			}
		}
	}

	//******************************************************************************************
	// add questions and answers to a Subject file. Receives data through user input.
	//******************************************************************************************
	public void add(String path, Scanner input) {
		boolean moreQuestions = true;
		boolean moreAnswers = true;
		String question;
		String answer;
		ArrayList<String> answers = new ArrayList<String>();

		if(load(path)) {

			//enter questions into list
			while(moreQuestions) {
				moreAnswers = true;
				answers = new ArrayList<String>();
				System.out.printf("Question (blank=exit): ");
				question = input.nextLine();
				if(question.isEmpty()) {
					moreQuestions = false;
				}
				while (moreQuestions && moreAnswers) {
					System.out.print("> ");
					answer = input.nextLine();
					if(answer.isEmpty())
						moreAnswers = false;
					else
						answers.add(answer);
					
					
				}
				if(!question.isEmpty() && answers.size() > 0) {
					questions.add(new DateQuestion(new Question(answers, question)));
				}
			}
			
			//when finised, sort list.
			Collections.sort(questions, new DateQuestionComparator());
			
			//write to disk
			save(path);
		}
	}

	//******************************************************************************************
	// Takes a subject file and rebalances the questions within it. It takes all questions that
	// are on or before the current date and evenly spreads them out over a number of days.
	// The specific number of days is provided by the numberOfDaysString argument. 
	//******************************************************************************************
	public void rebalance(String path, String numberOfDaysString) {
		LocalDate date;
		date = LocalDate.now();
		OurDate today = new OurDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
		OurDate temp;
		ArrayList<DateQuestion> todayQuestions = new ArrayList<DateQuestion>();
		boolean questionsToAnswer = false;
		int questionsPerDay;
		int questionsPerDayRemainder;
		boolean appliedRemainder = false;
		int numberOfDays = 0;

		/* Check bad user input. */
		try {
			numberOfDays = Integer.parseInt(numberOfDaysString);
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Failed to parse argument.");
			return;
		}
		if(numberOfDays < 2) {
			System.out.println("Invalid value for number of days argument. Must be at least 2.");
			return;
		}

		if(load(path)) {
			//find questions to do today	
			for(int i = 0;i<questions.size();i++) {
				if(questions.get(i).getReviewOn().isLesser(today)) {
					todayQuestions.add(new DateQuestion(questions.get(i)));
					questionsToAnswer = true;
				}
			}
			if(questionsToAnswer) {
				Collections.shuffle(todayQuestions);

				/* protect against division by 0, least number of questions per day can only be 1. */
				if(numberOfDays > todayQuestions.size()) {
					numberOfDays = todayQuestions.size();
				}

				questionsPerDay = todayQuestions.size()/numberOfDays;
				questionsPerDayRemainder = todayQuestions.size()%numberOfDays;
				for(int i = 0,j = 0;i<todayQuestions.size();i++) {
					temp = new OurDate(today);
					for(int k = 0;k<j;k++) {
						temp.addOne();
					}
					match(todayQuestions.get(i)).setReviewOn(temp);
					if(appliedRemainder) {
						if(i%questionsPerDay == 0) {
							j++;
						}
					} else {
						if(i%(questionsPerDay+questionsPerDayRemainder) == 0 && i != 0) {
							j++;
						}
						appliedRemainder = true;
					}
				}
			}
			//when finised, sort list.
			Collections.sort(questions, new DateQuestionComparator());
			
			//write to disk
			save(path);
		}
	}

	//******************************************************************************************
	// main. processes arguments to figure out which subprogram the user wants to run.
	//******************************************************************************************
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		
		String usage = "java memorylog.SubjectTester <command> <path/to/subject_file>\n\n" +
			"Commands: \n" +
			"run <path/to/subject_file>\n" +
			"\tRun the given subject file.\n" +
			"add <path/to/subject_file>\n" +
			"\tAdd questions to the given subject file.\n" +
			"rebalance <path/to/subject_file> <number_of_days>" +
			"\tTake all questions currently waiting to be answered and redestribute them over the next number_of_days days.";

		if(args.length < 2) {
			System.out.println("Wrong number of arguments.");
			System.out.println(usage);
		} else {
			SubjectTester subjectTester = new SubjectTester();
			String command = args[0];
			switch (command) {
				case "run":
					if(args.length == 2) {
						subjectTester.run(args[1], input);
					} else {
						System.out.println("Wrong number of arguments for run command.");
						System.out.println(usage);
					}
					break;
				case "add":
					if(args.length == 2) {
						subjectTester.add(args[1], input);
					} else {
						System.out.println("Wrong number of arguments for add command.");
						System.out.println(usage);
					}
					break;
				case "rebalance":
					if(args.length == 3) {
						subjectTester.rebalance(args[1], args[2]);
					} else {
						System.out.println("Wrong number of arguments for rebalance command.");
						System.out.println(usage);
					}
					break;
				default:
					System.out.println("Unrecognized command.");
			}
		}
	}
}
