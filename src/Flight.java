import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Flight {
    private int flight_id;
    private Date start_date;
    private Date arrive_date;
    private int total_price;
    private String flight_time;
    private ArrayList<Edge> edge_list;

    /**
     * @param flight_id         Unique flight id
     * @param start_date        Defines the start date of the entire flight
     * @param arrive_date       Defines the arrival date of the entire flight
     * @param total_price       It indicates the total cost of the entire flight.
     * @param flight_time       Duration of the entire flight
     * @param edge_list         List defining the edges indicating all flights during the flight
     */

    public static ArrayList<Flight> flights = new ArrayList<>();  //It keeps all flights , ( firstly created graph )

    public Flight(int flight_id, Date start_date, Date arrive_date, int total_price, String flight_time, ArrayList<Edge> edge_list) {
        this.flight_id = flight_id;
        this.start_date = start_date;
        this.arrive_date = arrive_date;
        this.total_price = total_price;
        this.flight_time = flight_time;
        this.edge_list = edge_list;
        Flight.flights.add(this);
    }

    public static void setOneFLight(int flight_id, String flightLine) {
        //It saves the system by performing operations on the string indicating a flight.
        Date startDate = null;
        Date arriveDate = null;
        int totalPrice = 0;
        String[] splitLine = flightLine.split(",");

        //Sample String to split -> KBP,KL8222,ATH,LH0893,BRU,LH9137,BUD
        //KL8222	KBP->ATH||LH0893	ATH->BRU||LH9137	BRU->BUD	11:35/480

        ArrayList<Edge> edge_list = new ArrayList<>();
        String flight_time = "";

        //Separates the string to find the actual edge for each flight and save it to the class.
        for (int i = 1; i < splitLine.length; i = i + 2) {
            Edge currentEdge = Edge.findEdgeFromFlightId(splitLine[i]);
            edge_list.add(currentEdge);
            if (currentEdge != null) {
                if (i == 1)
                    startDate = currentEdge.getDept_date();
                if (i == splitLine.length - 2)
                    arriveDate = currentEdge.getArrival_date();
                totalPrice = totalPrice + currentEdge.getPrice();
            }
        }
        if (startDate != null && arriveDate != null) {
            long differenceInHours = arriveDate.getTime() - startDate.getTime();
            long hh = differenceInHours / (3600 * 1000);
            long mm = (differenceInHours - hh * 3600 * 1000) / (60 * 1000);
            flight_time = String.format("%02d:%02d", hh, mm);
        }

        //Creates the object by calculating the flight time and total cost.
        new Flight(flight_id, startDate, arriveDate, totalPrice, flight_time, edge_list);
    }

    public static void setAllFLights(ArrayList<List<String>> flightsList) {
        //The function that allows all possible flights on the graph to be recorded in the system.
        int id = 0;
        for (int i = 0; i < flightsList.size(); i++) {
            for (int j = 0; j < flightsList.get(i).size(); j++) {
                setOneFLight(id, flightsList.get(i).get(j));
                id++;
            }
        }
    }

    @Override
    public String toString() {
        String flightString = "";
        for (int i = 0; i < this.getEdge_list().size(); i++) {
            flightString = flightString + this.getEdge_list().get(i).toString();
            if (i != this.getEdge_list().size() - 1) {
                flightString = flightString + "||";
            }
        }
        flightString = flightString + "\t" + this.getFlight_time() + "/" + this.total_price;
        return flightString;
        //Sample output that creates
        // KL8222	KBP->ATH||LH0893	ATH->BRU||LH9137	BRU->BUD	11:35/480
    }

    public int comparePrice(Flight flight) {
        if (this.total_price > flight.getTotal_price()) {
            return 1;
        } else if (this.total_price == flight.getTotal_price()) {
            return 0;
        } else {
            return -1;
        }
        //The function that compares the fare between 2 flights.
    }

    public int compareFlightTime(Flight flight) throws ParseException {
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date time = simpleDateFormat.parse(this.flight_time);
        Date compareTime = simpleDateFormat.parse(flight.getFlight_time());
        return time.compareTo(compareTime);
        ////The function that compares the fare between 2 flights.
    }

    public static boolean isFlightInList(ArrayList<Flight> flights, Flight flight) {
        for (int i = 0; i < flights.size(); i++) {
            if (flight.getFlight_id() == flights.get(i).getFlight_id()) {
                return true;
            }
        }
        return false;
        //The function that compares the flight time between 2 flights.
    }

    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
        this.flight_id = flight_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getArrive_date() {
        return arrive_date;
    }

    public void setArrive_date(Date arrive_date) {
        this.arrive_date = arrive_date;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getFlight_time() {
        return flight_time;
    }

    public void setFlight_time(String flight_time) {
        this.flight_time = flight_time;
    }

    public ArrayList<Edge> getEdge_list() {
        return edge_list;
    }

    public void setEdge_list(ArrayList<Edge> edge_list) {
        this.edge_list = edge_list;
    }


}
