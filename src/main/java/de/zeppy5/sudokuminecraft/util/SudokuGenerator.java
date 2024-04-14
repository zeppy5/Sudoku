package de.zeppy5.sudokuminecraft.util;

public class SudokuGenerator {

    /*

    Algorithm by https://www.geeksforgeeks.org/program-sudoku-generator/

     */

    final int[][] mat;
    final int N;
    final int SRN;
    final int K;

    private SudokuGenerator(int N, int K) {
        this.N = N;
        this.K = K;

        // Compute square root of N
        double SRNd = Math.sqrt(N);
        SRN = (int) SRNd;

        mat = new int[N][N];
    }

    public void fillValues() {
        fillDiagonal();
        fillRemaining(0, SRN);
        removeKDigits();
    }

    private void fillDiagonal() {

        for (int i = 0; i < N; i = i + SRN)
            fillBox(i, i);
    }

    private boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < SRN; i++)
            for (int j = 0; j < SRN; j++)
                if (mat[rowStart+i][colStart+j] == num)
                    return false;

        return true;
    }

    private void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < SRN; i++) {
            for (int j = 0; j < SRN; j++) {
                do {
                    num = randomGenerator(N);
                } while (!unUsedInBox(row, col, num));

                mat[row+i][col+j] = num;
            }
        }
    }

    private int randomGenerator(int num) {
        return (int) Math.floor((Math.random() * num + 1));
    }

    private boolean CheckIfSafe(int i, int j, int num) {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i%SRN, j - j%SRN, num));
    }

    private boolean unUsedInRow(int i, int num) {
        for (int j = 0; j < N; j++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    private boolean unUsedInCol(int j, int num) {
        for (int i = 0; i < N; i++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    private boolean fillRemaining(int i, int j) {
        if (j >= N && i < N - 1) {
            i = i + 1;
            j = 0;
        }
        if (i >= N && j >= N)
            return true;

        if (i < SRN) {
            if (j < SRN)
                j = SRN;
        }
        else if (i < N - SRN) {
            if (j== (i/SRN) * SRN)
                j = j + SRN;
        }
        else {
            if (j == N-SRN) {
                i = i + 1;
                j = 0;
                if (i >= N)
                    return true;
            }
        }

        for (int num = 1; num <= N; num++) {
            if (CheckIfSafe(i, j, num)) {
                mat[i][j] = num;
                if (fillRemaining(i, j+1))
                    return true;

                mat[i][j] = 0;
            }
        }
        return false;
    }

    public void removeKDigits() {
        int count = K;
        while (count != 0) {
            int cellId = randomGenerator(N * N) - 1;

            int i = (cellId / N);
            int j = cellId % N;
            if (j != 0)
                j = j - 1;

            if (mat[i][j] != 0) {
                count--;
                mat[i][j] = 0;
            }
        }
    }

    public int[][] getMat() {
        return mat;
    }

    public static int[][] createSudoku() {
        SudokuGenerator sudokuGenerator = new SudokuGenerator(9, 20);
        sudokuGenerator.fillValues();
        return sudokuGenerator.getMat();
    }

}
