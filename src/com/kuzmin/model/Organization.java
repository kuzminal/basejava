package com.kuzmin.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private String title;
    private List<Experience> experiences;

    public Organization(String title, List<Experience> experiences) {
        this.title = title;
        this.experiences = experiences;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(experiences, that.experiences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, experiences);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(title + "\n");
        for (Experience experience : experiences){
            result.append(experience + "\n");
        }
        return result.toString();
    }
}
