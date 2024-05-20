package uz.pdp.lesson.bot.handler;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import uz.pdp.lesson.backend.model.Product;
import uz.pdp.lesson.backend.model.Role;
import uz.pdp.lesson.bot.state.BaseState;
import uz.pdp.lesson.bot.state.child.AddFood;
import uz.pdp.lesson.bot.state.child.MainState;
import uz.pdp.lesson.bot.state.child.ShowFood;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MassageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        String text = message.text();
        super.update = update;
        super.curUser = getUserOrCreate(from);

        if (Objects.equals(text, "/start")) {
            if (Objects.isNull(curUser.getPhoneNumber()) || curUser.getPhoneNumber().isEmpty()) {
                enterPhoneNumber();
                System.out.println(from.username());
            } else {
                mainManu();
            }
        } else {
            String baseStateString = curUser.getBaseState();
            BaseState baseState = BaseState.valueOf(baseStateString);
            switch (baseState) {
                case MAIN_STATE -> mainState();
                case ADD_FOOD_STATE -> addFoodState();
                case SHOW_PRODUCT_STATE -> showFoodState();
                case BUY_FOOD_STATE -> buyFoodState();
            }
        }
    }

    private void showFoodState() {
        String stateStr = curUser.getState();
        ShowFood showFood = ShowFood.valueOf(stateStr);
        switch (showFood){
            case SHOW_FOOD -> {
                SendMessage sendMessage = messageMaker.chooseCategory(curUser);
                bot.execute(sendMessage);

            }
        }
    }

    private void buyFoodState() {

    }

    public void addFoodState() {
        String stateStr = curUser.getState();
        AddFood state = AddFood.valueOf(stateStr);
        switch (state) {
            case CHOOSE_FOOD_CATEGORY -> {
                Message message = update.message();
                String text = message.text();
                if (text == null) {
                    incorrectData("Product name");
                    SendMessage sendMessage = messageMaker.chooseCategory(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                assignCategoryToProduct(text);
                deleteMessage(message.messageId());
                deleteInlineKeyboardButton(message);
            }
            case FOOD_NAME -> {
                Message message = update.message();
                String text = message.text();
                if (text == null) {
                    incorrectData("Product name");
                    SendMessage sendMessage = messageMaker.enterProductName(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                Product basked = productServise.getNonCompletedBasked(curUser.getId());
                if (basked == null) {
                    basked = Product.builder()
                            .name(text)
                            .userid(curUser.getId())
                            .isCompleted(false)
                            .build();
                } else {
                    basked.setName(text);
                }
                productServise.save(basked);

                curUser.setState(AddFood.FOOD_PRISE.name());
                userService.save(curUser);

                SendMessage sendMessage = messageMaker.enterPrise(curUser);
                bot.execute(sendMessage);

                deleteMessage(message.messageId());
            }
            case FOOD_PRISE -> {
                Message message = update.message();
                String text = message.text();
                if (text == null || !text.matches("\\d+(\\.\\d+)?")) {
                    incorrectData("Product price");
                    SendMessage sendMessage = messageMaker.enterPrise(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                double price = Double.parseDouble(text);
                Product product = productServise.getNonCompletedBasked(curUser.getId());
                if (product != null) {
                    product.setPrise(price);
                    productServise.save(product);
                }
                curUser.setState(AddFood.FOOD_PHOTO.name());
                userService.save(curUser);

                SendMessage sendMessage = messageMaker.enterPhoto(curUser);
                bot.execute(sendMessage);

                deleteMessage(message.messageId());
            }
            case FOOD_PHOTO -> {
                Message message = update.message();
                PhotoSize[] photo = message.photo();
                if (photo == null) {
                    incorrectData("Photo");
                    SendMessage sendMessage = messageMaker.enterPhoto(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                Product basked = productServise.getNonCompletedBasked(curUser.getId());
                basked.setPhotoId(Arrays.toString(photo));
                basked.setCompleted(true);
                productServise.save(basked);
                deleteMessage(message.messageId());
            }
        }
    }

    private void assignCategoryToProduct(String text) {
        Product product = productServise.getNonCompletedBasked(curUser.getId());
        if (product == null) {
            product = Product.builder()
                    .userid(curUser.getId())
                    .category(text)
                    .isCompleted(false)
                    .build();
        } else {
            product.setCategory(text);
        }
        productServise.save(product);

        curUser.setState(AddFood.FOOD_NAME.name());
        userService.save(curUser);

        SendMessage sendMessage = messageMaker.enterProductName(curUser);
        bot.execute(sendMessage);
    }

    private void mainState() {
        String stateStr = curUser.getState();
        MainState state = MainState.valueOf(stateStr);
        switch (state) {
            case REGISTER -> {
                Message message = update.message();
                Contact contact = message.contact();
                if (contact != null) {
                    String phoneNumber = contact.phoneNumber();
                    curUser.setPhoneNumber(phoneNumber);
                    userService.save(curUser);
                    mainManu();
                } else {
                    incorrectData("Phone Number");
                }
            }
        }
    }

    private void incorrectData(String data) {
        bot.execute(new SendMessage(curUser.getId(), "You entered incorrect " + data));
        System.out.println(data);
    }

    private void mainManu() {
        if (curUser.getRole().equals(Role.ADMIN)) {
            SendMessage sendMessage = messageMaker.adminMenu(curUser);
            bot.execute(sendMessage);
            curUser.setBaseState(BaseState.MAIN_STATE.name());
            curUser.setState(MainState.ADMIN_MENU.name());
            userService.save(curUser);
        } else {
            SendMessage sendMessage = messageMaker.userMenu(curUser);
            bot.execute(sendMessage);
            curUser.setState(MainState.MAIN_MENU.name());
            userService.save(curUser);
        }
    }

    public void enterPhoneNumber() {
        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
        bot.execute(sendMessage);
        curUser.setBaseState(BaseState.MAIN_STATE.name());
        curUser.setState(MainState.REGISTER.name());
        userService.save(curUser);
    }
}
