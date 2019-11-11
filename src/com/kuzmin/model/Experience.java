package com.kuzmin.model;

import java.time.YearMonth;

public class Experience {
    private YearMonth startDate;
    private YearMonth endDate;
    private String description;

    public Experience(YearMonth startDate, YearMonth endDate, String description){
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public YearMonth getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonth startDate) {
        this.startDate = startDate;
    }

    public YearMonth getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonth endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        String endDateNow = endDate.equals(YearMonth.now()) ? "по наст. врем" : endDate.toString();
        return startDate + " - " + endDateNow +
                " : " + description;
    }
}
