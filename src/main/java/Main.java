import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String user = "root";
        String url = "jdbc:mysql://localhost:3306/aviasales";
        String password = "Misha06122000)))";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            Airport airport = new Airport();
            airport.getListAllAirports();

            for (Map.Entry<String, String> entryAirport : airport.getMapAllAirports().entrySet()) {
                statement.execute("INSERT INTO airports (name, url) VALUES (" +
                        entryAirport.getKey() + "," + entryAirport.getValue() + ")");
            }

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot();
            telegramBotsApi.registerBot(bot);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
