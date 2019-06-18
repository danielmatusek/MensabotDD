import mensaApi.MealHandler;
import var.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

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
                else if(message_text.startsWith(Commands.alteMensa)) sendMeals(chat_id, 79, Constants.alteMensa, message_text);
                else if(message_text.startsWith(Commands.zeltMensa)) sendMeals(chat_id, 78, Constants.zelt, message_text);
                else if(message_text.startsWith(Commands.siedePunkt)) sendMeals(chat_id, 82, Constants.siedepunkt, message_text);
                else if(message_text.startsWith(Commands.wueins)) sendMeals(chat_id, 85, Constants.wuEins, message_text);
                else if(message_text.startsWith(Commands.biomensa)) sendMeals(chat_id, 88, Constants.biomensa, message_text);
                else if(message_text.startsWith(Commands.johannstadt)) sendMeals(chat_id, 87, Constants.johannstadt, message_text);
                else if(message_text.startsWith(Commands.grillpep)) sendNoMenuMessage(chat_id, Constants.grillpep, Constants.linkGrillCube);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMeals(long chat_id, int mensaId, String mensaName, String messageText) throws Exception {

        MealHandler mealHandler = new MealHandler();

        SendMessage message = new SendMessage()
                .setChatId(chat_id).enableMarkdown(true);
        String[] splitMessage = messageText.split("\\s+");
        if(splitMessage.length > 1){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(splitMessage[1].trim());
            } catch (ParseException pe) {{
                    System.out.println("EXCEPTION: !! WARNING ES BRENNT wrong input format, use yyyy-mm-dd");
                    return;
                }
            }
            Date date = dateFormat.parse(splitMessage[1]);
            System.out.println(date);
            String datum = dateFormat.format(date);
            execute(message.setText(mealHandler.getMealsForSending(mensaId, mensaName, datum)));

        } else {
            Date date  = java.util.Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String datum = sdf.format(date);
            execute(message.setText(mealHandler.getMealsForSending(mensaId, mensaName, datum)));
        }
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
