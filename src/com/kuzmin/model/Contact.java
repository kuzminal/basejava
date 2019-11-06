package com.kuzmin.model;

import java.util.Objects;

public class Contact {
    private String contact;

    public Contact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(this.contact, contact.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contact);
    }

    @Override
    public String toString() {
        return contact;
    }
}
