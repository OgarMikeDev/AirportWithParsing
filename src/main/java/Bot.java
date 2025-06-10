import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    Airport airport = null;
    private boolean bolForListDeparture = false;
    private boolean bolForListArrival = false;
    private boolean bolForFirstDepartureFlight = false;
    private boolean bolForFirstArrivalFlight = false;
    private String nameAirportForDeparture = "";

    //TODO Кнопка для вывода всех аэропортов
    private InlineKeyboardButton buttonForOutputAllAirports = InlineKeyboardButton.builder()
            .text("Список всех аэропортов")
            .callbackData("list all airports")
            .build();
    //TODO Кнопка для вывода вылетов
    private InlineKeyboardButton buttonForOutputListDepartureFlight = InlineKeyboardButton.builder()
            .text("Список вылетов из конкретного аэропорта")
            .callbackData("list departure flight")
            .build();
    //TODO Кнопка для вывода прилётов
    private InlineKeyboardButton buttonForOutputListArrivalFlight = InlineKeyboardButton.builder()
            .text("Список прилётов из конкретного аэропорта")
            .callbackData("list arrival flight")
            .build();
    //TODO Кнопка для вывода всех аэропортов
    private InlineKeyboardButton buttonForOutputFirstDepartureFlight = InlineKeyboardButton.builder()
            .text("Ближайший вылет из выбранного аэропорта")
            .callbackData("first departure flight")
            .build();
    //TODO Клавиатура для кнопок с основными командами
    private InlineKeyboardMarkup keyboardWithMainCommands = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(buttonForOutputAllAirports))
            .keyboardRow(List.of(buttonForOutputListDepartureFlight))
            .keyboardRow(List.of(buttonForOutputListArrivalFlight))
            .keyboardRow(List.of(buttonForOutputFirstDepartureFlight))
            .build();

    public Bot() {
        airport = new Airport();
    }

    @Override
    public void onUpdateReceived(Update update) {
        forWorkWithText(update);
        forWorkWithButtons(update);
    }

    //TODO метод для работы с текстом
    private void forWorkWithText(Update update) {
        if (update.hasMessage()) {
            String textMessage = update.getMessage().getText();
            Long userId = update.getMessage().getFrom().getId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(userId);

            if (textMessage.compareToIgnoreCase("/start") == 0) {
                        sendMessage.setText("Выберете действие");
                        sendMessage.setReplyMarkup(keyboardWithMainCommands);
            } else if (String.valueOf(airport.getListAllAirports()).contains(textMessage) &&
                            bolForListDeparture) {
                sendMessage.setText(String.valueOf(airport.getListAllDepartureFlightsFromSelectedUserAirport(textMessage)));
                bolForListDeparture = false;
            } else if (String.valueOf(airport.getListAllAirports()).contains(textMessage) &&
                            bolForListArrival) {
                sendMessage.setText(String.valueOf(airport.getListAllArrivalFlightsFromSelectedUserAirport(textMessage)));
                bolForListArrival = false;
            } else if (String.valueOf(airport.getListAllAirports()).contains(textMessage) &&
                            bolForFirstDepartureFlight) {
                bolForFirstDepartureFlight = false;
                bolForFirstArrivalFlight = true;
                nameAirportForDeparture = textMessage;
                sendMessage.setText("Введите название аэропорта,\n" +
                        "в кот-й планируете прилететь:");
            } else if (String.valueOf(airport.getListAllAirports()).contains(textMessage) &&
                    bolForFirstArrivalFlight) {
                bolForFirstArrivalFlight = false;
                sendMessage.setText(
                        String.valueOf(airport.getFirstArrivalFlight(nameAirportForDeparture, textMessage
                        ))
                );
            }

            try {
                execute(sendMessage);
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }

    //TODO метод для работы с кнопками
    private void forWorkWithButtons(Update update) {
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            System.out.println("Callback \"" + callbackData + "\"");
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);

            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("")
                    .build();

            if (callbackData.equals(buttonForOutputAllAirports.getCallbackData())) {
                sendDocument.setDocument(new InputFile(new File(writeFileTxtAndReturnPath(callbackData))));

                try {
                    execute(sendDocument);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            } else if (callbackData.equals(buttonForOutputListDepartureFlight.getCallbackData())) {
                bolForListDeparture = true;
                editMessageText.setText(
                        "\nВведите название аэропорта,\n" +
                                "чтобы получить список\n" +
                                "всех вылетов из него:");

                try {
                    execute(editMessageText);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            } else if (callbackData.equals(buttonForOutputListArrivalFlight.getCallbackData())) {
                bolForListArrival = true;
                editMessageText.setText(
                        "\nВведите название аэропорта,\n" +
                                "чтобы получить список\n" +
                                "всех прилётов на него:");

                try {
                    execute(editMessageText);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            } else if (callbackData.equals(buttonForOutputFirstDepartureFlight.getCallbackData())) {
                bolForFirstDepartureFlight = true;
                editMessageText.setText(
                        "\nВведите название аэропорта,\n" +
                                ",с кот-го планируете вылететь:");

                try {
                    execute(editMessageText);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }

        }
    }

    //TODO методов для записи в текстовый файл
    public String writeFileTxtAndReturnPath(String choiceFromUser) {
        String path = airport.pathToFilesTxt + choiceFromUser + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(path);
            String[] arrayAllAirports = airport.getListAllAirports().toString().split("\\,");

            StringBuilder str = new StringBuilder();
            for (String line : arrayAllAirports) {
                str.append(line.replaceAll("[\\]\\[]", "") + "\n");
            }

            fileWriter.write(str.toString());
        } catch (Exception ex) {
            ex.getMessage();
        }
        return path;
    }


    @Override
    public String getBotUsername() {
        return "@airport_with_parsing_bot";
    }

    @Override
    public String getBotToken() {
        return "7671162068:AAEpUpEGFeoB1MYk3fWk6Z3njLrXvCUtU8Y";
    }
}
