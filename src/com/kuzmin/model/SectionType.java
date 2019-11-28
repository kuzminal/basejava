package com.kuzmin.model;

import com.google.gson.annotations.SerializedName;

public enum SectionType {
    @SerializedName("PERSONAL")
    PERSONAL("Личные качества"),
    @SerializedName("OBJECTIVE")
    OBJECTIVE("Позиция"),
    @SerializedName("ACHIEVEMENT")
    ACHIEVEMENT("Достижения"),
    @SerializedName("QUALIFICATIONS")
    QUALIFICATIONS("Квалификация"),
    @SerializedName("EXPERIENCE")
    EXPERIENCE("Опыт работы"),
    @SerializedName("EDUCATION")
    EDUCATION("Образование");

    private final String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
