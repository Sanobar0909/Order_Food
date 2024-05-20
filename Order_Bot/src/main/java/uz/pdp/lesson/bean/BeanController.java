package uz.pdp.lesson.bean;

import com.pengrad.telegrambot.model.message.origin.MessageOriginHiddenUser;
import uz.pdp.lesson.backend.service.ProductServise;
import uz.pdp.lesson.bot.handler.MassageHandler;
import uz.pdp.lesson.bot.maker.MessageMaker;
import uz.pdp.lesson.backend.service.UserService;

public interface BeanController {
    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);
    ThreadLocal<MessageMaker> messageMakerByThreadLocal = ThreadLocal.withInitial(MessageMaker::new);
    ThreadLocal<ProductServise> productServiceByThreadLocal = ThreadLocal.withInitial(ProductServise::new);
}
