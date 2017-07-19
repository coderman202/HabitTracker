package com.example.android.habittracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Habit> habitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HabitTrackerDBHelper habitTrackerDB = new HabitTrackerDBHelper(this);

        String habitName = "Pet Care";
        String habitDescription = "Walked the dog for 2km";
        String habitDateTime = "12/10/2016 12:30";
        int habitDuration = 50;
        String habitDurationUnit = HabitTrackerContract.HabitEntry.DURATION_UNIT_MINUTES;

        Habit dogWalk = new Habit(habitName, habitDescription, habitDateTime,
                habitDuration, habitDurationUnit);

        habitName = "Exercise";
        habitDescription = "Went for a 10k run";
        habitDateTime = "17/10/2016 13:30";
        habitDuration = 1;
        habitDurationUnit = HabitTrackerContract.HabitEntry.DURATION_UNIT_HOURS;

        Habit run = new Habit(habitName, habitDescription, habitDateTime,
                habitDuration, habitDurationUnit);

        habitTrackerDB.insertHabit(dogWalk);
        habitTrackerDB.insertHabit(run);

        Cursor c = habitTrackerDB.readHabitEntryData();
        habitList = getAllHabits(c);


        Log.d("Habit List", habitList.toString());
    }



    /**
            * A method which gets all the habit entries in the db in list form.
            * @return  A list of all habit entries
     */
    public List<Habit> getAllHabits(Cursor c){
        if(c != null){
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String habitTypeName = c.getString(c.getColumnIndex(HabitTrackerContract.HabitType.HABIT_TYPE_NAME));
                String habitDurationUnit = c.getString(c.getColumnIndex(HabitTrackerContract.HabitEntry.HABIT_DURATION_UNIT));
                String habitDescription = c.getString(c.getColumnIndex(HabitTrackerContract.HabitEntry.HABIT_DESCRIPTION));
                String habitDateTime = c.getString(c.getColumnIndex(HabitTrackerContract.HabitEntry.HABIT_DATE_TIME));
                int habitDuration = c.getInt(c.getColumnIndex(HabitTrackerContract.HabitEntry.HABIT_DURATION));

                habitList.add(new Habit(habitTypeName, habitDescription, habitDateTime,
                        habitDuration, habitDurationUnit));
                c.moveToNext();
            }
            c.close();
            return habitList;

        }
        c.close();
        return null;
    }
}
