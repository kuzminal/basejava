package com.kuzmin.model;

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
}
