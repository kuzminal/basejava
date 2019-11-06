package com.kuzmin.model;

import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private List<Organization> organizations;

    public OrganizationSection(SectionType sectionType, List<Organization> elements) {
        this.organizations = elements;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
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
            organizationsInformation.append(organization.toString() + "\n");
        }
        return organizationsInformation.toString();
    }
}
