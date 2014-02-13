package automata;

import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.PatternSyntaxException;
//import org.apache.commons.lang3.*;


public class NFA extends AbstractNFA {

    public NFA() {
        super();
    }

    public NFA(String regex) {
        super(regex);
    }

    @Override
    protected AbstractNFA mkNFAFromRegEx(String regex) {
        Stack<AbstractNFA> nfaStack = new Stack<AbstractNFA>();
        char c;
        int pos = 0;
        int stackSize = 0;
        while (pos < regex.length()) {
        	c = regex.charAt(pos);
        	
        	if (!this.isMetaChar(c))
        	    nfaStack.push(mkNFAOfChar(c));
        	else if (c == '*')
        		nfaStack.push(starOf(nfaStack.pop()));
        	else if (c == '+')
        		nfaStack.push(plusOf(nfaStack.pop()));
        	else if (c == '?')
        		nfaStack.push(maxOnceOf(nfaStack.pop()));
        	else if (c == '.')
        		nfaStack.push(mkNFAOfAnyChar());
        	else if (c == '|') {
        		if (nfaStack.size() < 2) throw new PatternSyntaxException("You have misused an operator!", regex, stackSize);
        		nfaStack.push(unionOf(nfaStack.pop(), nfaStack.pop()));
        	}
        	else if (c == '&') {
        		if (nfaStack.size() < 2) throw new PatternSyntaxException("You have misused an operator!", regex, stackSize);
        		nfaStack.push(concatOf(nfaStack.pop(), nfaStack.pop()));
        	}
        	else if (c == '\\') {
        		pos++;
        		c = regex.charAt(pos);
        		if (c == 'd')
        			nfaStack.push(mkNFAOfDigit());
        		else if (c == 'w')
        			nfaStack.push(mkNFAOfAlphaNum());
        		else if (c == 's')
        			nfaStack.push(mkNFAOfWhite());
        		else if (c == 't') {
        			nfaStack.push(mkNFAOfTab());
        		} else if (c == '|' || c == '&' || c == '*' || c == '?' || c == '+' || c == '.')
        			nfaStack.push(mkNFAOfChar(c));
        		else throw new IllegalArgumentException("\\ followed by unknown parameter.");
        	} else throw new IllegalArgumentException("unrecognized character.");
        	
        	stackSize = nfaStack.size();
        	pos++;
        }

        if (nfaStack.size() != 1){
        	System.out.println(nfaStack.size());
            throw new IllegalArgumentException("illegal regular expression: "
                    + "the number of operators do not match"
                    + " the number of tokens.");
        }
        
        return nfaStack.pop();
    }
    
    @Override
    protected AbstractNFA mkNFAOfDigit() {
    	AbstractNFA tempNFA = new NFA();
    	int a = StateNumberKeeper.getNewStateNumber();
    	int b = StateNumberKeeper.getNewStateNumber();
    	
    	tempNFA.setStartStates(mkStates(a));
    	tempNFA.setFinalStates(mkStates(b));
    	
    	Labels tempLabels = new Labels();
    	tempLabels.addFromTo('0', '9');
    	tempNFA.setEdges(mkEdges(new Edge(a, b, tempLabels)));
        return tempNFA;
    }

    @Override
    protected AbstractNFA mkNFAOfAlphaNum() {
    	AbstractNFA tempNFA = new NFA();
    	int a = StateNumberKeeper.getNewStateNumber();
    	int b = StateNumberKeeper.getNewStateNumber();
    	
    	tempNFA.setStartStates(mkStates(a));
    	tempNFA.setFinalStates(mkStates(b));
    	
    	Labels tempLabels = new Labels();
    	tempLabels.addFromTo('a', 'z');
    	tempLabels.addFromTo('A', 'Z');
    	tempLabels.addFromTo('0', '9');
    	tempNFA.setEdges(mkEdges(new Edge(a, b, tempLabels)));
        return tempNFA;
    }
    
    @Override
    protected AbstractNFA mkNFAOfWhite() {
    	AbstractNFA tempNFA = new NFA();
    	int a = StateNumberKeeper.getNewStateNumber();
    	int b = StateNumberKeeper.getNewStateNumber();
    	
    	tempNFA.setStartStates(mkStates(a));
    	tempNFA.setFinalStates(mkStates(b));
    	
    	Labels tempLabels = new Labels();
    	tempLabels.add(32);
    	tempLabels.addFromTo(9, 13);
    	tempNFA.setEdges(mkEdges(new Edge(a, b, tempLabels)));
        return tempNFA;
    }
    
    protected AbstractNFA mkNFAOfTab() {
    	AbstractNFA tempNFA = new NFA();
    	int a = StateNumberKeeper.getNewStateNumber();
    	int b = StateNumberKeeper.getNewStateNumber();
    	
    	tempNFA.setStartStates(mkStates(a));
    	tempNFA.setFinalStates(mkStates(b));
    	
    	Labels tempLabels = new Labels();
    	tempLabels.add(9);
    	tempLabels.add(11);
    	tempNFA.setEdges(mkEdges(new Edge(a, b, tempLabels)));
        return tempNFA;
    }

