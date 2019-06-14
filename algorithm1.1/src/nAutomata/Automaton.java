package nAutomata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import nAutomata.State.Type;
import reTodfa.Graphviz;
import reTodfa.State_Re;
import word.Word;


public class Automaton {
	private Set<State> states;
	private State initState=new State("init");
	private Set<State> finalState;
	private int totalLevel=0;
	public Pattern pattern;
	//finite alphabet, may contain "<, >"
	private Set<String> alphabet;
	private Set<String> freshName;
	private Set<Transition> transitions;
	
	private boolean isWhole=true;
	private static int totalID=1;
	private List<State> sinkstates=new ArrayList<State>();

//constructor
	public Automaton(){
		
//		this.initState.setInitial();
		this.states=new HashSet();
//		this.states.add(initState);
		this.alphabet=new HashSet<>();
		this.finalState=new HashSet();
		this.freshName=new HashSet();
		this.transitions=new HashSet();
	}

	public void clear(){
		this.alphabet.clear();
		this.initState=new State("init");
		this.finalState.clear();
		this.states.clear();
		this.freshName.clear();
		this.transitions.clear();
		this.totalLevel=0;
	}
	
	//set initial state of the automaton
		public void setinitState(State s){
			this.states.remove(this.initState);
			this.initState=s;
			this.initState.setInitial();
			this.initState.setID(0);
			this.states.add(initState);
		}
//get initial state of the automaton
	public State getinitState(){
		return this.initState;
	}
//add an element into alphabet
	protected void addAlphabet(String s){
		if(!this.alphabet.contains(s) && !Pattern.matches("^[0-9]*$", s))
			this.alphabet.add(s);
	}
	//add list into alphabet
		protected void addAlphabet(Set l){
		
				this.alphabet.addAll(l);
		}
//return the automaton's alphabet
	public Set<String> getalphabet() {
		// TODO Auto-generated method stub
		return this.alphabet;
	}

	public Set<String> getfreshname() {
		// TODO Auto-generated method stub
		return this.freshName;
	}
	protected void addfreshname(String s) {
		// TODO Auto-generated method stub
		if(Pattern.matches("^[0-9]*$", s))
			this.freshName.add(s);
		
	}
	protected void addfreshname(Set l) {
		// TODO Auto-generated method stub
	
			this.freshName.addAll(l);
		
	}
//add one transition
	protected void addTransition(Transition t){
		this.transitions.add(t);
	}
//get all transitions
	public Set<Transition> getAllTransitions(){
		
		return  Collections.unmodifiableSet(this.transitions);
	} 
//add a state
	protected void addState(State s){
		if(!this.getAllstatasname().contains(s.getname())){
			s.setID(totalID);
			this.states.add(s);
			this.totalID++;
		}
	}
//add a final state 
	protected void addFinalState(String name){
		if(this.getAllstatasname().contains(name)){
			this.getState(name).setFinal();;
			this.finalState.add(this.getState(name));
		}else{
			State newstate=new State(name);
			newstate.setLevel(0);
			newstate.setFinal();
			this.addState(newstate);
			this.finalState.add(newstate);
		}
		
	}
//check all states and reset set of final states
	public void resetFinalstates(){
		this.finalState.clear();
		for(State s:this.states){
			if(s.getFinal()){
				this.finalState.add(s);
			}
		}
	}

	public Set<State> getAllFinalstates(){
		
		return  Collections.unmodifiableSet(this.finalState);
	}  

	public Map<State,String> getPreStates(State s){
		Map<State,String> preStates=new HashMap<State,String>();
		for(Transition t: this.getAllTransitions()) {
			if(t.getNext().equals(s)) {
				preStates.put( t.getPrevious(),t.getSymbol());
			}
		}
		return  Collections.unmodifiableMap(preStates);
	}

	public void setTotalLevel(){
		for(String n:this.freshName){
			this.totalLevel=Math.max(totalLevel, Integer.parseInt(n));
		}
	
	}
	
	public int getTotalLevel(){
		this.totalLevel=this.freshName.size();
		return this.totalLevel;
	}
	public Set<State> getAllstates(){
		
		return  Collections.unmodifiableSet(this.states);
	} 

