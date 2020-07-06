package com.example.myapplication.util;

import java.util.Random;

public class Generator {

    public static int[][] generate(int noOfMines, int width, int height, int startX, int startY) {
        Random r = new Random();
        int[] unavailableX = {startX-1, startX, startX+1};
        int[] unavailableY = {startY-1, startY, startY+1};

        int[][] grid = new int[width][height];

        for(int x=0; x<width; x++) {
            grid[x] = new int[height];
        }

        while(noOfMines > 0) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            boolean availableX = true;
            boolean availableY = true;
            for(int n : unavailableX) {
                if(n == x) availableX = false;
            }
            for(int n : unavailableY) {
                if(n == y) availableY = false;
            }

            if(grid[x][y] != -1 && (availableX || availableY)) {
                grid[x][y] = -1;
                noOfMines--;
            }
        }

        int noOfCells = width*height;

        while(noOfCells > 0) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            int z = r.nextInt(1000);

            boolean availableX = true;
            boolean availableY = true;
            for(int n : unavailableX) {
                if(n == x) availableX = false;
            }
            for(int n : unavailableY) {
                if(n == y) availableY = false;
            }

            if(grid[x][y] == 0 && (availableX || availableY)) {
                if(z > 0) grid[x][y] = 1;
                if(z > 600) grid[x][y] = -6;
                if(z > 900) grid[x][y] = -2;
                if(z > 925) grid[x][y] = -3;
                if(z > 950) grid[x][y] = -4;
                if(z > 975) grid[x][y] = -5;
            }
            noOfCells--;
        }
        grid = calculateNeighbours(grid, width, height);

        return grid;
    }

    private static int[][] calculateNeighbours(int[][] grid, int width, int height) {
        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++) {
                grid[x][y] = getNeighbourNumber(grid, x, y, width, height);
            }
        }

        return grid;
    }

    private static int getNeighbourNumber(int grid[][], int x, int y, int width, int height) {
        if(grid[x][y] < 0) {
            return grid[x][y];
        }

        int count = 0;
        if(isMineAt(grid, x-1, y+1, width, height))
            count++;
        if(isMineAt(grid, x, y+1, width, height))
            count++;
        if(isMineAt(grid, x+1, y+1, width, height))
            count++;
        if(isMineAt(grid, x-1, y, width, height))
            count++;
        if(isMineAt(grid, x+1, y, width, height))
            count++;
        if(isMineAt(grid, x-1, y-1, width, height))
            count++;
        if(isMineAt(grid, x, y-1, width, height))
            count++;
        if(isMineAt(grid, x+1, y-1, width, height))
            count++;

        return count;
    }

    private static boolean isMineAt(int [][] grid, int x, int y, int width, int height) {
        if(x >= 0 && y>=0 && x<width && y<height) {
            if(grid[x][y] == -1)
                return true;
        }
        return false;
    }
}
