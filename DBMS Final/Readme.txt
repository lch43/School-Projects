//Landon Higinbotham
//LCH43
//CS1555

Steps successfully compile and run the system correctly.

Setup database:
1. Run schema.sql on the database.
2. Run trigger.sql on the database.
3. Run init.sql on the database.

HOW TO RUN THE DRIVER PROGRAM:
1. Ensure you have Driver.txt and Olympic.java in the same directory as Driver.java
2. Compile Driver.java
If you are running java 7 or 8 compile the code in the commandline using
javac -cp "ojdbc7.jar;." Driver.java

if you are using other versions of java refer to
https://db.cs.pitt.edu/courses/cs1555/20-2/assignments/startup_tutorial.pdf
3.Run Olympic:
If you are running java 7 or 8 in the commandline use
java -cp "ojdbc7.jar;." Driver

if you are using other versions of java refer to
https://db.cs.pitt.edu/courses/cs1555/20-2/assignments/startup_tutorial.pdf

The driver scans Driver.txt as input for Olympic.java and uses Olympic.java the same way a user would.


Compile Olympic.java:
If you are running java 7 or 8 compile the code in the commandline using
javac -cp "ojdbc7.jar;." Olympic.java

if you are using other versions of java refer to
https://db.cs.pitt.edu/courses/cs1555/20-2/assignments/startup_tutorial.pdf

Run Olympic:
If you are running java 7 or 8 in the commandline use
java -cp "ojdbc7.jar;." Olympic

if you are using other versions of java refer to
https://db.cs.pitt.edu/courses/cs1555/20-2/assignments/startup_tutorial.pdf

Navigating the UI:
Throughout the program you will be given choices on how you would like to navigate. Certain situations require very specific inputs.

When asking to choose an option by entering a number, and the number is given on the left you should enter ONLY the number.
E.g //Upon receiving
Welcome to Landon Higinbotham - LCH43 CS1555 Project
Choose an option by entering the number to the left of the option.
*Reminder: All string inputs are case sensitive.*
1. Login
2. Exit
//Entering
1
//Would take you to the login interface.
Welcome to Landon Higinbotham - LCH43 CS1555 Project
Choose an option by entering the number to the left of the option.
*Reminder: All string inputs are case sensitive.*
1. Login
2. Exit
1
Enter username

When inputting any strings make sure you are being aware of case-sensitivity.

When inputting any format specific entries make sure you format your input correctly. If you are asked to enter only numbers then only enter numbers. Certain options inform the user of specific format inputs, and the program is not responsible for user error or misunderstanding of what input is required.

Helpful information:
To login as an organizer you can use
USERNAME: Carlos Arthur Nuzman
PASSWORD: Rio

To login as a coach you can use
USERNAME: Liu Guoliang
PASSWORD: LG

When using certain create options, you will be informed of the ID so you can use the ID to test other features that involve that entity

