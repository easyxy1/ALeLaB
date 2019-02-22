package learningbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import nAutomata.Automaton;
import nAutomata.Operators;
import reTodfa.Graphviz;

public class LearningProcess {
	//private static TableSet tableSet;
	
	private static Set<String> alphabet=new HashSet();
	private static String teachercolumn="\t\t\t\t\t\t\t\t";
	private Automaton aLearner= new Automaton();
	private int round=1;
	private int closedrounds=0;
	private int consistentrounds=0;
	
	public static void main(String[] arg) throws IOException {
		
	}
	//one simple version for LearningProcess
	/**
	 * 
	 * @param teacher
	 * @throws IOException 
	 */
	public LearningProcess(Teacher teacher) throws IOException{
		aLearner.clear();
		//learner starts learning
		round=1;
		alphabet=teacher.getalphabet();
		Table learner=new Table();
		learner.setAlphabet(alphabet);
		learner.setLongPrefix();
		//ask membership queries
		learner.processMembershipQueriesForTables(learner.getShortPrefixLabels());
		learner.processMembershipQueriesForTables(learner.getLongPrefixLabels());
		
		// check and make sure table closed and consistent
		makeTableClosedAndConsistent(learner);
		//build an automaton with the closed  and consistent table
		//Automaton aLearner= new BuildAutomaton(learner);
		aLearner= new BuildAutomaton(learner);
		//print table to console
		//learner.printTable();
		
		//ask teacher for an equivalence query
		String counterexample=teacher.checkAutomaton(aLearner);
		// Learner use the resule of the equivalence query. If there is a counter example, extends the table with the couterexample and modifies a new equivalence query. If not, terminates.
		while (counterexample!=null) {
			System.out.println("counterexample:"+counterexample);
			//a new round starts
			round++;
			//control the max running times
			if(round>100){
				System.out.println("stop by force");
						break;
			}
			
			//use counterexample to extend table
			try {
				learner.extendByCE(counterexample);
			}catch (Exception e) {  
				System.out.println("Exception in "+e.getMessage()+"\r\n");
						
			}
			
			//make table closed and consistent
			try {
				makeTableClosedAndConsistent(learner);
			}catch (Exception e) {  
				System.out.println("Exception in "+e.getMessage()+"\r\n");
			} 
			//print table to console
			//learner.printTable();
			
			try {
				aLearner= new BuildAutomaton(learner);
			}catch (Exception e) {  	
				System.out.println("Exception in "+e.getMessage()+"\r\n");
			} 
			
			counterexample=teacher.checkAutomaton(aLearner);	
			
		} 
		
		String finish="Finished.\n ";
		if(counterexample==null)
			Graphviz.createDotGraph(this.getLearnerAutomaton().tostringforDot(), "example");
		finish+="The learner got an automaton accepted the Teacher's language";
		//algorithm terminates
		System.out.println(finish);
				
		
	}
	
	private void makeTableClosedAndConsistent(Table table) throws IOException {
		// TODO Auto-generated method stub
		boolean closedAndConsistent = false;

		while (!closedAndConsistent) {
			closedAndConsistent = true;
			//System.out.println("closed: "+table.isClosed());
			if (!table.isClosed()) {
				closedrounds++;
				closedAndConsistent = false;
				table=closeTable(table);
				//System.out.println("After modifying, closed is "+table.isClosed());
				//table.printTable();
			}
			
			//System.out.println("consistent: "+table.isConsistentWithAlphabet());
			
			if (!table.isConsistentWithAlphabet()) {
				consistentrounds++;
				closedAndConsistent = false;
				table= ensureConsistency(table);
				//System.out.println("After modifying, consistent is "+table.isConsistentWithAlphabet());
				//table.printTable();
			}
		}
		//System.out.println("After modified, table is closed and consistent: ");
		//table.printTable();
		
	}
	private Table ensureConsistency(Table t) throws IOException {
		// TODO Auto-generated method stub
		InconsistencyDataHolder dataHolder = t.findInconsistentSymbol();

		if (dataHolder == null) {
			// It seems like this method has been called without checking if table is inconsistent first
			return t;
		}

		String witness = t.determineWitnessForInconsistency(dataHolder);
		String newSuffix = dataHolder.getDifferingSymbol()+witness;
	//	System.out.println("test on different rows: "+dataHolder.getFirstRow().s+", "+ dataHolder.getSecondRow().s);
	//	System.out.println("test: "+"symbol is "+ dataHolder.getDifferingSymbol());
	//	System.out.println("test: "+"witness is "+ witness);
		t.addSuffix(newSuffix);
	//	System.out.println("test: "+t.getSuffixes());
		
		
		t.processMembershipQueriesForTables(t.getShortPrefixLabels());
	//	System.out.println("test in ensureConsistency: add short prefix...");// test for over repeat
		t.processMembershipQueriesForTables(t.getLongPrefixLabels());
	//	System.out.println("test in ensureConsistency: add long prefix...");// test for over repeat
		
		return t;
				
		
	}
	private Table closeTable(Table t) throws IOException {
		// TODO Auto-generated method stub
		
		String candidate = t.findUnclosedState();
		
		//while (candidate != null) {
			t.moveLongPrefixToShortPrefixes(candidate);
			//update S.A by new element in S (already change this functionality into previous step)
			//t.appendAlphabetSymbolsToWord(candidate);
			////remove the row from SA, which its label exists in S 
			t.removeShortPrefixesFromLongPrefixes();
			//fill the result for new row
			t.processMembershipQueriesForTables(t.getLongPrefixLabels());
			//System.out.println("Test on closetable add long prefix...");// test for over repeat
			
			//candidate = t.findUnclosedState();
		//}
		return t;
	}
	public Automaton getLearnerAutomaton(){
		return this.aLearner;
	}
	
	public int getround(){
		return this.round;
	}
	public int getclosedrounds(){
		return this.closedrounds;
	}
	public int getconsistentrounds(){
		return this.consistentrounds;
	}
}
