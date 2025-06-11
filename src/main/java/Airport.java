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

            int counterId = 2;
            for (Map.Entry<String, String> entryAirport : airport.getMapAllAirports().entrySet()) {
                statement.execute(
                        "INSERT INTO airports " +
                            "VALUES(" +
                                counterId + ", '" +
                                entryAirport.getKey() + "', '" +
                                entryAirport.getValue() + "'" +
                            ")");
                counterId++;
            }

            statement.close();
            connection.close();

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot();
            telegramBotsApi.registerBot(bot);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
