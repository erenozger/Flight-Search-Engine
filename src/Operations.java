import java.text.ParseException;
import java.util.*;

public class Operations {
    public static void readCommands(ArrayList<String> commandList) throws ParseException {
        for (int i = 0; i < commandList.size(); i++) {
            String[] splitList = commandList.get(i).split("\t");
            String source = null;
            String desc = null;
            String date = null;
            if (splitList.length > 1) {
                String[] splitAirports = splitList[1].split("->");
                source = splitAirports[0];
                desc = splitAirports[1];
                date = splitList[2];
            }
            System.out.println("command : " + commandList.get(i));
            switch (splitList[0]) {
                case "listAll":
                    listAll(source, desc, date);
                    break;
                case "listProper":
                    listProper(Flight.flights);
                    break;
                case "listCheapest":
                    listCheapest();
                    break;
                case "listQuickest":
                    listQuickest();
                    break;
                case "listCheaper":
                    listCheaper(Integer.parseInt(splitList[3]));
                    break;
                case "listQuicker":
                    listQuicker(splitList[3]);
                    break;
                case "listExcluding":
                    listExcluding(splitList[3]);
                    break;
                case "listOnlyFrom":
                    listOnlyFrom(splitList[3]);
                    break;
                case "diameterOfGraph":
                    diameterOfGraph();
                    break;
                case "pageRankOfNodes":
                    pageRankOfNodes();
                    break;
                default:
                    System.out.println("Undefined Command!!");
                    break;
            }
            if (i != commandList.size() - 1) {
                System.out.println("\n");
            }
        }
    }

    public static void listAll(String source, String dest, String date) {
        ArrayList<List<String>> flightsList = findAllPossibleFlights(source, dest, date);
        Flight.setAllFLights(flightsList);
        //All flights between 2 targets are detected and recorded in the system.
        for (int i = 0; i < Flight.flights.size(); i++) {
            System.out.println(Flight.flights.get(i));
        }
    }

    public static ArrayList<List<String>> findAllPossibleFlights(String source, String dest, String date) {
        ArrayList<Airport> sourceAirports = Airport.getAirportsFromCity(source);
        ArrayList<Airport> destAirports = Airport.getAirportsFromCity(dest);
        //Since I store airports to the system with alias attributes, I have to make a city limitation.
        //For this, firstly, when an intercity flight is searched, all airports in those cities are determined and the flights are calculated respectively.

        ArrayList<List<String>> allPossibleFlights = new ArrayList<>();
        for (int i = 0; i < sourceAirports.size(); i++) {
            for (int j = 0; j < destAirports.size(); j++) {


                ArrayList<String> pathList = new ArrayList<>();
                HashSet<String> paths = new HashSet<String>();
                //Whenever a result is found, it is saved in the Paths list and the function is completed.
                //The other ongoing recursive function searches for new shortest paths and saves them in the paths list each time it finds them.

                boolean[] isVisited = new boolean[Graph.airportsMap.size()];
                int sourceAirportMapId = Graph.getKeyValueFromAdjList(sourceAirports.get(i).getAlias());
                int destAirportMapId = Graph.getKeyValueFromAdjList(destAirports.get(j).getAlias());
                pathList.add(Graph.airportsMap.get(sourceAirportMapId).getAlias());
                findAllFlightPaths(sourceAirportMapId, destAirportMapId, isVisited, pathList, paths, null, false);
                List<String> list = new ArrayList<String>(paths);
                allPossibleFlights.add(list);
            }
        }
        return allPossibleFlights;
    }

    public static void findAllFlightPaths(int source, int desc, boolean[] isVisited, ArrayList<String> pathList,
                                          HashSet<String> paths, Edge lastEdge, boolean isShortestPath) {

        //I keep a visited array for cycle detection, and in this way, there is no repeat flight to the same airport.
        //Flights between different airports in the same city are detected not by this array, but by the compareTwoFlightsCity function.
        isVisited[source] = true;
        if (source == desc) {
            String listString = String.join(",", pathList);
            //Shortest path detected and function completed. Pathlist completed and added to all paths list.
            paths.add(listString);
            isVisited[source] = false;
            return;
        }
        for (int destCount = 0; destCount < Graph.graphAdjList[source].length; destCount++) {
            ArrayList<Edge> edgeList = Graph.graphAdjList[source][destCount].getList();
            if (edgeList != null) {
                for (Edge i : edgeList) {
                    int nextAirportId;
                    if (i == null) {
                        nextAirportId = 0;
                    } else {
                        Airport j = i.getArrival_airport();
                        nextAirportId = Graph.getKeyValueFromAdjList(j.getAlias());
                    }
                    //While the recursive function continues, I can detect all possible flights with the control here.
                    //IsVisited [nextAirportId] blocks if a flight to that city has taken place
                    //With the compareTwoFlightsDate function, the departure time of the next flight is compared with the arrival time of the previous flight, and if this is the case, the function continues.
                    //compareTwoFlightsCity function blocks flights in the same city. If the next flight takes place to the same city, the function will not continue and will complete without saving in paths.
                    if (!isVisited[nextAirportId] && i != null && compareTwoFlightsDate(lastEdge, i) && compareTwoFlightsCity(pathList, i)) {
                        Edge nextEdge = i;
                        pathList.add(isShortestPath ? String.valueOf(nextEdge.getPrice()) : nextEdge.getFlight_id());
                        pathList.add(Graph.airportsMap.get(nextAirportId).getAlias());
                        findAllFlightPaths(nextAirportId, desc, isVisited, pathList, paths, nextEdge, isShortestPath);
                        pathList.remove(Graph.airportsMap.get(nextAirportId).getAlias());
                        int x = pathList.size();
                        pathList.remove(x - 1);
                    }
                }
                isVisited[source] = false;
            }
        }
        isVisited[source] = false;
    }

