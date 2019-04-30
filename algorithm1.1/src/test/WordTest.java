package test;

import static org.junit.Assert.*;

import org.junit.Test;

import word.Word;

public class WordTest {

	@Test
	public void testmaxlayer() {
		String s="a<>a<<1>>B";
		int i=Word.maxLayer(s);
		System.out.println(i);
	}
	@Test
	public void testunmatched() {
		String s="a<1111>a";
		int i=Word.unmatched(s);
		System.out.println(i);
	}
	@Test
	public void testlegal() {
		String s="a<1111>a<";
		
		System.out.println(Word.isLegal(s));
	}
	@Test
	public void testtolatex() {
		String s="a<1111>a*";
		
		System.out.println(Word.convertforLatex(s));
	}
}
