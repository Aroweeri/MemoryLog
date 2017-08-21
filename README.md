# MemoryLog

A utility that helps the user keep track of a leitner-style spaced repitition studying system. The program allows a user to have reminders for when they need to review something up to a precision of days. The days between studying periods are user-set, and the program is not intelligent. Each time you complete a task and go to update it, it will ask you to set the new revision period (the number of days until you review it again.) - it's up to the user to learn what works best for them. 

Another program in this repository is TestManager, which can be used as a rudimentary replacement for flash cards. See the examples directory for information on how to make the files that it takes.
  
# Compilation

Install the Java JDK so that you can compile the source.
cd to the MemoryLog directory.
Compile the source:

	javac memorylog.MemoryLog.java
	javac memorylog.TestManager.java

# Setup

We'll now create quiz in a text file, let the program be able to access it, and then create an entry for it in the program. You can see the example configuration.txt as well as the format of the quiz files in the examples directory.

1. Create a new file and start editing it.
2. On the next line, think of a question you want to enter. Write it's answer, and then tab once (You can have multiple answers as long as they are tab separated).
3. Write the question.
4. Save your file. You can put it anywhere, but keep track of the path.

With your quiz completed, you can now execute the quiz using the TestManager program. See the Running section for information on how to do that.

Now we will make an entry for this quiz in MemoryLog so that you can use it as a spaced repitition helper.
NOTE: If you make a mistake, just complete the prompts and then delete the entry afterwards. 

1. In the program menu (run the program), select the option to add an entry.
2. "Add this" means how many days inbetween reviewing there are. It's probably best to start with 1.
3. Enter the date on which it will appear in the program (probably choose the current date).
4. Name your entry.
5. You can decide if your entry has modifiers. For example, if you have a set of math problems, you can have it tell you to do the odd problems on one reviewing and the even problems on the next reviewing. Modifiers are simply strings that you enter.

You should have your entry set up now, when you say to display today's entries, your entry should appear there as long as it's review date is either today or in the past.

# Running

To execute MemoryLog, make sure you first have the program compiled, see the section on compilation if you didn't do this.

	java memorylog.MemoryLog

To take a quiz that you've made:

	java memorylog.TestManager <path/to/quiz>
