package learningbase;

import java.io.IOException;

public class MakeClosedandConsistent {
	//make table closed and consistent-------main body
	
			public void makeTableClosedAndConsistent(Table table) throws IOException {
				// TODO Auto-generated method stub
				boolean closedAndConsistent = false;

				while (!closedAndConsistent) {
					closedAndConsistent = true;
					System.out.println("closed: "+table.isClosed());
					if (!table.isClosed()) {
						closedAndConsistent = false;
						table=closeTable(table);
						System.out.println("After modifying, closed is "+table.isClosed());
						table.printTable();
					}
					
					System.out.println("consistent: "+table.isConsistentWithAlphabet());
					
					if (!table.isConsistentWithAlphabet()) {
						closedAndConsistent = false;
						table= ensureConsistency(table);
						System.out.println("After modifying, consistent is "+table.isConsistentWithAlphabet());
						table.printTable();
					}
				}
				table.printTable();
				
			}
			//make table consistent
			private static Table ensureConsistency(Table t) throws IOException {
				// TODO Auto-generated method stub
				InconsistencyDataHolder dataHolder = t.findInconsistentSymbol();

				if (dataHolder == null) {
					// It seems like this method has been called without checking if table is inconsistent first
					return t;
				}

				String witness = t.determineWitnessForInconsistency(dataHolder);
				String newSuffix = dataHolder.getDifferingSymbol()+witness;
				System.out.println("test on different rows: "+dataHolder.getFirstRow().s+", "+ dataHolder.getSecondRow().s);
				System.out.println("test: "+"symbol is "+ dataHolder.getDifferingSymbol());
				System.out.println("test: "+"witness is "+ witness);
				t.addSuffix(newSuffix);
				System.out.println("test: "+t.getSuffixes());
				
				
				t.processMembershipQueriesForTables(t.getShortPrefixLabels());
				System.out.println("test in ensureConsistency: add short prefix...");// test for over repeat
				t.processMembershipQueriesForTables(t.getLongPrefixLabels());
				System.out.println("test in ensureConsistency: add long prefix...");// test for over repeat
				
				return t;
						
				
			}
			//make table closed
			private static Table closeTable(Table t) throws IOException {
				// TODO Auto-generated method stub
				
				String candidate = t.findUnclosedState();
				System.out.println("Test on closetable: candidate is "+candidate);
				//while (candidate != null) {
					t.moveLongPrefixToShortPrefixes(candidate);
					//update S.A by new element in S (already change this functionality into previous step)
					//t.appendAlphabetSymbolsToWord(candidate);
					////remove the row from SA, which its label exists in S 
					t.removeShortPrefixesFromLongPrefixes();
					//fill the result for new row
					t.processMembershipQueriesForTables(t.getLongPrefixLabels());
					System.out.println("Test on closetable add long prefix...");// test for over repeat
					
					//candidate = t.findUnclosedState();
				//}
				return t;
			}
			
}
