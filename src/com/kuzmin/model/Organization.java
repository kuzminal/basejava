package com.kuzmin.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlRootElement
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String url;
    private List<Experience> experiences = Collections.emptyList();

    public Organization(String title, String url, List<Experience> experiences) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(experiences, "experiences must not be null");
        this.title = title;
        this.url = url;
        this.experiences = experiences;
    }

    public Organization() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        if (experiences != null) {
            this.experiences = experiences;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(url, that.url) &&
                Objects.equals(experiences, that.experiences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url, experiences);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(title).append("\n");
        result.append(url).append("\n");
        for (Experience experience : experiences){
            result.append(experience);
        }
        return result.toString();
    }

    public String toHTML() {
        StringBuilder result = new StringBuilder();
        result.append("<h4>").append(title).append("</h4>");
        result.append("<h4>").append(url).append("</h4>");
        for (Experience experience : experiences){
            result.append("<p>").append(experience).append("</p>");
        }
        return result.toString();
    }

    public void removeExperience(Experience experience) {
        if (experience != null) {
            experiences.remove(experience);
        }
    }

    public boolean isEmpty() {
        return url == null && title == null;
    }

    public void addExperience(Experience experience) {
        if (experience != null) {
            experiences.add(experience);
        }
    }
}
