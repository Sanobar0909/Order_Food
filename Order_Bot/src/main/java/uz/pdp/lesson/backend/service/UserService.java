package uz.pdp.lesson.backend.service;

import uz.pdp.lesson.backend.model.MyUser;
import uz.pdp.lesson.backend.repository.FileWriterAndLoader;
import uz.pdp.lesson.backend.statics.PathConstants;

import java.util.List;
import java.util.Objects;

public class UserService implements PathConstants {
    FileWriterAndLoader<MyUser> writerAndLoader;

    public UserService() {
        this.writerAndLoader = new FileWriterAndLoader<>(USERS_PATH);
    }

    public void save(MyUser myUser){
        List<MyUser> users = writerAndLoader.load(MyUser.class);
        for (int i = 0; i < users.size(); i++) {
            MyUser cur = users.get(i);
            if (Objects.equals(cur.getId(),myUser.getId())){
                users.set(i,myUser);
                writerAndLoader.write(users);
                return;
            }
        }
        users.add(myUser);
        writerAndLoader.write(users);
    }
    public MyUser get(Long id){
        List<MyUser> users = writerAndLoader.load(MyUser.class);
        for (int i = 0; i < users.size(); i++) {
            MyUser cur = users.get(i);
            if (Objects.equals(cur.getId(),id)){
                return cur;
            }
        }
        return null;
    }
}
