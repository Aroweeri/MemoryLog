# MemoryLog

A utility that helps the user keep track of a leitner-style spaced repitition studying system. The
program allows a user to have reminders for when they need to review something up to a precision of
days. The days between studying periods are user-set, and the program is not intelligent. Each time
you complete a task and go to update it, it will ask you to set the new revision period (the number
of days until you review it again.) - it's up to the user to learn what works best for them.
The program will however automatically spread out entries to make the load more predictable. Though
this only really makes sense when there are a lot of entries in the manager.

Another program called SubjectTester is used to test the user on a "subject". The idea is to load
a large number of questions into a file, which the program will then ask the user for the answers to
the ones that need to be reviewed on any specific day. The program uses a basic algorithm involving
increasing days between review on correct answers and resetting the review period to one day on
incorrect answers. This is an update to the TestManager program described below, but rather than
using small sets of questions, the granularity is increased so that the questions themselves are
the things being tracked rather than a group. 

Another program in this repository is TestManager, which can be used as a rudimentary replacement
for flash card sets. See the examples directory for information on how to make the files that it
takes.

Together, these three programs offer a considerable tool to study information, learn new
information, and keep track of recurring events.
  
# Compilation

Install the Java JDK so that you can compile the source.  
Install ant  
cd to the MemoryLog directory.  
`ant jar`

# Configuration

Configuration file is config.txt at root of project.   
* MEMORYLOGMAXBACKUPS  
   number of backups to store. Once limit is reached oldest backups will start to be deleted.

* MEMLOGPATH  
   the file that MemoryLog.java will read from (the main file)

# SubjectTester Setup

In order to use this program, you have to pass it a quiz file to take. You can do this by using the
subprogram "add" as described in the "Running" section.  
It is used to create questions and sets the proper date and initial review time for the question.

# TestManager Setup

We'll now create quiz in a text file, let the program be able to access it, and then create an entry
for it in the program. You can see the example configuration.txt as well as the format of the quiz
files in the examples directory.

1. Create a new file and start editing it.
2. On the next line, think of a question you want to enter. Write it's answer, and then tab once
	(You can have multiple answers as long as they are tab separated).
3. Write the question.
4. Save your file. You can put it anywhere, but keep track of the path.

With your quiz completed, you can now execute the quiz using the TestManager program. See the
Running section.

Now we will make an entry for this quiz in MemoryLog so that you can use it as a spaced repitition
helper.  
NOTE: If you make a mistake, just complete the prompts and then delete the entry afterwards. 

1. In the program menu (run the program), select the option to add an entry.
2. "Add this" means how many days inbetween reviewing there are. It's probably best to start with 1.
3. Enter the date on which it will appear in the program (probably choose the current date).
4. Name your entry.
5. You can decide if your entry has modifiers. For example, if you have a set of math problems, you
	can have it tell you to do the odd problems on one reviewing and the even problems on the
	next reviewing. Modifiers are simply strings that you enter.
6. Now decide if the "add this" value should ever change. For example, if you know in advance that
	you'll always have to do a task each week, you don't want the program to ask you to enter 7 every
	time - set this option to disable "add this" updates.

You should have your entry set up now, when you say to display today's entries, your entry should
appear there as long as it's review date is either today or in the past.

# Running

To execute MemoryLog, make sure you first have the program compiled, see the section on compilation
if you didn't do this.

`java -jar build/jar/MemoryLog.jar`  
This will display the usage for the memorylog program

To run through questions in a SubjectTester file:  
`java -jar build/jar/SubjectTester.jar run <path/to/subject_file>`

To add questions to a subject file (This can be done manually but it's a bit faster this way.)  
This will start an interactive prompt that will ask you for questions and answers:  
`java -jar build/jar/SubjectTester.jar add <path/to/subject_file>`

Note that the empty subject file must exist before running this command.

To take a quiz that you've made:  
`java -jar build/jar/TestManager.jar <path/to/quiz>`

# Running Tests

To run the JUnit tests, go to the root of the repo and run `ant test`  
The tests are found under `src/tests`
