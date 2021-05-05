import java.util.ArrayList;

public class Airport {
    private String alias;
    private String city;

    static ArrayList<Airport> airports = new ArrayList<Airport>();

    public Airport() {
    }

    public Airport(String alias, String city) {
        this.alias = alias;
        this.city = city;
        Airport.airports.add(this);
    }

    public static void setAllAirports(ArrayList<String> airportList) {
        for (int i = 0; i < airportList.size(); i++) {
            String[] splitLine = airportList.get(i).split("\t");
            int aliasSize = splitLine.length;
            String cityName = splitLine[0];
            for (int j = 1; j < aliasSize; j++) {
                new Airport(splitLine[j], cityName);
            }
        }
    }

    public static Airport getAirportFromAliasName(String alias){
        for(int i = 0 ; i<airports.size(); i++) {
            if(airports.get(i).getAlias().equals(alias)){
                return airports.get(i);
            }
        }
        return null;

    }

    public static void printAllAirports() {
        for (int i = 0; i < airports.size(); i++) {
            System.out.println(airports.get(i));
        }
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Airport{alias= " + this.getAlias() + ", city= " + this.getCity() + "}";
    }


}
