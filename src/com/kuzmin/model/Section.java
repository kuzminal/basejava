package com.kuzmin.model;

public class Section {
    private SectionType sectionType;

    public Section(SectionType sectionType) {
        this.sectionType = sectionType;
    }

    public SectionType getSectionType() {
        return sectionType;
    }

    public void setSectionType(SectionType sectionType) {
        this.sectionType = sectionType;
    }
}
