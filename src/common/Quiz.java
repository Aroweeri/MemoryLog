package common;
import java.util.ArrayList;

public class Quiz {

	private String title; /* Holds the title of the quiz object. */
	private ArrayList<Question> questions; /* An array of questions that are all on the same subject. */

	//*****************************************************************************************
	// default constructor
	//*****************************************************************************************
	public Quiz() {
		this(null,null);
	}

	//*****************************************************************************************
	// copy constructor
	//*****************************************************************************************
	public Quiz(Quiz quiz) {
			this.title = quiz.getTitle();
			questions = new ArrayList<Question>();
			for (int i = 0;i<quiz.getQuestions().size();i++) {
				questions.add(new Question());
				questions.get(i).setQuestion(quiz.getQuestions().get(i).getQuestion());
				questions.get(i).setAnswers(new ArrayList<String>());
				for (int j = 0;j<quiz.getQuestions().get(i).getAnswers().size();j++) {
					questions.get(i).getAnswers().add(quiz.getQuestions().get(i).getAnswers().get(j));
				}
			}
	}

	//*****************************************************************************************
	// initial constructor
	//*****************************************************************************************
	public Quiz(String title, ArrayList<Question> questions) {
		this.title = title;
		this.questions = questions;
	}

	//*****************************************************************************************
	// setter for title
	//*****************************************************************************************
	public void setTitle(String title) {
		this.title = title;
	}

	//*****************************************************************************************
	// getter for title
	//*****************************************************************************************
	public String getTitle() {
		return title;
	}

	//*****************************************************************************************
	// setter for questions
	//*****************************************************************************************
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	//*****************************************************************************************
	// getter for questions
	//*****************************************************************************************
	public ArrayList<Question> getQuestions() {
		return questions;
	}
}
