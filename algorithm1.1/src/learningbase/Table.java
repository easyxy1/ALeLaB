package learningbase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nAutomata.State;
import nAutomata.State.Type;
import word.Word;

public class Table {
	List<Row> rowS;
	List<Row> rowSA;
	List<String> setE;
	private int maxLevel=0;
	//alphabet
	Set<String> setA;
	Set<String> freshName=new HashSet<>();
	Set<String> operators=new HashSet<>();
	//variable for output automaton file
	private Set<String> contents=new HashSet<>();
	private Set<Row> distinguishedRows=new HashSet<>();
	private int totalStatenumber=0;
	
	//initial a table
	public Table(){
		//initial set of suffixes 
		this.setE=new ArrayList<>();
		this.setE.add("0");
		
		//initial set of short prefixes
		this.rowS=new ArrayList<>();
		Row initialRow=new Row("0");
		initialRow.setisS();
		this.rowS.add(initialRow);
		//initial set of long prefixes
		this.rowSA=new ArrayList<>();
		
		
		//initial set of alphabet
		this.setA=new HashSet<>();
		
		operators.add("<");
		operators.add(">");
					
	}
//Distinguish short prefixes and store them
	public void distinguishContent(){
		contents.clear();
		for(Row r: rowS){
			contents.add(r.distinguishContents());
			
		}
	}
	//Distinguish Rows and store them
		public Set<Row> getdistinguishedRows(){
			distinguishedRows.clear();
			distinguishedRows.add(this.getRowForPrefix("0"));
			this.distinguishContent();
			for(String s: this.contents){
				for(Row r: this.rowS){
					if(r.distinguishContents().equals(s)){
						distinguishedRows.add(r);
						break;
					}
				}
				
			}
			return distinguishedRows;
		}
	//get contexts
	public Set<String> getContent(){
		return Collections.unmodifiableSet(contents);
	}
	
	//print data of table as a table
	public void printTable(){
			System.out.println("Names are"+this.freshName.toString());
			//System.out.println("\t\t\t\t"+"lv"+"\t\t"+setE.toString());
			/*try use java formatter as a structure
			int widthforlabel=this.totalStatenumber*2;
			int widthforsetE=setE.size()*3;
			String format="%1$5s "+"%2$"+String.valueOf(widthforlabel)+"s %3$"+String.valueOf(widthforsetE)+"s";
			System.out.format("%5s%20s%20s","lv"," ",setE.toString());
			System.out.println();
			*/
			System.out.format("%-5s%-20s%-20s","lv"," ",setE.toString());
			System.out.println();
			for(Row r: rowS){
				
				//System.out.print("\t\t\t\t"+r.getlevel()+"\t"+r.s+"\t");
				//System.out.println(r.getContents().toString());
				System.out.format("%-5s%-20s%-20s",r.getlevel(),r.s,r.getContents().toString());
				System.out.println();
			}
			if(!rowSA.isEmpty()){
				//System.out.println("\t\t\t\t"+"-------------------");
				System.out.format("%1$20s%1$20s","---------------------");
				System.out.println();
				for(Row r: rowSA){
					//System.out.print("\t\t\t\t"+r.getlevel()+"\t"+r.s+"\t");
					//System.out.println(r.getContents().toString());
					System.out.format("%-5s%-20s%-20s",r.getlevel(),r.s,r.getContents().toString());
					System.out.println();
				}
				//System.out.println("\t\t\t\t"+"==================");
				System.out.format("%1$20s%1$20s","=====================");
				System.out.println();
			}
			
	}
	//get the total states number of this table
	public int gettotalStatenumber(){
		this.distinguishContent();
		this.totalStatenumber=contents.size();
		return this.totalStatenumber;
	}
	//set finite alphabets for table
	public void setAlphabet(Set<String> alphabet){
		//add finite alphabet
		for(String a :alphabet){
			setA.add(a);
		}			
			
	}
	//add freshname
	private void addfreshname(String s){
		int i=Integer.parseInt(s);
		if(!s.equals("0"))
			for(int j=1;j<=i;j++)
				this.freshName.add(String.valueOf(j));
	}
	//set the max level of current table
	private void setmaxlevel(int i){
		this.maxLevel=i;
	}
	//get the max level of current table
	public int getmaxlevel(){
		return	this.maxLevel;
	}
	
