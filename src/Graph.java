import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    public static HashMap<Integer, Airport> airportsMap = new HashMap<Integer, Airport>(); //holds vertices of the graph (airports)
    public static Edge[][] graphAdjList;

    public void createGraph(ArrayList<Airport> airports, ArrayList<Edge> edges) {
        graphAdjList = new Edge[airports.size()][airports.size()];
        for (int i = 0; i < airports.size(); i++) {
            airportsMap.put(i, airports.get(i));
        }
        for (int j = 0; j < edges.size(); j++) {
            addEdgeToGraph(graphAdjList, edges.get(j), airportsMap);
        }
//        printAdjMatrix(graphAdjList, airports.size(), airportsMap);
    }

    public int getKeyValueFromAdjList(String value, HashMap<Integer, Airport> airportsMap) {
        for (int i = 0; i < airportsMap.size(); i++) {
            if (airportsMap.get(i).getAlias().equals(value))
                return i;
        }
        return -1;
    }

    public void addEdgeToGraph(Edge[][] graphAdjList, Edge edge, HashMap<Integer, Airport> airportsMap) {
        int source = getKeyValueFromAdjList(edge.getDeparture_airport().getAlias(), airportsMap);
        int dest = getKeyValueFromAdjList(edge.getArrival_airport().getAlias(), airportsMap);
        graphAdjList[source][dest] = edge;
    }


    public static void printAdjMatrix(Edge[][] graphAdjList, int n, HashMap<Integer, Airport> airportsMap) {
        // Adj list testing display function
        System.out.print("\t");
        for (int z = 0; z < n; z++) {
            System.out.print(airportsMap.get(z).getAlias() + "\t");
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(airportsMap.get(i).getAlias() + "\t");
            for (int j = 0; j < n; j++) {
                if (graphAdjList[i][j] == null) {
                    System.out.print(0 + "\t");
                } else {
                    System.out.print("1" + "\t");
                }
            }
            System.out.println();
        }
    }
}
