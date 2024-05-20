package uz.pdp.lesson;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.lesson.bot.maneger.UpdateManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot {
    public static final String BOT_TOKEN = "7130370005:AAEBoNo7lkxYCdJiTJrdH42ZAs8diNIy7tk";
    static final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    static  ThreadLocal<UpdateManager> updateHandlerThreadLocal = ThreadLocal.withInitial(UpdateManager::new);
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(BOT_TOKEN);
        bot.setUpdatesListener((updates) -> {
            for (Update update : updates) {
                CompletableFuture.runAsync(()->{
                    try {
                        updateHandlerThreadLocal.get().manage(update);
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },pool);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        },Exception::printStackTrace);
    }
}
