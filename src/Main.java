import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static String airportListFile;
    public static String flightListFile;
    public static String commandListFile;

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        airportListFile = args[0];      //contains airport list
        flightListFile = args[1];       //contains flight list
        commandListFile = args[2];      //contains commands query

        PrintStream o = new PrintStream(new File("output.txt"));
        System.setOut(o);               // Prints to output file

        Airport.setAllAirports(Reader.readFile(airportListFile)); //Saves the entire airport file to the system.

        Edge.setAllEdges(Reader.readFile(flightListFile)); //Saves the entire flight list file to the system.

        Graph gr = new Graph();
        gr.createGraph(Airport.airports,Edge.edges.getList());
        //Creates a new graph and applies the adj matrix as read from the files.

        Operations.readCommands(Reader.readFile(commandListFile));
        //Line by line commands are applied on the graph saved in the system. It calculates the required operations inside the Operations folder.


    }

    public static Date stringToDate(String date) throws ParseException {
        String pattern = "dd/MM/yyyy HH:mm EEE";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date dateFormatted = simpleDateFormat.parse(date);
        return dateFormatted;
        //Converts the dates defined as string to date format.
    }

    public static String dateToString(Date date) {
        String pattern = "dd/MM/yyyy HH:mm EEE";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String stringDate = simpleDateFormat.format(date);
        return stringDate;
        //Converts date format to string to print it to the screen as a string.
    }


}


