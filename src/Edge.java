import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Edge {
    private String flight_id;
    private Airport departure_airport;
    private Airport arrival_airport;
    private Date dept_date;
    private String duration;
    private Date arrival_date;
    private int price;

    static ArrayList<Edge> edges = new ArrayList<Edge>();

    public Edge() {
    }

    public Edge(String flight_id, Airport departure_airport, Airport arrival_airport, Date dept_date, String duration, Date arrival_date, int price) {
        this.flight_id = flight_id;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.dept_date = dept_date;
        this.duration = duration;
        this.arrival_date = arrival_date;
        this.price = price;
        Edge.edges.add(this);
    }

    public static void setAllEdges(ArrayList<String> flightList) throws ParseException {
        for (int i = 0; i < flightList.size(); i++) {
            String[] splitLine = flightList.get(i).split("\t");
            String[] splitAirports = splitLine[1].split("->");
            Airport deptAirport = Airport.getAirportFromAliasName(splitAirports[0]);
            Airport arrivalAirport = Airport.getAirportFromAliasName(splitAirports[1]);
            Date deptDate = Main.stringToDate(splitLine[2]);
            String duration = splitLine[3];
            Date arrivalDate = findArrivalDate(deptDate, duration);
            int price = Integer.parseInt(splitLine[4]);
            new Edge(splitLine[0], deptAirport, arrivalAirport, deptDate, duration, arrivalDate, price);
        }
    }

    public static Date findArrivalDate(Date date, String duration) {
        Calendar cal = new GregorianCalendar();
        String[] splitDuration = duration.split(":");
        int hour = Integer.parseInt(splitDuration[0]);
        int minutes = Integer.parseInt(splitDuration[1]);
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public String getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(String flight_id) {
        this.flight_id = flight_id;
    }

    public Airport getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(Airport departure_airport) {
        this.departure_airport = departure_airport;
    }

    public Airport getArrival_airport() {
        return arrival_airport;
    }

    public void setArrival_airport(Airport arrival_airport) {
        this.arrival_airport = arrival_airport;
    }

    public Date getDept_date() {
        return dept_date;
    }

    public void setDept_date(Date dept_date) {
        this.dept_date = dept_date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(Date arrival_date) {
        this.arrival_date = arrival_date;
    }

    @Override
    public String toString() {
        return "Edge : \n" +
                "flight id : " + this.flight_id + "\n" +
                "departure_airport: " + this.departure_airport + "\n" +
                "arrival_airport: " + this.arrival_airport + "\n" +
                "dept_date : " + Main.dateToString(this.dept_date) + "\n" +
                "arrival_date: " + Main.dateToString(this.arrival_date) + "\n" +
                "duration: " + this.duration + "\n" +
                "price: " + this.price + "\n" +
                "-------------------------------------------------------------";
    }

    public static void printAllEdges() {
        for (int i = 0; i < edges.size(); i++)
            System.out.println(edges.get(i));
    }

}
