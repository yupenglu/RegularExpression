package automata;

import java.util.HashSet;
import java.util.Set;

public class Labels {
  // epsilon is the one in epsilon transitions
  public static final int epsilon = 0;
  public static final int anyChar = -1;

  // Use integer to store the character internally
  // so that special characters can be addressed elegantly.
  public Set<Integer> labels;

  public Labels() {
    labels = new HashSet<Integer>();
  }

  public Labels(char... chars) {
    labels = new HashSet<Integer>();
    add(chars);
  }

  public Labels(int... ints) {
    labels = new HashSet<Integer>();
    add(ints);
  }

  /**
   * Add characters to labels, ignoring the duplicates
   * @param chars
   * @return true
   */
  public boolean add(char... chars) {
    for (int i = 0; i < chars.length; i++) {
      labels.add(charToInt(chars[i]));
    }
    return true;
  }

  /**
   * Add integers to labels, ignoring the duplicates
   * @param ints
   * @return true
   */
  public boolean add(int... ints) {
    for (int i = 0; i < ints.length; i++) {
      labels.add(ints[i]);
    }
    return true;
  }

  /**
   * Add labels from a character to another character, inclusively
   * @param from
   * @param to
   * @return true
   */
  public boolean addFromTo(char from, char to) {
    if (from <= to) {
      for (int i = charToInt(from); i <= charToInt(to); i++) {
        labels.add(i);
      }
    } else {
      for (int i = charToInt(from); i >= charToInt(to); i--) {
        labels.add(i);
      }
    }
    return true;
  }

  /**
   * Add labels from an int to another int, inclusively
   * @param from
   * @param to
   * @return true
   */
  public boolean addFromTo(int from, int to) {
    if (from <= to) {
      for (int i = from; i <= to; i++) {
        labels.add(i);
      }
    } else {
      for (int i = from; i >= to; i--) {
        labels.add(i);
      }
    }
    return true;
  }

  public boolean match(char c) {
    // If it is a label, containing such is true;
    return labels.contains(charToInt(c));
  }

  public boolean match(int i) {
    // If it is a label, containing such is true;
    return labels.contains(i);
  }

  // Char is a 16-bit unicode character with minimum value of 0
  // while int is a 32-bit signed two's complement integer.
  // Thus the coercion is legit.
  private int charToInt(char c) {
    return (int)c;
  }
}
