package com.kuzmin.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class TextSection extends AbstractSection {
    private String textInformation;

    public TextSection(SectionType sectionType, String textInformation) {
        this.textInformation = textInformation;
    }

    public TextSection() {
    }

    public String getTextInformation() {
        return textInformation;
    }

    public void setTextInformation(String textInformation) {
        this.textInformation = textInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return Objects.equals(textInformation, that.textInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textInformation);
    }

    @Override
    public String toString() {
        return textInformation + "\n";
    }
}
