package automata;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NFATest {
	String input;
	AbstractNFA nfa;
	AbstractNFA nfa1;
	AbstractNFA nfa2;
	NFA nfa3;
	@Test
	public void testMkNFAFromRegEx() {
		input = "a*b&c|";
		nfa = new NFA(input);
		//assertTrue(nfa.accept("ab"));
		assertTrue(nfa.accept("c"));
		assertTrue(!nfa.accept("1b"));
		assertTrue(!nfa.accept("aaaabc"));
	}

	@Test
	public void testMkNFAOfDigit() {
		nfa = new NFA();
		nfa = nfa.mkNFAOfDigit();
		assertTrue(nfa.accept("0"));
		assertTrue(nfa.accept("1"));
		assertTrue(nfa.accept("2"));
		assertTrue(nfa.accept("3"));
		assertTrue(nfa.accept("4"));
		assertTrue(nfa.accept("5"));
		assertTrue(nfa.accept("6"));
		assertTrue(nfa.accept("7"));
		assertTrue(nfa.accept("8"));
		assertTrue(nfa.accept("9"));
		assertTrue(!nfa.accept("10"));
		assertTrue(!nfa.accept("ab"));
		assertTrue(!nfa.accept("10234"));
	}

	@Test
	public void testMkNFAOfAlphaNum() {
		nfa = new NFA();
		nfa = nfa.mkNFAOfAlphaNum();
		assertTrue(nfa.accept("0"));
		assertTrue(nfa.accept("a"));
		assertTrue(nfa.accept("A"));
		assertTrue(nfa.accept("b"));
		assertTrue(nfa.accept("C"));
		assertTrue(nfa.accept("d"));
		assertTrue(nfa.accept("6"));
		assertTrue(!nfa.accept("10"));
		assertTrue(!nfa.accept("ab"));
		assertTrue(!nfa.accept("10234"));
	}

	@Test
	public void testMkNFAOfWhite() {
		nfa = new NFA();
		nfa = nfa.mkNFAOfWhite();
		assertTrue(nfa.accept("	"));//v tab
		assertTrue(nfa.accept(" "));//whitespace
		assertTrue(nfa.accept("\n"));
		assertTrue(nfa.accept("\t"));//\v
		assertTrue(nfa.accept("\f"));//\f
		assertTrue(nfa.accept("\r"));//\r
		assertTrue(!nfa.accept("%"));//tab
	}

	@Test
	public void testMkNFAOfAnyChar() {
		nfa = new NFA();
		nfa = nfa.mkNFAOfAnyChar();
		assertTrue(nfa.accept("a"));
		assertTrue(nfa.accept("b"));
		assertTrue(nfa.accept("0"));
		assertTrue(nfa.accept(" "));
		assertTrue(nfa.accept("A"));
		assertTrue(nfa.accept("\r"));
		assertTrue(nfa.accept("%"));
	}

	@Test
	public void testMkNFAOfChar() {
		nfa = new NFA();
		nfa = nfa.mkNFAOfChar('a');
		assertTrue(nfa.accept("a"));
		nfa = nfa.mkNFAOfChar('%');
		assertTrue(nfa.accept("%"));
		assertTrue(!nfa.accept("b"));
	}

	@Test
	public void testUnionOf() {
		nfa = new NFA();
		nfa1 = new NFA();
		nfa1 = nfa1.mkNFAOfChar('a');
		nfa2 = new NFA();
		nfa2 = nfa2.mkNFAOfChar('b');
		nfa = nfa.unionOf(nfa1, nfa2);
		assertTrue(nfa.accept("a"));
		assertTrue(nfa.accept("b"));
		assertTrue(!nfa.accept("ab"));
	}

	@Test
	public void testConcatOf() {
		nfa = new NFA();
		nfa1 = new NFA();
		nfa1 = nfa1.mkNFAOfChar('a');
		nfa2 = new NFA();
		nfa2 = nfa2.mkNFAOfChar('b');
		nfa = nfa.concatOf(nfa1, nfa2);
		assertTrue(!nfa.accept("a"));
		assertTrue(!nfa.accept("b"));
		assertTrue(!nfa.accept("$"));
		assertTrue(nfa.accept("ba"));
		//assertTrue(nfa.accept("a$b"));
	}

	@Test
	public void testStarOf() {
		nfa = new NFA();
		nfa1 = new NFA();
		nfa1 = nfa1.mkNFAOfChar('a');
		nfa = nfa.starOf(nfa1);
		assertTrue(nfa.accept("aaaaaaa"));
		assertTrue(nfa.accept("$"));
		//assertTrue(nfa.accept());
	}

	@Test
	public void testPlusOf() {
		nfa = new NFA();
		nfa1 = new NFA();
		nfa1 = nfa1.mkNFAOfChar('a');
		nfa = nfa.plusOf(nfa1);
		assertTrue(nfa.accept("a"));
		assertTrue(nfa.accept("aaaaaaa"));		
	}

	@Test
	public void testMaxOnceOf() {
		nfa = new NFA();
		nfa1 = new NFA();
		nfa1 = nfa1.mkNFAOfChar('a');
		nfa = nfa.maxOnceOf(nfa1);
		assertTrue(nfa.accept("a"));
		assertTrue(!nfa.accept("aaaaaaa"));	
	}

	@Test
	public void testMkNFAOfTab() {
		AbstractNFA nfa3 = new NFA();
		nfa3 = ((NFA) nfa3).mkNFAOfTab();
		assertTrue(!nfa3.accept(" "));
		assertTrue(nfa3.accept("\t"));
		assertTrue(nfa3.accept("	"));// v tab
	}

}
