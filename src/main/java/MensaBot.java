import mensaApi.MealHandler;
import var.*;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MensaBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            try {
                if(message_text.startsWith(Commands.startCommand)) sendGreetings(chat_id);
                else if(message_text.startsWith(Commands.helpCommand)) sendHelp(chat_id);
                else if(message_text.startsWith(Commands.alteMensa)) sendMeals(chat_id, 79, Constants.alteMensa);
                else if(message_text.startsWith(Commands.zeltMensa)) sendMeals(chat_id, 78, Constants.zelt);
                else if(message_text.startsWith(Commands.siedePunkt)) sendMeals(chat_id, 82, Constants.siedepunkt);
                else if(message_text.startsWith(Commands.wueins)) sendMeals(chat_id, 85, Constants.wuEins);
                else if(message_text.startsWith(Commands.biomensa)) sendMeals(chat_id, 88, Constants.biomensa);
                else if(message_text.startsWith(Commands.johannstadt)) sendMeals(chat_id, 87, Constants.johannstadt);
                else if(message_text.startsWith(Commands.grillpep)) sendNoMenuMessage(chat_id, Constants.grillpep, Constants.linkGrillCube);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMeals(long chat_id, int mensaId, String mensaName) throws Exception {

        MealHandler mealHandler = new MealHandler();

        SendMessage message = new SendMessage()
                .setChatId(chat_id).enableMarkdown(true);

        execute(message.setText(mealHandler.getMealsForSending(mensaId, mensaName)));
    }

    public void sendGreetings(long chat_id) {

        String output = Constants.greetings;
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .enableMarkdown(true)
                .setText(output);

        executeMessage(message);
    }

    public void sendHelp(long chat_id) {

        String output = "Tippe '/' um die Auswahlmöglichkeiten zu sehen.";
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(output);

        executeMessage(message);
    }

    public void sendNoMenuMessage(long chat_id, String mensaName, String link) throws Exception {

        String output = "Für " + mensaName + " steht leider kein tägliches Menü zur Verfügung.\n"
                + "Bitte besuche folgenden Link, um dich über die Speisekarte zu informieren:\n"
                + link;

        SendMessage message =  new SendMessage()
                .setChatId(chat_id)
                .setText(output);

        executeMessage(message);
    }

    public void executeMessage (SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "ddmensa_bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
