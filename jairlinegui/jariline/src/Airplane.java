import java.io.*;

/**
 * A class that allows us to construct an airplane with first and economy seating
 * and find a place for a person or group to sit based on preferences.
 *
 * @author Christopher Murphy
 * @author Brandon Duke
 * @author Claire Wallace
 * @version 4/23/16
 */

public class Airplane {
  private String firstClass[][];
  private String economyClass[][];
  private String returnMessage;
  private int seatRow, seatCol, numPassengers;
  private File fFile;
  private File eFile;

  /**
   * @throws IOException
   * @throws ClassNotFoundException
   * Both are thrown so we can read from a file
   *
   * If the files our arrays are stored in exist, then we use them,
   * otherwise we make new arrays.
   */
  public Airplane() throws IOException, ClassNotFoundException {
    fFile = new File("firstSeats.dat");
    eFile = new File("econSeats.dat");

    int i, j;

    if (fFile.exists() && eFile.exists()){
      FileInputStream firstFIS = new FileInputStream(fFile);
      ObjectInputStream firstOIS = new ObjectInputStream(firstFIS);
      FileInputStream econFIS = new FileInputStream(eFile);
      ObjectInputStream econOIS = new ObjectInputStream(econFIS);
      firstClass = (String[][])firstOIS.readObject();
      economyClass = (String[][])econOIS.readObject();
      firstOIS.close();
      econOIS.close();
    }
    else {
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
  }

  /**
   * @param classChoice the chosen type of class
   * @param passengers the number of passengers
   * @param position where people want to sit
   * We then use all of this data in our algorithm to find a seat, or range, that matches the conditions
   */
  public void addPassenger(int classChoice, int passengers, int position) {
    String chosenClass[][];
    int numOfTravelers, i, j, k, keyColumns[], matchRow, matchCol;
    boolean matched, hasKey;

    if (classChoice == 1) {
      chosenClass = firstClass;
    } else if (classChoice == 2) {
      chosenClass = economyClass;
    } else {
      returnMessage = "Improper choice.";
      return;
    }
    numPassengers = passengers;
    numOfTravelers = passengers;

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

    if (position == 1) {
      keyColumns[0] = 0;

      if (chosenClass == economyClass) {
        keyColumns[1] = 5;
      } 
      else {
        keyColumns[1] = 3;
      }
    } 
    else if (position == 2) {
      if (chosenClass == economyClass) {
        keyColumns[0] = 2;
        keyColumns[1] = 3;
      } 
      else {
        keyColumns[0] = 1;
        keyColumns[1] = 2;
      }
    } 
    else if (chosenClass == economyClass && position == 3) {
      keyColumns[0] = 1;
      keyColumns[1] = 4;
    } 
    else {
      returnMessage = "Improper choice.";
      return;
    }

    /* Then, we must find a contiguous block of seats which both
     * can fit every traveler into a single row and include at least one of
     * the "key seats". */

    if (chosenClass[0].length < numOfTravelers) {
      if (chosenClass == firstClass) {
        returnMessage = "Your group cannot fit into any one row in first class.";
      } else {
        returnMessage = "Your group cannot fit into any one row in economy class.";
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
          if (chosenClass[i][j + k].equals("")) {
            if (j + k == keyColumns[0] || j + k == keyColumns[1]) {
              hasKey = true;
            }
          } 
          else {
            matched = false;
            break;
          }
        }

        if (matched && hasKey) {
          matchRow = i;
          matchCol = j;
          seatRow = i;
          seatCol = j;
          break;
        }
      }

      if (matched && hasKey) {
        break;
      }
    }

    if (!matched || !hasKey) {
      returnMessage = "Could not find open seating for your group.";
      return;
    }

    /* Finally, mark each of the chosen seats as being taken. */

    for (k = 0; k < numOfTravelers; k++) {
      chosenClass[matchRow][matchCol + k] = "X";
    }

    returnMessage = "To see where you will be sitting, look at the seating chart.";
  }

  /**
   * @return Any messages because of adding a passenger, as well as, seating
   */
  public String returnMessageForSeating(){
      if(returnMessage.equals("To see where you will be sitting, look at the seating chart.") && numPassengers > 1)
          return returnMessage + " You will be sitting in Row: " + seatRow + " Seats: " + seatCol + " through " + (seatCol + numPassengers - 1);
      else if (returnMessage.equals("To see where you will be sitting, look at the seating chart."))
        return returnMessage + " You will be sitting in Row: " + seatRow + " Seat: " + seatCol;
      else
        return returnMessage;
  }

  /**
   * @param x row in seating
   * @param y position in row of seating
   * @return the value of that seat, taken or not
   */
  public String returnFirstSeating(int x, int y){
      return firstClass[x][y];
  }

  /**
   * @param x row in seating
   * @param y position in row of seating
   * @return the value of that seat, taken or not
     */
  public String returnEconomySeating(int x, int y){
      return economyClass[x][y];
  }

  /**
   * @throws IOException
   * @throws ClassNotFoundException
   * Both are thrown so we can write to a file
   *
   * We use this so we can save our arrays and use them when running again.
     */
  public void saveFile() throws IOException, ClassNotFoundException {
    FileOutputStream firstFOS = new FileOutputStream(fFile);
    ObjectOutputStream firstOOS = new ObjectOutputStream(firstFOS);
    FileOutputStream econFOS = new FileOutputStream(eFile);
    ObjectOutputStream econOOS = new ObjectOutputStream(econFOS);
    firstOOS.writeObject(firstClass);
    econOOS.writeObject(economyClass);
    firstOOS.close();
    econOOS.close();
  }
}