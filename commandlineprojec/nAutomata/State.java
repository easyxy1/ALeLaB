package nAutomata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class State {
	
private String name;
private String distinguishedName;
private boolean isInitial=false;
private boolean isFinal=false;
private boolean isAlocateTo=false;
private boolean isAlocated=false;
private boolean isDealocateTo=false;
private boolean isDealocated=false;
private int level;//which level this state belongs to 
public enum Type{FINAL,SINK,PREFIX,INVALID}
public static final String FINAL="I";
public static final String NO="N";
public static final String PREFIX="P";
public static final String INVALID="X";
private Type type;
private int id;

private Set<Transition> transitionSet;

public State(String name){
	
	this.name=name;
	this.transitionSet=new HashSet<Transition>();
	this.type=Type.PREFIX;
	
}
public void setID(int id){
	this.id=id;
}

public void setname(String name){
	
	this.name=name;
}
public void setdistinguishedName(String name){
	
	this.distinguishedName=name;
}
public String getname(){
	return this.name;
}
public String getdistinguishedName(){
	
	return this.distinguishedName;
}
public void setLevel(int i){
	this.level=i;
}
public int getLevel(){
	return this.level;
}
public int getID(){
	return this.id;
}

public void settype(Type type){
	this.type=type;
}
public void setInitial(){
	this.isInitial=true;
	this.setLevel(0);;
	this.setID(0);
}
public boolean getInitial(){
	return this.isInitial;
}

public void setFinal(){
	if(this.level==0){
		this.isFinal=true;
		this.type=Type.FINAL;
	}else{
		throw new IllegalArgumentException("this state"+this.id+" is level "+this.level+". cannot be a final state.");
	}
}

public void unsetFinal(){
	if(this.getLevel()==0){
		this.isFinal=!this.isFinal;
		//this.type=Type.NORMAL;//question: this method is for complementation. is it normal or sink
	}
}

public boolean getFinal(){
	return this.isFinal;
}

public void setAlocateTo(State sTo){
	this.isAlocateTo=true;
	sTo.setBeAlocated();
	Transition t=new Transition(this, sTo, "<");
	this.transitionSet.add(t);
	int sTolevel=this.getLevel()+1;
	sTo.setLevel(sTolevel);
	
}


public void setBeAlocated(){
	this.isAlocated=true;
}
public boolean getIsAlocated(){
	return this.isAlocated;
}

public void setDealocateTo(State sTo){
	this.isDealocateTo=true;
	sTo.setBeDealocated();
	Transition t=new Transition(this, sTo, ">");
	this.transitionSet.add(t);
	int sTolevel=0;
	if((this.getLevel()-1)>-1)
		sTolevel=this.getLevel()-1;
	
	sTo.setLevel(sTolevel);
}

public void setBeDealocated(){
	this.isDealocated=true;
}
public boolean getIsDealocated(){
	return this.isDealocated;
}



public boolean getIsAlocateTo(){
	return this.isAlocateTo;
}



public boolean getIsDealocateTo(){
	return this.isDealocateTo;
}


//add a transition from this to a state with a symbol
public void setTransition(Transition t){
	if(t!=null){
		if(this.nextState(t.getSymbol())==null){
			switch(t.getSymbol()){
			case "<":{
				this.setAlocateTo(t.getNext());
				break;
			}
			case ">":{
				this.setDealocateTo(t.getNext());
				break;
			}
			default:{
				this.transitionSet.add(t);
				t.getNext().setLevel(this.level);
			}
			}
		}else{
			if(!this.nextState(t.getSymbol()).equals(t.getNext())){
				this.transitionSet.remove(this.getTransition(t.getSymbol()));
				this.transitionSet.add(t);
			}
		}
	}
}

public Transition getTransition(String symbol) {
	// TODO Auto-generated method stub
	for(Transition t:this.getTransitions()){
		if(t.getSymbol().equals(symbol))
			return t;
		
	}
	return null;
}
public Set<Transition> getTransitions(){
	return this.transitionSet;
}
//get the next state with transferring a symbol
public State nextState(String s){
	for(Transition t: transitionSet){
		if(t.getSymbol().equals(s)){
			return t.getNext();
		}
	}
	return null;
}


public Map<String,Boolean> getstatus(){
	Map<String,Boolean> status=new HashMap<String,Boolean>();
	status.put("isInitial", this.isInitial);
	status.put("isFinal", this.isFinal);
	status.put("isAlocated", this.isAlocated);
	status.put("isAlocateTo", this.isAlocateTo);
	status.put("isDealocated", this.isDealocated);
	status.put("isDealocateTo", this.isDealocateTo);
	return  Collections.unmodifiableMap(status);
}

//compare with another state, if they are equivalence, return true, otherwise return false 
public boolean equalstatus(State s) {
	Boolean result=false;
	if(s==null){
		//throw new IllegalArgumentException("Unable to find a state for '" + s + "'");
		return false;
	}
	if(this.getstatus().equals(s.getstatus())){
		
		result=true;
	}else
		result=false;
	return result;
	
}

public void setStatus(Map<String,Boolean> status){
	this.isInitial=status.get("isInitial");
	this.isFinal=status.get("isFinal");
	this.isAlocated=status.get("isAlocated");
	this.isAlocateTo=status.get("isAlocateTo");
	this.isDealocated=status.get("isDealocated");
	this.isDealocateTo=status.get("isDealocateTo");
}

public void setselfall(Set<String> alphabet, int i) {
	// TODO Auto-generated method stub

	for(String s:alphabet){
		Transition t=new Transition(this,this, s);
		this.setTransition(t);
	}
	for(int j=1;j<=i;j++){
		Transition t=new Transition(this,this, String.valueOf(j));
		this.setTransition(t);
	}
}

}
