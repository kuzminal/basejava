package com.kuzmin.model;

public enum ContactType {
    PHONE("Телефон"),
    EMAIL("e-mail"),
    ICQ("ICQ"),
    GIT("github");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
