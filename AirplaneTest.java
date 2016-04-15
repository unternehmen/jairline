import java.util.Scanner;

public class AirplaneTest
{
  private static void listCommands () {
    System.out.println ("1 - Add Passenger");
    System.out.println ("2 - Show Seating");
    System.out.println ("3 - Exit");
  }
  
  public AirplaneTest () {
    String line;
    Scanner scanner;
    Airplane airplane;
    boolean done;

    scanner = new Scanner (System.in);
    airplane = new Airplane (scanner);

    System.out.println ("Jairline Seat Editor 0.1");
    System.out.println ("Select a command...");
    listCommands ();
    
    done = false;
    while (!done) {
      System.out.print ("Command: ");
      line = scanner.nextLine ();
      
      if (line.equals ("1")) {
        airplane.addPassenger ();
        System.out.println ("Select a command...");
        listCommands ();
      } else if (line.equals ("2")) {
        airplane.displaySeating ();
        System.out.println ("Select a command...");
        listCommands ();
      } else if (line.equals ("3")) {
        done = true;
      } else {
        System.out.println ("Unrecognized command '" + line + "'");
        System.out.println ("Commands available...");
        listCommands ();
      }
    }
  }
}
