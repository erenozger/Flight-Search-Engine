import java.util.ArrayList;

public class Airport {
    private String alias;
    private String city;

    static ArrayList<Airport> airports = new ArrayList<Airport>();
    //Arraylist specifying all airports registered in the system.

    public Airport() {}
    //empty constructor.

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

    public static Airport getAirportFromAliasName(String alias) {
        for (int i = 0; i < airports.size(); i++) {
            if (airports.get(i).getAlias().equals(alias)) {
                return airports.get(i);
            }
        }
        return null;
    }

    public static ArrayList<Airport> getAirportsFromCity (String cityName) {
        ArrayList<Airport> cityAirports = new ArrayList<Airport>();
        for(int i = 0 ; i<airports.size();i++){
            if(airports.get(i).getCity().equals(cityName)){
                cityAirports.add(airports.get(i));
            }
        }
        return cityAirports;
    }

    public String getAlias() {
        return alias;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Airport{alias= " + this.getAlias() + ", city= " + this.getCity() + "}";
    }


}
