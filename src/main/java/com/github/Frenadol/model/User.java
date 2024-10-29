package com.github.Frenadol.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String name;
    private String password;
    private byte[] profileImage;
    private List<User> contacts;


    public User(String name, String password, byte[] profileImage, List<User> contacts) {
        this.name = name;
        this.password = password;
        this.profileImage = profileImage;
        this.contacts = contacts != null ? contacts : new ArrayList<>();
    }

    public User(String name, byte[] password) {
        this.name = name;
        this.password = new String(password);
        this.contacts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public List<User> getContacts() {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        return contacts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts != null ? contacts : new ArrayList<>();
    }

    public void addContactToList(User contact) {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        contacts.add(contact);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }



    @Override
    public String toString() {
        return "========== Informacion del usuario ==========\n" +
                "Name          : " + name + "\n" +
                "Password      : " + password + "\n" +
                "Profile Image : " + profileImage + "\n" +
                "================================";
    }
}