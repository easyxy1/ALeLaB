Welcome
Welcome to Learning Nominal Language with Binders Implementation.

How to run
run with eclipe
This implementation is created as an eclipse project. After you download "algorithm1.1" folder, you could import it into eclipse and run directly. For now, you have an example to show the learning progress: 
run the learningbase.Example.class, 
follow the instructions on the console to input the language you want to learn, 
When the messege "Finished." comes, it terminates.
You can see the pdf file "example.pdf" in the root folder of the project. This show a graph about the result of learning.

run with command line
After you download "algorithm1.1" folder, you should open a terminal and go to the "algorithm1.1/src" folder.

Type the following LINUX command to compile all the java files:

javac learningbase/*.java nAutomata/*.java word/*.java reTodfa/*.java
Then to run use:
java test.Example

When the messege "Finished." comes, it terminates.
You can see the pdf file "example.pdf" in the root folder of the project. This show a graph about the result of learning.

input example
1. input a canonical expression.
Before starting learning process, please input the target language and an initial alphabet.
Please enter the finite initial alphabet (separate by a space):
a b
Please choose how to input the language:
 1. enter a canonical expression.
 2. enter a file path. 
 you choose 1 or 2: 
2
Please enter a file path: 
testsamples/1.txt

2. input with a file
Before starting learning process, please input the target language and an initial alphabet.
Please enter the finite initial alphabet (separate by a space):
a b
Please choose how to input the language:
 1. enter a canonical expression.
 2. enter a file path. 
 you choose 1 or 2: 
2
Please enter a file path: 
testsamples/1.txt

Have fun!
