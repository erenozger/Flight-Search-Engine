import java.util.ArrayList;

public class ArrayOfEdges {
    //ArrayList <Edge> is the required list class for java1.8 creating.
    private ArrayList<Edge> list;

    public ArrayOfEdges() {
        this.list = new ArrayList<Edge>();
    }

    public ArrayOfEdges(ArrayList<Edge> list) {
        this.list = list;
    }

    public ArrayList<Edge> getList() {
        return this.list;
    }

    public void setList(ArrayList<Edge> list) {
        this.list = list;
    }
}