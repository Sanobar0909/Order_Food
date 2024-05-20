package uz.pdp.lesson.bot.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.lesson.backend.model.MyUser;

public class MessageMaker {
    public SendMessage userMenu(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Welcome to Order food bot " + curUser.getFirstname());
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Foots").callbackData("FOOTS"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage enterPhoneNumber(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Phone Number");
        KeyboardButton[][] buttons ={
                { new KeyboardButton("Phone Number").requestContact(true) }
        };
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).oneTimeKeyboard(true).resizeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage adminMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Admin");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Add new food").callbackData("ADD_FOOD"),
                        new InlineKeyboardButton("Show food").callbackData("SHOW_FOOD")
                },
                {
                  new InlineKeyboardButton("BACK").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage chooseCategory(MyUser user){
        SendMessage sendMessage = new SendMessage(user.getId(),"Choose fast-food");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Fast-Food").callbackData("FAST_FOOD"),
                        new InlineKeyboardButton("Turkish food").callbackData("TURKISH_FOOD")
                },
                {
                        new InlineKeyboardButton("BACK").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage enterProductName(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter food name");
        InlineKeyboardButton[][] buttons = {

                {
                        new InlineKeyboardButton("Back").callbackData("BACK"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage enterPrise(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter food prise");
        InlineKeyboardButton[][] buttons = {

                {
                        new InlineKeyboardButton("Back").callbackData("BACK"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage enterPhoto(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter food photo");
        InlineKeyboardButton[][] buttons = {

                {
                        new InlineKeyboardButton("Back").callbackData("BACK"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage showProduct(MyUser user){
        SendMessage sendMessage = new SendMessage(user.getId(), "Products");
        InlineKeyboardButton[][] buttons = {

                {
                        new InlineKeyboardButton("Back to Mein menu").callbackData("BACK"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }
}
