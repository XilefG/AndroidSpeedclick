package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.myapplication.util.Generator;
import com.example.myapplication.views.grid.Cell;
import com.example.myapplication.views.grid.Grid;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class GameEngine {
    private static GameEngine instance;

    private Context context;
    private int noOfMines = 15;
    private int width = 8;
    private int height = 8;
    private String gameType = "beginner";
    private String gameMode;
    private String endStatusText, endTimeText;
    private Chronometer timer;
    private TextView clicksRemainingWidget;
    private ProgressBar progress;
    private LocalDatabase data;
    private ArrayList<Cell> toExplore;
    private DatabaseReference highscoreDB;
    private int progressNo = 0;
    private double time;
    private int clicks = 0;
    private int clicksRemaining = 0;
    private String date;
    private double record = 0;
    private long startTime;
    private boolean available = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean loaded = false;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<Cell> soloGreens = new ArrayList<>();
    private ArrayList<Cell> positiveGreens = new ArrayList<>();
    private ArrayList<Cell> specialsPotentials = new ArrayList<>();
    private ArrayList<Pair<Cell, Integer>> specials = new ArrayList<>();

    private Cell[][] BoardGrid;

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    private GameEngine() {
    }

    public void createGrid(Context context, String gameMode) {
        this.gameMode = gameMode;
        this.context = context;
        this.gameStarted = false;
        this.gameOver = false;

        setGrid(context);
        if (gameMode.equals("standard")) {
            data = new LocalDatabase(context);
            if ((data.getSaveGame() && gameType.equals(data.getSavedGameType()) && this.width == data.getLength() && this.height == data.getHeight()) && data.getGridValues() != null || data.getFromSettings()) {
                data.setFromSettings(0);
                loadGame();
                revalidate();
                loaded = true;
                gameStarted = true;
            } else {
                loaded = false;
                data.resetStoredBoard();
            }
        } else if (gameMode.equals("puzzle")) {
            int x = (Math.round((float)(width/2)));
            int y = (Math.round((float)(height/2)));
            firstClick(x, y);
            storeInitialPuzzle();
            getPuzzleClicks();
            data.setPuzzleCreated(1);
        }
    }

    public void loadGame() {
        Integer[][][] storedBoard = data.getGridValues();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (storedBoard[x][y][0] != null) {
                    BoardGrid[x][y].setValue(storedBoard[x][y][0]);
                }
                if (storedBoard[x][y][1] != null) {
                    BoardGrid[x][y].setValue(storedBoard[x][y][1]);
                    BoardGrid[x][y].setClicked();
                }
                if (storedBoard[x][y][2] != null) {
                    BoardGrid[x][y].setValue(storedBoard[x][y][2]);
                    BoardGrid[x][y].setRevealed();
                }
            }
        }
    }

    public void resetGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BoardGrid[x][y].setValue(1);
                BoardGrid[x][y].invalidate();
            }
        }
        loaded = false;
        gameStarted = false;
        gameOver = false;
        timer.setText("000");
        MainActivity.resetBudgetTimer();
        progress.setProgress(0);
        progressNo = 0;
        progress.setMax((width * height) - noOfMines);
        available = false;
    }

    public void setDimensions(int mines, int setWidth, int setHeight, String gameType) {
        this.noOfMines = mines;
        this.width = setWidth;
        this.height = setHeight;
        this.gameType = gameType;
        BoardGrid = new Cell[width][height];
        checkAvailableUsername();

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void getWidgets(Chronometer timer, ProgressBar progress) {
        this.timer = timer;
        if(loaded) {
            timer.setText("---");
        }
        this.progress = progress;
        this.progress.setProgress(0);
        if(loaded) {
            this.progress.setMax(data.getSavedMaxProgress());
            this.progress.setProgress(data.getSavedGameProgress());
        } else {
            this.progress.setMax((width * height) - noOfMines);
        }
    }

    public void getPuzzleWidgets(TextView clicksRemaining, ProgressBar progress) {
        this.clicksRemainingWidget = clicksRemaining;
        this.progress = progress;
        this.progress.setProgress(0);
        if(data.getPuzzleCreated()) {
            this.progress.setMax(data.getSavedPuzzleMaxProgress());
            this.progress.setProgress(data.getSavedPuzzleProgress());
            this.clicksRemainingWidget.setText(Integer.toString(data.getSavedClicksRemaining()));
        } else {
            if (data.getPuzzleCreated()) {
                this.progress.setMax(data.getInitialPuzzleMaxProgress());
                this.progress.setProgress(data.getInitialPuzzleProgress());
                this.clicksRemainingWidget.setText(Integer.toString(data.getInitialPuzzleClicks()));
            } else {
                this.progress.setMax((width * height) - noOfMines);
            }
        }
    }

    public void revalidate() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BoardGrid[x][y].invalidate();
            }
        }
    }

    private void setGrid(Context context) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BoardGrid[x][y] = null;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (BoardGrid[x][y] == null) {
                    BoardGrid[x][y] = new Cell(context, x, y);
                }
                BoardGrid[x][y].invalidate();
            }
        }
    }

    private void setGridValues(int[][] grid) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BoardGrid[x][y].setValue(grid[x][y]);
            }
        }
    }

    public View getCellAt(int position) {
        int x = position % width;
        int y = position / width;

        return BoardGrid[x][y];
    }

    public Cell getCellAt(int x, int y) {
        return BoardGrid[x][y];
    }


    public void firstClick(int x, int y) {
        gameStarted = true;
        if(!loaded) {
            progressNo = 0;
        }
        int[][] generatedGrid = Generator.generate(noOfMines, width, height, x, y);
        setGridValues(generatedGrid);

        clickCell(x, y, true, false);
        for(Cell cell : checkAround(x, y)) {
            clickCell(cell.getXPos(), cell.getYPos(), true, false);
        }

        if (gameMode.equals("standard")) {
            timer.setBase(SystemClock.elapsedRealtime());
            startTime = System.nanoTime();
            timer.start();
        } else if (gameMode.equals("puzzle")) {
            storePuzzle();
        }
    }

    public void clickBlueUpButton(int x, int y) {

        Cell cellToClear;

        for (int i = -2; i < 3; i++) {
            if (y + i >= 0 && y + i < height) {

                cellToClear = getCellAt(x, y + i);

                if(!cellToClear.isClicked()) {

                    if(cellToClear.isMine()) {
                        cellToClear.defuseMine();
                        if(!gameOver)
                            progress.setMax(progress.getMax() + 1);
                    }

                    clickCell(x, y + i, false, false);
                }

            }
        }
    }

    public void clickBlueAcrossButton(int x, int y) {

        Cell cellToClear;

        for (int i = -2; i < 3; i++) {
            if (x + i >= 0 && x + i < width) {

                cellToClear = getCellAt(x + i, y);

                if(!cellToClear.isClicked()) {

                    if(cellToClear.isMine()) {
                        cellToClear.defuseMine();
                        if(!gameOver)
                            progress.setMax(progress.getMax() + 1);
                    }

                    clickCell(x + i, y, false, false);
                }

            }
        }
    }

    public void clickBlackButton(int x, int y) {

        for (Cell cellToClear : checkAround(x, y)){

            if (!cellToClear.isClicked()) {

                if (cellToClear.isMine()) {
                    cellToClear.defuseMine();
                    if(!gameOver)
                        progress.setMax(progress.getMax() + 1);
                }

                clickCell(cellToClear.getXPos(), cellToClear.getYPos(), false, false);
            }
        }
    }

    public void clickOrange(Cell cell, int spreadCount) {
        int x = cell.getXPos();
        int y = cell.getYPos();

        for(Cell toCheck : checkAround(x, y)) {
            if (!toCheck.isMine() && toCheck.isRevealed())
                toExplore.add(toCheck);
                if(spreadCount == 1)
                    clickOrange(toCheck,2);
        }

        if(spreadCount == 1) {
            for (Cell toClick : toExplore) {
                clickCell(toClick.getXPos(), toClick.getYPos(), false, false);
            }
        }


}

    public void clickCell(int x, int y, boolean expand, boolean realClick) {
        if(!gameOver) {
            Cell clickedCell = getCellAt(x, y);

            if (!gameStarted && !gameMode.equals("puzzle")) {
                clicks++;
                firstClick(x, y);
            }

            if (!clickedCell.isMine()) {
                if (!clickedCell.isClicked() && !gameOver) {
                    clickedCell.setClicked();
                    progressNo++;

                    if (realClick) {
                        switch (clickedCell.getValue()) {
                            case -2:
                                clickBlueUpButton(x, y);
                                break;
                            case -3:
                                clickBlueAcrossButton(x, y);
                                break;
                            case -4:
                                clickBlackButton(x, y);
                                break;
                            case -5:
                                toExplore = new ArrayList<>();
                                clickOrange(clickedCell, 1);
                                break;
                            case -6:
                                toExplore = new ArrayList<>();
                                clickOrange(clickedCell, 2);
                                for (Cell cell : toExplore) {
                                    clickCell(cell.getXPos(), cell.getYPos(), true, false);
                                }
                        }
                    }
                    revealSurrounding(x, y, expand);
                }
            } else {
                onGameLost();
                progress.setProgress(0);
            }

            if (realClick)
                clicks++;

            if (!gameOver) {
                progress.setProgress(progressNo);
                if (data.getPuzzleCreated() && gameMode.equals("puzzle")) {
                    clicksRemaining--;
                    clicksRemainingWidget.setText(Integer.toString(clicksRemaining));
                }
                checkEnd();

                if (gameOver) {
                    if (gameMode.equals("puzzle")) {
                        Toast.makeText(context, "Puzzle solved", Toast.LENGTH_LONG);
                    } else
                        dealWithEnd();
                }
            }
        }
    }

    private void revealSurrounding(int x, int y, boolean expand) {

        for(Cell cell : checkAround(x, y)) {
            cell.setRevealed();

            if (expand) {
                if (!anyMines(cell) && !cell.isMine()) {
                    clickCell(cell.getXPos(), cell.getYPos(), true, false);
                }
            }
        }
    }


    public boolean anyMines(Cell cell) {
        boolean mineExists = false;
        for(Cell checkCell : checkAround(cell.getXPos(), cell.getYPos())) {
            if(checkCell.isMine())
                mineExists = true;
        }
        return mineExists;
    }

    public ArrayList<Cell> checkAround(int x, int y) {
        ArrayList<Cell> aroundCells = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (x + i >= 0 && x + i < width && y + j >= 0 && y + j >= 0 && y + j < height && !(i == 0 && j == 0) && !getCellAt(x + i, y + j).isClicked()) {
                    aroundCells.add(getCellAt(x + i, y + j));
                }
            }
        }

        return aroundCells;
    }

    private boolean checkEnd() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!getCellAt(i, j).isClicked()) {
                    if (!getCellAt(i, j).isMine()) return false;
                }
            }
        }
        if (gameMode.equals("puzzle")) {
            if (clicksRemaining < 0)
                gameOver = true;
        }
        gameOver = true;
        return true;
    }

    private void dealWithEnd() {
        data.resetStoredBoard();
        progressNo = 0;
        gameOver = true;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.format(new Date());
        time = (System.nanoTime() - startTime) / 1e9;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        endTimeText = "" + formatter.format(time);
        timer.stop();
        if(!checkHighscore()) {
            endStatusText = "Victory!";
        } else {
            endStatusText = "New Highscore!";
        }
        playAgainShow();
        if(!gameType.equals("custom")) {
            data.updateGameWon(gameType);
        }
        data.addLevel(gameType);
        data.addData(gameType, time, date);

    }

    public void playAgainShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.play_again_ask, null);
        TextView endStatus = view.findViewById(R.id.endStatus);
        TextView endTime = view.findViewById(R.id.endTime);
        endStatus.setText(endStatusText);
        endTime.setText(endTimeText);
        Button selectBtn = view.findViewById(R.id.submitButton);
        Button cancelBtn = view.findViewById(R.id.cancelButton);
        builder.setView(view);
        final AlertDialog aD = builder.create();

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGrid();
                timer.setText("000");
                aD.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                context.startActivity(intent);
                aD.dismiss();
            }
        });
        aD.show();
    }

    public boolean checkHighscore() {
        boolean won = false;
        if (!gameType.equals("custom") && !loaded) {
            if (data.getHighscore(gameType) == 0) {
                submitData();
                won = true;
            } else if(data.getHighscore(gameType) > time) {
                submitData();
                won = true;
            }
        }
        return won;
    }

    public void checkAvailableUsername() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String getData = data.child("username").getValue(String.class);
                    usernames.add(getData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void submitData() {
        String username = data.getUsername();
        highscoreDB = FirebaseDatabase.getInstance().getReference();
        OnlineFinish storeRecord = new OnlineFinish(time, date, username);
        highscoreDB.child(gameType).push().setValue(storeRecord);
    }

    public void onGameLost() {
        data.resetStoredBoard();
        if(!gameType.equals("custom")) {
            data.updateGameLost(gameType);
        }
        endStatusText = "Defeat";
        endTimeText = "---";
        playAgainShow();
        gameOver = true;
        timer.stop();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (getCellAt(i, j).isMine()) getCellAt(i, j).setRevealed();
            }
        }
    }

    public void storeGame() {
        int clickStatus;
        data.resetStoredBoard();
        data.savedBoardDetails(width, height, gameType, progressNo, progress.getMax());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(BoardGrid[x][y].isClicked()) {
                    clickStatus = 1;
                } else if(BoardGrid[x][y].isRevealed()) {
                    clickStatus = 2;
                } else {
                    clickStatus = 0;
                }
                data.storeGame(x, y, BoardGrid[x][y].getValue(), clickStatus);
            }
        }
    }


    //PUZZLE STUFF

    public void storePuzzle() {
        int clickStatus;
        data.resetStoredPuzzle();
        data.savedPuzzleDetails(width, height, clicksRemaining, progressNo, progress.getMax());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(BoardGrid[x][y].isClicked()) {
                    clickStatus = 1;
                } else if(BoardGrid[x][y].isRevealed()) {
                    clickStatus = 2;
                } else {
                    clickStatus = 0;
                }
                data.storePuzzle(x, y, BoardGrid[x][y].getValue(), clickStatus);
            }
        }
    }

    public void loadPuzzle() {
        Integer[][][] storedPuzzle = data.getPuzzleGridValues();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (storedPuzzle[x][y][0] != null) {
                    BoardGrid[x][y].setValue(storedPuzzle[x][y][0]);
                }
                if (storedPuzzle[x][y][1] != null) {
                    BoardGrid[x][y].setValue(storedPuzzle[x][y][1]);
                    BoardGrid[x][y].setClicked();
                }
                if (storedPuzzle[x][y][2] != null) {
                    BoardGrid[x][y].setValue(storedPuzzle[x][y][2]);
                    BoardGrid[x][y].setRevealed();
                }
            }
        }
    }

    public void storeInitialPuzzle() {
        int clickStatus;
        data.resetInitialPuzzle();
        data.initialPuzzleDetails(width, height, progressNo, progress.getMax());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(BoardGrid[x][y].isClicked()) {
                    clickStatus = 1;
                } else if(BoardGrid[x][y].isRevealed()) {
                    clickStatus = 2;
                } else {
                    clickStatus = 0;
                }
                data.storeInitialPuzzle(x, y, BoardGrid[x][y].getValue(), clickStatus);
            }
        }
    }

    public void loadInitialPuzzle() {
        Integer[][][] storedPuzzle = data.getInitialPuzzleGridValues();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (storedPuzzle[x][y][0] != null) {
                    BoardGrid[x][y].setValue(storedPuzzle[x][y][0]);
                }
                if (storedPuzzle[x][y][1] != null) {
                    BoardGrid[x][y].setValue(storedPuzzle[x][y][1]);
                    BoardGrid[x][y].setClicked();
                }
                if (storedPuzzle[x][y][2] != null) {
                    BoardGrid[x][y].setValue(storedPuzzle[x][y][2]);
                    BoardGrid[x][y].setRevealed();
                }
            }
        }
        storePuzzle();
    }

    public void getPuzzleClicks() {
        int puzzleClicks = 0;
        while(!gameOver) {
            for (int x = 0; x < width; x++) {
                System.out.println();
                for (int y = 0; y < height; y++) {
                    if (!BoardGrid[x][y].isRevealed()) {
                        System.out.print("-");
                    } else if (BoardGrid[x][y].isClicked()) {
                        System.out.print("O");
                    } else if (BoardGrid[x][y].getValue() == 1) {
                        System.out.print("G");
                    } else if (BoardGrid[x][y].getValue() == -1) {
                        System.out.print("X");
                    } else
                        System.out.print("S");
                }
            }
            System.out.println();
            System.out.println("<><><><><><><><><>");
            System.out.println("<><><><><><><><><>");
            if(soloGreens.size() > 0) {
                clickRandom(soloGreens);
            } else if (positiveGreens.size() > 0) {
                clickRandom(positiveGreens);
            } else if (specials.size() > 0) {
                clickCell(specials.get(0).first.getXPos(), specials.get(0).first.getYPos(), false, true);
            }
            puzzleClicks++;
            soloGreens = new ArrayList<>();
            positiveGreens = new ArrayList<>();
            specials = new ArrayList<Pair<Cell, Integer>>();
            specialsPotentials = new ArrayList<>();
            checkSets();
        }
        clicksRemaining = puzzleClicks;
        data.setInitialPuzzleClicks(clicksRemaining);
    }

    public void checkSets() {
        checkSpecials();
        checkSpecialsPotentials();
        checkGreens();
    }

    public void checkGreens() {
        ArrayList<Cell> greens = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (BoardGrid[x][y].getValue() == 1 && BoardGrid[x][y].isRevealed() && !BoardGrid[x][y].isClicked()) {
                    greens.add(BoardGrid[x][y]);
                }
            }
        }
        checkSoloGreens(greens);


    }

    public void checkSoloGreens(ArrayList<Cell> greens) {
        ArrayList<Cell> nonSoloGreens = new ArrayList<>();
        for (int i = 0; i < greens.size(); i++) {
            boolean containsSpecial = false;
            ArrayList<Cell> surroundingCells = new ArrayList<>();
            surroundingCells = checkAround(greens.get(i).getXPos(), greens.get(i).getYPos());
            for (int j = 0; j < surroundingCells.size(); j++) {
                if (surroundingCells.get(j).isRevealed() && (surroundingCells.get(j).getValue() != 1 && surroundingCells.get(j).getValue() != -1)) {
                    containsSpecial = true;
                }
            }
            if (!containsSpecial) {
                soloGreens.add(greens.get(i));
            } else {
                nonSoloGreens.add(greens.get(i));
            }
        }
        checkPositiveGreens(nonSoloGreens);
    }

    public void checkPositiveGreens(ArrayList<Cell> nonSoloGreens) {
        for (int i = 0; i < nonSoloGreens.size(); i++) {
            int value = 1;
            if (specialsPotentials.contains(nonSoloGreens.get(i))) {
                value = 0;
            }
            value += getPositiveReveals(nonSoloGreens.get(i));
            if (value > 1) {
                positiveGreens.add(nonSoloGreens.get(i));
            }
        }
    }

    public int getPositiveReveals(Cell nonSoloGreen) {
        int revealCount = 0;
        ArrayList<Cell> aroundCells = checkAround(nonSoloGreen.getXPos(), nonSoloGreen.getYPos());
        for(int i = 0; i < aroundCells.size(); i++) {
            if(aroundCells.get(i).isRevealed() == false && specialsPotentials.contains(aroundCells.get(i))) {
                revealCount++;
            }
        }
        return revealCount;
    }

    public void checkSpecials() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((BoardGrid[x][y].getValue() != 1 && BoardGrid[x][y].getValue() != -1) && BoardGrid[x][y].isRevealed() && !BoardGrid[x][y].isClicked()) {
                    int value = 1;
                    ArrayList<Cell> aroundCells = checkAround(BoardGrid[x][y].getXPos(), BoardGrid[x][y].getYPos());
                    for (int i =0; i < aroundCells.size(); i++) {
                        if (aroundCells.get(i).isRevealed()) {
                            value++;
                        }
                    }
                    Pair<Cell, Integer> pairToAdd = new Pair<>(BoardGrid[x][y], value);
                    specials.add(pairToAdd);

                }
            }
        }
        Collections.sort(specials, new Comparator<Pair<Cell, Integer>>() {
            @Override
            public int compare(Pair<Cell, Integer> o1, Pair<Cell, Integer> o2) {
                return o1.second.compareTo(o2.second);
            }
        });
    }

    public void checkSpecialsPotentials() {
        for (int i = 0; i < specials.size(); i++) {
            ArrayList<Cell> aroundCells = checkAround(specials.get(i).first.getXPos(), specials.get(i).first.getYPos());
            for (int j = 0; j < aroundCells.size(); j++) {
                if (!specialsPotentials.contains(aroundCells.get(j))) {
                    specialsPotentials.add(aroundCells.get(j));
                }
            }
        }
    }

    public void clickRandom(ArrayList<Cell> cells) {
        Random rand = new Random();
        int n = rand.nextInt(cells.size());
        clickCell(cells.get(n).getXPos(), cells.get(n).getYPos(), false, true);
    }
}


