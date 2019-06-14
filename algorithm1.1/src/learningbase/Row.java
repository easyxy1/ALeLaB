package learningbase;

import java.util.Set;

import nAutomata.State;
import nAutomata.State.Type;
import word.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Row {
	// set of this row's data
	List<String> rowdata;
	// this row's label, s in S/S.A
	final String s;
	// whether this row in S or SA
	private boolean isS;
	//the level of "s"
	private int lv;
	
	//constructor
	public Row(String s){
		this.s=s;
		this.lv=setlevel();
		rowdata = new ArrayList<>();
	}
		
	//add value for a suffix 
	void addValue(String string){
		rowdata.add(string);
	}
	
	//clear this row values
	void clear(){
		rowdata.clear();
	}
	//get the label of this row
	public String getLabel(){
		return s;
	}
	
	//set lv's value
	private int setlevel(){
		int lv=0;
		lv=Word.unmatched(s);
		return lv;
	}
	//get lv's value
	public int getlevel(){
		return this.lv;
	}
	
	//get the value of whether this row is in set of S
	public boolean isS(){
		return isS;
	}
	//get the value of whether this row is in set of SA
		public boolean isSA(){
			return !isS;
		}
	
	//set  the value if this row is in set of S
	public void setisS(){
		this.isS=true;
	}
	
	//set  the value if this row is in set of S.A
	public void setisSA(){
		this.isS=false;
	}
	
	//get all cell contents in this row
	public List<String> getContents(){
		return  Collections.unmodifiableList(rowdata);
	}
	//get a combined content for distinguishing this row
		public String distinguishContents(){
			String contents=this.getlevel()+rowdata.toString();
			return  contents;
		}
	//get the first content. 
	public String getFirstContent(){
		return this.rowdata.get(0);
	}
	
	//whether is equal to another Row. levels are same and contents are same
	public boolean equals(Row second){
		boolean isequal=false;
		if (this == second) {
			return true;
		}

		if (!(second instanceof Row)) {
			return false;
		}
		
		if(this.getlevel()==second.getlevel()){
			if(this.isContentsEqual(second)){
				isequal=true;
			}
		}

		return isequal;
	}
	//compare the content with another row
	boolean isContentsEqual(Row row) {
		if(this.rowdata.isEmpty()||row.rowdata.isEmpty()){
			return false;
		}
		return rowdata.equals(row.rowdata);
	}
	
}
