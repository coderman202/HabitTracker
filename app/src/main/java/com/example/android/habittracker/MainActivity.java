package com.example.android.habittracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        List<Habit> habitList;
        habitList = habitTrackerDB.getAllHabits();


        Log.d("Habit List", habitList.toString());
    }
}
