package edu.xjtlu.cpt403.util;

import java.io.Serializable;
import java.util.*;

public class RowData extends HashMap<String,String> implements Serializable {

    private int rowIndex;

    private Map<String, Integer> colNameToColIndex;


    public Map<String, Integer> getColNameToColIndex() {
        return colNameToColIndex;
    }

    public void setColNameToColIndex(Map<String, Integer> colNameToColIndex) {
        this.colNameToColIndex = colNameToColIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
