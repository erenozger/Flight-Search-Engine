import java.io.*;

public class Main {
    public static String airportListFile;
    public static String flightListFile;
    public static String commandListFile;

    public static void main(String[] args) throws FileNotFoundException {
        airportListFile = args[0];      //contains airport list
        flightListFile = args[1];       //contains flight list
        commandListFile = args[2];      //contains commands query

        PrintStream o = new PrintStream(new File("output.txt"));
        System.setOut(o);               // Prints to output file

        Airport.setAllAirports(Reader.readFile(airportListFile));
        Airport.printAllAirports();











    }
}


