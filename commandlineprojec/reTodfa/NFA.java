package reTodfa;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nAutomata.State;
import nAutomata.Transition;

public class NFA {
	private LinkedList<State_Re> nfa;
	private int maxlayer;
	private Set<String> alphabet;
	private Set<String> names;
	private Set<String> scopes;
	private State_Re finalstate;
	private State_Re initstate;

	//private List<State_Re> sinkstates=new ArrayList<State_Re>();
	
	public NFA () {
		this.setNfa(new LinkedList<State_Re> ());
		this.getNfa().clear();
		alphabet=new HashSet();
		names=new HashSet();
		scopes=new HashSet();
		maxlayer=0;
	}

	public LinkedList<State_Re> getNfa() {
		return nfa;
	}

	public void setNfa(LinkedList<State_Re> nfa) {
		this.nfa = nfa;
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
		public void addalphabet(String i){
			
			if(!i.matches("\\d+"))
				if(i.equals("<")|i.equals(">")){
					this.scopes.add(i);
				}else
					this.alphabet.add(i);
		}
		//get alphabet
		public Set<String> getalphabet(){
			return this.alphabet;
		}
		//set names
		public void setnames(Set<String> names2){
			for(String c:names2)
				this.names.add(String.valueOf(c));
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
		
		//set layer informaton for all states after NFa completed
		public void setAllnextlevel(State_Re s) throws IOException{
//Recursive method
//			s.setNextstatelevel();
//			Character key; 
//			for (Map.Entry entry : s.getNextState().entrySet()) {
//				key= (Character) entry.getKey();
//				ArrayList <State_Re> list = s.getNextState().get(key);
//			
//				if (list != null) {
//					for(State_Re next:list)
//						this.setAllnextlevel(next);
//				}
//		    }
						
		}
		public void setAllnextlevel() {
			//not-recursive method
			for(State_Re s:this.getNfa()){
				s.setNextstatelevel();
			}
									
		}
		//set finalstate;
		public void setfinalstate(State_Re finalstate) {
			// TODO Auto-generated method stub
			this.finalstate= finalstate;
		}
		//get finalstate;
		public State_Re getfinalstate() {
			// TODO Auto-generated method stub
			return this.finalstate;
		}
		//set initstate;
		public void setinitstate(State_Re initstate) {
			// TODO Auto-generated method stub
			this.initstate= initstate;
			this.initstate.setlevel(0);
		}
		//set finalstate;
		public State_Re getinitstate( ) {
			// TODO Auto-generated method stub
			return this.initstate;
		}

		//output nfa to a file 
		public void outputtoTXT(String name){
			File file=new File("testNFA"+name+".txt");
			
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
		
			String txt_header="final " +this.finalstate.getStateID()+"\r\n";
			String initStateInfo="init "+this.initstate.getlevel()+"\r\n";
			//write transitions data into file 
			try {  
		            FileWriter fileWriter = new FileWriter(file);   
		            fileWriter.write(txt_header);  
		            fileWriter.write(initStateInfo);
		            for(State_Re s: this.nfa){
		            	for(String key:s.getNextState().keySet()){
		            		List<State_Re> nexts=s.getNextState().get(key);
		            		for(State_Re st:nexts){
		            			int next=st.getStateID();
			            		String line=s.getStateID()+" "+key+" "+next+"\r\n";
			            		fileWriter.write(line);
		            		}
		            	}
//		            	for(Map.Entry e: s.getNextState().entrySet()){
//		            		Character key=(Character) e.getKey();
//		            		List<State_Re> nexts=(ArrayList<State_Re>) e.getValue();
//		            		for(State_Re st:nexts){
//		            			int next=st.getStateID();
//			            		String line=s.getStateID()+" "+key+" "+next+"\r\n";
//			            		fileWriter.write(line);
//		            		}
//		            		
//		            	}
		            }
		            fileWriter.write("\r\n");
		            fileWriter.close(); // close filewriter 
		              
		  
		        } catch (IOException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }  
		}
		
		//expand transitions
		public void expandtransitons(){
			int maxstate=this.getNfa().size();
			List<State_Re> deadstates=new ArrayList<State_Re>();
			for(int i=0; i<=this.maxlayer;i++){
				while(this.getState(maxstate)!=null){
					maxstate++;
				}
				State_Re deadstate=new State_Re(maxstate);
				deadstate.setlevel(i);
				deadstate.setselfall(this.alphabet,i);
				this.getNfa().add(deadstate);
				deadstates.add(deadstate);
			}
			
			for(State_Re st:this.getNfa()){
				//20180907comments: if the state with epslon transition, it does not be expanded
				if(!st.getAllTransitions(RegularExpression.epslon).isEmpty()){
					//System.out.println("state "+st.getStateID()+"transistions"+st.getAllTransitions(RegularExpression.epslon));
					continue;
				}
				for(String s: this.alphabet	){
					if(st.getAllTransitions(s).isEmpty()){
						State_Re next=deadstates.get(st.getlevel());
						st.addTransition(next, s);
					}
				}
				for(int i=1;i<=st.getlevel();i++	){
					
					if(st.getAllTransitions(String.valueOf(i)).isEmpty()){
						State_Re next=deadstates.get(st.getlevel());
						st.addTransition(next, String.valueOf(i));
					}
				}
				for(String s: this.scopes	){
					if(st.getAllTransitions(s).isEmpty()){
						switch(s){
						case "<":
							if(st.getlevel()<this.maxlayer){
								State_Re next=deadstates.get(st.getlevel()+1);
								st.addTransition(next, s);
							}
							break;
						case ">":
							if(st.getlevel()>0){
								State_Re next=deadstates.get(st.getlevel()-1);
								st.addTransition(next, s);
							}
							break;
						}
					}
				}
				//System.out.println("state "+st.getStateID()+"transistions"+st.getNextState());
			}
			
			
			
		}
		

		public State_Re getState(int id) {
			// TODO Auto-generated method stub
			State_Re s=null;
			for(State_Re st:this.getNfa()){
				if(st.getStateID()==id){
					s=st;
				}
				
			}
			return s;
		}
		//20180907comments: create for testing nfa creation and conversion
		public String allstateinformation(){
			String info="";
			for(State_Re s:this.getNfa()){
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