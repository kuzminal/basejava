package com.kuzmin.model;

import java.util.Objects;

public class Contact {
    private String contactContent;

    public Contact(String contactContent) {
        this.contactContent = contactContent;
    }

    public String getContactContent() {
        return contactContent;
    }

    public void setContactContent(String contactContent) {
        this.contactContent = contactContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(contactContent, contact.contactContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactContent);
    }

    @Override
    public String toString() {
        return contactContent;
    }
}
