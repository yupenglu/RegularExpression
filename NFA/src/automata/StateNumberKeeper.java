package automata;

/**
 * A class always issues unique state number
 * CAUTION: if the length of regex is too long,
 * the uniqueness of state number cannot be guaranteed.
 */
public class StateNumberKeeper {
  private static int currentStateNumber = 0;

  public static int getNewStateNumber() {
    currentStateNumber = currentStateNumber + 1;
    return currentStateNumber;
  }
}