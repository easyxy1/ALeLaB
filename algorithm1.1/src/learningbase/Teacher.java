package learningbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import nAutomata.Automaton;
import nAutomata.Operators;
import nAutomata.State;
import nAutomata.State.Type;
import nAutomata.Transition;
import reTodfa.DFA;
import reTodfa.NFA;
import reTodfa.RegularExpression;
import reTodfa.State_Re;
import word.Word;



public class Teacher {
	
	private Automaton teacherAutomaton;
	private Set<String> selectedCounterexample;
	private int equivalentRound=0;
	//numbers of answering membership queries
	private static int answeredMQ=0;
	private static List<State> checkedState=new ArrayList<State>();
	//the strategy to select counterexamples
	private Strategy strategy=Strategy.VERTICAL;
	private int totalLevel=0;
	private String testCounterexampleRecording="";
	public enum Strategy{VERTICAL, HORIZONTAL, MIX}
	public final static boolean Fromfile=true;
	public final static boolean FromRE=false;
	
	private String regularExpression=null;
	private String dataFrom=null;
	
	
	/**
	 * set up teacher with file of automata data
	 * @param choice the way to create Teacher Oracle, true is to create from automata data, false is to create from regular expressions	
	 * @param alphabet finite alphabet
	 * @param strategy the way to choose counterexamples
	 * @param language depends on choice, it is a regular expression or a file path
	 */
	public Teacher(boolean choice,Set<String> alphabet, Strategy strategy, String language){
		//init
		setStrategy(strategy);
		this.totalLevel=0;
		this.setdataFrom(language);
		this.selectedCounterexample=new HashSet<String>();
		this.answeredMQ=0;
		teacherAutomaton=null;
		
		//creat teacher
		if(choice){
		//case 1, create teacher with files
			File file=new File(language);
			teacherAutomaton =new BuildAutomaton(file,alphabet);
				
			
		}else{
		//case 2, create teacher with regular expressions
		
				this.setRE(language);
			
				RegularExpression.clear();
				RegularExpression.addalphabet(alphabet);
				NFA nfa=RegularExpression.generateNFA(language);
				
				nfa.outputtoTXT("og");
				//System.out.println(nfa.allstateinformation());
				//add sink states
				//nfa.expandtransitons();
				//nfa.outputtoTXT("ep");
				
				//System.out.println("first "+nfa.getNfa().getFirst().getStateID());
				DFA dfa=RegularExpression.generateDFA(nfa);
				//System.out.println("not minimised \n"+dfa.allstateinformation());
				
				dfa.minimisedfa();
				//System.out.println("minimised "+dfa.allstateinformation());
				
				dfa.outputtoTXT("teacherdfa");
				File file=new File("teacherdfa.txt");
				teacherAutomaton =new BuildAutomaton(file,alphabet);
				
				
		}
		if(teacherAutomaton!=null){
			this.totalLevel=teacherAutomaton.getTotalLevel();
		}else{
			System.out.println("Teacher_3 constuctor creates no automata");
		}
	}
	
	//data relevant method
	//set strategy of selecting counterexample
	public void setStrategy(Strategy i){
		this.strategy=i;
	}
	public Strategy getStrategy(){
		return this.strategy;
	}
	@SuppressWarnings("static-access")
	public int getAnsweredMQ(){
		return this.answeredMQ;
	}
	@SuppressWarnings("static-access")
	public Automaton getAutomaton(){
		return this.teacherAutomaton;
	}

	private void setRE(String re){
		this.regularExpression=re;
	}
	
	public String getRE() {
		// TODO Auto-generated method stub
		return this.regularExpression;
	}
	
	private void setdataFrom(String dataFrom){
		this.dataFrom=dataFrom;
	}
	
	public String getdataFrom() {
		// TODO Auto-generated method stub
		return this.dataFrom;
	}
	
	//functionality methods
	public Type getAnswer(String word){
		answeredMQ++;
		word=Word.deleteLambda(word);
		State s= teacherAutomaton.getStateBypath(word);
		if(s!=null){
			if(s.getFinal()){
				return State.Type.FINAL;
			}else{
				/* this method had infinite loop in some deterministic nominal automata */
				  if(Operators.getAcceptedSuffix1(teacherAutomaton, word)!=null){
					return State.Type.PREFIX;
				  }else
					return State.Type.SINK;	
			}
		}else
			return State.Type.SINK;
	
	}
	
	public Set<String> getAlphabet() {
		// TODO Auto-generated method stub
		return teacherAutomaton.getalphabet();
	}
	//return a bigger value for setting the automaton's total level. 
	public static int getMax(int i,int j){
		int max=j;
		if(i>j){
			max=i;
		}
		return max;
		
	}
	
