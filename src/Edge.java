import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Edge {
    private int flight_id;
    private Airport departure_airport;
    private Airport arrival_airport;
    private String dept_date;
    private String duration;
    private String arrival_date;
    private int price;

    static ArrayList<Edge> edges = new ArrayList<Edge>();


    public Edge() {
    }

    public Edge(int flight_id, Airport departure_airport, Airport arrival_airport, String dept_date, String duration, String arrival_date, int price) {
        this.flight_id = flight_id;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.dept_date = dept_date;
        this.duration = duration;
        this.price = price;
        Edge.edges.add(this);
    }

    public static void setAllEdges(ArrayList<String> flightList) throws ParseException {
        for (int i = 0; i < flightList.size(); i++) {
            String[] splitLine = flightList.get(i).split("\t");
            Date flightDate = Main.stringToDate(splitLine[2]);
            System.out.println("flight date = " + Main.dateToString(flightDate));
            System.out.println("duration time = " + splitLine[3]);
            System.out.println("arrival date = " + Main.dateToString(findArrivalDate(flightDate, splitLine[3])));
            System.out.println("compare result = " + flightDate.compareTo(findArrivalDate(flightDate, splitLine[3])));
            System.out.println("-------------------------------------------------------------------");
        }
    }

    public static Date findArrivalDate(Date date,String duration){
        Calendar cal = new GregorianCalendar();
        String[] splitDuration = duration.split(":");
        int hour = Integer.parseInt(splitDuration[0]);
        int minutes = Integer.parseInt(splitDuration[1]);
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        cal.add(Calendar.MINUTE , minutes);
        return cal.getTime();
    }

    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
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

    public String getDept_date() {
        return dept_date;
    }

    public void setDept_date(String dept_date) {
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


}
