package remoteokdesktop.service;

import lombok.Getter;
import lombok.Setter;
import remoteokdesktop.model.User;

public class LoggedUser {
    private static User user;

    public static void setUser(User user) {
        user = user;
    }

    public static User getUser() {
        return user;
    }
}
