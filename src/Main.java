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

        Airport.setAllAirports(Reader.readFile(airportListFile));
//        Airport.printAllAirports();

        Edge.setAllEdges(Reader.readFile(flightListFile));
//        Edge.printAllEdges();

        Graph gr = new Graph();
        gr.createGraph(Airport.airports,Edge.edges);

        Graph.printAdjMatrix(Graph.graphAdjList,Airport.airports.size(),Graph.airportsMap);

        Operations.readCommands(Reader.readFile(commandListFile));

    }

    public static Date stringToDate(String date) throws ParseException {
        String pattern = "dd/MM/yyyy HH:mm EEE";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date dateFormatted = simpleDateFormat.parse(date);
        return dateFormatted;
    }

    public static String dateToString(Date date) {
        String pattern = "dd/MM/yyyy HH:mm EEE";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String stringDate = simpleDateFormat.format(date);
        return stringDate;
    }


}


