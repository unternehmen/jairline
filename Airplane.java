import java.util.Scanner;

public class Airplane
{
  private String firstClass[][];
  private String economyClass[][];
  private Scanner scanner;

  public Airplane (Scanner scanner) {
    int i, j;

    this.scanner = scanner;

    firstClass = new String[5][4];
    economyClass = new String[15][6];

    for (i = 0; i < firstClass.length; i++) {
      for (j = 0; j < firstClass[0].length; j++) {
        firstClass[i][j] = "";
      }
    }

    for (i = 0; i < economyClass.length; i++) {
      for (j = 0; j < economyClass[0].length; j++) {
        economyClass[i][j] = "";
      }
    }
  }

  public void addPassenger () {
    String line;
    String chosenClass[][];
    int numOfTravelers, i, j, k, keyColumns[], matchRow, matchCol;
    boolean matched, hasKey;

    System.out.println ("Choose which section of the plane you want.");
    System.out.println ("1 - First class");
    System.out.println ("      A purposefully roomy part of the plane which costs more");
    System.out.println ("      but is generally quieter.  Each row has four seats");
    System.out.println ("      divided in the middle by an aisle.");
    System.out.println ("2 - Economy class");
    System.out.println ("      A euphemistically named part of the plane which costs");
    System.out.println ("      less, has tighter seating, and is generally louder.");
    System.out.println ("      Each row has six seats, divided in the middle by an");
    System.out.println ("      aisle.");

    System.out.print ("COMMAND: ");
    line = scanner.nextLine ();
    if (line.equals ("1")) {
      chosenClass = firstClass;
      System.out.println ("First class chosen.");
    } else if (line.equals ("2")) {
      chosenClass = economyClass;
      System.out.println ("Economy class chosen.");
    } else {
      System.out.println ("Improper choice.  Returning to main menu.");
      return;
    }

    System.out.println ("How many people are traveling with you?");
    numOfTravelers = Integer.parseInt (scanner.nextLine ());

    System.out.println ("Choose the preferred placement of your group.");
    System.out.println ("1 - Window");
    System.out.println ("2 - Aisle");
    if (chosenClass == economyClass) {
      System.out.println ("3 - Middle");
    }

    /* The way this seating algorithm works is it makes sure all of the
     * travelers can sit in the same row, together, and that at least one
     * of them occupies the requested position (window, aisle, or middle).
     * If those two criteria cannot be met, the function returns without
     * changing the state of the airplane.  This is a really dirty
     * algorithm though, and it doesn't support, e.g., splitting the
     * group into two. */

    /* First, determine which seating arrangement is required by the
     * client.  There are only ever two seats that qualify as either
     * aisle, window, or middle, so the keyColumns[] array which will
     * contain the necessary seat positions has only two items in it.
     * Seats in a row are indexed from 0, so that seat 0 is the leftmost
     * window seat and seat 3 is (in first class) the rightmost window
     * seat. */

    keyColumns = new int[2];

    System.out.print ("COMMAND: ");
    line = scanner.nextLine ();
    if (line.equals ("1")) {
      keyColumns[0] = 0;

      if (chosenClass == economyClass) {
        keyColumns[1] = 5;
      } else {
        keyColumns[1] = 3;
      }
    } else if (line.equals ("2")) {
      if (chosenClass == economyClass) {
        keyColumns[0] = 2;
        keyColumns[1] = 3;
      } else {
        keyColumns[0] = 1;
        keyColumns[1] = 2;
      }
    } else if (chosenClass == economyClass && line.equals ("3")) {
      keyColumns[0] = 1;
      keyColumns[2] = 4;
    } else {
      System.out.println ("Improper choice.  Returning to main menu.");
      return;
    }

    /* Then, we must find a contiguous block of seats which both
     * can fit every traveler into a single row and include at least one of
     * the "key seats". */

    if (chosenClass[0].length < numOfTravelers) {
      if (chosenClass == firstClass) {
        System.out.println ("Your group cannot fit into any one row in first class.");
      } else {
        System.out.println ("Your group cannot fit into any one row in economy class.");
      }

      return;
    }

    matched = false;
    hasKey = false;
    matchRow = 0;
    matchCol = 0;
    for (i = 0; i < chosenClass.length; i++) {
      for (j = 0; j < chosenClass[0].length - numOfTravelers + 1; j++) {
        matched = true;
        hasKey = false;

        for (k = 0; k < numOfTravelers; k++) {
          if (chosenClass[i][j + k].equals ("")) {
            if (j + k == keyColumns[0] || j + k == keyColumns[1]) {
              hasKey = true;
            }
          } else {
            matched = false;
            break;
          }
        }

        if (matched && hasKey) {
          matchRow = i;
          matchCol = j;
          break;
        }
      }

      if (matched && hasKey) {
        break;
      }
    }

    if (!matched || !hasKey) {
      System.out.println ("Could not find open seating for your group.");
      return;
    }

    /* Finally, mark each of the chosen seats as being taken. */

    for (k = 0; k < numOfTravelers; k++) {
      chosenClass[matchRow][matchCol + k] = "X";
    }

    System.out.println ("To see where you will be sitting, look at the seating chart.");
  }

  public void displaySeating () {
    int i, j;

    /* Print first class seats. */
    for (i = 0; i < firstClass.length; i++) {
      for (j = 0; j < firstClass[0].length; j++) {
        if (firstClass[i][j].equals ("")) {
          System.out.print ("[ ]");
        } else {
          System.out.print ("[X]");
        }
      }
      System.out.println ();
    }

    for (i = 0; i < economyClass.length; i++) {
      for (j = 0; j < economyClass[0].length; j++) {
        if (economyClass[i][j].equals ("")) {
          System.out.print ("[ ]");
        } else {
          System.out.print ("[X]");
        }
      }
      System.out.println ();
    }

    /* Print second class seats. */
  }
}
