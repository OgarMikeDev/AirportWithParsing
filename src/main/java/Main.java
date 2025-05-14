import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String urlMainPageAirports = "https://www.aviasales.ru";
        Airport airport = new Airport(urlMainPageAirports);
        //TODO вывод всех аэропортов с ссылками на их страницами
        System.out.println("Вывод всех аэропортов с ссылками на их страницами:");
        airport.printMapAirports();

        //TODO вывод списка вылетов из выбранного пользователем аэропорта
        System.out.println("\nВведите название аэропорта из списка предложенных:\n" +
                "Иркутск;\n" +
                "Минеральные Воды;\n" +
                "Казань;\n" +
                "Шереметьево;\n" +
                "Жуковский;\n" +
                "Пулково;\n" +
                "Внуково;\n" +
                "Домодедово;\n" +
                "Кольцово;\n" +
                "Уфа\n" +
                "А мы выведем список всех вылетов из него:");
        String nameAirport = new Scanner(System.in).nextLine();
        airport.printListDepartureFlightsFromSelectedUserAirport(nameAirport);

        //TODO вывод списка прилётов в выбранный пользователем аэропорт

        //TODO вывод ближайшего прилёта в выбранный пользователем аэропорт

        /*
        TODO вывод списка вылетов
         в выбранный пользователем аэропорт
         и в указанное кол-во часов
         */
    }
}
