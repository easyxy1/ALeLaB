package learningbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Query {
	private static List queries=new ArrayList();
	private static String ep;
	
	//construct membership queries
	public Query(String ep){
		
		this.ep=ep;
		while(ep.length()>0){
			if(ep.charAt(ep.length()-1)=='|' || ep.charAt(ep.length()-1)=='*'){
				ep=ep.substring(0, ep.length()-1);
			
			}else{
				queries.add(ep);
				ep=ep.substring(0, ep.length()-1);
			}
		}
	}
	//get the max number of register
	public int registerNo(){
		int numNo=0, numpre=0; 
		String checkEP=ep;
		while(checkEP.length()>0){
			if(checkEP.contains("<")){
				char ch= checkEP.charAt(0);
				if(ch=='<'){
					numpre++;
					if(numpre>numNo){
						numNo++;
					}
				}else if(ch=='>'){
					numpre--;
				}
				checkEP=checkEP.substring(1);
			}
		}
		return numNo;
	}
	//check a word whether in the given language
	public static String answernote(String s,String e){
		String ep=s+e;
		queries.contains(ep);
		return "O";
	}
	//output queries in a vertical list
	public String toString(){
		String listqueries="";
		for(int i=0;i<queries.size();i++){
			listqueries=listqueries+queries.get(i)+"\t";
		}
		return listqueries;
	}
}
