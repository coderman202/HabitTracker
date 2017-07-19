package com.example.android.habittracker;

/**
 * Created by Reggie on 18/07/2017.
 * A custom class to represent habits
 */
public class Habit {

    private String habitName;

    private String habitDescription;

    private String habitDateTime;

    private int habitDuration;

    private String habitDurationUnit;

    /**
     * Instantiates a new Habit.
     *
     * @param habitName         the habit name
     * @param habitDescription  the habit description
     * @param habitDateTime     the habit date time
     * @param habitDuration     the habit duration
     * @param habitDurationUnit the habit duration unit
     */
    public Habit(String habitName, String habitDescription, String habitDateTime, int habitDuration,
                 String habitDurationUnit) {
        this.habitName = habitName;
        this.habitDescription = habitDescription;
        this.habitDateTime = habitDateTime;
        this.habitDuration = habitDuration;
        this.habitDurationUnit = habitDurationUnit;
    }

    /**
     * Gets habit name.
     *
     * @return the habit name
     */
    public String getHabitName() {
        return habitName;
    }

    /**
     * Gets habit description.
     *
     * @return the habit description
     */
    public String getHabitDescription() {
        return habitDescription;
    }

    /**
     * Gets habit date time.
     *
     * @return the habit date time
     */
    public String getHabitDateTime() {
        return habitDateTime;
    }

    /**
     * Gets habit duration.
     *
     * @return the habit duration
     */
    public int getHabitDuration() {
        return habitDuration;
    }

    /**
     * Gets habit duration unit.
     *
     * @return the habit duration unit
     */
    public String getHabitDurationUnit() {
        return habitDurationUnit;
    }

    @Override
    public String toString() {
        return "Habit{" +
                habitName + ": " + habitDescription + " on " + habitDateTime +
                " for " + habitDuration + " " + habitDurationUnit + '\'' +
                '}';
    }
}
