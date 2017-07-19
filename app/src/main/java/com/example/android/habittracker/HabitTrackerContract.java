package com.example.android.habittracker;

import android.provider.BaseColumns;

/**
 * Created by Reggie on 17/07/2017.
 * Custom contract class to store the tables for my database
 */

public final class HabitTrackerContract {

    private HabitTrackerContract(){}

    /**
     * Main table with foreign keys for the type of habit and the duration unit.
     * Includes a description and the date and time of the habit.
     */
    public static final class HabitEntry implements BaseColumns{

        public static final String TABLE_NAME = "Habits";

        public static final String PK_HABIT_ID = BaseColumns._ID;

        public static final String FK_HABIT_TYPE_ID = "HabitTypeID";

        public static final String HABIT_DESCRIPTION = "HabitDescription";

        public static final String HABIT_DATE_TIME = "HabitDateTime";

        public static final String HABIT_DURATION = "HabitDuration";

        public static final String HABIT_DURATION_UNIT = "HabitDurationUnitID";

        // Constants to represent the possible units of duration.
        public static final String DURATION_UNIT_HOURS = "hours";
        public static final String DURATION_UNIT_MINUTES = "minutes";
        public static final String DURATION_UNIT_DAYS = "days";
    }

    /**
     * Habit Type table includes an id and a name of the type such as:
     * 'Exercise', 'TV Viewing', 'Gardening', etc..
     */
    public static final class HabitType implements BaseColumns{

        public static final String TABLE_NAME = "HabitType";

        public static final String PK_HABIT_TYPE_ID = BaseColumns._ID;

        public static final String HABIT_TYPE_NAME = "HabitTypeName";
    }


}
