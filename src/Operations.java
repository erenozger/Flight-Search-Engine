import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static java.util.Comparator.comparing;

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
                    Operations.listAll(source, desc, date);
                    break;
                case "listProper":
                    Operations.listProper(Flight.flights);
                    break;
                case "listCheapest":
                    Operations.listCheapest();
                    break;
                case "listQuickest":
                    Operations.listQuickest();
                    break;
                case "listCheaper":
                    Operations.listCheaper(Integer.parseInt(splitList[3]));
                    break;
                case "listQuicker":
                    Operations.listQuicker(splitList[3]);
                    break;
                case "listExcluding":
                    Operations.listExcluding(splitList[3]);
                    break;
                case "listOnlyFrom":
                    Operations.listOnlyFrom(splitList[3]);
                    break;
                case "diameterOfGraph":
//                    System.out.println("diameterOfGraph");
                    break;
                case "pageRankOfNodes":
//                    System.out.println("pageRankOfNodes");
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
        for (int i = 0; i < Flight.flights.size(); i++) {
            System.out.println(Flight.flights.get(i));
        }
    }

    public static ArrayList<List<String>> findAllPossibleFlights(String source, String dest, String date) {
        ArrayList<Airport> sourceAirports = Airport.getAirportsFromCity(source);
        ArrayList<Airport> destAirports = Airport.getAirportsFromCity(dest);
        ArrayList<List<String>> allPossibleFlights = new ArrayList<>();
        for (int i = 0; i < sourceAirports.size(); i++) {
            for (int j = 0; j < destAirports.size(); j++) {
                ArrayList<String> pathList = new ArrayList<>();
                HashSet<String> paths = new HashSet<String>();
                boolean[] isVisited = new boolean[Graph.airportsMap.size()];
                int sourceAirportMapId = Graph.getKeyValueFromAdjList(sourceAirports.get(i).getAlias());
                int destAirportMapId = Graph.getKeyValueFromAdjList(destAirports.get(j).getAlias());
                pathList.add(Graph.airportsMap.get(sourceAirportMapId).getAlias());
                findAllFlightPaths(sourceAirportMapId, destAirportMapId, isVisited, pathList, paths, null);
                List<String> list = new ArrayList<String>(paths);
                allPossibleFlights.add(list);
            }
        }
        return allPossibleFlights;
    }

    public static void findAllFlightPaths(int source, int desc, boolean[] isVisited, ArrayList<String> pathList,
                                          HashSet<String> paths, Edge lastEdge) {
        isVisited[source] = true;
        if (source == desc) {
            String listString = String.join(",", pathList);
            paths.add(listString);
            isVisited[source] = false;
            return;
        }
        for (int destCount = 0; destCount < Graph.graphAdjList[source].length; destCount++) {
            ArrayList<Edge> edgeList = Graph.graphAdjList[source][destCount];
            if (edgeList != null) {
                for (Edge i : edgeList) {
                    int nextAirportId;
                    if (i == null) {
                        nextAirportId = 0;
                    } else {
                        Airport j = i.getArrival_airport();
                        nextAirportId = Graph.getKeyValueFromAdjList(j.getAlias());
                    }
                    if (!isVisited[nextAirportId] && i != null && compareTwoFlightsDate(lastEdge, i) && compareTwoFlightsCity(pathList, i)) {
                        Edge nextEdge = i;
                        pathList.add(nextEdge.getFlight_id());
                        pathList.add(Graph.airportsMap.get(nextAirportId).getAlias());
                        findAllFlightPaths(nextAirportId, desc, isVisited, pathList, paths, nextEdge);
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

        Date latest_date = Main.stringToDate(latest_date_time);
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getArrive_date().compareTo(latest_date) == -1) {
                quickerFlights.add(flights.get(i));
            }
        }
        Operations.listProper(quickerFlights);
    }

    public static void listExcluding(String company) throws ParseException {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> excludingFlights = new ArrayList<>();

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
        }
        Operations.listProper(excludingFlights);
    }

    public static void listOnlyFrom(String company) throws ParseException {
        ArrayList<Flight> flights = Flight.flights;
        ArrayList<Flight> listOnlyFlights = new ArrayList<>();

        for (int i = 0; i < flights.size(); i++) {
            boolean isAnotherCompanyInPath = false;
            ArrayList<Edge> edge_list = flights.get(i).getEdge_list();

            for(int j = 0 ; j < edge_list.size();j++) {
                if(!edge_list.get(j).getFlight_id().substring(0,2).equals(company)){
                    isAnotherCompanyInPath = true;
                }
            }
            if(!isAnotherCompanyInPath){
                listOnlyFlights.add(flights.get(i));
            }
        }
        Operations.listProper(listOnlyFlights);
    }


}
