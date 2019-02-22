package test;

//import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import learningbase.LearningProcess;
import learningbase.Teacher;
import learningbase.Teacher.Strategy;
import nAutomata.Automaton;
//import nAutomata.Transition;
import reTodfa.Graphviz;
import word.Word;

public class LearningProcessTest {
	int testcount=1;
	Set<String> alphabet =new HashSet<String>();
	final String INPUTFILE="1";
	final String INPUTRE="2";
	final int STRATEGYVERTICAL=1;
	final int STRATEGYHORIZONTAL=2;
	File file=new File("exprimentrecord.txt");
	File file1=new File("exprimentrecord.csv");
	Set<String> finitalphabet=new HashSet<String>();
	@Before
	public void setup(){
		alphabet.add("a");
		alphabet.add("b");
		finitalphabet.add("a");
		finitalphabet.add("b");
		finitalphabet.add("c");
		finitalphabet.add("d");
		finitalphabet.add("e");
		finitalphabet.add("f");
		finitalphabet.add("g");
		finitalphabet.add("h");
		finitalphabet.add("i");
		finitalphabet.add("j");
		finitalphabet.add("k");
		if (!file.exists()) {  
	       try {
			file.createNewFile();
	       } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	       } 
	    } 
		
		if (!file1.exists()) {  
		       try {
				file1.createNewFile();
				FileWriter fileWriter = new FileWriter(file,true);   
    			//String head="Expressions\t"+ "Strategy\t"+"Time\t"+ "Rounds\t"+"Memory"+ "\r\n";

    			String head="Expressions,"+ "Strategy,"+"Time,"+ "Rounds,"+"Memory"+ "\r\n";
				fileWriter.write(head);
				fileWriter.close(); // close filewriter 
		       } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		       } 
		    }
	}
	
	private String runningrecord(Teacher teacher1){
	//	Teacher_3 teacher1=new Teacher_3(INPUTRE,alphabet,STRATEGYVERTICAL,testRE);
		String testRE=teacher1.getdataFrom();
		String recordforcsv=Word.convertforLatex(testRE);
		Automaton a =teacher1.getAutomaton();
		a.visualisation();
		if(a!=null){
			String timerecord="";
			String memoryrecord="";
			int closedrounds=0;
			int consistentrounds=0;
			//Graphviz.createDotGraph(a.tostringforDot(), "teacher"+testcount+"via1");
			try {
				//start data collection
				Runtime run=Runtime.getRuntime();
				run.gc();
				long startTime=System.currentTimeMillis();
				long startMemory=run.totalMemory()-run.freeMemory();
				System.out.println("memory before run: total "+run.totalMemory()+" free "+run.freeMemory()+" use "+startMemory);
				
				//learning process
				LearningProcess test1= new LearningProcess(teacher1);
				
				//get closed & consistent rounds and membership queries rounds
				closedrounds=test1.getclosedrounds();
				System.out.println("closed rounds:"+closedrounds);
				consistentrounds=test1.getconsistentrounds();
				System.out.println("consistent rounds rounds:"+consistentrounds);
				int membershipqueries=teacher1.getansweredMQ();
				//System.out.println("membership queries rounds:"+membershipqueries);
				
				//end data collection
				long endTime=System.currentTimeMillis();
				long endMemory=run.totalMemory()-run.freeMemory();
				//System.out.println("memory after run: total "+run.totalMemory()+" free "+run.freeMemory()+" use "+endMemory);
				
				
				//analyse time and memory
				long MainloopcostedTime=endTime-startTime;
				long costedMemory=endMemory-startMemory;
				timerecord="Main loop costed Time: "+MainloopcostedTime+"ms.\r\n";
				memoryrecord="Main loop costed memory: "+costedMemory+".\r\n";
				//System.out.println(test1.getLearnerAutomaton().tostringforDot());
				String outputname="learner"+String.valueOf(testcount)+"via1";
				Graphviz.createDotGraph(test1.getLearnerAutomaton().tostringforDot(), outputname);
				
				//test records
				String processroundrecord="Main loop runs "+test1.getround()+" rounds.\r\n";
				
//				FileWriter fileWriter = new FileWriter(file,true);   
//    			String record="Test date: "+ java.time.LocalDate.now()+"test sample from:"+ testRE+ "\r\n";
//    			String teststrategy="test parameters: counterexample strategy is "+ teacher1.getStrategy()+"\r\n";
//    			record=record+teststrategy+timerecord+processroundrecord+memoryrecord+"----------------"+"\r\n";
//				fileWriter.write(record);
//				fileWriter.close(); // close filewriter for txt
				
//				FileWriter fileWriterCSV = new FileWriter(file1,true);   
    			//recordforcsv=recordforcsv+"\t"+teacher1.getStrategy()+"\t"+MainloopcostedTime+"\t"+test1.getround()+"\t"+costedMemory+"\r\n";
    			recordforcsv=recordforcsv+","+teacher1.getStrategy()+","+MainloopcostedTime+","+test1.getround()+","+costedMemory+"\r\n";
//   			fileWriterCSV.write(recordforcsv);
//    			fileWriterCSV.close();// close filewriter for csv
				
    			System.out.println("one recorded.");
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			  
         
			
		}else{
			System.out.println("teacher creation failed");
		}
		
		return recordforcsv;
	}

	
	private String runningaverage(String test, int n, Strategy strategy){
		String recordforcsv="";
		
		int m=n;
		long costedMemory=0;
		long MainloopcostedTime=0;
		int rounds=0;
		int closedrounds=0;
		int consistentrounds=0;
		int membershipqueries=0;
		
		while(m>0){
			Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,strategy,test);
			String testRE=teacher1.getdataFrom();
			recordforcsv=n+","+Word.convertforLatex(testRE);
			Automaton a =teacher1.getAutomaton();
			if(a!=null){
				try {
					//start data collection
					Runtime run=Runtime.getRuntime();
					run.gc();
					long startTime=System.currentTimeMillis();
					long startMemory=run.totalMemory()-run.freeMemory();
					
					//learning process
					LearningProcess test1= new LearningProcess(teacher1);
					
					//end data collection
					long endTime=System.currentTimeMillis();
					long endMemory=run.totalMemory()-run.freeMemory();
					
					System.out.println("memory before run: "+(endMemory-startMemory)+"round"+test1.getround()+" time "+(endTime-startTime));
					
					//analyse time, round and memory
					MainloopcostedTime=MainloopcostedTime+endTime-startTime;
					costedMemory=costedMemory+endMemory-startMemory;
					rounds=rounds+test1.getround();
					
					//get closed & consistent rounds and membership queries rounds
					closedrounds= closedrounds+test1.getclosedrounds();
					
					consistentrounds=consistentrounds+test1.getconsistentrounds();
					membershipqueries=membershipqueries+teacher1.getansweredMQ();
					
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				 
				m--;
				
				}else{
				System.out.println("teacher creation failed");
				}
			}
			
			costedMemory=costedMemory/n;
			MainloopcostedTime=MainloopcostedTime/n;
			rounds=rounds/n;
			membershipqueries=membershipqueries/n;
			closedrounds=closedrounds/n;
			consistentrounds=consistentrounds/n;
			//record for csv file
			//template:strategy,time,equivalence rounds,memory,closed rounds, consistent rounds, number of membership queries 
			recordforcsv=recordforcsv+","+strategy+","+MainloopcostedTime+","+rounds+","+costedMemory+","+closedrounds+","+consistentrounds+","+membershipqueries+"\r\n";
		
		return recordforcsv;
	}
	//test on input a file with visualised output via counterexample strategy VERTICAL
	@Test
		public void test1() throws IOException{
			
			String testpath="testsamples/1.txt";//file needs to be created
			Teacher teacher1=new Teacher(Teacher.Fromfile,alphabet,Strategy.VERTICAL,testpath);
			
			FileWriter fileWriterCSV = new FileWriter(file1,true);  
		
			fileWriterCSV.write(runningrecord(teacher1));
			fileWriterCSV.close();// close filewriter for csv
			testcount++;
		}//visualisation pass but not perfect, and content pass 
	
//test input a file with visualisation of learner's automaton via counterexample strategy HORIZONTAL
	@Test
	public void test2(){
		String testpath="testsamples/1.txt";
		Teacher teacher1=new Teacher(Teacher.Fromfile,alphabet,Strategy.HORIZONTAL,testpath);
		runningrecord(teacher1);
		testcount++;
	}
	
	//test on input regular expression and output via counterexample strategy HORIZONTAL
	
	@Test
	public void test3(){
		String testRE="<1<2<3>>>";
		
		Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.HORIZONTAL,testRE);
		//System.out.println(teacher1.getAutomaton().getAllstates().size()+"\n"+teacher1.getAutomaton().tostringforDot());
		Graphviz.createDotGraph(teacher1.getAutomaton().tostringforDot(), "teacher"+testcount+"via1");
		runningrecord(teacher1);
		testcount++;
	}//visualisation pass and content pass 20180830 (on buildautomaton with whole data)
	@Test
	public void test3_1(){
		String testRE="ab+<1a>+<b<2>>";
		
		Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.VERTICAL,testRE);
		
		runningrecord(teacher1);
		testcount++;
	}
	
	@Test
	public void testaveragebyrepeatingtimes() throws IOException{
		String testRE="<1<2<3>>>";
		File file=new File("averagedata.csv");
		//int times=1;
		if (!file.exists()) {  
		       try {
		    	   file.createNewFile();
		    	   FileWriter fileWriter = new FileWriter(file,true);   
 			
		    	   String head="Repeating,Expressions,Strategy,Time,Rounds,Memory,Closed Rounds, Consistent Rounds, No. of MQ\r\n";
		    	   fileWriter.write(head);
		    	   fileWriter.close(); // close filewriter 
		       } catch (IOException e) {
				// TODO Auto-generated catch block
		    	   e.printStackTrace();
		       } 
		}
		FileWriter fileWriter = new FileWriter(file,true); 
		//Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.VERTICAL,testRE);
		//String output=times+","+runningaverage(testRE,times);
		fileWriter.write(runningaverage(testRE,1,Strategy.HORIZONTAL));
		fileWriter.write(runningaverage(testRE,10,Strategy.VERTICAL));
		fileWriter.write(runningaverage(testRE,20,Strategy.VERTICAL));
		fileWriter.write(runningaverage(testRE,50,Strategy.VERTICAL));
		fileWriter.write(runningaverage(testRE,100,Strategy.VERTICAL));
		fileWriter.close(); // close filewriter 
	}
	@Test
	public void testcompare1() throws IOException{
		String testRE1="<1<2<3>>>";
		String testRE2="<<<123>>>";
		File compare1=new File("compare1.csv");
		//int times=1;
		if (!compare1.exists()) {  
		       try {
		    	   compare1.createNewFile();
		    	   FileWriter fileWriter = new FileWriter(compare1,true);   
 			
		    	   String head="Repeating,Expressions,Strategy,Time,Equavalence Rounds,Memory\r\n";
		    	   fileWriter.write(head);
		    	   fileWriter.close(); // close filewriter 
		       } catch (IOException e) {
				// TODO Auto-generated catch block
		    	   e.printStackTrace();
		       } 
		}
		FileWriter fileWriter = new FileWriter(compare1,true); 
		//Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.VERTICAL,testRE);
		//String output=times+","+runningaverage(testRE,times);
		fileWriter.write(runningaverage(testRE1,5,Strategy.VERTICAL));
		fileWriter.write(runningaverage(testRE2,5,Strategy.VERTICAL));
		fileWriter.write(runningaverage(testRE1,5,Strategy.HORIZONTAL));
		fileWriter.write(runningaverage(testRE2,5,Strategy.HORIZONTAL));
		fileWriter.write(runningaverage(testRE1,10,Strategy.VERTICAL));
		fileWriter.write(runningaverage(testRE2,10,Strategy.VERTICAL));
		fileWriter.write(runningaverage(testRE1,10,Strategy.HORIZONTAL));
		fileWriter.write(runningaverage(testRE2,10,Strategy.HORIZONTAL));
		fileWriter.close(); // close filewriter 
	}
	private void printaveragedata(String expression,String filename) throws IOException{
		File file=new File(filename+".csv");
		//int times=1;
		if (!file.exists()) {  
		       try {
		    	   file.createNewFile();
		    	   FileWriter fileWriter = new FileWriter(file,true);   
 			
		    	   String head="Repeating,Expressions,Strategy,Time,Rounds,Memory\r\n";
		    	   fileWriter.write(head);
		    	   fileWriter.close(); // close filewriter 
		       } catch (IOException e) {
				// TODO Auto-generated catch block
		    	   e.printStackTrace();
		       } 
		}
		FileWriter fileWriter = new FileWriter(file,true); 
		
		fileWriter.write(runningaverage(expression,1,Strategy.VERTICAL));
		fileWriter.write(runningaverage(expression,10,Strategy.VERTICAL));
		fileWriter.write(runningaverage(expression,20,Strategy.VERTICAL));
		fileWriter.write(runningaverage(expression,50,Strategy.VERTICAL));
		fileWriter.write(runningaverage(expression,100,Strategy.VERTICAL));
		fileWriter.close(); // close filewriter 
	}
	
	@Test
	public void testplusincreasing() throws IOException{
		List<String> testREs=new ArrayList<String>();
		testREs.add("<a>");
		testREs.add("<b>");
		testREs.add("<1>");
		testREs.add("<a+b+1>");
		testREs.add("<a>+<b>+<1>");
		for(String testRE:testREs){
			String filename="plusincreasing"+testREs.indexOf(testRE);
			printaveragedata(testRE,filename);
		}
		
	}
	@Test
	public void testcomplexsample() throws IOException{
		Set<String> finitalphabet=new HashSet<String>();
	//	String testRE=
//		for(String testRE:testREs){
//			String filename="plusincreasing"+testREs.indexOf(testRE);
//			printaveragedata(testRE,filename);
//		}
		
	}
}
