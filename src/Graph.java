import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    public static HashMap<Integer, Airport> airportsMap = new HashMap<Integer, Airport>(); //holds vertices of the graph (airports)
    public static ArrayOfEdges[][] graphAdjList;

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
    }

    public static int getKeyValueFromAdjList(String value) {
        for (int i = 0; i < airportsMap.size(); i++) {
            if (airportsMap.get(i).getAlias().equals(value))
                return i;
        }
        return -1;
    }

    public void addEdgeToGraph(ArrayOfEdges[][]  graphAdjList, Edge edge, HashMap<Integer, Airport> airportsMap) {
        int source = getKeyValueFromAdjList(edge.getDeparture_airport().getAlias());
        int dest = getKeyValueFromAdjList(edge.getArrival_airport().getAlias());
        graphAdjList[source][dest].getList().add(edge);
    }



}
