package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.Object;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import learningbase.BuildAutomaton;
import learningbase.Teacher;
import learningbase.Teacher.Strategy;
import nAutomata.Automaton;
import nAutomata.State;
import nAutomata.State.Type;
import nAutomata.Transition;
import reTodfa.Graphviz;

public class teacherAUTTest {
	Teacher teacher1;
	Teacher teacher3;
	Automaton teacher2;
	Set<String> alphabet =new HashSet<String>();
	@Before
	public void setUp() throws Exception {
		alphabet.add("a");
		alphabet.add("b");
		String testRE="a<1>";
		//teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.HORIZONTAL,testRE);
		
	}
	
	@Test
	public void testbuildfromRE() {
		/*
		 * 20180905 comments: regular expressions have only alphabets in a layer may build an incorrect automaton, 
		 * it misses a distinguishing state from the sink state. such as <a<12>> is not incorrect, <1<12>> is correct. (fixed)
		 * 20180925 comments: (a+ab)* build successfully. (a+<a>b)* build successfully.
		 */
		//String testRE="(a(a+(a(c)b))b)";
		String testRE="a<1+a*b<2>>";
		Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.HORIZONTAL,testRE);
		
		Automaton a =teacher1.getAutomaton();
		
		System.out.println("total level is "+a.getTotalLevel());
		
		
		a.visualisation();

	}
	@Ignore
	public void testbuildfromFile() {
		
		String folderpath= "testsamples";
				
		File folder= new File(folderpath);
				
		if(!folder.exists()){
				System.out.println(folderpath + " not exists");
		}else{
				
				String testname=folderpath+"/1.txt";
				teacher3=new Teacher(true,alphabet,Strategy.VERTICAL,testname);
						
		}
		
		Automaton a =teacher3.getAutomaton();
		
		System.out.println("total level is "+a.getTotalLevel());
		System.out.println(a.toStringforDot());
		a.visualisation();

	}
	
	@Ignore
	public void testacceptedwordlist() {
		Set<String> wordlist=new HashSet<String>();
		String testRE="a+b*+<1>";
		
		Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.HORIZONTAL,testRE);
		
		Automaton a =teacher1.getAutomaton();
		a.acceptedPaths(a.getinitState(),"", wordlist, 3);
		System.out.println(wordlist.size()+"\t"+wordlist.toString());
		
//		Set<String> wordlist1=new HashSet<String>();
//		Automaton b =teacher2;
//		b.acceptedPaths(b.getinitState(),"", wordlist1, 4);
//		System.out.println(wordlist1.size()+"\t"+wordlist1.toString());
		
	}
	@Ignore
	public void testCEstrategy1() {
		
		Automaton a =teacher1.getAutomaton();
		Automaton b =teacher2;
		
		String ce1=teacher1.checkAutomaton(b);
		
		System.out.println(ce1);
		
	}
	@Ignore
	public void testCEstrategy2() {
		
		Automaton a =teacher1.getAutomaton();
		Automaton b =teacher2;
		teacher1.setStrategy(Strategy.HORIZONTAL);
		String ce1=teacher1.checkAutomaton(b);
		
		System.out.println(ce1);
		
	}
	@Ignore
	public void testgetAnswer() {
		String word="";
		assertEquals(State.Type.FINAL,teacher1.getAnswer(word));

	}
	@Test
	public void testcheckAutomaton() {
		String testlanguage="a";
		Automaton  automaton=new BuildAutomaton(testlanguage,alphabet);
		assertEquals(null,teacher1.checkAutomaton(automaton));

	}
}
