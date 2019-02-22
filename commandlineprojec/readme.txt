Welcome
Welcome to Learning Nominal Language with Binders Implementation.

How to run
run with command line
Type the following LINUX command to compile all the java files:

javac learningbase/*.java  word/*.java reTodfa/*.java nAutomata/*.java learningbase/*.java  
Then to type:
javac test/Example.java 
To run use:
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

PS: A graph output file is accessible when you machine could run dot commands. If not, the "dotsource.dot" file is as an output to decribe the result.

Have fun!
