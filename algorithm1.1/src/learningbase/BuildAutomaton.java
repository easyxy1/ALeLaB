package learningbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import nAutomata.*;
import nAutomata.State.Type;
import reTodfa.DFA;
import reTodfa.NFA;
import reTodfa.RegularExpression;
import reTodfa.State_Re;
import word.Word;

public class BuildAutomaton extends Automaton {
//	private static final String epslon="\u03B5";
//	private static Set <Character> input = new HashSet <Character> ();
//	private static Stack<Character> operator = new Stack<Character> ();	
//	private static Stack<NFA> stackNfa = new Stack<NFA> ();
//	private State initState=new State("init");
//	private Set<State> finalState=new HashSet();
//	private Set<State> states=new HashSet();
//	public int totallevel=0;
//	public Pattern pattern;
//	private List<String> alphabet=new ArrayList<String>();
//	private Set<String> freshname=new HashSet();
//	private Set<Transition> transitions=new HashSet();	

	//create an automaton from an observation table [Version 1.0]
	public BuildAutomaton(Table table){
		//Set<State> states=new HashSet();
		table.distinguishContent();
		//System.out.println(t.getContent().size());
		
		//initialise initial state
		//State initState=this.getinitState();
		this.addAlphabet(table.setA);
		this.addfreshname(table.freshName);
		List<String> operators=new ArrayList<String>();
		operators.add("<");
		operators.add(">");
		int statecount=0;
		for(String s:table.getContent()){
			State newstate=new State(String.valueOf(statecount));
			newstate.setdistinguishedName(s);
			this.addState(newstate);
			statecount++;
			
		}
		//System.out.println(this.getAllstatasname().size()+ this.getAllstatasname().toString());
		//System.out.println(t.setA.size()+ t.setA.toString());
		
		//set states details and transistions
		//String fromstate=null;
	    State fromState=null;
	    //String tostate=null;
	    State toState=null;
	    //String transition=null;
	    Transition newT=null;
	    /*
	     * @c1
	     * @c2
	     * @c3
	     */
	    int count=0, c1=0,c2=0,c3=0;
	    //add tranisitons for states
	    for(Row r:table.getdistinguishedRows()){
	    	count++;
	    	fromState=this.getStatebydistinguishedName(r.distinguishContents());
	    	if(r.getFirstContent()==State.FINAL){
	    		fromState.setFinal();
    		}
	    	fromState.setLevel(r.getlevel());
	    	//set fromstate type
	    	switch(r.getFirstContent()){
	    	case State.FINAL:{
	    		fromState.settype(Type.FINAL);break;
	    	}
	    	case State.PREFIX:{
	    		fromState.settype(Type.PREFIX);break;
	    	}
	    	case State.NO:{
	    		fromState.settype(Type.SINK);break;
	    	}
	    	case State.INVALID:{
	    		fromState.settype(Type.INVALID);break;
	    	}
	    	}
			for(String a:table.setA){
				String to=Word.deleteLambda(r.getLabel()+a);
				Row toRow=table.getRowForPrefix(to);
				toState=this.getStatebydistinguishedName(toRow.distinguishContents());
		    	
				newT=new Transition(fromState,toState, a);
    			fromState.setTransition(newT);
    			
    			this.addTransition(newT);
    			c1++;
			}
			for(String a:table.freshName){
				String to=Word.deleteLambda(r.getLabel()+a);
				Row toRow=table.getRowForPrefix(to);
				
				if(toRow!=null){
					toState=this.getStatebydistinguishedName(toRow.distinguishContents());
					newT=new Transition(fromState,toState, a);
					fromState.setTransition(newT);
    			
					this.addTransition(newT);
					c2++;
				}
			}
			for(String a:operators){
				String to=Word.deleteLambda(r.getLabel()+a);
				Row toRow=table.getRowForPrefix(to);
				
				if(toRow!=null){
					toState=this.getStatebydistinguishedName(toRow.distinguishContents());
					newT=new Transition(fromState,toState, a);
					fromState.setTransition(newT);
					//System.out.println(to);
					this.addTransition(newT);
					c3++;
				}
			}
			if(r.getLabel().equals("0")){
	    		//set initial state information
	    		this.setinitState(fromState);
	    		}
		}
	    //System.out.println(this.getAllTransitions().size()+"\t"+count+c1+c2+c3);
    	
	    this.resetFinalstates();
		
	}
	
	private static Map<String, Set<String[]>> transitions; 
	
