package reTodfa;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DFA {
	private LinkedList<State_Re> dfa;
	private int maxlayer;
	private Set<String> alphabet;
	private Set<String> names;
	private Set<String> scopes;
	private Set<State_Re> finalstate;
	private State_Re initstate;
	public DFA () {
		this.setDfa(new LinkedList<State_Re> ());
		this.getDfa().clear();
		alphabet=new HashSet();
		names=new HashSet();
		scopes=new HashSet();
		maxlayer=0;
		finalstate=new HashSet();
	}

	public LinkedList<State_Re> getDfa() {
		return dfa;
	}

	public void setDfa(LinkedList<State_Re> nfa) {
		this.dfa = nfa;
	}
	//set maxlayer
	public void setmaxlayer(int i){
		this.maxlayer=i;
	}
	//get maxlayer
	public int getmaxlayer(){
		return this.maxlayer;
	}
	//set alphabet
			public void setalphabet(Set<String> i){
				this.alphabet=i;
			}
			//get alphabet
			public Set<String> getalphabet(){
				return this.alphabet;
			}
			//set names
			public void setnames(Set<String> i){
				this.names=i;
			}
			//get names
			public Set<String> getnames(){
				return this.names;
			}
			//set scopes
			public void setscopes(Set<String> i){
				this.scopes=i;
			}
			//get scopes
			public Set<String> getscopes(){
				return this.scopes;
			}
			
	//output dfa to a file 
	public void outputtoTXT(String name){
		File file=new File(name+".txt");
		//String flag=name.substring(0,1);
		if (file.exists()) {  
	       file.delete();
	    } 
		
		//create new file for automaton
	    try {  
	       file.createNewFile(); 
	    } catch (IOException e) {  
	         // TODO Auto-generated catch block  
	       e.printStackTrace();  
	    }  
	    
	    //write initial state and final states into file
		List<String> finallist=new ArrayList<String>();
		for(State_Re s:this.finalstate){
			finallist.add(String.valueOf(s.getStateID()));
		}
		String finalstates=String.join(" ",	finallist);
		String txt_final="final " +finalstates+"\r\n";
		String txt_initial="initial "+this.getinitstate().getStateID()+"\r\n";
		String txt_header=txt_initial+txt_final;
		//write transitions data into file 
		try {  
	            FileWriter fileWriter = new FileWriter(file);   
	            fileWriter.write(txt_header);  
	            //output transitions as the sequence storing in set
//	            for(State_Re s: this.dfa){
//	            	
//	            	for(Map.Entry e: s.getNextState().entrySet()){
//	            		Character key=(Character) e.getKey();
//	            		List<State_Re> nexts=(ArrayList<State_Re>) e.getValue();
//	            		int next=nexts.get(0).getStateID();
//	            		String line=s.getStateID()+" "+key+" "+next+"\r\n";
//	            		fileWriter.write(line);
//	            	}
//	            }
	            //output transitions as the layer location
	            //System.out.println("this dfa has "+this.dfa.size()+" states");
	            for(int i=0;i<=this.maxlayer;i++){
	                for(State_Re s: this.dfa){
	                	if(s.getlevel()==i){
	                		for(Map.Entry e: s.getNextState().entrySet()){
	                			String key= (String) e.getKey();
		            			List<State_Re> nexts=(ArrayList<State_Re>) e.getValue();
		            			int next=nexts.get(0).getStateID();
		            			String line=s.getStateID()+" "+key+" "+next+"\r\n";
		            			fileWriter.write(line);
		                	}
	                	}
	            	}
	            }
	            fileWriter.write("\r\n");
	            fileWriter.close(); // close filewriter 
	              
	  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	}

	public void setfinalstates() {
		// TODO Auto-generated method stub
		this.finalstate.clear();
		for(State_Re s:this.dfa){
			if(s.isAcceptState())
				this.finalstate.add(s);
		}
	}
	public Set<State_Re> getfinalstates() {
		// TODO Auto-generated method stub
		return this.finalstate;
	}
	//set initstate;
	public void setinitstate() {
		// TODO Auto-generated method stub
		for(State_Re s:this.dfa){
			if(s.getinitstate()){
				this.initstate= s;
				break;
			}
		}
				
	}
	
	//set finalstate;
	public State_Re getinitstate( ) {
		// TODO Auto-generated method stub
		return this.initstate;
	}
	
	//minimise dfa
	public void minimisedfa(){
		LinkedList<State_Re> minimiseddfa=new LinkedList<State_Re> ();
		// Create an arrayList of unprocessed States
		LinkedList <State_Re> unprocessed = new LinkedList<State_Re> ();
		unprocessed.addAll(this.dfa);
		// Create an arrayList of processed States
		LinkedList <State_Re> processed = new LinkedList<State_Re> ();
		// Create an arrayList of unique States
		Set <State_Re> uniquestates = new HashSet<State_Re> ();
		// Create a map of equivalent states
		Set<HashSet<State_Re>> equivalentStates=new HashSet< HashSet<State_Re>> ();
		
		while(!unprocessed.isEmpty()){
			
			for(State_Re s:unprocessed){
				processed.add(s);
				LinkedList <State_Re> comparing = new LinkedList<State_Re> ();
				comparing.addAll(unprocessed);
				comparing.remove(s);
				boolean unique=true;
	
				HashSet<State_Re> equals=new HashSet<State_Re>();
				equals.add(s);
				for(State_Re u:comparing){
					if(s.equalsTo(u)){
						unique=false;
						processed.add(u);
						//boolean found=false;
						
						equals.add(u);
						equals.add(s);
						equivalentStates.add(equals);
						
					}
				}
			
				if(unique){
					uniquestates.add(s);
				}
			}
			unprocessed.removeAll(processed);
		}
		//System.out.println("unique states "+uniquestates.size());
		
		//create new states for equivalentStates
		Map<State_Re, State_Re> newstatesmap=new HashMap<State_Re, State_Re>();
		Set<State_Re>  newstates=new HashSet<State_Re> ();
		for(HashSet<State_Re> l: equivalentStates){
			
			if(!l.isEmpty()){
				
				State_Re dfaStart = l.iterator().next();
				newstates.add(dfaStart);
				for(State_Re s:l){
					newstatesmap.put(s, dfaStart);
				}
				
			}
			
		}
		//System.out.println("new unique states: "+newstates.size());
		
		//construct minimised dfa
		
		
		
		for(State_Re s:this.dfa){
			
			for (Map.Entry<String, ArrayList<State_Re>> entry : s.getNextState().entrySet()) {
				String key= (String) entry.getKey();
				ArrayList <State_Re> list = entry.getValue();
				if(!uniquestates.containsAll(list)){
					
					s.replaceTransition(newstatesmap.get(list.get(0)), key);
//					s.removetranistion(key);
//					s.addTransition(newstatesmap.get(list.get(0)), key);
				}
			}
		}
		minimiseddfa.clear();
		minimiseddfa.addAll(uniquestates);
		minimiseddfa.addAll(newstates);
		this.setDfa(minimiseddfa);
		this.setfinalstates();
	}
	
	//output dfa to console
			public void outputToconsole(){
				//System.out.println(this.dfa.size());
				for(State_Re s:this.dfa){
					System.out.println(s.toString()+": "+s.getNextState().toString());
				}
			}
			
		//output string data for "dot" visualise it
			public String toStringforDOT(){
			//	String lineforinittransition="node [ shape = point , fontsize = 12];__0__";
				String lineforfinalstates="node [shape = doublecircle];{rank = same;";
				String linefornormalstates="node [shape = circle];";
			    String transitions="";
				
			    //figure out final states 
				List<Integer> finallist=new ArrayList<Integer>();
				for(State_Re s:this.finalstate){
					finallist.add(s.getStateID());
					lineforfinalstates=lineforfinalstates+s.getStateID()+";";
				}
				//nodes for finalstates are made
				lineforfinalstates=lineforfinalstates+"}";
				
				//initialise list for storing level information
				List<String> dividedstatesset=new ArrayList<String>();
				for(int i=0;i<this.maxlayer+1;i++){
					dividedstatesset.add("{rank = same;");
				}
				 
				
			     for(State_Re s: this.dfa){
			    	// if(s.getlevel()>)
			    	 
			    	 
			    	 String temp=dividedstatesset.get(s.getlevel());
			    	 temp=temp+s.getStateID()+";";
			    	 dividedstatesset.set(s.getlevel(), temp);
			           for(Map.Entry e: s.getNextState().entrySet()){
			            	Character key=(Character) e.getKey();
			            	List<State_Re> nexts=(ArrayList<State_Re>) e.getValue();
			            	int next=nexts.get(0).getStateID();
			            	String line=s.getStateID()+"\t->\t"+next+"\t[label=\""+key+"\"];";
			            	transitions=transitions+line;
			            		
			            }
			     }
			     //nodes for dividing level finish
			     for(int i=0;i<dividedstatesset.size();i++){
			    	 String temp=dividedstatesset.get(i);
			    	 temp=temp+"}";
			    	 dividedstatesset.set(i, temp);
			    	 linefornormalstates=linefornormalstates+dividedstatesset.get(i); 
				}
			     //try to change levels location, but no differences
//			     int max=dividedstatesset.size()-1;
//			     for(int i=max;i>=0;i--){
//			    	 String temp=dividedstatesset.get(i);
//			    	 temp=temp+"}";
//			    	 dividedstatesset.set(i, temp);
//			    	 linefornormalstates=linefornormalstates+dividedstatesset.get(i); 
//				}
			 
			     String sum=lineforfinalstates+"\r\n"+linefornormalstates+"\r\n"+transitions;
			     return sum;
			}
			//20180907comments: create for testing Dfa creation and conversion
			public String allstateinformation(){
				String info="";
				for(State_Re s:this.getDfa()){
					for(String key:s.getNextState().keySet()){
	            		List<State_Re> nexts=s.getNextState().get(key);
	            		for(State_Re st:nexts){
	            			int next=st.getStateID();
		            		String line="level "+s.getlevel()+" state "+s.getStateID()+" "+key+" "+next+"\r\n";
		            		info=info+line;
	            		}
	            	}
				}
				return info;
			}
}
// This line make it work