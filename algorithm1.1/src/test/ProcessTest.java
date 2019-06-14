package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import learningbase.BuildAutomaton;
import learningbase.Learner;
import learningbase.Teacher;
import learningbase.Teacher.Strategy;
import nAutomata.Automaton;
import nAutomata.State;
import reTodfa.DFA;
import reTodfa.Graphviz;
import reTodfa.NFA;
import reTodfa.RegularExpression;

public class ProcessTest {

	public static void main(String[] args) {
		Set<String> alphabet=new HashSet<>();
		alphabet.add("a");
		alphabet.add("b");
		alphabet.add("c");
		alphabet.add("d");
		alphabet.add("e");
		alphabet.add("f");
		alphabet.add("g");
		alphabet.add("h");
		alphabet.add("i");
		String testpath="concreteExample.txt";
		Teacher teacher1=new Teacher(Teacher.Fromfile,alphabet,Strategy.None,testpath);
		//learning process
		try {
			Learner test1= new Learner(teacher1);
			Graphviz.createDotGraph(test1.getLearnerAutomaton().simplityToStringforDot(), "teachervia1");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}
