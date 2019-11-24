package com.kuzmin.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrganizationSection.class, name = "organization"),
        @JsonSubTypes.Type(value = TextSection.class, name = "textSection"),
        @JsonSubTypes.Type(value = TextListSection.class, name = "textListSection")
        })
public abstract class AbstractSection implements Serializable {
}
