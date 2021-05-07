import java.util.*;


public class Graph {

    public static HashMap<Integer, Airport> airportsMap = new HashMap<Integer, Airport>(); //holds vertices of the graph (airports)
    //Specifying all airports in the system and hash map assigned an integer identification to each airport.

    public static ArrayOfEdges[][] graphAdjList;
    //Adjacency matrix for graph. 3D Array that contains all airports as integer and on graph every value that contains
    //edge list for represent multi graph.

    public void createGraph(ArrayList<Airport> airports, ArrayList<Edge> edges) {
        graphAdjList = new ArrayOfEdges[airports.size()][airports.size()];
        for (int i = 0; i < airports.size(); i++) {
            for (int j = 0; j < airports.size(); j++) {
                ArrayOfEdges emptyList = new ArrayOfEdges();
                graphAdjList[i][j] = emptyList;
            }
        }
        for (int i = 0; i < airports.size(); i++) {
            airportsMap.put(i, airports.get(i));
        }
        for (int j = 0; j < edges.size(); j++) {
            addEdgeToGraph(graphAdjList, edges.get(j), airportsMap);
        }
        //The function that records all the flights(edges) of the graph in the system.
    }

    public static int getKeyValueFromAdjList(String value) {
        //A function that finds and returns integer values with the names of airports defined for the adj matrix.
        for (int i = 0; i < airportsMap.size(); i++) {
            if (airportsMap.get(i).getAlias().equals(value))
                return i;
        }
        return -1;

    }

    public void addEdgeToGraph(ArrayOfEdges[][] graphAdjList, Edge edge, HashMap<Integer, Airport> airportsMap) {
        //A function that saves the airports received as strings in graph.
        int source = getKeyValueFromAdjList(edge.getDeparture_airport().getAlias());
        int dest = getKeyValueFromAdjList(edge.getArrival_airport().getAlias());
        graphAdjList[source][dest].getList().add(edge);
    }
}
