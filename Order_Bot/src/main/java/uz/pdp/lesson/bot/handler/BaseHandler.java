package uz.pdp.lesson.bot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import uz.pdp.lesson.Bot;
import uz.pdp.lesson.backend.model.Product;
import uz.pdp.lesson.backend.model.Role;
import uz.pdp.lesson.backend.service.ProductServise;
import uz.pdp.lesson.bean.BeanController;
import uz.pdp.lesson.bot.maker.MessageMaker;
import uz.pdp.lesson.backend.model.MyUser;
import uz.pdp.lesson.backend.service.UserService;
import uz.pdp.lesson.bot.state.BaseState;
import uz.pdp.lesson.bot.state.child.MainState;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Product product;
    protected Update update;
    protected UserService userService;
    protected MessageMaker messageMaker;
    protected ProductServise productServise;

    public BaseHandler() {
        this.bot = new TelegramBot(Bot.BOT_TOKEN);
        this.userService = BeanController.userServiceByThreadLocal.get();
        this.messageMaker = BeanController.messageMakerByThreadLocal.get();
        this.productServise = BeanController.productServiceByThreadLocal.get();
    }

    public abstract void handle(Update update);


    protected MyUser getUserOrCreate(User from){
        MyUser myUser = userService.get(from.id());
        if (myUser==null){
            myUser = MyUser.builder()
                    .id(from.id())
                    .username(from.username())
                    .baseState(BaseState.MAIN_STATE.name())
                    .state(MainState.REGISTER.name())
                    .firstname(from.firstName())
                    .lastname(from.lastName())
                    .role(Role.USER)
                    .build();
            userService.save(myUser);
        }
        return myUser;
    }

    protected void deleteMessage(int messageId){
        DeleteMessage deleteMessage = new DeleteMessage(curUser.getId(), messageId);
        bot.execute(deleteMessage);
    }
    protected void deleteInlineKeyboardButton(Message message) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(message.chat().id(), message.messageId());
        bot.execute(editMessageReplyMarkup);
    }
}
