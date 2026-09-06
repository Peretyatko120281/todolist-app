package ru.alex.entity.dto;

import ru.alex.entity.Record;

import java.util.List;

public class RecordsConteinerDto {
    private String userName;
    private List<Record> records;
    private long numberOfDoneRecords;
    private long numberOfActiveRecords;

    public RecordsConteinerDto(String userName, List<Record> records, int numberOfDoneRecords, int numberOfActiveRecords) {
        this.userName = userName;
        this.records = records;
        this.numberOfDoneRecords = numberOfDoneRecords;
        this.numberOfActiveRecords = numberOfActiveRecords;
    }

    public RecordsConteinerDto() {

    }

    public List<Record> getRecords() {
        return records;
    }

    public long getNumberOfDoneRecords() {
        return numberOfDoneRecords;
    }

    public long getNumberOfActiveRecords() {
        return numberOfActiveRecords;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
    public void setNumberOfActiveRecords(long numberOfActiveRecords) {
        this.numberOfActiveRecords = numberOfActiveRecords;
    }

    public void setNumberOfDoneRecords(long numberOfDoneRecords) {
        this.numberOfDoneRecords = numberOfDoneRecords;
    }
}
