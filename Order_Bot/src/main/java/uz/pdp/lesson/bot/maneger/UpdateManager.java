package uz.pdp.lesson.bot.maneger;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.lesson.bot.handler.BaseHandler;
import uz.pdp.lesson.bot.handler.CallBackQueryHandler;
import uz.pdp.lesson.bot.handler.MassageHandler;

public class UpdateManager{
    private BaseHandler massegeHandler;
    private BaseHandler callBackHandler;

    public UpdateManager() {
        this.massegeHandler = new MassageHandler();
        this.callBackHandler = new CallBackQueryHandler();
    }


    public void manage(Update update){
        if (update.message() != null){
            massegeHandler.handle(update);
        }else if (update.callbackQuery() != null){
            callBackHandler.handle(update);
        }
    }
}