	// The set of suffixes in the observation table, often called "E". 
		// add one more suffix
	void addSuffix(String suffix) {
		suffix = Word.deleteLambda(suffix);
		while(suffix.length()>0){
			if(!setE.contains(suffix) ){
				setE.add(suffix);
			}
			suffix=suffix.substring(1);
			
		}
			
	}

	//Add a row in S
	void addShortPrefix( String shortPrefix) {
	
			final Row row = new Row(shortPrefix);
			row.setisS();;
			rowS.add(row);
		
	}
	public void setLongPrefix(){
		//add long prefix rows for alphabets
		for(String s: this.getShortPrefixLabels()){
			for(String a : setA){
				String sa=Word.deleteLambda(s+a);
				//if(s=="0" && Word.legal(a)){
//				
//					this.addLongPrefix(a);
//				}else{
					//sa=s+a;
					if(Word.isLegal(sa)){
						this.addLongPrefix(sa);
						//add dealocated symbol
//						if(Word.alcatedNumber(sa)>Word.dealcatedNumber(sa)) {
//							String newlongprefix=sa.concat(">");
//							this.addLongPrefix(newlongprefix);
//						}
					}
			//	}
			}
			if(this.maxLevel>0){
				for(String a : this.freshName){
					//String sa=s+a;
					String sa=Word.deleteLambda(s+a);
					if(Word.isLegal(sa)){
						this.addLongPrefix(sa);
						//add dealocated symbol
//						if(Word.alcatedNumber(sa)>Word.dealcatedNumber(sa)) {
//							String newlongprefix=sa.concat(">");
//							this.addLongPrefix(newlongprefix);
//						}
					}
				}
				for(String a : this.operators){
					//String sa=s+a;
					String sa=Word.deleteLambda(s+a);
					if(Word.isLegal(sa) && 0<=Word.unmatched(sa) && Word.unmatched(sa)<=this.maxLevel){
						this.addLongPrefix(sa);
						//add dealocated symbol
//						if(Word.alcatedNumber(sa)>Word.dealcatedNumber(sa)) {
//							String newlongprefix=sa.concat(">");
//							this.addLongPrefix(newlongprefix);
//						}
					}
				}
			}
		}
		
				
			
		
	}
	//add a row in SA
	int addLongPrefix( String longPrefix) {
		//if prefix in S, do nothing
		if(this.getShortPrefixLabels().contains(longPrefix)){
			return -1;
		}else{
			//if prefix does not exist, add it into SA
			if(!this.getLongPrefixLabels().contains(longPrefix)){
				final Row row = new Row(longPrefix);
				row.setisSA();
				rowSA.add(row);
				return 0;
			}
//			else{
//				if(this.rowSA.isEmpty()){
//					final Row row = new Row(longPrefix);
//					row.setisSA();
//					rowSA.add(row);
//					return 1;
//				}
//			}
		}
		//if nothing is done, return 1
		return 1;
	}
	//get all short prefix rows, that is, all row in S
	public Collection<Row> getShortPrefixRows() {
		return Collections.unmodifiableCollection(rowS);
	}


		//get all short prefix label, that is, all s in S
	public List<String> getShortPrefixLabels() {
		List<String> labels =new ArrayList<>(); 			
		for (Row row : rowS) {
			labels.add(row.getLabel());
		}
			
		return labels;
	}
	
	//get all long prefix rows, that is, all row in S.A
	public Collection<Row> getLongPrefixRows() {
		return Collections.unmodifiableCollection(rowSA);
	}
		
	//get all long prefix label, that is, all s in S.A
	public List<String> getLongPrefixLabels() {
		List<String> labels =new ArrayList<>(); 
		for (Row row : rowSA) {
			labels.add(row.getLabel());
		}
		return labels;
	}
	
	public List<String> getSuffixes() {
		return Collections.unmodifiableList(setE);
	}
		
	
		