	public List<String> getAllstatasname(){
		List<String> names=new ArrayList<String>();
		for(State s:this.states){
			names.add(s.getname());
		}
		return  Collections.unmodifiableList(names);
	}
	public List<String> getAlldistinguishedname(){
		List<String> names=new ArrayList<String>();
		for(State s:this.states){
			names.add(s.getdistinguishedName());
		}
		return  Collections.unmodifiableList(names);
	}
	//get a state
	public State getState(String name){
		State returnstate=null;
		for(State s:this.states){
			if(s.getname().equals(name)){
				returnstate=s;
				break;
			}
		}
		return returnstate;
	}
	
	public State getState(int id){
		State returnstate=null;
		for(State s:this.states){
			if(s.getID()==id){
				returnstate=s;
				break;
			}
		}
		return returnstate;
	}
	
	public State getStatebydistinguishedName(String name){
		State returnstate=null;
		for(State s:this.states){
			if(s.getdistinguishedName().equals(name)){
				returnstate=s;
				break;
			}
		}
		return returnstate;
	}
	public State getStateBypath(String path){
		State returnstate=null;
		State fromstate=this.initState;
		State tostate=null;
		if(path.length()==0) {
			return this.getinitState();
		}
		while(path.length()>0){
			String onestep=path.substring(0, 1);
			
			tostate=fromstate.nextState(onestep);
			if(tostate==null){
				return null;
			}else{
				fromstate=tostate;
			}
			path=path.substring(1);
		}
		returnstate=tostate;
		return returnstate;
	}
	//get a state from s by running a path. 
	public State getNextState(State s,String path){
		State returnstate=null;
		State fromstate=s;
		State tostate=null;
		if(path.length()==0) {
			return s;
		}
		while(path.length()>0){
			String onestep=path.substring(0, 1);
			
			tostate=fromstate.nextState(onestep);
			if(tostate==null){
				return null;
			}else{
				fromstate=tostate;
			}
			path=path.substring(1);
		}
		returnstate=tostate;
		return returnstate;
	}
	

	public boolean isPattern(){
		if(this.pattern==null){
			return false;
		}else{
			return true;
		}
	}

	public List<String> getnamesOfaLevele(int i){
		List<String> names=new ArrayList<String>();
		for(State s:this.states){
			if(s.getLevel()==i){
				names.add(s.getname());
			}
		}
		return  Collections.unmodifiableList(names);
	}

