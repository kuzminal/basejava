package com.kuzmin.model;

import com.google.gson.annotations.SerializedName;

public enum ContactType {
    @SerializedName("PHONE")
    PHONE("Телефон"),
    @SerializedName("EMAIL")
    EMAIL("e-mail") {
        @Override
        public String toHTML0(String value) {
            return value == null ? "" : "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    @SerializedName("ICQ")
    ICQ("ICQ"),
    @SerializedName("GIT")
    GIT("github"),
    @SerializedName("SKYPE")
    SKYPE("Skype") {
        @Override
        public String toHTML0(String value) {
            return value == null ? "" : "<a href='skype:" + value + "'>" + value + "</a>";
        }
    };

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHTML0 (String value) {
        return title + " : " + value;
    }

    public String toHTML(String value) {
        return value == null ? "" : toHTML0(value);
    }
}
