import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String urlMainPageAirports = "https://www.aviasales.ru";
        Airport airport = new Airport(urlMainPageAirports);
        //TODO вывод всех аэропортов с ссылками на их страницами
        System.out.println("Вывод всех аэропортов с ссылками на их страницами:");
        airport.printMapAirports();

        //TODO возвращение списка вылетов из выбранного пользователем аэропорта
        System.out.println("\nВведите название аэропорта:\n" +
                "А мы вернём список всех вылетов из него:");
        String nameAirportDeparture = new Scanner(System.in).nextLine();
        System.out.println(airport.getListDepartureFlightsFromSelectedUserAirport(nameAirportDeparture));

        //TODO возвращение списка прилётов в выбранный пользователем аэропорт
        System.out.println("\nВведите название аэропорта:\n" +
                "А мы вернём список всех прилётов из него:");
        String nameAirportArrival = new Scanner(System.in).nextLine();
        System.out.println(airport.getListArrivalFlightsFromSelectedUserAirport(nameAirportArrival));

        //TODO возвращение ближайшего прилёта в выбранный пользователем аэропорт
        System.out.println("\nВведите название аэропорта:\n" +
                "А мы вернём ближайший прилёт на него:");
        String nameAirportForFirstArrival = new Scanner(System.in).nextLine();
        airport.getFirstArrivalInSelectedUserAirport(nameAirportForFirstArrival);

        /*
        TODO вывод списка вылетов
         в выбранный пользователем аэропорт
         и в указанное кол-во часов
         */
    }
}
