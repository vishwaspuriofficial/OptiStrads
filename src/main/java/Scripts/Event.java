package Scripts;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;



public class Event implements Cloneable,Comparable<Event>{
    String title;
    String startTime;
    String endTime;
    String date;
    ArrayList<String> repeatDate;
    int stressLevel;
    int timeInterval = 30;

    boolean dynamic;
    public Event(String _title, String _date, String _startTime, String _endTime, ArrayList<String> _repeatDate, int _stressLevel, boolean _dynamic){
        title = _title;
        date = _date;
        startTime = _startTime;
        endTime = _endTime;
        repeatDate = _repeatDate;
        stressLevel = _stressLevel;
        dynamic = _dynamic;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getRepeatDate() {
        return repeatDate;
    }

    public int getStressLevel() {
        return stressLevel;
    }

    public int getTimeInterval() {
        return timeInterval;
    }
    public boolean getDynamic() {
        return dynamic;
    }

    @Override
    public int compareTo(Event o) {
        return this.stressLevel-o.stressLevel;
    }

}
