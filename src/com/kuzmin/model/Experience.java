package com.kuzmin.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import com.kuzmin.util.YearMonthAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Experience implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlJavaTypeAdapter(YearMonthAdapter.class)
    @JsonSerialize(using = YearMonthSerializer.class)
    @JsonDeserialize(using = YearMonthDeserializer.class)
    private YearMonth startDate;
    @XmlJavaTypeAdapter(YearMonthAdapter.class)
    @JsonSerialize(using = YearMonthSerializer.class)
    @JsonDeserialize(using = YearMonthDeserializer.class)
    private YearMonth endDate;
    private String description;

    public Experience(YearMonth startDate, YearMonth endDate, String description){
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public Experience() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, description);
    }
}