	public String checkAutomaton(Automaton a){
		String counterexample=null;
		Automaton teacher =this.getAutomaton();
		Automaton learner =a;
		this.equivalentRound++;
		//check two automata equivalence.
		/**
		 * according to two minimal automata, if they have same size of states they accept the same language. 
		 * otherwise, there is at least one word in either of them.
		 */

		//System.out.println("start checking equivalence");
		if(teacher.getAllstates().size()!=learner.getAllstates().size()){
			//set a basic range of counterexamples' length
			int teacherLength=teacher.getAllstates().size();
			int learnerLength=learner.getAllstates().size();
			int maxLength=0;
//			if(this.strategy==Strategy.HORIZONTAL)
//				maxLength=Math.min(teacherLength, learnerLength);
//			else
				maxLength=Math.max(teacherLength, learnerLength);
			int counterexampleLength=learnerLength;
			//System.out.println("teacher has "+teacher.getAllstates().size());
			
			Set<String> availableCEset= new HashSet<String>();
			counterexample=selectCE(learner,counterexampleLength,maxLength);
			//version: manage following code to the method selecteCE
//			if(learner.getAllFinalstates().isEmpty() && teacher.getAllFinalstates().isEmpty()){
//				counterexample=null;
//			}else{ //find out all words of the length of teacher automaton's states' quantity 
//					//while(availableCEset.isEmpty() && counterexampleLength<maxLength){
//					while( counterexampleLength<maxLength){
//						availableCEset.addAll(Operators.getAvailableCEset(teacher, learner, counterexampleLength));
//						//remove all the selected counterexample, then the result is the set of available counterexamples for this turn.
//						//availableCEset.removeAll(selectedcounterexample);
//						counterexampleLength++;
//					}
//					
//			
//			}
//			//System.out.println("availableCEset is empty?"+availableCEset);
//			this.testCounterexampleRecording=testCounterexampleRecording+"\n equivalence query round "+this.equivalentRound;
//			if(!availableCEset.isEmpty()){
//				//System.out.println("start selecting a counterexample");
//				switch(this.strategy){
//				case VERTICAL: counterexample=verticalStrategy(availableCEset);break;
//				case HORIZONTAL: counterexample=horizontalStrategy(availableCEset);break;
//				default: System.out.println("Unknown strategy");
//				}
//				this.testCounterexampleRecording=testCounterexampleRecording+"\n selected counterexample is "+counterexample;
//			}else{
//
//				System.out.println("no enough sample, please add counterexample length");
//			}
		
	}//end if
		//equivalence query finished, reply to learner
		//equivalentRound++;
		this.selectedCounterexample.add(counterexample);
		
		return counterexample;
		
	}
	
	//select a counterexample under strategy
	private String selectCE(Automaton learner,int counterexampleLength,int maxLength){
		String selectedString = null;
		int index=0;
		Automaton teacher =this.getAutomaton();
		int lengthFlag=maxLength;
		
		Set<String> availableCEset= new HashSet<String>();
		while(availableCEset.isEmpty() && index<(maxLength*2)){
			lengthFlag=lengthFlag+index;
			if(learner.getAllFinalstates().isEmpty() && teacher.getAllFinalstates().isEmpty()){
				selectedString=null;
			}else{ //find out all words of the length of teacher automaton's states' quantity 
					//while(availableCEset.isEmpty() && counterexampleLength<maxLength){
					while( counterexampleLength<lengthFlag){
						availableCEset.addAll(Operators.getAvailableCEset(teacher, learner, counterexampleLength));
						//remove all the selected counterexample, then the result is the set of available counterexamples for this turn.
						availableCEset.removeAll(this.selectedCounterexample);
						counterexampleLength++;
					}
					
			
			}
			index++;
		}
		switch(this.strategy){
		case VERTICAL: selectedString=verticalStrategy(availableCEset);break;
		case HORIZONTAL: selectedString=horizontalStrategy(availableCEset);break;
		default: System.out.println("Unknown strategy");
		}
		this.testCounterexampleRecording=testCounterexampleRecording+"\n selected counterexample is "+selectedString;
	
		
		return selectedString;
	}
	
	
	//output record for couterexample
	public String getrecordofCE(){
		return this.testCounterexampleRecording;
	}
	
	//Strategy 1 VERTICAL: add more names
	private String verticalStrategy(Set<String> wordlist){
		String selected=null;
		
				//strategy main body. select one ideal word as the counterexample
				int choice=this.getAutomaton().getTotalLevel();
				//version 20181026: return a fixed location's counterexample
//				while(choice>=0 && !wordlist.isEmpty()){
//					
//					for(String s: wordlist){
//									
//						if(choice==0){
//							selected=s;
//							choice=-1;
//							break;
//						}else 
//							if(Word.maxlayer(s)==choice){
//								selected=s;
//								choice=-1;
//								break;
//							}
//						
//					}
//					choice--;
//				}
				//verion after 20181026: return a ideal random counterexample
				List<String> idealset=new ArrayList<String>();
				while(!wordlist.isEmpty() && choice>=0 && idealset.isEmpty()){
					for(String s:wordlist){
						if(Word.maxLayer(s)==choice){
							idealset.add(s);
						}
					}
					if(idealset.isEmpty())
						choice--;
				}
				if(!idealset.isEmpty()){
					Random random=new Random();
					int randomNo=random.nextInt(idealset.size());
					selected=idealset.get(randomNo);
				}
		return selected;
	}

	//Strategy 2 HORIZAONTAL: the completion of a layer (not precise, base functionality)
		private String horizontalStrategy(Set<String> wordlist){
			String selected=null;
			boolean notfound=true;
			
					//strategy main body. select one ideal word as the counterexample
//					int choice=0;
//					if(!wordlist.isEmpty()){
//						//System.out.println(this.getAutomaton().getTotalLevel());
//						while(choice<=this.getAutomaton().getTotalLevel() && notfound){
//							for(String w: wordlist){
//																
//								if(choice==Word.maxlayer(w)){
//									selected=w;
//									notfound=false;
//									break;
//								}
//							
//							}
//							choice++;
//						}
//					}
			//verion after 20181026: return a ideal random counterexample
			List<String> idealset=new ArrayList<String>();
			int choice=0;
			while(!wordlist.isEmpty() && choice<=this.getAutomaton().getTotalLevel() && idealset.isEmpty()){
				for(String s:wordlist){
					if(Word.maxLayer(s)==choice){
						idealset.add(s);
					}
				}
				if(idealset.isEmpty())
					choice++;
			}
			if(!idealset.isEmpty()){
				Random random=new Random();
				int randomNo=random.nextInt(idealset.size());
				selected=idealset.get(randomNo);
			}
			return selected;
		}

}
