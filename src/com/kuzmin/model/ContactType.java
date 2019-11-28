package com.kuzmin.model;

import com.google.gson.annotations.SerializedName;

public enum ContactType {
    @SerializedName("PHONE")
    PHONE("Телефон"),
    @SerializedName("EMAIL")
    EMAIL("e-mail"),
    @SerializedName("ICQ")
    ICQ("ICQ"),
    @SerializedName("GIT")
    GIT("github");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