		//remove the row from SA, which its label exists in S 
		void removeShortPrefixesFromLongPrefixes() {
			List<String> longPrefixLabels = getLongPrefixLabels();
			longPrefixLabels.retainAll(getShortPrefixLabels());

			List<Row> rowsToRemove = new ArrayList<>();

			for (Row row : rowSA) {
				if (longPrefixLabels.contains(row.getLabel())) {
					rowsToRemove.add(row);
				}
			}

			rowSA.removeAll(rowsToRemove);
		}
		// move a row from SA to S
		void moveLongPrefixToShortPrefixes(String longPrefix) {
			while(!longPrefix.isEmpty()){
				Row rowToMove = this.getRowForPrefix(longPrefix);
				if (!rowToMove.isS()) {
					//remove one row from longprefix and add it to shortprefix
					rowSA.remove(rowToMove);
					rowToMove.setisS();
					rowS.add(rowToMove);
					//append this row with alphabet, extend longprefixes
					this.appendAlphabetSymbolsToWord(longPrefix);
				}
				longPrefix=longPrefix.substring(0, (longPrefix.length()-1));
			}
		}

		//Add the result of a membership query to this table
		void addResult(String prefix, String suffix, String string) {
			if (!setE.contains(suffix)) {
				throw new IllegalArgumentException("Suffix '" + suffix + "' is not part of the suffixes set");
			}

			final int suffixPosition = setE.indexOf(suffix);
			Row row = getRowForPrefix(prefix);

			addResultToRow(string, suffixPosition, row);
		}

		private void addResultToRow(String string, int suffixPosition, Row row) {
			final List<String> values = row.getContents();
			if (values.size() > suffixPosition) {
				if (!values.get(suffixPosition).equals(string)) {
					throw new IllegalStateException(
							"New result " + values.get(suffixPosition) + " differs from old result " + string);
				}
			}
			else {
				row.addValue(string);
			}
		}
		
		//get a row data by its label
		
		Row getRowForPrefix(String state) {
			state=Word.deleteLambda(state);
			if(state==""){
				state="0"; //reset lambda visible if the string is lambda
			}
			for (Row row : rowS) {
				if (row.getLabel().equals(state)) {
					return row;
				}
			}

			for (Row row : rowSA) {
				if (row.getLabel().equals(state)) {
					return row;
				}
			}

			//throw new IllegalArgumentException("Unable to find a row for '" + state + "'");
			return null;
		}

		public boolean isClosed() {
			// TODO Auto-generated method stub
			return (findUnclosedState()==null);
		}
		//find the unclosed row in the table, if table is closed, return null
		public String findUnclosedState() {
			for (Row candidate : rowSA) {
				boolean found = false;
				//System.out.print(candidate.getContents());
				for (Row stateRow : rowS) {
					//System.out.print(stateRow.getContents());
					if(candidate.getlevel()==stateRow.getlevel()&& candidate.isContentsEqual(stateRow)){
						
							found = true;
							break;
					
					}
				}

				if (!found) {
					//System.out.print(candidate.getLabel());
					return candidate.getLabel();
				}
			}

			return null;
		}

