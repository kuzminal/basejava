package com.kuzmin.model;

import java.util.List;

public class ExperienceAndEducationSection extends Section {
    private List<ExperienceAndEducationDescription> elements;

    public ExperienceAndEducationSection(SectionType sectionType, List<ExperienceAndEducationDescription> elements) {
        super(sectionType);
        this.elements = elements;
    }

    public List<ExperienceAndEducationDescription> getElements() {
        return elements;
    }

    public void setElements(List<ExperienceAndEducationDescription> elements) {
        this.elements = elements;
    }
}
