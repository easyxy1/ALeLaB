package test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import learningbase.Learner;
import learningbase.Teacher;
import learningbase.Teacher.Strategy;

public class Strategytest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> alphabet=new HashSet();
		alphabet.add("a");
		alphabet.add("b");
		String languagesource="a+b+<1>";
		Teacher teacher1=new Teacher(Teacher.FromRE,alphabet,Strategy.HORIZONTAL,languagesource);
		try {
			Learner exampleH=new Learner(teacher1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		System.out.println(languagesource+"is learned in HORIZONTAL:"+teacher1.getrecordofCE());
		
		Teacher teacher2=new Teacher(Teacher.FromRE,alphabet,Strategy.VERTICAL,languagesource);
		try {
			Learner exampleV=new Learner(teacher2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		System.out.println(languagesource+"is learned in VERTICAL:"+teacher2.getrecordofCE());
	}

}
