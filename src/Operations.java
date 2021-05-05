import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Operations {

    public static void readCommands(ArrayList<String> commandList) {
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

            switch (splitList[0]) {
                case "listAll":
                    Operations.listALlFlights(commandList.get(i), source, desc, date);
                    System.out.println("\n");
                    break;
                case "listProper":
                    System.out.println("listProper");
                    break;
                case "listCheapest":
//                    System.out.println("listCheapest");
                    break;
                case "listQuickest":
//                    System.out.println("listQuickest");
                    break;
                case "listCheaper":
//                    System.out.println("listCheaper");
                    break;
                case "listQuicker":
//                    System.out.println("listQuicker");
                    break;
                case "listExcluding":
//                    System.out.println("listExcluding");
                    break;
                case "listOnlyFrom":
//                    System.out.println("listOnlyFrom");
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
        }
    }

    public static void listALlFlights(String command, String source, String dest, String date) {
        System.out.println("command : " + command);
        ArrayList<Airport> sourceAirports = Airport.getAirportsFromCity(source);
        ArrayList<Airport> destAirports = Airport.getAirportsFromCity(dest);
        ArrayList<String> allPossiblePaths = new ArrayList<String>();
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
                for (int z = 0; z < list.size(); z++) {
                    displayOneFlight(list.get(z));
                }
            }
        }


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
        for (int destCount = 0 ; destCount < Graph.graphAdjList[source].length ; destCount++) {
            ArrayList<Edge> edgeList = Graph.graphAdjList[source][destCount];
            if(edgeList != null){
                for(Edge i : edgeList){
                    int nextAirportId;
                    if (i == null) {
                        nextAirportId = 0;
                    } else {
                        Airport j = i.getArrival_airport();
                        nextAirportId = Graph.getKeyValueFromAdjList(j.getAlias());
                    }
                    if (!isVisited[nextAirportId] && i != null && compareTwoFlightsDate(lastEdge, i) && compareTwoFlightsCity(pathList,i)) {
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

    public static boolean compareTwoFlightsCity(ArrayList<String> pathList, Edge nextEdge){
        for(int i = 0 ; i<pathList.size();i = i + 2) {
            String edgeCity = Airport.getAirportFromAliasName(pathList.get(i)).getCity();
            String newEdgeCity = Airport.getAirportFromAliasName(nextEdge.getArrival_airport().getAlias()).getCity();
            if (edgeCity.equals(newEdgeCity)) {
                return false;
            }
        }
        return true;
    }

    public static void displayOneFlight(String flightLine){
        Date startDate = null;
        Date arriveDate = null;
        Integer totalPrice = 0;
        String[] splitLine = flightLine.split(",");
        for(int i = 1 ; i<splitLine.length; i = i + 2 ){
            Edge currentEdge = Edge.findEdgeFromFlightId(splitLine[i]);
            if(currentEdge != null){
                if(i == 1)
                    startDate = currentEdge.getDept_date();
                if(i == splitLine.length-2)
                    arriveDate = currentEdge.getArrival_date();
                totalPrice = totalPrice + currentEdge.getPrice();
                System.out.print(currentEdge);
                if(i != splitLine.length -2)
                    System.out.print("||");
            }
        }
        if(startDate != null && arriveDate != null){
            long differenceInHours = arriveDate.getTime() -startDate.getTime();
            long hh = differenceInHours / (3600 * 1000);
            long mm = (differenceInHours - hh * 3600 * 1000) / (60 * 1000);
            System.out.printf("\t%02d:%02d", hh, mm);
        }
        System.out.print("/"+totalPrice);
        System.out.println();
    }

}
