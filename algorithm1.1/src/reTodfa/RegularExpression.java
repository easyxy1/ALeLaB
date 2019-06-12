package reTodfa;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

public class RegularExpression {
	private static int stateID = 0;
	public static final String epslon="\u03B5";
	private static int maxlayer=0;
	private static Stack<NFA> stackNfa = new Stack<NFA> ();
	private static Stack<String> operator = new Stack<String> ();	

	private static Set<State_Re> set1 = new HashSet <State_Re> ();
	private static Set<State_Re> set2 = new HashSet <State_Re> ();
	
	// Set of inputs
	private static Set <String> input = new HashSet <String> ();
	private static Set <String> names = new HashSet <String> ();
	//private int maxlayer;
	
	//clear 
	public static void clear(){
		stackNfa.clear();
		operator.clear();
		set1.clear();
		set2.clear();
		input.clear();
		names.clear();
		stateID = 0;
	}
	
	//set finite alphabet
	public static void addalphabet( Set<String> alphabet){
		if(!alphabet.isEmpty()){
			for(String a:alphabet){
				input.add(a);
			}
		}
	}
	
	// Generates NFA using the regular expression
	public static NFA generateNFA(String regular) {
		// Only inputs available
		Set<String> op=new HashSet<String>();
		op.add("(");
		op.add(")");
		op.add(".");
		op.add("+");
		op.add("*");
		
		for(int i=0;i<regular.length();i++){
			String c=String.valueOf(regular.charAt(i));
			if(!op.contains(c)){
				boolean bool=Pattern.matches("[0-9]", String.valueOf(c));
				if(bool)
					names.add(c);
				else if(c.equals("<")  || c.equals(">"))
					
					input.add(c);
				
				else if(!input.contains(c))
						throw new IllegalArgumentException(c+"expression symbol beyond the alphabet");
					
				
			}
		}
		
			
		// Generate regular expression with the concatenation
		regular = FormulaBinders(regular);
		System.out.println(regular);
		regular = AddConcat (regular);
		System.out.println(regular);
		
		//initialise layer information
		int currentlevel=0;
		int maxlayer=0;
		// Cleaning stacks
		stackNfa.clear();
		operator.clear();

		for (int i = 0 ; i < regular.length(); i++) {	
			//System.out.println(regular.charAt(i)+"\t"+operator.toString()+"\t"+stackNfa.toString());
			String singlechar=String.valueOf(regular.charAt(i));
			if (isInputCharacter (singlechar)) {
				
				//create states first, then set up current level
				pushStack(singlechar,currentlevel);
				if(singlechar.equals("<")){
					currentlevel++;
					if(maxlayer<currentlevel)
						maxlayer=currentlevel;
					
				}else	if(singlechar.equals(">"))
					currentlevel--;
					
				
			} else if (operator.isEmpty()) {
				operator.push(singlechar);
				
				} else if (singlechar.equals("(")) {
				operator.push(singlechar);
				
				} else if (singlechar.equals(")")) {
					while (!operator.get(operator.size()-1).equals( "(")) {
						doOperation();
					}				
		
					// Pop the '(' left parenthesis
					operator.pop();
				
				} else {
						while (!operator.isEmpty() && Priority (singlechar, operator.get(operator.size() - 1)) ){
							doOperation ();
						}
						operator.push(singlechar);
				}		
		}		
		
		// Clean the remaining elements in the stack
		while (!operator.isEmpty()) {	doOperation(); }
		
		// Get the complete nfa
		NFA completeNfa = stackNfa.pop();
		completeNfa.setmaxlayer(maxlayer);
		names.clear();
		for(int i=1;i<=maxlayer;i++){
			String newname=String.valueOf(i);
			names.add(newname);
		}
		completeNfa.setnames(names);
		
		for(String c:input){
			
			completeNfa.addalphabet(String.valueOf(c));
		}
		//set initial state
		State_Re initstate=completeNfa.getNfa().get(0);
		initstate.setinitstate(true);
		completeNfa.setinitstate(initstate);
		
		// add the accpeting state to the end of NFA
		State_Re finalstate=completeNfa.getNfa().get(completeNfa.getNfa().size() - 1);
		finalstate.setAcceptState(true);
		completeNfa.setfinalstate(finalstate);
		
		//expand nfa
		//completeNfa.expandtransitons();
		
		// return the nfa
		return completeNfa;
	}

	private static String FormulaBinders(String regular) {
		// TODO Auto-generated method stub
		String newRegular = new String ("");
		CharSequence allocateBinder="<";
		CharSequence deallocateBinder=">";
		if(regular.contains(allocateBinder)){
		newRegular = regular.replaceAll("<", "(<.(");
		}
		if(regular.contains(deallocateBinder)){
		newRegular = newRegular.replaceAll(">", ").>)");
		}
		
		return newRegular;
	}