    @Override
    protected AbstractNFA mkNFAOfAnyChar() {
    	AbstractNFA tempNFA = new NFA();
    	int a = StateNumberKeeper.getNewStateNumber();
    	int b = StateNumberKeeper.getNewStateNumber();
    	
    	tempNFA.setStartStates(mkStates(a));
    	tempNFA.setFinalStates(mkStates(b));
    	
    	Labels tempLabels = new Labels();
    	tempLabels.add(Labels.anyChar);
    	tempNFA.setEdges(mkEdges(new Edge(a, b, tempLabels)));
        return tempNFA;
    }

    @Override
    protected AbstractNFA mkNFAOfChar(char c) {
    	AbstractNFA tempNFA = new NFA();
    	int a = StateNumberKeeper.getNewStateNumber();
    	int b = StateNumberKeeper.getNewStateNumber();
    	
    	tempNFA.setStartStates(mkStates(a));
    	tempNFA.setFinalStates(mkStates(b));
    	tempNFA.setEdges(mkEdges(new Edge(a, b, new Labels(c))));
        return tempNFA;
    }

    @Override
    protected AbstractNFA unionOf(AbstractNFA nfa1, AbstractNFA nfa2) {
    	int tempLabel = StateNumberKeeper.getNewStateNumber();
    	AbstractNFA tempNFA = new NFA();
    	Set<Edge> tempEdge = new HashSet<Edge>();
    	tempEdge.addAll(nfa1.getEdges());
    	tempEdge.addAll(nfa2.getEdges());
    	for (int i: nfa1.getStartStates())
    		tempEdge.add(new Edge(tempLabel, i, new Labels(Labels.epsilon)));
    	for (int j: nfa2.getStartStates())
    		tempEdge.add(new Edge(tempLabel, j, new Labels(Labels.epsilon)));
    	tempNFA.setEdges(tempEdge);
    	tempNFA.setStartStates(mkStates(tempLabel));
    	
    	Set<Integer> tempFinalStates = new HashSet<Integer>();
    	// set final states
    	tempFinalStates.addAll(nfa1.getFinalStates());
    	tempFinalStates.addAll(nfa2.getFinalStates());
    	tempNFA.setFinalStates(tempFinalStates);
    	
        return tempNFA;
    }

    @Override
    protected AbstractNFA concatOf(AbstractNFA nfa1, AbstractNFA nfa2) {
    	// result should be nfa2-e-nfa1
    	AbstractNFA tempNFA = nfa2;
    	for (int i: nfa2.getFinalStates()) {
    		for (int j: nfa1.getStartStates()) {
    			Set<Edge> temp = nfa2.getEdges();
    			temp.addAll((mkEdges(new Edge(i, j, new Labels(Labels.epsilon)))));
    			temp.addAll(nfa1.getEdges());
    			tempNFA.setEdges(temp);
    		}
    	}
    	tempNFA.setFinalStates(nfa1.getFinalStates());
    	
        return tempNFA;
    }

    @Override
    protected AbstractNFA starOf(AbstractNFA nfa) {
    	AbstractNFA tempNFA = nfa;
    	for (Integer i: nfa.getFinalStates()) {
    		for (Integer j: nfa.getStartStates()) {
    			Set<Edge> temp = nfa.getEdges();
    			temp.addAll((mkEdges(new Edge(i, j, new Labels(Labels.epsilon)))));
    			temp.addAll((mkEdges(new Edge(j, i, new Labels(Labels.epsilon)))));
    			tempNFA.setEdges(temp);
    		}
    	}
        return tempNFA;
    }

    @Override
    protected AbstractNFA plusOf(AbstractNFA nfa) {
    	AbstractNFA tempNFA = nfa;
    	for (Integer i: nfa.getFinalStates()) {
    		for (Integer j: nfa.getStartStates()) {
    			Set<Edge> temp = nfa.getEdges();
    			temp.addAll((mkEdges(new Edge(i, j, new Labels(Labels.epsilon)))));
    			tempNFA.setEdges(temp);
    		}
    	}
    	return tempNFA;
    }

    @Override
    protected AbstractNFA maxOnceOf(AbstractNFA nfa) {
    	AbstractNFA tempNFA = nfa;
    	for (Integer i: nfa.getFinalStates()) {
    		for (Integer j: nfa.getStartStates()) {
    			Set<Edge> temp = nfa.getEdges();
    			temp.addAll((mkEdges(new Edge(j, i, new Labels(Labels.epsilon)))));
    			tempNFA.setEdges(temp);
    		}
    	}
    	return tempNFA;
    }

    private boolean isMetaChar(char c) {
        return c == '\\' || c == '.' || c == '&' || c == '|' || c == '*'
                || c == '+' || c == '?';
    }

    private Set<Integer> mkStates(int... indices) {
        Set<Integer> states = new HashSet<Integer>();
        for (int i = 0; i < indices.length; i++) {
            states.add(indices[i]);
        }
        return states;
    }

    private Set<Edge> mkEdges(Edge... edges) {
        Set<Edge> edgeSet = new HashSet<Edge>();
        for (int i = 0; i < edges.length; i++) {
            edgeSet.add(edges[i]);
        }
        return edgeSet;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out
            .println("usage: NFA arg1 arg2\n"
                    + "arg1: the pattern to match\n"
                    + "arg2: the input string");
        } else {
            NFA nfa = new NFA(args[0]);
            System.out.println("arguments have been validated");
            String input = "";
            for (int i = 1; i < args.length; i++) {
            	input = input + " " + args[i];
            }
            input = input.substring(1);
            System.out.println(nfa.accept(input));
        }
        return;
    }
}
