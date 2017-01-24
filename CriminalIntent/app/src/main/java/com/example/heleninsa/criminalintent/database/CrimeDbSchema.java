package com.example.heleninsa.criminalintent.database;

/**
 * Created by heleninsa on 2017/1/22.
 */

public class CrimeDbSchema {

    public static final class CrimeTable {
        public final static String NAME = "crimes";

        public static final class Cols {
            public final static String UUID = "uuid";
            public final static String TITLE = "title";
            public final static String DATE = "date";
            public final static String SOLVED = "solved";
            public final static String SUSPECTED = "suspect";
        }
    }


}
