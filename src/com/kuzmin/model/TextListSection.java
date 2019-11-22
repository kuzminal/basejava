package com.kuzmin.model;

import java.util.List;
import java.util.Objects;

public class TextListSection extends AbstractSection {
    private List<String> textInformation;

    public TextListSection(SectionType sectionType, List<String> textInformation) {
        this.textInformation = textInformation;
    }

    public List<String> getTextInformation() {
        return textInformation;
    }

    public void setTextInformation(List<String> textInformation) {
        this.textInformation = textInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextListSection that = (TextListSection) o;
        return Objects.equals(textInformation, that.textInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textInformation);
    }

    @Override
    public String toString() {
        StringBuilder information = new StringBuilder();
        for (String sectionInformation : textInformation) {
            information.append(sectionInformation + "\n");
        }
        return information.toString();
    }
}
