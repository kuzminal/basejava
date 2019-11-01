package com.kuzmin.model;

public class TextSection extends Section{
    private String textInformation;

    public TextSection(SectionType sectionType, String textInformation) {
        super(sectionType);
        this.textInformation = textInformation;
    }

    public String getTextInformation() {
        return textInformation;
    }

    public void setTextInformation(String textInformation) {
        this.textInformation = textInformation;
    }
}
