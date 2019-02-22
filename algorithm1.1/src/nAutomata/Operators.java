package nAutomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import learningbase.Teacher.Strategy;

//assume that all automata working on are dereministic and minimal

public class Operators {
	public static Automaton Complementation(Automaton a){
		Automaton pOfa=a;
		for(State s: pOfa.getAllstates()){
			s.unsetFinal();
			
		}
		pOfa.resetFinalstates();
		System.out.println("Operators.java:: "+pOfa.toString()+" after complementation, final states: "+ pOfa.getAllFinalstatesNames());
		return pOfa;
		
	}
	/**	 * 
	 * @param a is teacher's automaton
	 * @param b is learner's automaton
	 *  both of them are minimal automata.
	 * @return an empty string if a and b accept the same language; a nonempty string accepted by either a or b.
	 */
	public static String Comparation(Automaton a, Automaton b){
		
		String counterexample="";
		
		Map<State,String> lastintersection=new HashMap<State,String>();//store a path to a state in Automaton d 
		Set<Transition> previous= new HashSet<Transition>();
		//if b has no final state, return an accepted word of a
		if(b.getAllFinalstates().isEmpty()) {
			counterexample= getAcceptedSuffix1(a, "");
			return counterexample;
		}
		
		Automaton c=Complementation(b);//c is complementation of b
		Automaton d=new Automaton();//d is intersection of a and b
		Map<State,List<State>> toDo=new HashMap<State,List<State>>();//current states in d which have no transitions.
		
		lastintersection.put(d.getinitState(),""); //initial the starting path
		State inita= a.getinitState();
		State initb= c.getinitState();
		if(inita.getFinal() && initb.getFinal()){
			d.getinitState().setFinal();
		}
		
		List<State> initlist=new ArrayList<>();
		initlist.add(inita);
		initlist.add(initb);
		toDo.put(d.getinitState(),initlist);
		
//		while(previous.isEmpty() || !previous.equals(d.getAllTransitions())){//compare d's current transitions and previous, if equal, ends loop. otherwise expanding d
//			previous.clear();
//			previous.addAll(d.getAllTransitions());
			
			while(!toDo.isEmpty()){
				Map<State,List<State>> temp=new HashMap<State,List<State>>();
				@SuppressWarnings("unchecked")
				Set<State> kset=toDo.keySet();
				for(State s: kset){
					String pathFors= lastintersection.get(s);
					State tostateD;
					State fromstateA=toDo.get(s).get(0);
					State fromstateB=toDo.get(s).get(1);
					Transition newtrans;
					for(Transition t:fromstateA.getTransitions()){
						String symbol=t.getSymbol();
						String newpath=pathFors.concat(symbol);
						//System.out.println("newpath: "+newpath);  
						State tostateA=fromstateA.nextState(symbol);
						State tostateB=fromstateB.nextState(symbol);
						if(tostateB!=null){//if fromstateB has a transition of the same label, create an transition for this label
							String tostatename=tostateA.getname()+tostateB.getname();
							//update data for Automata d
							if(d.getAllstatasname().contains(tostatename)){
								tostateD=d.getState(tostatename);
							}else{
								tostateD = new State(tostateA.getname()+tostateB.getname());
								d.addState(tostateD);
							}
							newtrans= new Transition(s, tostateD, symbol);
							d.addTransition(newtrans);
						
							switch(symbol){
							case "<":{//in this case, state's level +1
							s.setAlocateTo(tostateD);break;
							}
							case ">":{//in this case, state's level -1
								s.setDealocateTo(tostateD);break;
							}
							default: {//in this case, state's level does not change 
								s.setTransition(newtrans);break;
							
							}
							}
						
							//update data for next loop
						
							lastintersection.put(tostateD, newpath);
							if(tostateA.getFinal() && tostateB.getFinal()){
								tostateD.setFinal();
								counterexample=newpath;
								System.out.println("Operators.java:: "+"case meeting final state: "+tostateD.getname()); 
								return counterexample;
							}

							List<State> newlist=new ArrayList<>();
							newlist.add(tostateA);
							newlist.add(tostateB);
							temp.put(tostateD,newlist);
						}else{//in this case, it means @symbol is a new transition to Automaton b
							switch(symbol){
							case "<":{
								System.out.println("b's level: "+b.getTotalLevel());
								if(fromstateB.getLevel()<b.getTotalLevel()){//it means ignore the allocated action in lower level. 
									continue;						  //if in the top level of b, it should add this new action in "default" following
								}else{
									if(getAcceptedSuffix1(a, newpath)!=null) {
										counterexample=newpath+ getAcceptedSuffix1(a, newpath);
										System.out.println("case <"+newpath);   
										return counterexample;
									}
								}
							}
							default: {//in this case, state's level does not change 
								counterexample=newpath;
								System.out.println("Operators.java:: "+"case default:"+newpath);  
								return counterexample;
							
							}
							}
						}
					
					}
				/*	toDo.remove(s);//after adding all transitions for s, remove it from toDo set
					if(kset==null)
						break;*/
				}
				if(toDo.equals(temp))
					break;
				else
					toDo=temp;
			}
//		}
		
		
		return counterexample;
		
	}
	