		/**
		 * @param alphabet
		 * 		The {@link Alphabet} for which the consistency is checked
		 * @return if the observation table is consistent with the given alphabet.
		 */
		boolean isConsistentWithAlphabet() {
			return findInconsistentSymbol() == null;
		}

/*	attempt with mcrl2
 * 	InconsistencyDataHolder findInconsistentSymbol() {
			
				for (int firstStateCounter = 0; firstStateCounter < rowS.size(); firstStateCounter++) {
					Row firstRow = rowS.get(firstStateCounter);

					for (int secondStateCounter = firstStateCounter + 1; secondStateCounter < rowS.size(); secondStateCounter++) {
						Row secondRow = rowS.get(secondStateCounter);
						if(firstRow.equals(secondRow)){
							String symbol=null;
							// case 1: two rows have same number of own SA elements
							if(this.getOwnSAbyLabel(firstRow.getLabel()).size()>=this.getOwnSAbyLabel(secondRow.getLabel()).size()){
								
								for(Row r_1: this.getOwnSAbyLabel(firstRow.getLabel())){
									String label_1=r_1.getLabel();
									String lastsymbol=label_1.substring(label_1.length()-1);
									String label_2=secondRow.getLabel().concat(lastsymbol);
									Row r_2=this.getRowForPrefix(label_2);
									if(r_1.equals(r_2)){
										return null;
									}else{
										System.out.println("inconsistent rows: "+firstRow.s+","+secondRow.s+", for symbol:"+lastsymbol);
										return new InconsistencyDataHolder(firstRow, secondRow, lastsymbol);
									}
//									for(Row r_2: this.getOwnSAbyLabel(secondRow.getLabel())){
//										String label_2=r_2.getLabel();
//										if(label_2.endsWith(lastsymbol)){
//											if(!r_1.isContentsEqual(r_2)){
//												symbol=lastsymbol;
//												System.out.println("inconsistent rows: "+firstRow.s+","+secondRow.s+","+symbol);
//												return new InconsistencyDataHolder(firstRow, secondRow, symbol);
//											}
//										}
//									}
								}
							}else{
								
								for(Row r_2: this.getOwnSAbyLabel(secondRow.getLabel())){
									String label_2=r_2.getLabel();
									String lastsymbol=label_2.substring(label_2.length()-1);
									String label_1=firstRow.getLabel().concat(lastsymbol);
									Row r_1=this.getRowForPrefix(label_1);
									if(r_2.equals(r_1)){
										return null;
									}else{
										System.out.println("inconsistent rows: "+firstRow.s+","+secondRow.s+","+lastsymbol);
										return new InconsistencyDataHolder(firstRow, secondRow, lastsymbol);
									}
								}
								
								
								
							// case 2: two rows have different number of own SA elements
//								if(this.getOwnSAbyLabel(firstRow.getLabel()).size()>this.getOwnSAbyLabel(secondRow.getLabel()).size()){
//									//	String symbol= this.getOwnSAbyLabel(firstRow.getLabel()).get(this.getOwnSAbyLabel(firstRow.getLabel()).size()-1).getLabel();
//									symbol=findmakeNONexistedSymbol(firstRow.getLabel(),secondRow.getLabel());
//									System.out.println("test inconsistentsymbol: "+"symbol is "+ symbol);
//								
//								}else if(this.getOwnSAbyLabel(firstRow.getLabel()).size()<this.getOwnSAbyLabel(secondRow.getLabel()).size()){
//									//	String symbol=this.getOwnSAbyLabel(secondRow.getLabel()).get(this.getOwnSAbyLabel(secondRow.getLabel()).size()-1).getLabel();
//									symbol=findmakeNONexistedSymbol(firstRow.getLabel(),secondRow.getLabel());
//									System.out.println("test inconsistentsymbol: "+"symbol is "+ symbol);
//								
//								}
//								if(symbol==null){
//									return null;
//								}else{
//									return new InconsistencyDataHolder(firstRow, secondRow, symbol);
//							
//								}
							
							}
							
							
						}
						
				// first attempt to check consisstent with fresh name. but have some problems to make program into infinite loop.
						//check consistent with finite alphabets
						for (String symbol : this.setA) {
							if (checkInconsistency(firstRow, secondRow, symbol)) {
								return new InconsistencyDataHolder(firstRow, secondRow, symbol);
							}
						}
						//core technique for nominal automata. checking consistent with fresh name
						if(firstRow.getLabel().contains("<")&& secondRow.getLabel().contains("<")){
							for (String symbol : this.freshname) {
								if (checkInconsistency(firstRow, secondRow, symbol)) {
									return new InconsistencyDataHolder(firstRow, secondRow, symbol);
								}
							}
						}else{
							if(firstRow.getLabel().contains("<")){
								if(!allocateNewName(firstRow.getLabel()).equals("0")){
									String newname=allocateNewName(firstRow.getLabel());
									return new InconsistencyDataHolder(firstRow, secondRow, newname);
								} 
							}else if(secondRow.getLabel().contains("<")){
								if(!allocateNewName(secondRow.getLabel()).equals("0")){
									String newname=allocateNewName(secondRow.getLabel());
									return new InconsistencyDataHolder(firstRow, secondRow, newname);
								} 
							}
						}
					
					}
					
					
				}
			

			return null;
		}
		*/
	
