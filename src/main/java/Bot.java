import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    Airport airport = null;

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

            if (textMessage.compareToIgnoreCase("/start") == 0) {
                Long userId = update.getMessage().getFrom().getId();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(userId)
                        .text("Выберете действие")
                        .replyMarkup(keyboardWithMainCommands)
                        .build();

                try {
                    execute(sendMessage);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }
    }

    //TODO метод для работы с кнопками
    private void forWorkWithButtons(Update update) {
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);

            if (callbackData.equals(buttonForOutputAllAirports.getCallbackData())) {
                sendDocument.setDocument(new InputFile(new File(writeFileTxtAndReturnPath(callbackData))));
            }

            try {
                execute(sendDocument);
            } catch (Exception ex) {
                ex.getMessage();
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
                str.append(line + "\n");
            }

            fileWriter.write(str.toString());
        } catch (Exception ex) {
            ex.getMessage();
        }
        return path;
    }


    @Override
    public String getBotUsername() {
        return "@study_tg_java_bot";
    }

    @Override
    public String getBotToken() {
        return "7154507074:AAGgAs43pzip_1L_UldR_KWYAPp6jFWCKqQ";
    }
}
