package org.example.model;

import java.util.List;

public class Table {
    private List<Row> rows;

    public Table(List<Row> rows) {
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}