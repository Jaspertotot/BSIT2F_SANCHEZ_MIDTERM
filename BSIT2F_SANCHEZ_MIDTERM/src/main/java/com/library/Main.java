package com.library;

import com.library.dao.DaoException;
import com.library.dao.ScenarioDao;
import com.library.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        try {
            new ConsoleUI(new ScenarioDao()).start();
        } catch (DaoException e) {
            System.out.println("FATAL: " + e.getMessage());
        }
    }
}
