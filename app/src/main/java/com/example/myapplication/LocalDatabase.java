package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "appData";
    private static final String COLUMN_ID = "id";

    private static final String TABLE_HIGHSCORES = "highscores";
    private static final String COLUMN_GAMETYPE = "gameType";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_DATE = "date";

    private static final String TABLE_USER = "user";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_BEGINNERWINS = "beginnerWins";
    private static final String COLUMN_BEGINNERLOSSES = "beginnerLosses";
    private static final String COLUMN_INTERMEDIATEWINS = "intermediateWins";
    private static final String COLUMN_INTERMEDIATELOSSES = "intermediateLosses";
    private static final String COLUMN_EXPERTWINS = "expertWins";
    private static final String COLUMN_EXPERTLOSSES = "expertLosses";
    private static final String COLUMN_LEVEL = "level";

    private static final String TABLE_STATUS = "status";
    private static final String COLUMN_DEFAULTSTART = "defaultStart";
    private static final String COLUMN_DEFAULTMINES = "defaultMines";
    private static final String COLUMN_DEFAULTX = "defaultX";
    private static final String COLUMN_DEFAULTY = "defaultY";
    private static final String COLUMN_CONFIRMONRESTART = "confirmOnRestart";
    private static final String COLUMN_CURRENTGAMETYPE = "currentGameType";
    private static final String COLUMN_SAVEGAME = "saveGame";
    private static final String COLUMN_FROMSETTINGS = "fromSettings";
    private static final String COLUMN_PUZZLE_CREATED = "puzzleCreated";
    private static final String COLUMN_INITIAL_PUZZLE_CLICKS = "initialPuzzleClicks";
    private static final String COLUMN_STYLE = "style";

    private static final String TABLE_SAVED_STATUS = "savedStatus";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_SAVEDGAMETYPE = "savedGameType";
    private static final String COLUMN_SAVEDGAMEPROGESS = "savedGameProgress";
    private static final String COLUMN_SAVEDPROGRESSMAX = "progressMax";
    private static final String COLUMN_CLICKSREMAINING = "clicksRemaining";

    private static final String TABLE_BOARD = "board";
    private static final String TABLE_PUZZLE = "puzzle";
    private static final String TABLE_STORED_PUZZLE = "storedPuzzle";
    private static final String COLUMN_ROW = "X";
    private static final String COLUMN_COLUMN = "Y";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_CLICKED = "clicked";



    public LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HIGHSCORES + "(" +
                                            COLUMN_ID + " INTEGER PRIMARY KEY," +
                                            COLUMN_GAMETYPE + " TEXT," +
                                            COLUMN_TIME + " DOUBLE," +
                                            COLUMN_DATE + " TEXT)";
        db.execSQL(CREATE_HIGHSCORES_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
                                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                                    COLUMN_USERNAME + " TEXT," +
                                    COLUMN_BEGINNERWINS + " INTEGER," +
                                    COLUMN_BEGINNERLOSSES + " INTEGER," +
                                    COLUMN_INTERMEDIATEWINS + " INTEGER," +
                                    COLUMN_INTERMEDIATELOSSES + " INTEGER," +
                                    COLUMN_EXPERTWINS + " INTEGER," +
                                    COLUMN_EXPERTLOSSES + " INTEGER," +
                                    COLUMN_LEVEL + " INETGER)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STATUS + "(" +
                                        COLUMN_ID + " INTEGER PRIMARY KEY," +
                                        COLUMN_DEFAULTSTART + " INTEGER," +
                                        COLUMN_DEFAULTMINES + " INTEGER," +
                                        COLUMN_DEFAULTX + " INTEGER," +
                                        COLUMN_DEFAULTY + " INTEGER," +
                                        COLUMN_CONFIRMONRESTART + " INTEGER," +
                                        COLUMN_CURRENTGAMETYPE + " TEXT," +
                                        COLUMN_SAVEGAME + " INTEGER," +
                                        COLUMN_FROMSETTINGS + " INTEGER," +
                                        COLUMN_PUZZLE_CREATED + " INTEGER," +
                                        COLUMN_INITIAL_PUZZLE_CLICKS + " INTEGER," +
                                        COLUMN_STYLE + " INTEGER)";

        db.execSQL(CREATE_STATUS_TABLE);

        String CREATE_SAVED_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SAVED_STATUS + "(" +
                                            COLUMN_ID + " INTEGER PRIMARY KEY," +
                                            COLUMN_LENGTH + " INTEGER," +
                                            COLUMN_HEIGHT + " INTEGER," +
                                            COLUMN_SAVEDGAMETYPE + " TEXT," +
                                            COLUMN_SAVEDGAMEPROGESS + " INTEGER," +
                                            COLUMN_SAVEDPROGRESSMAX + " INTEGER," +
                                            COLUMN_CLICKSREMAINING + " INTEGER)";
        db.execSQL(CREATE_SAVED_STATUS_TABLE);

        String CREATE_BOARD_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BOARD + "(" +
                                        COLUMN_ID + " INTEGER PRIMARY KEY," +
                                        COLUMN_ROW + " INTEGER," +
                                        COLUMN_COLUMN + " INTEGER," +
                                        COLUMN_VALUE + " INTEGER," +
                                        COLUMN_CLICKED + " INTEGER)";
        db.execSQL(CREATE_BOARD_TABLE);

        String CREATE_PUZZLE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PUZZLE + "(" +
                                        COLUMN_ID + " INTEGER PRIMARY KEY," +
                                        COLUMN_ROW + " INTEGER," +
                                        COLUMN_COLUMN + " INTEGER," +
                                        COLUMN_VALUE + " INTEGER," +
                                        COLUMN_CLICKED + " INTEGER)";
        db.execSQL(CREATE_PUZZLE_TABLE);

        String CREATE_STORED_PUZZLE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STORED_PUZZLE + "(" +
                                                COLUMN_ID + " INTEGER PRIMARY KEY," +
                                                COLUMN_ROW + " INTEGER," +
                                                COLUMN_COLUMN + " INTEGER," +
                                                COLUMN_VALUE + " INTEGER," +
                                                COLUMN_CLICKED + " INTEGER)";
        db.execSQL(CREATE_STORED_PUZZLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUZZLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORED_PUZZLE);

        onCreate(db);
    }

    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE status");
    }

    //HIGHSCORE METHODS

    public void addData(String gameType, double time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GAMETYPE, gameType);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_DATE, date);

        db.insert(TABLE_HIGHSCORES, null, values);
        db.close();
    }

    public double getHighscore(String gameType) {
        String query = "SELECT time FROM highscores WHERE gametype = '" + gameType + "' ORDER BY time";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getDouble(0);
        }
        return 0;
    }

    public HashMap<Double, String> getTimes(String gameType) {
        HashMap<Double, String> timeList = new HashMap<>();
        String query = "SELECT time, date FROM highscores WHERE gametype = '" + gameType + "' ORDER BY time";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        while(data.moveToNext()) {
            timeList.put(data.getDouble(0), data.getString(1));
        }

        return timeList;

    }


    public double getAverageTime(String gameType) {
        String query = "SELECT time FROM highscores WHERE gametype = '" + gameType + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        int totalData = data.getCount();
        double totalTime = 0;

        if(totalData != 0) {
            for(int i = 0; i < totalData; i++) {
                data.moveToNext();
                totalTime += data.getDouble(0);
            }
            double average = totalTime/totalData;
            return average;
        }
        return 0;
    }

    //USER METHODS

    public void addUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_BEGINNERWINS, 0);
        values.put(COLUMN_BEGINNERLOSSES, 0);
        values.put(COLUMN_INTERMEDIATEWINS, 0);
        values.put(COLUMN_INTERMEDIATELOSSES, 0);
        values.put(COLUMN_EXPERTWINS, 0);
        values.put(COLUMN_EXPERTLOSSES, 0);
        values.put(COLUMN_LEVEL, 10);

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public boolean checkAvailableUsername(String username) {
        String query = "SELECT username FROM user WHERE username = '" + username + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.getCount() == 0) {
            return true;
        } else
            return false;
    }

    public String getUsername() {
        String query = "SELECT username FROM user";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst())
            return data.getString(0);
        else
            return "Invalid";
    }

    public int getGamesWon(String gameType) {
        String query = "SELECT " + gameType + "Wins FROM user";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst())
            return data.getInt(0);
        else
            return 0;
    }

    public int getGamesLost(String gameType) {
        String query = "SELECT " + gameType + "Losses FROM user";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst())
            return data.getInt(0);
        else
            return 0;
    }

    public int getLevel() {
        String query = "SELECT level FROM user";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst())
            return data.getInt(0);
        else
            return 0;
    }

    public void addLevel(String gameType) {
        int toAdd;
        if(gameType.equals("beginner")) {
            toAdd = 1;
        } else if(gameType.equals("intermediate")) {
            toAdd = 3;
        } else {
            toAdd = 5;
        }
        int newLevel = getLevel() + toAdd;
        String query = "UPDATE user SET level = " + newLevel;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();

    }
    public int getTotalPlayed(String gameType) {
        int gamesPlayed;
        int gamesWon = getGamesWon(gameType);
        int gamesLost = getGamesLost(gameType);

        gamesPlayed = gamesWon + gamesLost;
        return gamesPlayed;
    }

    public double getWinPercent(String gameType) {
        double winPercentage;
        int gamesWon = getGamesWon(gameType);
        int totalGamesPlayed = getGamesLost(gameType)+ getGamesWon(gameType);

        if(totalGamesPlayed != 0)
            winPercentage = (double)gamesWon/totalGamesPlayed;
        else
            winPercentage = 0;

        return winPercentage;
    }


    public void updateGameWon(String gameType) {
        int newGamesWon = getGamesWon(gameType) + 1;
        String query = "UPDATE user SET " + gameType + "Wins = " + newGamesWon;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public void updateGameLost(String gameType) {
        int newGamesLost = getGamesLost(gameType) + 1;
        String query = "UPDATE user SET " + gameType + "Losses = " + newGamesLost;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }


    //STATUS METHODS

    public void addStatus() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DEFAULTSTART, 0);
        values.put(COLUMN_DEFAULTMINES, 0);
        values.put(COLUMN_DEFAULTX, 0);
        values.put(COLUMN_DEFAULTY, 0);
        values.put(COLUMN_CONFIRMONRESTART, 0);
        values.put(COLUMN_CURRENTGAMETYPE, "");
        values.put(COLUMN_SAVEGAME, 0);
        values.put(COLUMN_FROMSETTINGS, 0);
        values.put(COLUMN_PUZZLE_CREATED, 0);
        values.put(COLUMN_INITIAL_PUZZLE_CLICKS, 0);
        values.put(COLUMN_STYLE, 0);

        db.insert(TABLE_STATUS, null, values);
        db.close();
    }

    public void addSavedStatus() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LENGTH, 0);
        values.put(COLUMN_HEIGHT, 0);
        values.put(COLUMN_SAVEDGAMETYPE, "");
        values.put(COLUMN_SAVEDGAMEPROGESS, 0);
        values.put(COLUMN_SAVEDPROGRESSMAX, 0);
        values.put(COLUMN_CLICKSREMAINING, 0);

        db.insert(TABLE_SAVED_STATUS, null, values);
        db.close();
    }

    public void setPuzzleCreated(int bool) {
        String query = "UPDATE status SET puzzleCreated = " + bool;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public boolean getPuzzleCreated() {
        String query = "SELECT puzzleCreated FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            if (data.getInt(0) == 1) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public void setInitialPuzzleClicks(int clicks) {
        String query = "UPDATE status SET initialPuzzleClicks = " + clicks;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public int getInitialPuzzleClicks() {
        String query = "SELECT initialPuzzleClicks FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public void setDefaultStartBool(int bool) {
        String query = "UPDATE status SET defaultStart = " + bool;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public void setDefaultStart(int mines, int x, int y, String gameType) {
        String mineQuery = "UPDATE status SET defaultMines = " + mines;
        String xQuery = "UPDATE status SET defaultX = " + x;
        String yQuery = "UPDATE status SET defaultY = " + y;
        String typeQuery = "UPDATE status SET currentGameType = '" + gameType + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(mineQuery);
        db.execSQL(xQuery);
        db.execSQL(yQuery);
        db.execSQL(typeQuery);

        db.close();
    }

    public void setConfirmRestartBool(int bool) {
        String query = "UPDATE status SET confirmOnRestart = " + bool;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public void setStyle(int stylePos) {
        String query = "UPDATE status SET style = " + stylePos;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public void setSaveGame(int bool) {
        String query = "UPDATE status SET saveGame = " + bool;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public void setCurrentGameType(String gameType) {
        String query = "UPDATE status SET currentGameType = '" + gameType + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public void setFromSettings(int bool) {
        String query = "UPDATE status SET fromSettings = " + bool;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();
    }

    public boolean getDefaultStartBool() {
        String query = "SELECT defaultStart FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            if (data.getInt(0) == 1) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public boolean getSaveGame() {
        String query = "SELECT saveGame FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            if (data.getInt(0) == 1) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public boolean getFromSettings() {
        String query = "SELECT fromSettings FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            if (data.getInt(0) == 1) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }


    public boolean getConfirmRestart() {
        String query = "SELECT confirmOnRestart FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            if (data.getInt(0) == 1) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public String getGameType() {
        String query = "SELECT currentGameType FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getString(0);
        } else
            return "Invalid";
    }

    public int getStylePos() {
        String query = "SELECT style FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getDefaultMines() {
        String query = "SELECT defaultMines FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getDefaultX() {
        String query = "SELECT defaultX FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getDefaultY() {
        String query = "SELECT defaultY FROM status";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getLength() {
        String query = "SELECT length FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getHeight() {
        String query = "SELECT height FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public void storeGame(int rowNumber, int columnNumber, int value, int clicked) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROW, rowNumber);
        values.put(COLUMN_COLUMN, columnNumber);
        values.put(COLUMN_VALUE, value);
        values.put(COLUMN_CLICKED, clicked);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_BOARD, null, values);
        db.close();

    }

    public void resetStoredBoard() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOARD);
    }

    public Integer[][][] getGridValues() {
        Integer[][][] grid  = new Integer[getLength()][getHeight()][3];

        String query = "SELECT X, Y, value, clicked FROM board";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            grid[data.getInt(0)][data.getInt(1)][data.getInt(3)] = data.getInt(2);
            while(data.moveToNext()) {
                grid[data.getInt(0)][data.getInt(1)][data.getInt(3)] = data.getInt(2);
            }
            return grid;
        } else
            return null;
    }

    public String getSavedGameType() {
        String query = "SELECT savedGameType FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getString(0);
        } else
            return "Invalid";
    }

    public int getSavedGameProgress() {
        String query = "SELECT savedGameProgress FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getSavedMaxProgress() {
        String query = "SELECT progressMax FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public void savedBoardDetails(int length, int height, String gameType, int gameProgress, int progressMax) {
        String lengthQuery = "UPDATE savedStatus SET length = " + length;
        String heightQuery = "UPDATE savedStatus SET height = " + height;
        String gameTypeQuery = "UPDATE savedStatus SET savedGameType = '" + gameType + "'";
        String progressQuery = "UPDATE savedStatus SET savedGameProgress = " + gameProgress;
        String progressMaxQuery = "UPDATE savedStatus SET progressMax = " + progressMax;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(lengthQuery);
        db.execSQL(heightQuery);
        db.execSQL(gameTypeQuery);
        db.execSQL(progressQuery);
        db.execSQL(progressMaxQuery);

        db.close();
    }


    //PUZZLE


    public void storePuzzle(int rowNumber, int columnNumber, int value, int clicked) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROW, rowNumber);
        values.put(COLUMN_COLUMN, columnNumber);
        values.put(COLUMN_VALUE, value);
        values.put(COLUMN_CLICKED, clicked);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PUZZLE, null, values);
        db.close();

    }

    public void resetStoredPuzzle() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STORED_PUZZLE);
    }

    public Integer[][][] getPuzzleGridValues() {
        Integer[][][] grid  = new Integer[getLength()][getHeight()][3];

        String query = "SELECT X, Y, value, clicked FROM puzzle";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            grid[data.getInt(0)][data.getInt(1)][data.getInt(3)] = data.getInt(2);
            while(data.moveToNext()) {
                grid[data.getInt(0)][data.getInt(1)][data.getInt(3)] = data.getInt(2);
            }
            return grid;
        } else
            return null;
    }

    public int getSavedPuzzleProgress() {
        String query = "SELECT savedGameProgress FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getSavedPuzzleMaxProgress() {
        String query = "SELECT progressMax FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getSavedClicksRemaining() {
        String query = "SELECT clicksRemaining FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public void savedPuzzleDetails(int length, int height, int clicksRemaining, int gameProgress, int progressMax) {
        String lengthQuery = "UPDATE savedStatus SET length = " + length;
        String heightQuery = "UPDATE savedStatus SET height = " + height;
        String clicksRemaningQuery = "UPDATE savedStatus SET clicksRemaining = " + clicksRemaining;
        String progressQuery = "UPDATE savedStatus SET savedGameProgress = " + gameProgress;
        String progressMaxQuery = "UPDATE savedStatus SET progressMax = " + progressMax;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(lengthQuery);
        db.execSQL(heightQuery);
        db.execSQL(clicksRemaningQuery);
        db.execSQL(progressQuery);
        db.execSQL(progressMaxQuery);

        db.close();
    }

    public void storeInitialPuzzle(int rowNumber, int columnNumber, int value, int clicked) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROW, rowNumber);
        values.put(COLUMN_COLUMN, columnNumber);
        values.put(COLUMN_VALUE, value);
        values.put(COLUMN_CLICKED, clicked);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PUZZLE, null, values);
        db.close();

    }

    public void resetInitialPuzzle() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PUZZLE);
    }

    public Integer[][][] getInitialPuzzleGridValues() {
        Integer[][][] grid  = new Integer[getLength()][getHeight()][3];

        String query = "SELECT X, Y, value, clicked FROM puzzle";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            grid[data.getInt(0)][data.getInt(1)][data.getInt(3)] = data.getInt(2);
            while(data.moveToNext()) {
                grid[data.getInt(0)][data.getInt(1)][data.getInt(3)] = data.getInt(2);
            }
            return grid;
        } else
            return null;
    }

    public int getInitialPuzzleProgress() {
        String query = "SELECT savedGameProgress FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public int getInitialPuzzleMaxProgress() {
        String query = "SELECT progressMax FROM savedStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        if(data.moveToFirst()) {
            return data.getInt(0);
        } else
            return 0;
    }

    public void initialPuzzleDetails(int length, int height, int gameProgress, int progressMax) {
        String lengthQuery = "UPDATE savedStatus SET length = " + length;
        String heightQuery = "UPDATE savedStatus SET height = " + height;
        String progressQuery = "UPDATE savedStatus SET savedGameProgress = " + gameProgress;
        String progressMaxQuery = "UPDATE savedStatus SET progressMax = " + progressMax;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(lengthQuery);
        db.execSQL(heightQuery);
        db.execSQL(progressQuery);
        db.execSQL(progressMaxQuery);

        db.close();
    }
}