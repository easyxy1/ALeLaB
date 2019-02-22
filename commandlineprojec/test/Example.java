package test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import learningbase.LearningProcess;
import learningbase.Teacher;
import learningbase.Teacher.Strategy;
import reTodfa.Graphviz;

public class Example {

	public static void main(String[] args) {
		Set<String> alphabet=new HashSet();
		int languagechoose = 0;
		int strategychoose = 0;
		// TODO Auto-generated method stub
		System.out.println("Before starting learning process, please input the target language and an initial alphabet.");
		System.out.println("Please enter the finite initial alphabet (separate by a space):");
		Scanner scanner = new Scanner(System.in);
		String alphabetset = scanner.nextLine();
		String[] alphabets=alphabetset.split(" ");
		for(String s :alphabets){
			alphabet.add(s);
		}
		System.out.println("Please choose how to input the language:\n 1. enter a canonical expression.\n 2. enter a file path. \n you choose 1 or 2: ");
	
		languagechoose=scanner.nextInt();
		Boolean sourcefrom=true;
		while(languagechoose!=1 && languagechoose!=2){
		
			System.out.println("Please choose 1 or 2: ");
			languagechoose=scanner.nextInt();
		}
		
		String languagesource="";
		switch(languagechoose){
		case 1:{
			System.out.println("Please enter a canonical expression: ");
			scanner = new Scanner(System.in);
			languagesource = scanner.nextLine();
			sourcefrom=Teacher.FromRE;
			break;}
		case 2:{
			System.out.println("Please enter a file path: ");
			scanner = new Scanner(System.in);
			languagesource = scanner.nextLine();
			sourcefrom=Teacher.Fromfile;
			break;}
		default:System.out.println("Does not match! "+languagesource);
		}
		//System.out.println("Does not match! "+languagesource);
		
//		System.out.println("Please select a strategy for counterexamples:\n 1. HORIZONTAL.\n 2. VERTICAL. \n you choose 1 or 2: ");
//		
//		strategychoose=scanner.nextInt();
//	
//		while(languagechoose!=1 && languagechoose!=2){
//		
//			System.out.println("Please choose 1 or 2: ");
//			languagechoose=scanner.nextInt();
//		}
		Teacher teacher1=null;
//		switch(strategychoose){
//		case 1:{
			teacher1=new Teacher(sourcefrom,alphabet,Strategy.HORIZONTAL,languagesource);
//			break;}
//		case 2:{
//			teacher1=new Teacher(sourcefrom,alphabet,Strategy.VERTICAL,languagesource);
//			break;}
//		}
		//target language is setted up
		//Learning process starts
		//learning process
		LearningProcess example=null;
		try {
			example = new LearningProcess(teacher1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Graphviz.createDotGraph(example.getLearnerAutomaton().tostringforDot(), "example");
	}



}
