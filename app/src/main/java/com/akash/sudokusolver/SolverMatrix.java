package com.akash.sudokusolver;

public class SolverMatrix {

    private int[][] cells = new int[9][9];

    public SolverMatrix(int[][] cells) {
        for(int i=0; i<9; i++){
            System.arraycopy(cells[i], 0, this.cells[i], 0, 9);
        }
    }

    private boolean isSafe(int row, int col, int val, int[][] cells) {
        for (int k = 0; k < 9; ++k)
            if (val == cells[k][col])
                return false;
        for (int k = 0; k < 9; ++k)
            if (val == cells[row][k])
                return false;
        int boxRowOffset = (row / 3) * 3;
        int boxColOffset = (col / 3) * 3;
        for (int k = 0; k < 3; ++k)
            for (int m = 0; m < 3; ++m)
                if (val == cells[boxRowOffset + k][boxColOffset + m])
                    return false;
        return true;
    }

    boolean solve(int row, int col) {
        if (row == 9) {
            row = 0;
            if (++col == 9)
                return true;
        }
        if (cells[row][col] != 0)
            return solve(row + 1, col);
        for (int val = 1; val <= 9; ++val) {
            if (isSafe(row, col, val, cells)) {
                cells[row][col] = val;
                if (solve(row + 1, col))
                    return true;
            }
        }
        cells[row][col] = 0;
        return false;
    }

    int[][] getSolution(int row, int col) {
        solve(row, col);
        return cells;
    }

}
