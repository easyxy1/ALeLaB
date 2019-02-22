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
import learningbase.Teacher;
import learningbase.Teacher.Strategy;
import nAutomata.Automaton;
import nAutomata.State;
import reTodfa.DFA;
import reTodfa.Graphviz;
import reTodfa.NFA;
import reTodfa.RegularExpression;

public class ProcessTest {

	Set<String> alphabet =new HashSet<String>();
	String RE;
	File file;
	Automaton automaton;
	
	@Before
	public void setup(){
		alphabet.add("a");
		alphabet.add("b");
		RE="<a<1>>";
	}
	
	@Ignore
	public void testREtoDFA() {
		
		RegularExpression.clear();
		RegularExpression.addalphabet(alphabet);
		NFA nfa=RegularExpression.generateNFA(RE);
		//add sink states
		nfa.outputtoTXT("og");
		nfa.expandtransitons();
		nfa.outputtoTXT("ep");
		
		//System.out.println(nfa.allstateinformation());
		DFA dfa=RegularExpression.generateDFA(nfa);
		dfa.minimisedfa();
		
		dfa.outputtoTXT("teacherdfa");


	}
	
	@Ignore
	public void testDFAfileToAutomata() {
		File file=new File("teacherdfa.txt");
		Automaton test=new BuildAutomaton(file,alphabet);
		Graphviz.createDotGraph(test.tostringforDot(), "outputname");


	}
	

	@Test
	public void testTeachercreation() {
		Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.HORIZONTAL,RE);
		Automaton a =teacher1.getAutomaton();
		Graphviz.createDotGraph(a.tostringforDot(), "teacher3");
	}//test pass only this single. if test all tests by sequence, nullpoint error comes.
	
		
}
