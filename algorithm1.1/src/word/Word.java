package word;

public class Word {
	public static String deleteLambda(String s){
		String returnWord="";
		if(s.contains("0") /*&& s.length()>1*/){
			//solution 1 to delete lamda in a word
			String[] splitparts=s.split("0");
			for(String part:splitparts){
				returnWord=returnWord + part;
			}
	
		}else
			returnWord=s;
		
		return returnWord;
	}
	//check a string is legal string
	public static boolean isLegal(String word) {
		// TODO Auto-generated method stub
		boolean legal=true;
		int langenumber=0;
		int rangenumber=0;
		String num="1234567890";
		while(word.length()>0){
			String c= word.substring(0, 1);
			if(c.equals("<")){
				langenumber++;
			}else if(c.equals(">")){
				rangenumber++;
				if(langenumber<rangenumber) {
					legal= false;
					break;
				}
			}else if(num.contains(c)){
			//	System.out.println("arrival");
				int checkpoint=langenumber-rangenumber;
				if(checkpoint<Integer.valueOf(c)) {
					legal= false;
					break;
				}
			}
			word=word.substring(1);
		}
	
		
		return legal;
	}
	//check how many unmatched open brackets in a string
	public static int unmatched(String s){
		int unmatched=0;
		if(s.contains("<")){
			while(s.length()>0){
				if(s.charAt(0)=='<'){
					unmatched++;
				}if(s.charAt(0)=='>'){
					unmatched--;
				}
				s=s.substring(1);
			}
		}
		return unmatched;
	}
	public static int dealcatedNumber(String word) {
		int num=0;
		while(word.contains(">")) {
			num++;
			word=word.replaceFirst(">", "");
		}
		return num;
	}
	public static int alcatedNumber(String word) {
		int num=0;
		while(word.contains("<")) {
			num++;
			word=word.replaceFirst("<", "");
		}
		return num;
	}
	
	public static int maxLayer(String word){
		String w=word;
		int layer=0;
		while(w.contains("<")){
			layer++;
			String p="(<([a-zA-Z_0-9])*>)+";
			//Pattern regx = Pattern.compile(p);
			String[] r=w.split(p);
			w="";
			for(String s:r){
				w=w.concat(s);
			}
		}
		return layer;
	}

	public static String convertforLatex(String s){
		String stringforLatex="$"+s+"$";
		if(stringforLatex.contains("<")){

			stringforLatex=stringforLatex.replaceAll("<", "\\\\langle ");
		}
		if(stringforLatex.contains(">")){
			
			stringforLatex=stringforLatex.replaceAll(">", " \\\\rangle ");
		}
		if(stringforLatex.contains("*")){
			
			stringforLatex=stringforLatex.replaceAll("[*]", "^*");
		}
		return stringforLatex;
			
	}

}
