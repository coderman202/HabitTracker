package com.example.android.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reggie on 17/07/2017.
 * A db helper class to allow for the maintenance of the db. Creating tables, inserting data and
 * removing data.
 */

public class HabitTrackerDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = HabitTrackerDBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "HabitTrackerDB.db";
    private static final int DATABASE_VERSION = 9;

    private Context context;

    // Table names of each table in the DB
    public static final String HABIT_TABLE = HabitTrackerContract.HabitEntry.TABLE_NAME;
    public static final String HABIT_TYPE_TABLE = HabitTrackerContract.HabitType.TABLE_NAME;

    // Column names for the Habit table
    public static final String PK_HABIT_ID = HabitTrackerContract.HabitEntry.PK_HABIT_ID;
    public static final String FK_HABIT_TYPE_ID = HabitTrackerContract.HabitEntry.FK_HABIT_TYPE_ID;
    public static final String HABIT_DESCRIPTION = HabitTrackerContract.HabitEntry.HABIT_DESCRIPTION;
    public static final String HABIT_DATE_TIME = HabitTrackerContract.HabitEntry.HABIT_DATE_TIME;
    public static final String HABIT_DURATION = HabitTrackerContract.HabitEntry.HABIT_DURATION;
    public static final String HABIT_DURATION_UNIT = HabitTrackerContract.HabitEntry.HABIT_DURATION_UNIT;

    // Column names for the Habit Type table
    public static final String PK_HABIT_TYPE_ID = HabitTrackerContract.HabitType.PK_HABIT_TYPE_ID;
    public static final String HABIT_TYPE_NAME = HabitTrackerContract.HabitType.HABIT_TYPE_NAME;


    public HabitTrackerDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    // This method is not required to do anything as of yet as no updates are required.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * A method to create tables in the db
     * @param db    the SQLite DB
     */
    public void createTables(SQLiteDatabase db){
        String query = "CREATE TABLE IF NOT EXISTS " + HABIT_TYPE_TABLE + " (" +
                PK_HABIT_TYPE_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                HABIT_TYPE_NAME + " TEXT NOT NULL);";
        db.execSQL(query);
        query = "CREATE TABLE IF NOT EXISTS " + HABIT_TABLE + " (" +
                PK_HABIT_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                FK_HABIT_TYPE_ID + " INTEGER NOT NULL," +
                HABIT_DESCRIPTION + " TEXT," +
                HABIT_DATE_TIME + " TEXT NOT NULL," +
                HABIT_DURATION + " INTEGER NOT NULL," +
                HABIT_DURATION_UNIT + " TEXT NOT NULL," +
                "FOREIGN KEY(" + FK_HABIT_TYPE_ID + ") REFERENCES " + HABIT_TYPE_TABLE + "(" + PK_HABIT_TYPE_ID + ") ON DELETE SET NULL);";
        db.execSQL(query);
    }

    /**
     * A simple drop tables method
     * @param db    the SQLite DB
     */
    public void dropTables(SQLiteDatabase db){
        String query = "DROP TABLE IF EXISTS " + HABIT_TABLE + ";";
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + HABIT_TYPE_TABLE + ";";
        db.execSQL(query);
    }

    /**
     * Insert a new habit into the table. Check if the type name and duration unit exist before
     * creating new ones. Also getting the foreign keys from the Type table and the Duration table.
     *
     * @param habit the habit
     */
    public void insertHabit(Habit habit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABIT_DESCRIPTION, habit.getHabitDescription());
        values.put(HABIT_DATE_TIME, habit.getHabitDateTime());
        values.put(HABIT_DURATION, habit.getHabitDuration());
        values.put(FK_HABIT_TYPE_ID, addNewHabitType(habit.getHabitName()));
        values.put(HABIT_DURATION_UNIT, habit.getHabitDurationUnit());
        db.insert(HABIT_TABLE, null, values);
        db.close();
    }

    /**
     * Adding a new habit type if it doesn't already exist. Ensure the String is formatted
     * properly so if the entry is 'gardening' or 'GARDENING', it gets converted to 'Gardening'
     * as standard. This avoids duplicate entries of the same unit.
     * Returns the ID of the new entry for insertion as the foreign key of the main Habit table.
     *
     * @param typeName  The name of the type to be inserted.
     * @return the int  The id.
     */
    public int addNewHabitType(String typeName){
        typeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1).toLowerCase();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + HABIT_TYPE_TABLE + " WHERE " +
                HABIT_TYPE_NAME + " = '" + typeName + "';";

        Cursor c = db.rawQuery(query, null);
        if(c.getCount() == 0){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(HABIT_TYPE_NAME, typeName);
            db.insert(HABIT_TYPE_TABLE, null, values);
        }
        c.close();
        db = this.getReadableDatabase();
        c = db.rawQuery(query, null);
        if(c != null){
            c.moveToFirst();
            int typeID = c.getInt(c.getColumnIndex(PK_HABIT_TYPE_ID));
            c.close();
            return typeID;
        }
        c.close();
        return 0;
    }


    /**
     * A method which gets all the habit entries in the db in list form.
     * @return  A list of all habit entries
     */
    public List<Habit> getAllHabits(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + HABIT_TABLE + ";";

        List<Habit> habitList = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if(c != null){
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int habitTypeID = c.getInt(c.getColumnIndex(FK_HABIT_TYPE_ID));
                String habitTypeName = getHabitTypeNameByID(habitTypeID);
                String habitDurationUnit = c.getString(c.getColumnIndex(HABIT_DURATION_UNIT));
                String habitDescription = c.getString(c.getColumnIndex(HABIT_DESCRIPTION));
                String habitDateTime = c.getString(c.getColumnIndex(HABIT_DATE_TIME));
                int habitDuration = c.getInt(c.getColumnIndex(HABIT_DURATION));

                habitList.add(new Habit(habitTypeName, habitDescription, habitDateTime,
                        habitDuration, habitDurationUnit));
                c.moveToNext();
            }
            c.close();
            db.close();
            return habitList;

        }
        c.close();
        db.close();
        return null;
    }

    /**
     * A method to get the type name of a habit by its ID
     * @param typeID    The type id
     * @return          The String name of the habit.
     */
    public String getHabitTypeNameByID(int typeID){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + HABIT_TYPE_TABLE + " WHERE " +
                PK_HABIT_TYPE_ID + " = " + typeID + ";";

        Cursor c = db.rawQuery(query, null);
        if(c != null) {
            c.moveToFirst();
            String habitTypeName = c.getString(c.getColumnIndex(HABIT_TYPE_NAME));
            c.close();
            db.close();
            return habitTypeName;
        }
        c.close();
        db.close();
        return null;
    }

    /**
     * A method which queries the db and returns all the habit entries in a Cursor object.
     *
     * @return A cursor object
     */
    public Cursor readHabitEntryData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ HABIT_TABLE + ", " + HABIT_TYPE_TABLE + " WHERE " +
                HABIT_TABLE + "." + FK_HABIT_TYPE_ID + " = " + HABIT_TYPE_TABLE + "." + PK_HABIT_TYPE_ID + ";";

        Cursor c = db.rawQuery(query, null);

        return c;
    }
}
