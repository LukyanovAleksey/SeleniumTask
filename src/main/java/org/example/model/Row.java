package org.example.model;

import java.util.List;

public class Row {

    private List<Cell> row;

    public Row(List<Cell> row) {
        this.row = row;
    }

    public List<Cell> getCells() {
        return row;
    }

    public Cell getCell(int index) {
        return row.get(index);
    }

    public void setCells(List<Cell> row) {
        this.row = row;
    }
}
