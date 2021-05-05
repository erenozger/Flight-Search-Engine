import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    public static HashMap<Integer, Airport> airportsMap = new HashMap<Integer, Airport>(); //holds vertices of the graph (airports)
    public static ArrayList<Edge>[][] graphAdjList;

    public void createGraph(ArrayList<Airport> airports, ArrayList<Edge> edges) {
        graphAdjList = new ArrayList[airports.size()][airports.size()];
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

    public void addEdgeToGraph(ArrayList<Edge>[][]  graphAdjList, Edge edge, HashMap<Integer, Airport> airportsMap) {
        int source = getKeyValueFromAdjList(edge.getDeparture_airport().getAlias());
        int dest = getKeyValueFromAdjList(edge.getArrival_airport().getAlias());
        if(graphAdjList[source][dest] == null){
            ArrayList<Edge> newEdgeList = new ArrayList<Edge>();
            newEdgeList.add(edge);
            graphAdjList[source][dest] = newEdgeList;
        }else{
            graphAdjList[source][dest].add(edge);
        }
    }


    public static void printAdjMatrix(ArrayList<Edge>[][] graphAdjList, int n, HashMap<Integer, Airport> airportsMap) {
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
