package com.example.frontend;

import android.content.Context;
import android.view.View;

public class GameBoard extends View {

    private int numRows;
    private int numColumns;


    GameBoard (Context context, int numRows, int numColumns) {
        super(context);
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }
}
