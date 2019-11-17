package com.kuzmin.model;

import java.util.List;
import java.util.Objects;

public class TextListSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private List<String> textInformations;

    public TextListSection(SectionType sectionType, List<String> textInformations) {
        this.textInformations = textInformations;
    }

    public List<String> getTextInformations() {
        return textInformations;
    }

    public void setTextInformations(List<String> textInformations) {
        this.textInformations = textInformations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextListSection that = (TextListSection) o;
        return Objects.equals(textInformations, that.textInformations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textInformations);
    }

    @Override
    public String toString() {
        StringBuilder information = new StringBuilder();
        for (String sectionInformation : textInformations) {
            information.append(sectionInformation + "\n");
        }
        return information.toString();
    }
}
