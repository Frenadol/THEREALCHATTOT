package com.github.Frenadol.utils;

import com.github.Frenadol.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}
    private User selectedUser;

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
    }


    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}