	// Priority of operands
	private static boolean Priority (String first, String second) {
		if(first.equals(second)) {	return true;	}
		if(first.equals("*")) 	{	return false;	}
		if(second.equals("*"))  	{	return true;	}
		if(first.equals(".") )	{	return false;	}
		if(second.equals(".") )	{	return true;	}
		if(first.equals("+") )	{	return false;	} 
		if(second.equals("<") )	{	return true;	} 
		else 				{	return true;	}
	}

	// Do the desired operation based on the top of stackNfa
	private static void doOperation () {
		if (RegularExpression.operator.size() > 0) {
			String charAt = operator.pop();

			switch (charAt) {
				case ("+"):
					union ();
					break;
	
				case ("."):
					concatenation ();
					break;
	
				case ("*"):
					star ();
					break;
	
				default :
					System.out.println("Unkown Symbol !"+charAt);
					System.exit(1);
					break;			
			}
		}
	}
		
	// Do the star operation
	private static void star() {
		// Retrieve top NFA from Stack
		NFA nfa = stackNfa.pop();
		
		// Create states for star operation
		State_Re start = new State_Re (stateID++);
		State_Re end	= new State_Re (stateID++);
		
		// Add transition to start and end state
//		start.addTransition(end, epslon);
//		start.addTransition(nfa.getNfa().getFirst(), epslon);
//				
//		nfa.getNfa().getLast().addTransition(end, epslon);
//		nfa.getNfa().getLast().addTransition(nfa.getNfa().getFirst(), epslon);
		
		start.addTransition(nfa.getNfa().getFirst(), epslon);
		start.setlevel(nfa.getNfa().getFirst().getlevel());
		start.addTransition(end, epslon);
		nfa.getNfa().getLast().addTransition(nfa.getNfa().getFirst(), epslon);
		nfa.getNfa().getLast().addTransition(end, epslon);
		
		nfa.getNfa().addFirst(start);
		nfa.getNfa().addLast(end);
		
		// Put nfa back in the stackNfa
		stackNfa.push(nfa);
	}

	// Do the concatenation operation
	private static void concatenation() {
		// retrieve nfa 1 and 2 from stackNfa
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();
		
		// Add transition to the end of nfa 1 to the begin of nfa 2
		// the transition uses empty string
		nfa1.getNfa().getLast().addTransition(nfa2.getNfa().getFirst(), epslon);
		
		// Add all states in nfa2 to the end of nfa1
		for (State_Re s : nfa2.getNfa()) {	nfa1.getNfa().addLast(s); }

		// Put nfa back to stackNfa
		stackNfa.push (nfa1);
	}
	
	// Makes union of sub NFA 1 with sub NFA 2
	private static void union() {
		// Load two NFA in stack into variables
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();
		
		// Create states for union operation
		State_Re start = new State_Re (stateID++);
		State_Re end	= new State_Re (stateID++);

		// Set transition to the begin of each subNFA with empty string
		start.addTransition(nfa1.getNfa().getFirst(), epslon);
		
		start.addTransition(nfa2.getNfa().getFirst(), epslon);
		

		// Set transition to the end of each subNfa with empty string
		nfa1.getNfa().getLast().addTransition(end, epslon);
		nfa2.getNfa().getLast().addTransition(end, epslon);

		
		// Add all states in nfa2 to the end of nfa1
		// in order	
		for (State_Re s : nfa2.getNfa()) {
			nfa1.getNfa().addLast(s);
		}
		// Add start to the end of each nfa
		nfa1.getNfa().addFirst(start);
		nfa1.getNfa().addLast(end);
		
		// Put NFA back to stack
		stackNfa.push(nfa1);		
	}
	
	// Push input symbol into stackNfa
	private static void pushStack(String symbol,int currentlevel) {
		State_Re s0 = new State_Re (stateID++);
		State_Re s1 = new State_Re (stateID++);
		//make sure state in correct level
		s0.setlevel(currentlevel);
		
		// add transition from 0 to 1 with the symbol
		s0.addTransition(s1, symbol);
		
		// new temporary NFA
		NFA nfa = new NFA ();
		
		// Add states to NFA
		nfa.getNfa().addLast(s0);
		nfa.getNfa().addLast(s1);		
		
		// Put NFA back to stackNfa
		stackNfa.push(nfa);
	}