		//attempt for own method "Operators"
		InconsistencyDataHolder findInconsistentSymbol() {
			
			for (int firstStateCounter = 0; firstStateCounter < rowS.size(); firstStateCounter++) {
				//get first row for checking consistent
				Row firstRow = rowS.get(firstStateCounter);

				for (int secondStateCounter = firstStateCounter + 1; secondStateCounter < rowS.size(); secondStateCounter++) {
					//get second row for checking consistent
					Row secondRow = rowS.get(secondStateCounter);
					//if two are in the same level and have same contents, continuing to check consistent
					if(firstRow.equals(secondRow)){
						String symbol=null;
						// set a flag to loop
						String loopflag=null;
						Row compared=null;
						if(this.getOwnSAbyLabel(firstRow.getLabel()).size()>=this.getOwnSAbyLabel(secondRow.getLabel()).size()){
							loopflag=firstRow.getLabel();
							compared=secondRow;
						}else{
							loopflag=secondRow.getLabel();
							compared=firstRow;
						}
						//check consistent
						for(Row r_1: this.getOwnSAbyLabel(loopflag)){
							String label_1=r_1.getLabel();
							String lastsymbol=label_1.substring(label_1.length()-1);
							String label_2=compared.getLabel().concat(lastsymbol);
							Row r_2=this.getRowForPrefix(label_2);
							
							if(r_1.equals(r_2)){
								continue;
							}else{
								//System.out.println("inconsistent rows: "+loopflag+","+compared.s+", for symbol:"+lastsymbol);
								return new InconsistencyDataHolder(firstRow, compared, lastsymbol);
							}
						}
					}
					
				}
			}
			return null;
		}
	
		private String findmakeNONexistedSymbol(String firstRow, String secondRow) {
			// TODO Auto-generated method stub
			String symbol=null;
			Row first=this.getRowForPrefix(firstRow);
			Row second=this.getRowForPrefix(secondRow);
			if(first.getlevel()==second.getlevel()){
				List<Row> sa1=getOwnSAbyLabel(firstRow);
				List<Row> sa2=getOwnSAbyLabel(secondRow);
				if(sa1.size()>sa2.size()){
					for(Row r: sa1){
						String remainsa=r.getLabel().substring(firstRow.length());
						String checkRowLabel=secondRow.concat(remainsa);
						Row checkRow=this.getRowForPrefix(checkRowLabel);
						if(!sa2.contains(checkRow)){
							symbol=remainsa;
							break;
						}
					}
				}else{
					for(Row r: sa2){
						String remainsa=r.getLabel().substring(secondRow.length());
						String checkRowLabel=firstRow.concat(remainsa);
						Row checkRow=this.getRowForPrefix(checkRowLabel);
						if(!sa1.contains(checkRow)){
							symbol=remainsa;
							break;
						}
					}
				}
			}
				
			return symbol;
		}

		private List<Row> getOwnSAbyLabel(String label){
			List<Row> ownSA = new ArrayList<>(); 
			//attempt 1. 
//			if(label.equals("0"))
//				label="";
//			else
//				label=Word.deleteLambda(label);
			
//			for(String a: this.setA){
//				String newlabel=label+a;
//				ownSA.add(this.getRowForPrefix(newlabel));
//			}
			
			if(label.equals("0")){
				for(String s: this.getShortPrefixLabels()){
					if(s.length()==label.length()&& s!=label){
						if(!ownSA.contains(this.getRowForPrefix(s))){
							ownSA.add(this.getRowForPrefix(s));
						}
						
					}
				}
				for(String sa: this.getLongPrefixLabels()){
					if(sa.length()==(label.length())){
						if(!ownSA.contains(this.getRowForPrefix(sa))){
							ownSA.add(this.getRowForPrefix(sa));
						}
							
					}
				}
			}else{
				for(String s: this.getShortPrefixLabels()){
					if(s.length()==(label.length()+1) && s.startsWith(label)){
						if(!ownSA.contains(this.getRowForPrefix(s))){
							ownSA.add(this.getRowForPrefix(s));
						}
						
					}
				}
				for(String sa: this.getLongPrefixLabels()){
					if(sa.length()==(label.length()+1)&& sa.startsWith(label)){
						if(!ownSA.contains(this.getRowForPrefix(sa))){
							ownSA.add(this.getRowForPrefix(sa));
						}
							
					}
//					else{
//						int case2=label.length()+2;// situation 2: sa is allocating a new name and deallocating
//						//check the situation of deallocation
//						if(sa.length()==case2 && label.length()>0 && sa.startsWith(label)){
//							String remainsa=sa.substring(label.length()-1);
//							if(remainsa.endsWith(">"))
//								ownSA.add(this.getRowForPrefix(sa));
//							}
//					}
				}
			}
			
			return ownSA;
		}

