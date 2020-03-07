package com.kuzmin.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlRootElement
public class OrganizationSection extends AbstractSection {
    private List<Organization> organizations = Collections.emptyList();

    public OrganizationSection(List<Organization> elements) {
        this.organizations = elements;
    }

    public OrganizationSection() {
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        if (organizations != null) {
            this.organizations = organizations;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(organizations, that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @Override
    public String toString() {
        StringBuilder organizationsInformation = new StringBuilder();
        for (Organization organization : organizations){
            organizationsInformation.append(organization.toString()).append("\n");
        }
        return organizationsInformation.toString();
    }

    public String toHTML() {
        StringBuilder organizationsInformation = new StringBuilder();
        for (Organization organization : organizations){
            organizationsInformation.append(organization.toHTML());
        }
        return organizationsInformation.toString();
    }

    public void removeOrganisation(Organization organization) {
        if (organization != null) {
            organizations.remove(organization);
        }
    }
}
