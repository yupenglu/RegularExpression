package automata;

public class Label {
    char c;
    
    Label(char c){
        this.c = c;
    }
    
    boolean match(char c){
        return this.c == c;
    }
    /***
     * we choose to use '$' as our empty string symbol
     * @return
     */
    boolean epsilon(){
        return c=='$';
    }
}