		private boolean checkInconsistency(Row firstRow, Row secondRow, String alphabetSymbol) {

			if (!firstRow.isContentsEqual(secondRow)) {
				return false;
			}

			String extendedFirstState = firstRow.getLabel() + alphabetSymbol;
			String extendedSecondState = secondRow.getLabel() + alphabetSymbol;
			Row rowForExtendedFirstState = getRowForPrefix(extendedFirstState);
			Row rowForExtendedSecondState = getRowForPrefix(extendedSecondState);

			return !rowForExtendedFirstState.isContentsEqual(rowForExtendedSecondState);
		}
		
		//compute the inconsistent letter in column "E"
		String determineWitnessForInconsistency( InconsistencyDataHolder dataHolder) {
			String firstState = dataHolder.getFirstState()+dataHolder.getDifferingSymbol();
			String secondState = dataHolder.getSecondState()+dataHolder.getDifferingSymbol();
			
			//these two rows may be null. cannot be found in S and SA
			Row firstRow = getRowForPrefix(firstState);
			Row secondRow = getRowForPrefix(secondState);
			//select out e in E which makes two rows different
			if(firstRow==null){
				return "";
			}else if(secondRow==null){
				return "";
			}else{
				final List<String> firstRowContents = firstRow.getContents();
				final List<String> secondRowContents = secondRow.getContents();

				for (int i = 0; i < firstRow.getContents().size(); i++) {
					String symbolFirstRow = firstRowContents.get(i);
					String symbolSecondRow = secondRowContents.get(i);
					if (!symbolFirstRow.equals(symbolSecondRow)) {
						return setE.get(i);
					}
				}
			}
			throw new IllegalStateException("Both rows are identical, unable to determine a witness!");
		}

		/**
		 * When new states are added to the observation table, this method fills the table values. For each
		 * given state it sends one membership query for each specified suffix symbol to the oracle of the
		 * form (state,symbol).
		 *
		 * @param states
		 * 		The new states which should be evaluated.
		 * @param suffixes
		 * 		The suffixes which are appended to the states before sending the resulting word to the oracle.
		 * @throws IOException 
		 */
//		public void processMembershipQueriesForTables(Collection<String> states) throws IOException{
//			//Collection<String> states= this.getShortPrefixLabels();
//			Collection<String> suffixes=this.getSuffixes();
//			List<DefaultQuery> queries = new ArrayList<>(states.size());
//			for (String label : states) {
//				for (String suffix : suffixes) {
//					queries.add(new DefaultQuery(label, suffix));
//					
//				}
//			}
//			for(DefaultQuery query : queries) {
//				String state = query.getPrefix();
//				String suffix=	query.getSuffix();
//				String word=state+suffix;
//			//	if(Word.legal(word)){
//					query.answer(Teacher.getanswer(word));// language input sotluion 1
//					this.addResult(state, suffix, query.getOutput());
//			//	}else{
//			//		this.addResult(state, suffix,"X"); //for attempt 3: with "X", algorithm goes more complex and associated automaton is not minimal
//			//	}
//			}
//		}
		
		/**
		 * Appends each symbol of the alphabet (with size m) to the given word (with size w),
		 * thus returning m words with a length of w+1.
		 *
		 * @param word
		 *      The {@link Word} to which the {@link Alphabet} is appended.
		 * @return
		 *      A set with the size of the alphabet, containing each time the word
		 *      appended with an alphabet symbol.
		 */
		