	/**
	 * 
	 * @param a is teacher's automaton
	 * @param b is learner's automaton
	 *  both of them are minimal automata.
	 * @return an empty set if a and b accept the same language; a set of strings accepted by either a or b.
	 */
	public static Set<String> Equivalence(Automaton a, Automaton b,Strategy strategy){
		Set<String> availableCEset= new HashSet<String>();
		String prefix="";
		String currentsymbol="";
		String suffix="";
		Map<State,State> equalstates=new HashMap<State,State>();
		equalstates.put(a.getinitState(), b.getinitState());
		//set up condition data for while loop. it is the set of learner's transition set
		Set<Transition> unprocessed=b.getAllTransitions();
		
		//set up strategy
		State start1;
		State start2;
		switch(strategy){
		case VERTICAL: {//more layer first. consider all teacher's transition 
			 start1=b.getinitState();
			 start2=a.getinitState();
			break;
			}
		case HORIZONTAL: {//a layer completion first. consider all learner's transition
			 start1=a.getinitState();
			 start2=b.getinitState();
			break;
		}
		default: throw new IllegalArgumentException("unknown strategy");
		}
	
		//loop for finding a transition which lead to states which have different functionalities
		while(!unprocessed.isEmpty()){
			for(Transition t:start2.getTransitions()){
				if(equalstates.containsKey(t.getNext())){
					if(equalstates.get(t.getNext()).equals(start1.nextState(t.getSymbol()))){
						unprocessed.remove(t);
					}else{
						
					}
				}else{
					equalstates.put(t.getNext(), start1.nextState(t.getSymbol()));
					unprocessed.remove(t);
				}
			}
		}
		
		return availableCEset;
	}
	/**
	 * 
	 * @param a is an automaton
	 * @param prefix is the path from initial state to a state
	 * @return a string following prefix can lead to a final state
	 */
	public static String getAcceptedSuffix1(Automaton a, String prefix){

		String suffix=null;
		State s=a.getStateBypath(prefix);
		if(s.getFinal())
			return ""; //case: s is a final state. return empty string directly.
		
		//case: find a final state, and then get the path from s to that as suffix.
		//if does not exist, return null.
		for(State f:a.getAllFinalstates()){
			
			Map<State,String> sNextstates=new HashMap<State,String>();// the set of states that s leads to
			Map<State,String> fPrestates=new HashMap<State,String>(); // the set of states leads to f
			fPrestates.putAll(a.getPreStates(f));
			Map<State,String> temp=new HashMap<State,String>();
			
			while(!fPrestates.containsKey(s) && !fPrestates.equals(temp)) {
			
				//find previous states for recent previous states
				temp.putAll(fPrestates);
				Map<State,String> newput=new HashMap<State,String>();
				for(State p: fPrestates.keySet()) {
					String value=fPrestates.get(p);
					Map<State,String> tempForp=a.getPreStates(p);
					for(State sforv:tempForp.keySet()) {
						
						if(!fPrestates.keySet().contains(sforv)) {
							value=tempForp.get(sforv)+value;
							newput.put(sforv, value);
						}
						
					}
				}
				if(!newput.isEmpty()) {
					fPrestates.putAll(newput);
				}
			}
			if(fPrestates.containsKey(s)) {
				suffix=fPrestates.get(s);
				return suffix;
			}
			
		}
		
		return suffix;
	}

	
	
 	private static boolean existedFinal(Collection<State> values) {
		for(State s: values){
			if(s.getFinal())
				return true;
		}
		return false;
	}
	private static String getAnAcceptedstring(Automaton a) {
		
		return null;
		
	}
	
	private static State getStatebypath(Automaton a, String path) {
		State fromstate=a.getinitState();
		while(path.length()>0){
			String s = path.substring(0, 1);
			State tostate = fromstate.nextState(s);
			if(tostate==null){
				return null;
			}
			fromstate=tostate;
			path = path.substring(1);
		}
		
		return fromstate;
		
	}

	/**
	 * 
	 * @param a teacher's automaton
	 * @param b learner's automaton
	 * @param k length of a word
	 * @return a set of k-length words accepted in either of two automaton
	 * call this method when a,b have final states.
	 */
	 
	public static Set<String> getAvailableCEset(Automaton a,Automaton b, int k){
		Set<String> availableCEset= new HashSet<String>();
		
		Set<String> union= new HashSet<String>();
		Set<String> inters= new HashSet<String>();
		
		//words in the wordlistofteacher and the wordlistoflearner would be a counterexample.
		Set<String> wordlistofteacher=new HashSet<String>();
		Set<String> wordlistoflearner=new HashSet<String>();
		
		
		//find out all words of the length of teacher automaton's states' quantity 
		a.acceptedPaths(a.getinitState(),"", wordlistofteacher, k);
		b.acceptedPaths(b.getinitState(),"", wordlistoflearner, k);
		
		if(!wordlistofteacher.isEmpty() || !wordlistoflearner.isEmpty()){
			//union of two wordlists
			union.addAll(wordlistofteacher);
			union.addAll(wordlistoflearner);
			//System.out.println(union.toString());
			
			//intersection of two wordlists
			inters.addAll(wordlistofteacher);
			inters.retainAll(wordlistoflearner);
			//System.out.println(inters.toString());
			
			//the set of all words in either teacher's language or learner's language
			availableCEset.addAll(union);
			availableCEset.removeAll(inters);
		}
		
		//System.out.println(availableCEset.toString());
		return availableCEset;
	}
	
}
