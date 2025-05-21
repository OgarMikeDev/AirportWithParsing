import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Airport airport = new Airport();

        //TODO Вывод всех аэропортов в удобочитаемом формате
        //airport.printMapAllAirports();

        /*
        TODO
         Ввод названия аэропорта
         и получение списка
         всех вылетов из него
         */
        System.out.println(
                "\nВведите название аэропорта,\n" +
                "чтобы получить список\n" +
                "всех вылетов из него:");
        String nameAirport = new Scanner(System.in).nextLine();
        airport.getListAllDepartureFlightsFromSelectedUserAirport(nameAirport);
    }
}