	public BuildAutomaton(File file,Set<String> alphabet) {
		//initialise the parameter
		this.clear();
		State initState=this.getinitState();
		initState.setInitial();
		this.addState(initState);
		this.addAlphabet(alphabet);
		transitions=new HashMap<String, Set<String[]>>();
		
		//check txt file does exist
		//File file=new File(URL);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		int transferTime=0;
//		int temptotallevel=0;
		
		if(line.contains("regular")){
			String tempreg= line.substring(8);	//Separate line data by " "(a space)
			this.pattern=Pattern.compile(tempreg);
		
		}
		
		while(line.length()>0){	//read one line each time
			
			//set final states
			if(line.startsWith("final")){
				String[] tmp = line.split(" ");	//Separate line data by " "(a space)
				for(String tmpstring:tmp){
					if(!tmpstring.isEmpty() && !tmpstring.equals("final")){
						
						this.addFinalState(tmpstring);
					
					}
				}
			}else if(line.startsWith("initial")){
				String[] tmp = line.split(" ");	//Separate line data by " "(a space)
				for(String tmpstring:tmp){
					if(!tmpstring.isEmpty() && !tmpstring.equals("initial")){
						initState.setname(tmpstring);
						this.setinitState(initState);
					
					}
				}
			}else {
				/*
				 * @line each line is a transition. divide it into fromstate, symbol, tostate
				 */
				String[] tmp = line.split(" ");	//Separate line data by " "(a space)
				
				if(tmp.length>1){
					String[] nextTo={tmp[1],tmp[2]};
				
					if(!transitions.containsKey(tmp[0])){
						Set<String[]> transitionset=new HashSet<String[]>();
						transitionset.add(nextTo);
						transitions.put(tmp[0],transitionset );
					}else{
						//System.out.println(transitions.get(tmp[0]).toString());
						transitions.get(tmp[0]).add(nextTo);
					}
				}
			
			}
			try {
				if(br.ready()){
					line = br.readLine();
				}else
					break;
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//System.out.println("file build transitions: "+transitions.size()+transitions.keySet().toString());
		createstatesbypath(transitions);
		
		
		//System.out.println("states number:"+this.getAllstates().size());
		
		this.expandtransitons();
	
	}
	
	public BuildAutomaton(String expression, Set<String> alphabet){
		RegularExpression.clear();
		RegularExpression.addalphabet(alphabet);
		if(expression.length()==0){
			this.clear();
			State initState=this.getinitState();
			initState.setInitial();
			this.addState(initState);
			this.addAlphabet(alphabet);
			State finalState=new State("f");
			finalState.setFinal();
			this.addState(finalState);
			this.addFinalState("f");
			transitions=new HashMap<String, Set<String[]>>();
			for(String s:alphabet){
				Transition t=new Transition(initState,initState,s);
				initState.setTransition(t);
				this.addTransition(t);
			}
			
		}else if(expression=="0"){
			this.clear();
			this.addAlphabet(alphabet);
			State initState=this.getinitState();
			initState.setInitial();
			initState.setFinal();
			this.addState(initState);
			this.expandtransitons();
//			State sinkState=new State("s");
//			transitions=new HashMap<String, Set<String[]>>();
//			for(String s:alphabet){
//				Transition t=new Transition(initState,sinkState,s);
//				initState.setTransition(t);
//				this.addTransition(t);
//			}
			this.resetFinalstates();
		}else{
		NFA nfa=RegularExpression.generateNFA(expression);
		//add sink states
		//nfa.outputtoTXT("og");
		//System.out.println(nfa.allstateinformation());
		//nfa.expandtransitons();
		//nfa.outputtoTXT("ep");
		
		//System.out.println("first "+nfa.getNfa().getFirst().getStateID());
		DFA dfa=RegularExpression.generateDFA(nfa);
		//System.out.println("not minimised \n"+dfa.allstateinformation());
		
		dfa.minimisedfa();
		//System.out.println("minimised "+dfa.allstateinformation());
		
		dfa.outputtoTXT("teacherdfa");
		File file=new File("teacherdfa.txt");
		Automaton newAutomaton= new BuildAutomaton(file,alphabet);
		this.setinitState(newAutomaton.getinitState());
		
		}
	}
	public void gettransitons(){
		if(transitions.size()>0){
			for(String key:transitions.keySet()){
				String transTo="";
				if(transitions.get(key).size()>0){
					for(String[] to:transitions.get(key)){
						transTo=transTo+to[0]+"\t"+to[1]+"\t\n";
					}
				}
				System.out.println(key+"\t"+transTo);
			}
		}
	}
	

	private void createstatesbypath(Map<String, Set<String[]>> transitions){
		if(!transitions.isEmpty()){
			
			int temptotallevel=0;
			State fromstate=this.getinitState();
			//System.out.println("this initial state name "+fromstate.getname());
			String startpoint=this.getinitState().getname();
			Queue<String> unprocessed=new LinkedList<String>();
			unprocessed.offer(this.getinitState().getname());
			for(State s:this.getAllFinalstates()){
				if(!unprocessed.contains(s.getname()))
					unprocessed.offer(s.getname());
			}
			while(!unprocessed.isEmpty()){
				startpoint=unprocessed.poll();
				fromstate=this.getState(startpoint);
				
				if(transitions.get(startpoint)==null){
					continue;
				}
				else{
					Set<String[]> toInfo=transitions.get(startpoint);
				
				for(String[] tmp :toInfo){
					String symbol=tmp[0];
					State tostate;
					if(!unprocessed.contains(tmp[1])&&!this.getAllstatasname().contains(tmp[1])){
						tostate=new State(tmp[1]);
						//this.addState(tostate);
						unprocessed.offer(tmp[1]);
					}else{
						tostate=this.getState(tmp[1]);
					}	
				
					switch(symbol){
					case "<":{
						fromstate.setAlocateTo(tostate);
						temptotallevel=Math.max(tostate.getLevel(),temptotallevel); 
						//System.out.println(temptotallevel);
						this.addfreshname(String.valueOf(temptotallevel));
						Transition t=new Transition(fromstate,tostate,symbol);
						this.addTransition(t);
						fromstate.setTransition(t);
						break;
					}
					case ">":{ 
						fromstate.setDealocateTo(tostate);
						Transition t=new Transition(fromstate,tostate,symbol);
						this.addTransition(t);
						fromstate.setTransition(t);
						break;
					}
					default: {
						this.addAlphabet(symbol);
						this.addfreshname(symbol);
						Transition t=new Transition(fromstate,tostate,symbol);
						fromstate.setTransition(t);
						
						this.addTransition(t);
					}
					}
					this.addState(tostate);
				}//end for
				//unprocessed.poll();
			}//end else-if 
				
			}//end while
			
			this.setTotalLevel();
		}else{
			throw new EmptyStackException();
		}
	}

}