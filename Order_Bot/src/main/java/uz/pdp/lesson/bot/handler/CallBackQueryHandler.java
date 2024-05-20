package uz.pdp.lesson.bot.handler;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.lesson.backend.model.Product;
import uz.pdp.lesson.bot.state.BaseState;
import uz.pdp.lesson.bot.state.child.AddFood;
import uz.pdp.lesson.bot.state.child.MainState;
import uz.pdp.lesson.bot.state.child.ShowFood;

import java.util.List;


public class CallBackQueryHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getUserOrCreate(from);
        super.update = update;
        String baseStateString = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateString);
        switch (baseState) {
            case MAIN_STATE -> mainSate();
            case ADD_FOOD_STATE -> addFoodState();
            case SHOW_PRODUCT_STATE -> showProductState();
        }
    }

    private void showProductState() {
        String state = curUser.getState();
        ShowFood showFood = ShowFood.valueOf(state);
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        String data = callbackQuery.data();

        switch (showFood){
            case SHOW_FOOD -> {
                if (data.equals("BACK")) {
                    curUser.setBaseState(BaseState.MAIN_STATE.name());
                    curUser.setState(MainState.MAIN_MENU.name());
                    userService.save(curUser);

                    SendMessage sendMessage = messageMaker.adminMenu(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                } else {
                    List<Product> products = productServise.getProductsByCategory(data);

                    for (Product product : products) {
                        if (product.getCategory().equalsIgnoreCase(data)) {
                            sendProductPhoto(curUser.getId(), product.getPhotoId());
                            String messageText = "Name: " + product.getName() +
                                    ", Price: " + product.getPrise() +
                                    "\n";

                            SendMessage sendMessage = new SendMessage(curUser.getId(), messageText);
                            bot.execute(sendMessage);
                        }
                    }
                }
            }
        }
    }

    private void sendProductPhoto(Long id, String photoId) {
        SendPhoto sendPhoto = new SendPhoto(id, photoId);
        bot.execute(sendPhoto);
    }

    private void addFoodState() {
        String state = curUser.getState();
        AddFood addFood = AddFood.valueOf(state);
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        String data = callbackQuery.data();

        switch (addFood) {
            case CHOOSE_FOOD_CATEGORY -> {
                if (data.equals("BACK")){
                    changeState(addFood);
                    SendMessage sendMessage = messageMaker.adminMenu(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                } else {
                    assignCategoryToProduct(data);
                    deleteMessage(message.messageId());
                    deleteInlineKeyboardButton(message);
                }
            }
            case FOOD_NAME -> {
                if (data.equals("BACK")) {
                    changeState(addFood);
                    SendMessage sendMessage = messageMaker.chooseCategory(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                }
            }
            case FOOD_PRISE -> {
                if (data.equals("BACK")) {
                    changeState(addFood);
                    SendMessage sendMessage = messageMaker.enterProductName(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                }
            }
            case FOOD_PHOTO -> {
                if (data.equals("BACK")) {
                    changeState(addFood);
                    SendMessage sendMessage = messageMaker.enterPrise(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                }
            }
        }
    }

    private void assignCategoryToProduct(String category) {
        Product product = productServise.getNonCompletedBasked(curUser.getId());
        if (product == null) {
            product = Product.builder()
                    .userid(curUser.getId())
                    .category(category)
                    .isCompleted(false)
                    .build();
        } else {
            product.setCategory(category);
        }
        productServise.save(product);

        curUser.setState(AddFood.FOOD_NAME.name());
        userService.save(curUser);

        SendMessage sendMessage = messageMaker.enterProductName(curUser);
        bot.execute(sendMessage);
    }

    private void mainSate() {
        String stateStr = curUser.getState();
        MainState state = MainState.valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();
        switch (state) {
            case MAIN_MENU -> {
                String data = callbackQuery.data();
                userMenu(data);
            }
            case ADMIN_MENU -> {
                String data = callbackQuery.data();
                adminMenu(data);
            }
        }
    }

    private void adminMenu(String data) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        switch (data) {
            case "ADD_FOOD" -> {
                addFood();
                deleteMessage(message.messageId());
            }
            case "SHOW_FOOD" -> {
                showFood();
                deleteMessage(message.messageId());
            }
        }
    }

    private void showFood() {
        SendMessage sendMessage = messageMaker.chooseCategory(curUser);
        bot.execute(sendMessage);
        curUser.setBaseState(BaseState.SHOW_PRODUCT_STATE.name());
        curUser.setState(ShowFood.SHOW_FOOD.name());
        userService.save(curUser);
    }

    private void addFood() {
        SendMessage sendMessage = messageMaker.chooseCategory(curUser);
        bot.execute(sendMessage);
        curUser.setBaseState(BaseState.ADD_FOOD_STATE.name());
        curUser.setState(AddFood.CHOOSE_FOOD_CATEGORY.name());
        userService.save(curUser);
    }

    private void userMenu(String data) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        switch (data) {
            case "FOOTS" -> {
                deleteMessage(message.messageId());
            }
        }
    }

    private void changeState(AddFood state) {
        AddFood pervState = state.getPervState();
        if (pervState == null) {
            curUser.setBaseState(BaseState.MAIN_STATE.name());
            curUser.setState(MainState.MAIN_MENU.name());
        } else {
            curUser.setState(pervState.name());
        }
        userService.save(curUser);
    }
}