	public void printAutomaton(){
		for(int i=0;i<=this.totalLevel;i++){
			System.out.println(i);
			for(State s: this.states){
				if(s.getLevel()==i){
					System.out.print(s.getLevel()+" "+s.getname());
					for(Transition t:s.getTransitions()){
						System.out.print("\t"+t.getSymbol()+" "+t.getNext().getname());
					}
				}
				System.out.println();
			}
		}
	}
	/**
	 * @param name (teacher or learner) determinate the file for the Teacher or the Learner
	 */
	public void outputToTXTforprolog(String name){
		File file=new File(name+"Automaton.txt");
		String flag=name.substring(0,1);
		if (file.exists()) {  
	       file.delete();
	    } 
		
		//create new file for learner automaton
	    try {  
	       file.createNewFile(); 
	    } catch (IOException e) {  
	         // TODO Auto-generated catch block  
	       e.printStackTrace();  
	    }  
	    
	
		List<String> finallist=new ArrayList<String>();
		for(State s:this.finalState){
			finallist.add(s.getname());
		}
		String finalstates=String.join(",",	finallist);
		String txt_header=flag+"finalStates([" +finalstates+"]).\r\n";
		
		//write data into "automaton" file 
		try {  
	            FileWriter fileWriter = new FileWriter(file);   
	            fileWriter.write(txt_header);  
	            for(Transition t: this.transitions){
	            	String line=flag+"transition("+t.toString()+").\r\n";
	                fileWriter.write(line);
	            }
	            fileWriter.write("\r\n");
	            fileWriter.close(); // close filewriter 
	              
	  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	}

	/**
	 * 
	 * output string data for dot visualisation 
	 */
	public String toStringforDot(){
		String layerset="0";
		
		//initialise list for storing level information
		List<String> dividedstatesset=new ArrayList<String>();
		this.setTotalLevel();
		for(int i=0;i<this.totalLevel+1;i++){
			dividedstatesset.add("{rank = same;");
			if(i>0)
				layerset=layerset+":"+i;
		}
		boolean hasnormalstates=false;
		for(State s: this.states){
			//if(!s.getFinal()){
				 String temp=dividedstatesset.get(s.getLevel());
		    	 temp=temp+s.getID()+";";
		    	 dividedstatesset.set(s.getLevel(), temp);
		    	 hasnormalstates=true;
			//}
	    	
		}
		//rank all states
		String lineforrankstate="";
		for(String s:dividedstatesset){
			lineforrankstate=lineforrankstate+s+"}\r\n";
		}
		
		String lineforfinalstates="";
		String linefornormalstateshead="node [shape = circle,layer=";
		String linefornormalstates="";
	    String transitions="";
		
	    //figure out final states 
		List<String> finallist=new ArrayList<String>();
		for(State s:this.finalState){
			finallist.add(s.getname());
			//lineforfinalstates=lineforfinalstates+s.getname()+";";
			lineforfinalstates=lineforfinalstates+s.getID()+"[shape = doublecircle,layer=\"0\"];\r\n";
		}
		//nodes for finalstates are made
		//lineforfinalstates=lineforfinalstates+"}";
		
		String temp="";
		 //nodes for dividing level finish
//		if(hasnormalstates){
//			for(int i=0;i<dividedstatesset.size();i++){
//				temp=dividedstatesset.get(i);
//				//temp=temp+"}";
//				//dividedstatesset.set(i, temp);
//				//linefornormalstates=linefornormalstates+"\""+i+"\"];"+temp; 
//				temp=linefornormalstateshead+"\""+i+"\"];"+dividedstatesset.get(i)+"}"; 
//				linefornormalstates=temp;
//	     	}
//		}
		
		
		
		for(State s :this.getAllstates()){
			if(!s.getFinal()){
				linefornormalstates=linefornormalstates+s.getID()+"[shape = circle,layer=\""+s.getLevel()+"\"];\r\n";
			}
			for(Transition t:s.getTransitions()){
				String line=t.getPrevious().getID()+"\t->\t"+t.getNext().getID()+"\t[label=\""+t.getSymbol()+"\"];\r\n";
				transitions=transitions+line;
			}
		}
		
	     String sum=lineforfinalstates+linefornormalstates+lineforrankstate+"\r\n"+transitions;
	     return sum;
	
	}
	
	public String simplityToStringforDot(){
		String layerset="0";
		
		//initialise list for storing level information
		List<String> dividedstatesset=new ArrayList<String>();
		this.setTotalLevel();
		for(int i=0;i<this.totalLevel+1;i++){
			dividedstatesset.add("{rank = same;");
			if(i>0)
				layerset=layerset+":"+i;
		}
		boolean hasnormalstates=false;
		for(State s: this.states){
			if(s.getType()!=Type.SINK){
				 String temp=dividedstatesset.get(s.getLevel());
		    	 temp=temp+s.getID()+";";
		    	 dividedstatesset.set(s.getLevel(), temp);
		    	 hasnormalstates=true;
			}
	    	
		}
		//rank all states
		String lineforrankstate="";
		for(String s:dividedstatesset){
			lineforrankstate=lineforrankstate+s+"}\r\n";
		}
		
		String lineforfinalstates="";
		String linefornormalstateshead="node [shape = circle,layer=";
		String linefornormalstates="";
	    String transitions="";
		
	    //figure out final states 
		List<String> finallist=new ArrayList<String>();
		for(State s:this.finalState){
			finallist.add(s.getname());
			//lineforfinalstates=lineforfinalstates+s.getname()+";";
			lineforfinalstates=lineforfinalstates+s.getID()+"[shape = doublecircle,layer=\"0\"];\r\n";
		}
		//nodes for finalstates are made
		//lineforfinalstates=lineforfinalstates+"}";
		
		String temp="";
	
		
		for(State s :this.getAllstates()){
			if(!s.getFinal()){
				linefornormalstates=linefornormalstates+s.getID()+"[shape = circle,layer=\""+s.getLevel()+"\"];\r\n";
			}
			for(Transition t:s.getTransitions()){
				if(t.getNext().getType()!=Type.SINK){
					String line=t.getPrevious().getID()+"\t->\t"+t.getNext().getID()+"\t[label=\""+t.getSymbol()+"\"];\r\n";
					transitions=transitions+line;
				}
			}
		}
		
	     String sum=lineforfinalstates+linefornormalstates+lineforrankstate+"\r\n"+transitions;
	     return sum;
	
	}

	/*
	 * output automata as pdf 
	 */
	public void visualisation(){
		Graphviz.createDotGraph(this.toStringforDot(), this.getClass().getName());
	}
	
	public String getAllFinalstatesNames() {
		// TODO Auto-generated method stub
		String names="";
		for(State s:this.getAllFinalstates()) {
			names=names+" "+s.getname();
		}
		
		return names;
	}
	
//	public void accept_Wordlist_ofLength(State s, int length, List<String> wordlist ){
//		String path=Space.;
//	
//			for(String a:this.alphabet){
//				if(this.getNextState(s,	a)!=null){
//					path=path+a;
//					acceptedPaths(this.getNextState(s,a),path,wordlist);
//				}
//			}
//			
//		
//	}
	//find all possible paths from state s to final state in length transitions.
	public void acceptedPaths(State s, String prefix,Set<String> wordlist, int length){
		
		String localprefix=prefix;
		if(length==0 ){
			if(s.getFinal() && Word.isLegal(localprefix)){
				
				wordlist.add(localprefix);
			}
		}else{
			//System.out.println("state's transitions size:"+s.getTransitions().size());
			for(Transition t: s.getTransitions()){
				//version before 201181019: recursion 
				/**2019061
				 * try: never go to State.No
				 */
				if(t.getNext().getType()!=State.Type.SINK){
					prefix=prefix+t.getSymbol();
					acceptedPaths(t.getNext(),prefix,wordlist,length-1);
					prefix=localprefix;
				}
			//version created 20181019
				
//					localprefix=localprefix+t.getSymbol();
//					acceptedPaths(t.getNext(),localprefix,wordlist,length-1);
				
			}
		}
		
		
	}
	private int gettotalID(){
		return this.totalID;
	}
	
	
	//return this automaton is dfa
	public boolean iswholeTransitions(){
		for(State st:this.states){
			for(String s: this.alphabet	){
				if(st.nextState(s)==null){
					this.isWhole=false;
					return this.isWhole;
				
				}
			}
		}
		return this.isWhole;
	}
	
	//expand transitions
	public void expandtransitons(){
		if(!this.iswholeTransitions()){
		for(int i=0;i<=this.totalLevel;i++){
			State sinkstate=new State("ss"+i);
			sinkstate.setLevel(i);
			sinkstate.setselfall(this.alphabet,i);
			sinkstate.settype(Type.SINK);
			this.sinkstates.add(i, sinkstate);
		}
		for(State st:this.states){
			for(String s: this.alphabet	){
				if(st.nextState(s)==null){
					State next=this.sinkstates.get(st.getLevel());
					Transition t=new Transition(st,next, s);
					st.setTransition(t);
				
				}
			}
			for(int i=1;i<=st.getLevel();i++	){
						
				if(st.nextState(String.valueOf(i))==null){
					State next=this.sinkstates.get(st.getLevel());
					Transition t=new Transition(st,next, String.valueOf(i));
					st.setTransition(t);
				
				}
			}
			
			if(st.nextState("<")==null){
				if(st.getLevel()<this.totalLevel){
						State next=this.sinkstates.get(st.getLevel()+1);
						Transition t=new Transition(st,next, "<");
						st.setTransition(t);
						
				}
					
			}
			if(st.nextState(">")==null){
				if(st.getLevel()>0){
						State next=this.sinkstates.get(st.getLevel()-1);
						Transition t=new Transition(st,next, ">");
						st.setTransition(t);
						
				}
					
			}
			
			for(Transition t:st.getTransitions()){
				this.addTransition(t);
			}
		}
		
		for(int i=0; i<this.sinkstates.size();i++){
			State st=this.sinkstates.get(i);
			if(st!=null){
			
				if(st.nextState("<")==null){
					if(st.getLevel()<this.totalLevel){
						State next=this.sinkstates.get(st.getLevel()+1);
						Transition t=new Transition(st,next, "<");
						st.setTransition(t);
						
					}
					
				}
				if(st.nextState(">")==null){
					if(st.getLevel()>0){
						State next=this.sinkstates.get(st.getLevel()-1);
						Transition t=new Transition(st,next, ">");
						st.setTransition(t);
						
					}
					
				}
			
				for(Transition t:st.getTransitions()){
					this.addTransition(t);
				}
				this.addState(st);
			}
		}
		}
		
	}
	private State addsinkstate(int i){
		
		if(this.sinkstates.size()>=(i+1)){
			return this.sinkstates.get(i);
		}else{
			State sinkstate=new State("ss"+this.sinkstates.size());
			sinkstate.setLevel(i);
			sinkstate.setselfall(this.alphabet,i);
			sinkstate.settype(Type.SINK);
			this.sinkstates.add(i, sinkstate);
			
			return sinkstate;
		}
	}
	
}