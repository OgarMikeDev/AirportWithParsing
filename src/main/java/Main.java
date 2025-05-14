public class Main {
    public static void main(String[] args) {
        String urlMainPageAirports = "https://www.aviasales.ru";
        Airport airport = new Airport(urlMainPageAirports);
        System.out.println(airport.getMapAirports());
    }
}
