package com.kuzmin.model;

import java.util.List;

public class TextListSection extends Section{
    private List<String> textInformations;

    public TextListSection(SectionType sectionType, List<String> textInformations) {
        super(sectionType);
        this.textInformations = textInformations;
    }


    public List<String> getTextInformations() {
        return textInformations;
    }

    public void setTextInformations(List<String> textInformations) {
        this.textInformations = textInformations;
    }
}