    public static boolean compareTwoFlightsDate(Edge edgeName, Edge nextEdge) {
        if (edgeName == null) {
            return true;
        } else {
            if (edgeName.getArrival_date().compareTo(nextEdge.getDept_date()) == -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean compareTwoFlightsCity(ArrayList<String> pathList, Edge nextEdge) {
        for (int i = 0; i < pathList.size(); i = i + 2) {
            String edgeCity = Airport.getAirportFromAliasName(pathList.get(i)).getCity();
            String newEdgeCity = Airport.getAirportFromAliasName(nextEdge.getArrival_airport().getAlias()).getCity();
            if (edgeCity.equals(newEdgeCity)) {
                return false;
            }
        }
        return true;
    }

    public static void listProper(ArrayList<Flight> flights) throws ParseException {
        if (flights.size() == 0) {
            System.out.println("No suitable flight plan is found");
        } else {
            ArrayList<Flight> properList = new ArrayList<>();
            properList.add(flights.get(0));
            for (int i = 1; i < flights.size(); i++) {
                int size = properList.size();
                for (int j = size - 1; j >= 0; j--) {
                    int priceCompare = properList.get(j).comparePrice(flights.get(i));
                    int timeCompare = properList.get(j).compareFlightTime(flights.get(i));
                    if ((priceCompare == 1 && timeCompare == 1)) {
                        properList.remove(j);
                        if (!Flight.isFlightInList(properList, flights.get(i))) {
                            properList.add(flights.get(i));
                        }
                    } else if ((priceCompare == 1 && timeCompare == -1) || (priceCompare == -1 && timeCompare == 1)) {
                        if (!Flight.isFlightInList(properList, flights.get(i))) {
                            properList.add(flights.get(i));
                        }
                    } else if (priceCompare == 0 && timeCompare == 0) {
                        if (!Flight.isFlightInList(properList, flights.get(i))) {
                            properList.add(flights.get(i));
                        }
                    }
                }
            }
            if (properList.size() == 0) {
                System.out.println("No suitable flight plan is found");
            } else {
                for (int k = 0; k < properList.size(); k++) {
                    System.out.println(properList.get(k));
                }
            }
        }


    }

    public static void listCheapest() {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> cheapestFlights = new ArrayList<>();

        //As a result of the flights, the cheapest flights are added to the list and the whole list is shown as a result.
        //If their prices are the same, both flights are added.
        //If a cheaper flight is found, a new list is created.
        cheapestFlights.add(flights.get(0));
        for (int i = 1; i < flights.size(); i++) {
            if (cheapestFlights.get(0).comparePrice(flights.get(i)) == 0) {
                cheapestFlights.add(flights.get(i));
            } else if (cheapestFlights.get(0).comparePrice(flights.get(i)) == 1) {
                ArrayList<Flight> newList = new ArrayList<>();
                newList.add(flights.get(i));
                cheapestFlights = newList;
            }
        }
        for (int k = 0; k < cheapestFlights.size(); k++) {
            System.out.println(cheapestFlights.get(k));
        }
    }

    public static void listQuickest() throws ParseException {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> quickestFlights = new ArrayList<>();

        //As a result of the flights, the quickest flights are added to the list and the whole list is shown as a result.
        //If their durations are the same, both flights are added.
        //If a quicker flight is found, a new list is created.
        quickestFlights.add(flights.get(0));
        for (int i = 1; i < flights.size(); i++) {
            if (quickestFlights.get(0).compareFlightTime(flights.get(i)) == 0) {
                quickestFlights.add(flights.get(i));
            } else if (quickestFlights.get(0).comparePrice(flights.get(i)) == -1) {
                ArrayList<Flight> newList = new ArrayList<>();
                newList.add(flights.get(i));
                quickestFlights = newList;
            }
        }
        for (int k = 0; k < quickestFlights.size(); k++) {
            System.out.println(quickestFlights.get(k));
        }
    }

    public static void listCheaper(int max_price) throws ParseException {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> cheaperFlights = new ArrayList<>();

        //Flights that took flight before a certain date are detected and listed.
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getTotal_price() < max_price) {
                cheaperFlights.add(flights.get(i));
            }
        }
        Operations.listProper(cheaperFlights);
    }

    public static void listQuicker(String latest_date_time) throws ParseException {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> quickerFlights = new ArrayList<>();

        //It filters the flights that take place in less than a certain time and is recorded in the list as they are detected. T
        Date latest_date = Main.stringToDate(latest_date_time);
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getArrive_date().compareTo(latest_date) == -1) {
                quickerFlights.add(flights.get(i));
            }
        }
        //The whole list is display after the detect proper list.
        Operations.listProper(quickerFlights);
    }

    public static void listExcluding(String company) throws ParseException {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> excludingFlights = new ArrayList<>();

        //During the entire flight, if a flight from the specified company is included,
        //the value of company found (isCompanyFound) will be true and will not be added to the list at the next check.
        for (int i = 0; i < flights.size(); i++) {
            boolean isCompanyFound = false;
            for (int j = 0; j < flights.get(i).getEdge_list().size(); j++) {
                if (flights.get(i).getEdge_list().get(j).getFlight_id().substring(0, 2).equals(company)) {
                    isCompanyFound = true;
                    break;
                }
            }
            if (isCompanyFound == false) {
                excludingFlights.add(flights.get(i));
            }
            //In this way, unwanted flights are not included in the function.
        }
        Operations.listProper(excludingFlights);
    }

    public static void listOnlyFrom(String company) throws ParseException {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> listOnlyFlights = new ArrayList<>();

        //If there is another company in the flights in the graph, the value of
        //isAnotherCompanyInPath is true and it is determined that the flight does not fit the desired format.
        for (int i = 0; i < flights.size(); i++) {
            boolean isAnotherCompanyInPath = false;
            ArrayList<Edge> edge_list = flights.get(i).getEdge_list();

            for (int j = 0; j < edge_list.size(); j++) {
                if (!edge_list.get(j).getFlight_id().substring(0, 2).equals(company)) {
                    isAnotherCompanyInPath = true;
                }
            }
            if (!isAnotherCompanyInPath) {
                listOnlyFlights.add(flights.get(i));
            }
        }

        //If all flights are from the desired company, it is added to the list.
        //Then, the best flights are determined by sending it to the listProper function.
        Operations.listProper(listOnlyFlights);
    }

    public static void diameterOfGraph() {
        //i will find all shortest path in graph then set all shortest path to Flight array ,
        //then i will pick the flight that has maximum price
        ArrayList<Airport> airports = Airport.airports;  //airports
        ArrayList<Integer> diameters = new ArrayList<>();
        for (int i = 0; i < airports.size(); i++) {
            Airport firstAirport = airports.get(i);
            for (int j = 0; j < airports.size(); j++) {
                Airport secondAirport = airports.get(j);
                if (firstAirport != secondAirport || !firstAirport.getCity().equals(secondAirport.getCity())) {
                    int shortestPathValue = findShortestPaths(firstAirport.getAlias(), secondAirport.getAlias());
                    if (shortestPathValue != -1)
                        diameters.add(shortestPathValue);
                }
            }
        }
        if(diameters.size() != 0)
            System.out.println("The diameter of graph : " + Collections.max(diameters));
        else
            System.out.println("The diameter of graph : "+"null");

    }

    public static Integer findShortestPaths(String source, String dest) {

        //Infrastructures required to compute shortest paths.
        ArrayList<String> pathList = new ArrayList<>();
        HashSet<String> paths = new HashSet<String>();
        boolean[] isVisited = new boolean[Graph.airportsMap.size()];
        int sourceAirportMapId = Graph.getKeyValueFromAdjList(source);
        int destAirportMapId = Graph.getKeyValueFromAdjList(dest);

        //First, the departure airport is added to the pathList.
        pathList.add(Graph.airportsMap.get(sourceAirportMapId).getAlias());
        findAllFlightPaths(sourceAirportMapId, destAirportMapId, isVisited, pathList, paths, null, true);
        List<String> list = new ArrayList<String>(paths);
        //All possible flights are obtained and the shortest path is calculated for each.


        ArrayList<Integer> shortestPathsPriceValues = new ArrayList<Integer>();
        //It stores the total cost of flights and helps to choose the cheapest flight among all flights
        for (int i = 0; i < list.size(); i++) {
            String[] splitLine = list.get(i).split(",");
            int totalPrice = 0;
            for (int j = 1; j < splitLine.length; j = j + 2) {
                totalPrice = totalPrice + Integer.parseInt(splitLine[j]);
            }
            shortestPathsPriceValues.add(totalPrice);
        }

        //The cheapest (shortest path) flight is determined and only returns the price.
        if (shortestPathsPriceValues.size() != 0) {
            return Collections.min(shortestPathsPriceValues);
        } else {
            return -1;
            //Returns -1 if there is no flight between 2 destinations (source, dest).
        }
    }

    public static void pageRankOfNodes(){
        System.out.println("eren");
    }


}
