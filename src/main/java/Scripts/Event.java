package Scripts;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;



public class Event {
    String path = "../Databases/mainsave.txt";
    String title;
    String startTime;
    String endTime;
    String date;
    ArrayList<String> repeatDate;
    ArrayList<String> linkedEvents;
    int stressLevel;
    int timeInterval = 30;

    public Event(String _title, String _date, String _startTime, String _endTime, ArrayList<String> _repeatDate, ArrayList<String> _linkedEvents, int _stressLevel){
        title = _title;
        date = _date;
        startTime = _startTime;
        endTime = _endTime;
        repeatDate = _repeatDate;
        linkedEvents = _linkedEvents;
        stressLevel = _stressLevel;
    }

    public void saveToFile() throws IOException {
//
        FileWriter file = new FileWriter(path,true);
        PrintWriter write = new PrintWriter(file);


//        write.print("#day("+date+")\n");
//
//        write.close();
    }

}