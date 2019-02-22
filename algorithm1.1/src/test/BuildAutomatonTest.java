package test;

import static org.junit.Assert.*;

import java.io.File;
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
import reTodfa.Graphviz;

public class BuildAutomatonTest {
	Automaton test;
	int testcount=1;
	Set<String> alphabet =new HashSet<String>();
	final String INPUTFILE="1";
	final String INPUTRE="2";
	final int STRATEGYVERTICAL=1;
	final int STRATEGYHORIZONTAL=2;
		
	@Before
	public void setup(){
		alphabet.add("a");
		//alphabet.add("b");
	}
	
	@Ignore
	public void test() {
		String folderpath= "testsamples";
		
		File folder= new File(folderpath);
		Set<String> alphabet =new HashSet<String>();
		alphabet.add("a");
		alphabet.add("b");
		if(!folder.exists()){
			System.out.println(folderpath + " not exists");
		}else{
		
				String testname=folderpath+"/1.txt";
				File file=new File(testname);
				test=new BuildAutomaton(file,alphabet);
			}
		System.out.println(test.getAllstates().size());

		System.out.println(test.getAllstatasname());
		System.out.println(test.getAllstates().toString());
		Graphviz.createDotGraph(test.tostringforDot(), "outputname");
		//test.expandtransitons();
		//Graphviz.createDotGraph(test.tostringforDot(), "expanded");
	}//passed on input partial automata and output completed dfa 20180830
	
	//test on create automata from regular expresstions
	@Test
	public void test1() {
		File file=new File("testNFAep.txt");
		BuildAutomaton test=new BuildAutomaton(file,alphabet);
		//test.gettransitons();
		System.out.println("states: "+test.getAllstates().size());
		System.out.println("Transitions: "+test.getAllTransitions().size());

		System.out.println("initial: "+test.getinitState().getname()+"\tfinal: "+test.getAllFinalstatesNames());
		for(State s:test.getAllstates()){
			System.out.println("state "+s.getname()+"\tlevel\t"+s.getLevel()+"\t transition\t"+s.getTransitions().size());
		}
		Graphviz.createDotGraph(test.tostringforDot(), "outputname");
		
	}
	@Test
	public void test2() {
		File file=new File("testsamples/1.txt");
		BuildAutomaton test=new BuildAutomaton(file,alphabet);
		//test.gettransitons();
		System.out.println("states: "+test.getAllstates().size());
		System.out.println("Transitions: "+test.getAllTransitions().size());

		System.out.println("initial: "+test.getinitState().getname()+"\tfinal: "+test.getAllFinalstatesNames());
		for(State s:test.getAllstates()){
			System.out.println("state "+s.getname()+"\tlevel\t"+s.getLevel()+"\t transition\t"+s.getTransitions().size());
		}
		Graphviz.createDotGraph(test.tostringforDot(), "outputname");
		
	}
}
