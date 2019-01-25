package com.cubee.db;

public class Run {
    private static Run ourInstance = new Run();

    public static Run getInstance() {
        return ourInstance;
    }

    private Run() {
    }
}
