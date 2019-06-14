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
import nAutomata.State;
import reTodfa.Graphviz;
import word.Word;

public class Learner {
	//private static TableSet tableSet;
	
	private static Set<String> alphabet=new HashSet();
	private static String teachercolumn="\t\t\t\t\t\t\t\t";
	private Automaton aLearner= new Automaton();
	private int round=1;
	private int numberofMQ=0;
	private int closedrounds=0;
	private int consistentrounds=0;
	private Teacher teacher=null;
	public static void main(String[] arg) throws IOException {
		
	}
	//one simple version for LearningProcess
	/**
	 * 
	 * @param teacher
	 * @throws IOException 
	 */
	public Learner(Teacher teacher) throws IOException{
		this.teacher=teacher;
		aLearner.clear();
		//learner starts learning
		round=1;
		alphabet=teacher.getAlphabet();
		Table nOTable=new Table();
		nOTable.setAlphabet(alphabet);
		nOTable.setLongPrefix();
		//nOTable.printTable();
		//ask membership queries through table's method
		//learner.processMembershipQueriesForTables(learner.getShortPrefixLabels());
		//learner.processMembershipQueriesForTables(learner.getLongPrefixLabels());
		//ask membership queries through own method
		processMembershipQueriesForTables(nOTable,teacher);
		nOTable.printTable();
		// check and make sure table closed and consistent
		makeTableClosedAndConsistent(nOTable);
		//build an automaton with the closed  and consistent table
		//Automaton aLearner= new BuildAutomaton(learner);
		aLearner= new BuildAutomaton(nOTable);
		//print table to console
		nOTable.printTable();
		
		//ask teacher for an equivalence query
		String counterexample=teacher.checkAutomaton(aLearner);
		
		// Learner use the resule of the equivalence query. If there is a counter example, extends the table with the couterexample and modifies a new equivalence query. If not, terminates.
		while (counterexample!=null) {
			System.out.println("counterexample:"+counterexample);
			//a new round starts
			round++;
			//control the max running times, change it for huge example 
			if(round>100){
				System.out.println("stop by force");
						break;
			}
			
			//use counterexample to extend table
			try {
				nOTable.extendByCE(counterexample);
				processMembershipQueriesForTables(nOTable,teacher);
			}catch (Exception e) {  
				System.out.println("Exception in "+e.getMessage()+"\r\n");
						
			}
			
			//make table closed and consistent
			try {
				makeTableClosedAndConsistent(nOTable);
			}catch (Exception e) {  
				System.out.println("Exception in "+e.getMessage()+"\r\n");
			} 
			//print table to console
			nOTable.printTable();
			
			try {
				aLearner= new BuildAutomaton(nOTable);
			}catch (Exception e) {  	
				System.out.println("Exception in "+e.getMessage()+"\r\n");
			} 
			
			counterexample=teacher.checkAutomaton(aLearner);	
			
		} 
		
		String finish="Finished.\n ";
		if(counterexample==null)
			Graphviz.createDotGraph(this.getLearnerAutomaton().toStringforDot(), "example");
		finish+="The learner got an automaton accepted the Teacher's language";
		//algorithm terminates
		System.out.println(finish);
				
		
	}
	/**
	 * When new states are added to the observation table, this method fills the table values. For each
	 * given state it sends one membership query for each specified suffix symbol to the oracle of the
	 * form (state,symbol).
	 *
	 * @param nOTable
	 * 		.
	 * @param teacher
	 * 		.
	 * @throws IOException 
	 */
	private void processMembershipQueriesForTables(Table nOTable,Teacher teacher) throws IOException{
		Collection<String> states= nOTable.getShortPrefixLabels();
		states.addAll(nOTable.getLongPrefixLabels());
		Collection<String> suffixes=nOTable.getSuffixes();
		List<DefaultQuery> queries = new ArrayList<>(states.size());
		for (String prefix : states) {
			for (String suffix : suffixes) {
				//queries.add(new DefaultQuery(prefix, suffix));
				String word=prefix+suffix;
				if(Word.isLegal(word)){
					//query.answer(Teacher.getanswer(word));// language input sotluion 1
					nOTable.addResult(prefix, suffix, teacher.getAnswer(word));
					this.numberofMQ++;
				}else
					nOTable.addResult(prefix, suffix,State.INVALID);
			}
		}
	}
	private void makeTableClosedAndConsistent(Table table) throws IOException {
		// TODO Auto-generated method stub
		boolean closedAndConsistent = false;
		int singleClosedround=0;
		int singleConsisround=0;
		while (!closedAndConsistent) {
			closedAndConsistent = true;
			//System.out.println("closed: "+table.isClosed());
			if (!table.isClosed()) {
				closedrounds++;
				singleClosedround++;
				closedAndConsistent = false;
				table=closeTable(table);
				//System.out.println("After modifying, closed is "+table.isClosed());
				//table.printTable();
			}
			
			//System.out.println("consistent: "+table.isConsistentWithAlphabet());
			
			if (!table.isConsistentWithAlphabet()) {
				consistentrounds++;
				singleConsisround++;
				closedAndConsistent = false;
				table= ensureConsistency(table);
				//System.out.println("After modifying, consistent is "+table.isConsistentWithAlphabet());
				//table.printTable();
			}
		}
		System.out.println("After modified "+singleClosedround+" closednes "+singleConsisround+ " consistency, table is closed and consistent: ");
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
		
		
		//t.processMembershipQueriesForTables(t.getShortPrefixLabels());
		
	//	System.out.println("test in ensureConsistency: add short prefix...");// test for over repeat
		//t.processMembershipQueriesForTables(t.getLongPrefixLabels());
	//	System.out.println("test in ensureConsistency: add long prefix...");// test for over repeat
		processMembershipQueriesForTables(t,this.teacher);
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
			//t.processMembershipQueriesForTables(t.getLongPrefixLabels());
			//System.out.println("Test on closetable add long prefix...");// test for over repeat
			processMembershipQueriesForTables(t,this.teacher);
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
	
	public int getnumberofMQ(){
		return this.numberofMQ;
	}
	public int getclosedrounds(){
		return this.closedrounds;
	}
	public int getconsistentrounds(){
		return this.consistentrounds;
	}
}
