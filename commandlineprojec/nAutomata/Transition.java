package nAutomata;

public class Transition {
private State stateFrom;
private State stateTo;
private String tSymbol;

public Transition(State from, State to, String t){
	this.stateFrom=from;
	this.stateTo=to;
	this.tSymbol=t;
}
public State getNext(){
	return this.stateTo;
}

public String getSymbol(){
	return this.tSymbol;
}

public State getPrevious() {
	return this.stateFrom;
}
public String toString(){
	String t=this.stateFrom.getname()+","+this.tSymbol+","+this.stateTo.getname();
	return t;
}
public boolean equalTo(Transition t){
	boolean flag=false;
	if(t.getSymbol().equals(this.getSymbol()))
		if(t.getPrevious().equals(this.getPrevious()))
			if(t.getNext().equals(this.getNext()))
				flag=true;
	
	return flag;
	
}

}
