package automata;

import java.util.HashSet;
import java.util.Set;
//import java.util.Stack;

/**
 * The whole NFA diagram as opposed to a node in the graph.
 * State set can be implied by edges; start states and final states
 * are stored as S, and F respectively; transition function is specified by
 * edges; the alphabet is the same set of the type char.
 */
public abstract class AbstractNFA {
  private Set<Integer> S, F; //represent start and final states
  private Set<Edge> edges; //a directed graph

  public AbstractNFA() {
    S = new HashSet<Integer>();
    F = new HashSet<Integer>();
    edges = new HashSet<Edge>();
  }

  /**
   * A constructor which given a regular expression
   * makes an NFA for it
   * @param regex
   */
  public AbstractNFA(String regex) {
    AbstractNFA abstractNFA = mkNFAFromRegEx(regex);
    setStartStates(abstractNFA.getStartStates());
    setFinalStates(abstractNFA.getFinalStates());
    setEdges(abstractNFA.getEdges());
  }

  public void setStartStates(Set<Integer> S) {
    this.S = S;
  }

  public void setFinalStates(Set<Integer> F) {
    this.F = F;
  }

  public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

  public Set<Integer> getStartStates() {
    return new HashSet<Integer>(S);
  }

  public Set<Integer> getFinalStates() {
    return new HashSet<Integer>(F);
  }

  public Set<Edge> getEdges() {
    return new HashSet<Edge>(edges);
  }

  /**
   * given a string input
   * says whether or not it is accepted
   * @param input
   * @return
   */
  public boolean accept(String input) {
    //use P to represent the intermediate states we have seen
    // obviously we begin with start state
    Set<Integer> P = new HashSet<Integer>(S);
    Set<Edge> E = new HashSet<Edge>();
    int i = 0;
    for (i = 0; i < input.length(); i++) {
    	
    	P = addEpsilon(P);
    	E = new HashSet<Edge>();
    	
    	for (Edge e: edges)
    		if (P.contains(e.getSrc()) && (e.getLabels().match(input.charAt(i)) ||
    				e.getLabels().match(Labels.anyChar)))
    			E.add(e);
    	P = new HashSet<Integer>();
    	for (Edge e: E)
    		P.add(e.getDst());
    	
    	if (P.isEmpty()) return false;
    }
    //now do some kind of graph traversal to see if this input can be accepted
    for (int j: F)
    	if (P.contains(j))
    		return true;
    return false;
  }
  
  private Set<Integer> addEpsilon(Set<Integer> P) {
	  // adds all the states that can go via epsilon transition
	  Set<Integer> temp = P;
	  int pSize = -1;
	  while (pSize != P.size()) {
		  pSize = P.size();
	
		  for (Edge e: edges) {
			  if (P.contains(e.getSrc()) && e.getLabels().match(Labels.epsilon))
				  P.add(e.getDst());
		  }
	  }
	  return temp;
  }

  // following methods need to be implement in a child class
  protected abstract AbstractNFA mkNFAFromRegEx(String regex);

  /**
   * makes an NFA accepting one digit
   * @return
   */
  protected abstract AbstractNFA mkNFAOfDigit();

  /**
   * makes an NFA accepting one alphanumeric character
   * @return
   */
  protected abstract AbstractNFA mkNFAOfAlphaNum();


  /**
   * makes an NFA accepting one white character
   * @return
   */
  protected abstract AbstractNFA mkNFAOfWhite();



  /**
   * makes an NFA accepting one character of any kind
   * @return
   */
  protected abstract AbstractNFA mkNFAOfAnyChar();

  /**
   * makes an NFA accepting the given character
   * @param c
   * @return
   */
  protected abstract AbstractNFA mkNFAOfChar(char c);

  /**
   * makes an NFA union construction of the given two
   * @param nfa1
   * @param nfa2
   * @return
   */
  protected abstract AbstractNFA unionOf(AbstractNFA nfa1, AbstractNFA nfa2);

  /**
   * makes an NFA concatenation construction of the given two
   * @param nfa1
   * @param nfa2
   * @return
   */
  protected abstract AbstractNFA concatOf(AbstractNFA nfa1, AbstractNFA nfa2);

  /**
   * makes an NFA kleene star construction of the given one
   * @param nfa
   * @return
   */
  protected abstract AbstractNFA starOf(AbstractNFA nfa);

  /**
   * makes an NFA accepting what the given one accepts at least once
   * @param nfa
   * @return
   */
  protected abstract AbstractNFA plusOf(AbstractNFA nfa);

  /**
   * makes an NFA accepting what the given one accepts at most once
   * @param nfa
   * @return
   */
  protected abstract AbstractNFA maxOnceOf(AbstractNFA nfa);
}
