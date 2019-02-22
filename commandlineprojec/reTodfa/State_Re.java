package reTodfa;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

// State class with Map of transition for each input
// also is the basic one, both NFA and DFA class
// uses this class
public class State_Re {
	private int stateID;
	private Map<String, ArrayList<State_Re>> nextState;
	private Set <State_Re> states;
	private boolean acceptState;
	private boolean initstate;
	private int level=0;
	
	// This constructor is used for NFA
	public State_Re (int ID) {
		this.setStateID(ID);
		this.setNextState(new HashMap <String, ArrayList<State_Re>> ());
		this.setAcceptState(false);
		this.setinitstate(false);
	}
	//set initial boolean
	public void setinitstate(boolean b) {
		// TODO Auto-generated method stub
		this.initstate=b;
	}
	//get initial boolean
		public boolean getinitstate() {
			// TODO Auto-generated method stub
			return this.initstate;
		}
	// This constructor is used for DFA
	public State_Re(Set<State_Re> states, int ID) {
		this.setStates(states);
		this.setStateID(ID);
		this.setNextState(new HashMap <String, ArrayList<State_Re>> ());
		
		// find if there is final state in this set of states
		for (State_Re p : states) {
			if (p.isAcceptState()) {
				this.setAcceptState(true);
				break;
			}
		}
		// find if there is final state in this set of states
				for (State_Re p : states) {
					if (p.getinitstate()) {
						this.setinitstate(true);
						break;
					}
				}
		// set level of this state
				this.setlevel(states.iterator().next().getlevel());
	}
	
	//set the layer of state
	public void setlevel(int level){
		if(level<0)
			throw new IllegalArgumentException("this state "+this.getStateID()+"cann not be in level "+level);
		this.level=level;
	}
	
	//get the layer information of state
	public int getlevel(){
		return this.level;
	}
	
	// Add transition between states and insert into the arrayList
	// or create the arrayList based on key
	public void addTransition (State_Re next, String key) {
		ArrayList <State_Re> list = this.nextState.get(key);
		
		if (list == null) {
			list = new ArrayList<State_Re> ();
			switch(key){
			case "<": next.setlevel(this.getlevel()+1);break;
			case ">": next.setlevel(this.getlevel()-1);break;
			default:  next.setlevel(this.getlevel());
			}
			this.nextState.put(key, list);
		}
		
		list.add(next);
	}
	
	// Add transition between states and insert into the arrayList
		// or create the arrayList based on key
		public void replaceTransition (State_Re next, String key) {
			ArrayList <State_Re> list = this.nextState.get(key);
			ArrayList<State_Re> newlist =new ArrayList<State_Re> ();
			newlist.add(next);
		
			if (list == null) {
							
				this.nextState.put(key, newlist);
			}else
				this.nextState.replace(key, newlist);
			
		}
	// Add transition between states and insert into the arrayList
		// or create the arrayList based on key
//		public void addTransitionsforall (Set <Character> input) {
//			for(Character key: input){
//				ArrayList <State_Re> list = this.nextState.get(key);
//				
//				if (list == null) {
//					list = new ArrayList<State_Re> ();
//					switch(key){
//					case '<': next.setlevel(this.getlevel()+1);break;
//					case '>': next.setlevel(this.getlevel()-1);break;
//					default:  next.setlevel(this.getlevel());
//					}
//					this.nextState.put(key, list);
//				}
//				
//				list.add(next);
//			}
//			
//			
//		}
	

	// Get all transition states based on symbol
	public ArrayList<State_Re> getAllTransitions(String c) {	
		//version 20181029: does not work for getting states with names
		if (this.getNextState().get(c) == null)	{	return new ArrayList<State_Re> ();	}
		else 								{	return this.getNextState().get(c);	}
		
	}
	
	// Autogenerated Getters and Setters
	public Map<String, ArrayList<State_Re>> getNextState() {
		return this.nextState;
	}

	public void setNextState(HashMap<String, ArrayList<State_Re>> hashMap) {
		this.nextState = hashMap;
	}
	
	public int getStateID() {
		return stateID;
	}

	public void setStateID(int stateID) {
		this.stateID = stateID;
	}

	public boolean isAcceptState() {
		return acceptState;
	}

	public void setAcceptState(boolean acceptState) {
		this.acceptState = acceptState;
	}

	public Set <State_Re> getStates() {
		return states;
	}

	public void setStates(Set <State_Re> states) {
		this.states = states;
	}

	@SuppressWarnings("null")
	public void setNextstatelevel() {
		// TODO Auto-generated method stub
		String key; 
		
		for (Map.Entry<String, ArrayList<State_Re>> entry : this.getNextState().entrySet()) {
			key= (String) entry.getKey();
			ArrayList <State_Re> list = entry.getValue();

			if (list != null) {
				switch(key){
				case "<": for(State_Re next:list)
					next.setlevel(this.getlevel()+1);
					break;
				case ">": for(State_Re next:list)
					next.setlevel(this.getlevel()-1);
					break;
				default:  for(State_Re next:list)
					next.setlevel(this.getlevel());
				}
			}
		}  
	}
	public void setselfall(Set<String> alphabet, int i) {
		// TODO Auto-generated method stub
		for(String s:alphabet){
			this.addTransition(this, s);
		}
		for(int j=1;j<=i;j++){
			this.addTransition(this, String.valueOf(j));
		}
	}
	public void removetranistion(String key) {
		// TODO Auto-generated method stub
		this.nextState.remove(key);
		
	}
	public boolean equalsTo(State_Re s){
		boolean flag=false;
		if(this.isAcceptState()==s.isAcceptState()){
			if(this.getinitstate()==s.getinitstate()){
				if(this.getlevel()==s.getlevel()){
					if(this.getNextState().equals(s.getNextState())){
						flag=true;
					}
				}
			}
		}
		return flag;
	}
}

// this line make it work