		public void appendAlphabetSymbolsToWord(String word) {
			List<String> newCandidates = new ArrayList<>();
			
			for(String a : setA){
				String sa=Word.deleteLambda(word+a);
					if(Word.isLegal(sa))
						newCandidates.add(sa);
			}
			if(this.maxLevel>0){
				for(String a : this.freshName){
					String sa=Word.deleteLambda(word+a);
					if(Word.isLegal(sa)){
						newCandidates.add(sa);
					}
				}
				for(String a : this.operators){
					
					String sa=Word.deleteLambda(word+a);
					if(Word.isLegal(sa) && 0<=Word.unmatched(sa) && Word.unmatched(sa)<=this.maxLevel){
						newCandidates.add(sa);
					}
				}
			}
		/**
			if(word.contains("<")){
				
				if(!allocateNewName(word).equals("0")){
					String newname=allocateNewName(word);
					if(!this.freshname.contains(newname)){
						this.addfreshname(newname);
						this.setmaxlevel(Integer.parseInt(newname));
					}
					String newCandidate = word+">";
					newCandidates.add(newCandidate);
					newCandidate = word+newname;
					newCandidates.add(newCandidate);
					newCandidate=newCandidate+">";
					newCandidates.add(newCandidate);
				}
			}
			for (String alphabetSymbol : setA) {
				String newCandidate = word+alphabetSymbol;
				
				newCandidates.add(newCandidate);
				
			}
		**/
			
			//return newCandidates;
			for(String candidate: newCandidates){
				this.addLongPrefix(candidate);
			}
		}
		//calculate the top layer of name in word
		private String allocateNewName(String word) {
			// TODO Auto-generated method stub
			String newword=word;
			int newname=0;
			String allocate="<";
			String deallocate=">";
			//System.out.println("test:"+"calculate fresh name");
			while(newword.contains(allocate)){
				
				if(newword.contains(deallocate)){
					if(newword.indexOf(allocate)>newword.indexOf(deallocate)){
						newname--;
						newword=newword.replaceFirst(deallocate, "");
					}else{
						newname++;
						newword=newword.replaceFirst(allocate, "");
					}
				}else{
					newname++;
					newword=newword.replaceFirst(allocate, "");
				}
				
			}
			if(newword.contains(deallocate)){
				newname--;
			}
			String namestring=String.valueOf(newname);
			//System.out.println(namestring);
			return namestring;
		}

		//get all reachable short prefixes' labels in this table
		public List<Row> getreachableLabels(){
			List<Row> reachableLabels=new ArrayList<Row>();
			for(Row r:this.rowS){
				if(r.getFirstContent().equals("P")){
					reachableLabels.add(r);
				}
			}
			
			return  Collections.unmodifiableList(reachableLabels);
		}
		
		//check whether a string is in the table 
		private boolean isRowexisted(String string){
			if(this.getRowForPrefix(string)==null){
				return false;
			}
			return true;
		}
		
		
		//use counterexample to extend table (in table)
		public void extendByCE(String c){
			//System.out.println("counterexample is " + c);
			
			if(Word.maxLayer(c)!=0){
				this.setmaxlevel(Math.max(maxLevel, Word.maxLayer(c)));
				String newname=String.valueOf(this.maxLevel);
				if(!this.freshName.contains(newname)){
					this.addfreshname(newname);
					
				}
			}
			while(!c.isEmpty()){
				//Row newS=new Row(counterexample);
				if(!isRowexisted(c)){
					this.addShortPrefix(c);
					this.appendAlphabetSymbolsToWord(c);
					
				}else{
					if(this.getRowForPrefix(c).isSA()){
						this.moveLongPrefixToShortPrefixes(c);
					}
				}
				c=c.substring(0, c.length()-1);
			}
			//System.out.println("maxlevel is " + maxlevel);
			this.setLongPrefix();
//			try {
//				this.processMembershipQueriesForTables(this.getShortPrefixLabels());
//				this.processMembershipQueriesForTables(this.getLongPrefixLabels());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		}

}