	// add "." when is concatenation between to symbols that
	// concatenates to each other
	/**
	private static String AddConcat(String regular) {
		String newRegular = new String ("");

		for (int i = 0; i < regular.length() - 1; i++) {
			 if ( regular.charAt(i) == '<' && !isInputCharacter(String.valueOf(regular.charAt(i+1))) ) {
				newRegular += "("+regular.charAt(i) + "(";
				
				
			}else if ( regular.charAt(i) == '<' && isInputCharacter(String.valueOf(regular.charAt(i+1))) ) {
				newRegular += "("+regular.charAt(i) + ".(";
				
				
			}else if ( regular.charAt(i) == '>' && regular.charAt(i+1) == '>' ) {
				newRegular += regular.charAt(i) + ")).";
				
				
			}else if ( regular.charAt(i) == '>' && isInputCharacter(String.valueOf(regular.charAt(i+1))) && regular.charAt(i+1) != '>') {
				newRegular += regular.charAt(i) + ").";
				
				
			}else if ( regular.charAt(i) == '>' && !isInputCharacter(String.valueOf(regular.charAt(i+1)) )) {
				newRegular += regular.charAt(i) + ")";
				
			}else if (  isInputCharacter(String.valueOf(regular.charAt(i))) && regular.charAt(i+1) == '<') {
				newRegular += regular.charAt(i) + ".";
				
			}else if (  !isInputCharacter(String.valueOf(regular.charAt(i))) && regular.charAt(i+1) == '<') {
				newRegular += ""+regular.charAt(i) + ".";
				
			}else if (  isInputCharacter(String.valueOf(regular.charAt(i))) && regular.charAt(i+1) == '>') {
				newRegular += regular.charAt(i) + ").";
				
				
			}else if (  !isInputCharacter(String.valueOf(regular.charAt(i))) && regular.charAt(i+1) == '>') {
				newRegular += regular.charAt(i) + ").";
			
				
			}else	if ( isInputCharacter(String.valueOf(regular.charAt(i)))  && isInputCharacter(String.valueOf(regular.charAt(i+1))) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( isInputCharacter(String.valueOf(regular.charAt(i))) && regular.charAt(i+1) == '(' ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == ')' && isInputCharacter(String.valueOf(regular.charAt(i+1)) )) {
				newRegular += regular.charAt(i) + ".";
				
			} else if (regular.charAt(i) == '*'  && isInputCharacter(String.valueOf(regular.charAt(i+1))) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == '*' && regular.charAt(i+1) == '(' ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == ')' && regular.charAt(i+1) == '(') {
				newRegular += regular.charAt(i) + ".";			
				
			} else {
				newRegular += regular.charAt(i);
			}
		}
		newRegular += regular.charAt(regular.length() - 1);
		if(regular.charAt(regular.length() - 1)=='>')
			newRegular += ')';
		return newRegular;
	}
**/
	private static String AddConcat(String regular) {
		String newRegular = new String ("");

		for (int i = 0; i < regular.length() - 1; i++) {
			if ( isInputCharacter(String.valueOf(regular.charAt(i)))  && isInputCharacter(String.valueOf(regular.charAt(i+1))) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( isInputCharacter(String.valueOf(regular.charAt(i))) && regular.charAt(i+1) == '(' ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == ')' && isInputCharacter(String.valueOf(regular.charAt(i+1))) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if (regular.charAt(i) == '*'  && isInputCharacter(String.valueOf(regular.charAt(i+1))) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == '*' && regular.charAt(i+1) == '(' ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == ')' && regular.charAt(i+1) == '(') {
				newRegular += regular.charAt(i) + ".";			
				
			} else {
				newRegular += regular.charAt(i);
			}
		}
		newRegular += regular.charAt(regular.length() - 1);
		return newRegular;
	}
	
	
	// Return true if is part of the automata Language else is false
	private static boolean isInputCharacter(String charAt) {
		if (input.contains(charAt))	return true;
		else if (charAt == epslon)	return true;
		else if (names.contains(charAt))	return true;
		else 
						return false;
	}

	
	// Using the NFA, generates the DFA
	public static DFA generateDFA(NFA nfa) {
		// Creating the DFA
		DFA dfa = new DFA ();
		//if(nfa!=null){
		//System.out.println(layer);
		dfa.setmaxlayer(nfa.getmaxlayer());
		dfa.setalphabet(nfa.getalphabet());
		dfa.setnames(nfa.getnames());
		dfa.setscopes(nfa.getscopes());
		//System.out.println("nfa has "+nfa.getNfa().size()+" states");
		// Clearing all the states ID for the DFA
		stateID = 0;

		// Create an arrayList of unprocessed States
		LinkedList <State_Re> unprocessed = new LinkedList<State_Re> ();
		
		// Create sets
		set1 = new HashSet <State_Re> ();
		set2 = new HashSet <State_Re> ();

		// Add first state to the set1
		//System.out.println("first state: "+nfa.getNfa().getFirst().getStateID());
		set1.add(nfa.getNfa().getFirst());

		// Run the first remove Epsilon the get states that
		// run with epsilon
		removeEpsilonTransition ();

		// Create the start state of DFA and add to the stack
		State_Re dfaStart = new State_Re (set2, stateID++);
		dfaStart.setlevel(0);
		dfaStart.setinitstate(true);
		
		dfa.getDfa().addLast(dfaStart);
		dfa.setinitstate();
		unprocessed.addLast(dfaStart);
		
		/*
		 * Original input not contain names
		 * now try contain them use a new variable then instead of @input in for loop
		 */
		Set <String> wholeinput = new HashSet <String> ();
		wholeinput.addAll(input);
		wholeinput.addAll(names);
		
		// While there is elements in the stack
		while (!unprocessed.isEmpty()) {
			// Process and remove last state in stack
			State_Re state = unprocessed.removeLast();
			
			
			
			// Check if input symbol
			//for (Character symbol : input) {
			for (String symbol : wholeinput) {	
				set1 = new HashSet<State_Re> ();
				set2 = new HashSet<State_Re> ();
				//System.out.println(symbol.charValue());
				
				moveStates (symbol, state.getStates(), set1);
				//if set1 has no transitions over alphabets then continue a new loop 
				if(set1.isEmpty())
					continue;
				
				removeEpsilonTransition ();

				boolean found = false;
				State_Re st = null;

				for (int i = 0 ; i < dfa.getDfa().size(); i++) {
					st = dfa.getDfa().get(i);

					if (st.getStates().containsAll(set2)) {
						found = true;
						break;
					}
				}

				//version for selecting valid transitions
				boolean valid=false;
				 
				int newlevel=0;
				switch(String.valueOf(symbol)){
				case "<":
					if(state.getlevel()<dfa.getmaxlayer()){
							
						valid=true;
						newlevel=state.getlevel()+1;
					}
						break;
					
				case ">":
					if(state.getlevel()>0){
						valid=true;
						newlevel=state.getlevel()-1;
					}
						break;
						
				default:  {
				
			            	valid=true;
			            	newlevel=state.getlevel();
			            
					}
				}

				
				// Not in the DFA set, add it
				// IMPORTANT for nominal automata, the scopes symbol and names should be bounded
				if (!found) {
					/*
					 * if not found, create new state and add new transition with certain symbol
					 */
					//orginal version
//					boolean valid=false;
//					State_Re p = new State_Re (set2, stateID++);
//
//						unprocessed.addLast(p);
//						dfa.getDfa().addLast(p);
//						state.addTransition(p, symbol);
					
					if(valid){
						//System.out.println(state.getStateID()+" "+symbol+" new state id "+stateID);
						State_Re p = new State_Re (set2, stateID++);
						p.setlevel(newlevel);
						unprocessed.addLast(p);
						dfa.getDfa().addLast(p);
						state.addTransition(p, symbol);
						
					}
						
				// Already in the DFA set
				} else {


					if(valid){
						state.addTransition(st, symbol);
						
					}
					
					//original version
					//state.addTransition(st, symbol);
				}//end if
			}//end for		
			//dfa.outputToconsole();
		}//end while
		//set final states
		dfa.setfinalstates();
		// Return the complete DFA
				return dfa;
		//}
		//return null;
	}

	// Remove the epsilon transition from states
	private static void removeEpsilonTransition() {
		Stack <State_Re> stack = new Stack <State_Re> ();
		set2 = set1;

		for (State_Re st : set1) { stack.push(st);	}

		while (!stack.isEmpty()) {
			State_Re st = stack.pop();

			ArrayList <State_Re> epsilonStates = st.getAllTransitions (epslon);

			for (State_Re p : epsilonStates) {
				// Check p is in the set otherwise Add
				if (!set2.contains(p)) {
					set2.add(p);
					stack.push(p);
				}				
			}
		}		
	}

	// Move states based on input symbol
	private static void moveStates(String c, Set<State_Re> states,	Set<State_Re> set) {
		ArrayList <State_Re> temp = new ArrayList<State_Re> ();

		for (State_Re st : states) {	temp.add(st);	}

		for (State_Re st : temp) {	
			//System.out.println(st.getStateID()+"\t"+st.getNextState().toString());
			ArrayList<State_Re> allStates = st.getAllTransitions(c);
			//original version
			//for (State_Re p : allStates) {	set.add(p);	}
			
			if(allStates.size()>0)
				for (State_Re p : allStates) 
					set.add(p);
		
			
		}
	}	
	
	
}
// This line make it work
