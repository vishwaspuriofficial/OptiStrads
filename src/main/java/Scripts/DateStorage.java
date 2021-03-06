package Scripts;

import Execution.Main;

import java.lang.reflect.Executable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.io.File;
import java.io.FileWriter;


public class DateStorage {
    ArrayList<Event> events;

    public DateStorage(ArrayList<Event> _events) {
        events = _events;
    }


    public ArrayList<Event> getEvents() {
        ArrayList<Event> _event = new ArrayList<>();

        Event curEvent = events.get(0);
        for (int i = 0; i < events.size() - 1; i++) {
            if (curEvent.title.equals(events.get(i + 1).title)) { //If Adj is equal, add the time
                curEvent.timeInterval += 30;
            } else {
                _event.add(curEvent);
                curEvent = events.get(i + 1);
            }
        }

        return _event;

    }


    public static LinkedList<Event>[] addEventToDate(LinkedList<Event> _event) throws ParseException, CloneNotSupportedException {
        HashMap<String, LinkedList<Event>> plannedDatesData = Main.getPlannedDatesData();
        plannedDatesData.put(_event.get(0).getDate(), _event);
        return getSuggestions(_event.get(0).getDate(), _event.get(0));
    }

    public static void deleteEventAtDate(String date, LinkedList<Event> _event, Event target, String id) {
        _event.remove(target);
        Main.getPlannedDatesData().replace(date, _event);
        switch (id) {
            case "2" -> Main.getGui().getDatePanel().setDate(date, _event, false);
            case "3" -> Main.getGui().getSuggestion1().setDate(date, _event, false);
            case "4" -> Main.getGui().getSuggestion2().setDate(date, _event, false);
            case "5" -> Main.getGui().getSuggestion3().setDate(date, _event, false);
        }
    }

    public static int getEventDay(LocalDate date) {
        int eventDay = 0;
        switch (date.getDayOfWeek().toString()) {
            case "MONDAY":
                eventDay = 1;
                break;
            case "TUESDAY":
                eventDay = 2;
                break;
            case "WEDNESDAY":
                eventDay = 3;
                break;
            case "THURSDAY":
                eventDay = 4;
                break;
            case "FRIDAY":
                eventDay = 5;
                break;
            case "SATURDAY":
                eventDay = 6;
                break;
            case "SUNDAY":
                eventDay = 7;
                break;
        }
        return eventDay;
    }

    public static LinkedList<Event> getMerge(String _date) {

        HashMap<String, LinkedList<Event>> plannedDatesData = Main.getPlannedDatesData();
        LinkedList<Event> mainEvents = (LinkedList<Event>) plannedDatesData.get(_date);
        if (mainEvents == null) {
            mainEvents = new LinkedList<>();
        } else {
            mainEvents = (LinkedList<Event>) mainEvents.clone();
        }
        LinkedList<Event> repeatedEvents = Main.getRepeatingEvents();

        String[] splitted = _date.split("/");
        int day = Integer.parseInt(splitted[0]);
        int month = Integer.parseInt(splitted[1]);
        int year = Integer.parseInt(splitted[2]);

        LocalDate date = LocalDate.of(year, month, day);

        int eventDay = getEventDay(date);

        //Merge repeated and main events
        for (Event event : repeatedEvents) {
            for (String repeatDay : event.repeatDate) {
                if (Integer.parseInt(repeatDay) == eventDay)//Check to see if main event
                    // s list of the date chosen requires the merge of repeated events
                {
                    mainEvents.add(event); //Adds
                }
            }
        }
        return mainEvents;
    }

    public static LinkedList<Event>[] getSuggestions(String _date, Event _event) throws CloneNotSupportedException, ParseException {
        LinkedList<Event> mainEvents = getMerge(_date);

        //Algorithm part
        ArrayList<Float> busyTimeSlots = new ArrayList<>();
        ArrayList<Float> availableTimeSlots = new ArrayList<>();


        ArrayList<Event> dynamicEvents = new ArrayList<>();
        if (_event.dynamic) {
            dynamicEvents.add(_event);
        }
        else {
            mainEvents.add(_event);
        }

        //Getting the dates from string to float, and storing all these busy slots to start time lst
        for (Event event : mainEvents) {
            if (!event.dynamic) {
                String[] split = event.startTime.split(":");
                float hour = Float.parseFloat(split[0]);
                float minute = Float.parseFloat((split[1])) / 10;
                if (minute == 0.3) {
                    minute += 0.2;
                }
                float time = hour + minute;
                busyTimeSlots.add(time);
            }
        }
        Collections.sort(busyTimeSlots);


        //Find all open timeslots
        float currentTime = 0.0f; //float flaw

        //Very high potential to be very bad (just smth in case I can't find a better solution)
        int x = 0;
        Boolean stop = false;
        while (currentTime != 24) {
            if (!busyTimeSlots.contains(currentTime)) {
                availableTimeSlots.add(currentTime);
            }
            currentTime += 0.50;
        }


        //preparing the list of possible solutions
        LinkedList<Event>[] possibleSolutions = new LinkedList[3];
        LinkedList<Event> s1 = (LinkedList<Event>) mainEvents.clone();
        LinkedList<Event> s2 = (LinkedList<Event>) mainEvents.clone();
        LinkedList<Event> s3 = (LinkedList<Event>) mainEvents.clone();

        possibleSolutions[0] = s1;
        possibleSolutions[1] = s2;
        possibleSolutions[2] = s3;






        for (Event e : mainEvents) {
            if (e.dynamic) {
                dynamicEvents.add(e);
            }
        }





        //Solution 1: Random
        for (Event e : dynamicEvents) {
            Event result = setPosition(e, availableTimeSlots);
            if (result ==null) {
                s1=null;
                break;
            }
            if(s1.contains(_event)){
                s1.set(s1.indexOf(result),result);
            }
            else{
                s1.add(_event);
            }
        }

        //Solution 2: Easy To Hard
        Collections.sort(dynamicEvents); //

        for (Event e : dynamicEvents) {
            Event result = setPosition(e, availableTimeSlots);
            if (result ==null) {
                s2=null;
                break;
            }
            if(s2.contains(_event)){
                s2.set(s2.indexOf(result),result);

                //1 -

            }
            else{
                s2.add(_event);
            }
        }

        //Solution 3: Hard To Easy
        Collections.sort(dynamicEvents,Collections.reverseOrder()); //

        for (Event e : dynamicEvents) {
            Event result = setPosition(e, availableTimeSlots);
            if (result ==null) {
                s3=null;
                break;
            }
            if(s3.contains(_event)){
                s3.set(s3.indexOf(result),result);
            }
            else{
                s3.add(_event);
            }
        }
//        System.out.println(s1);
        return possibleSolutions;
    }



    public static Event setPosition(Event _event, ArrayList<Float> availableTimeSlots) throws ParseException, CloneNotSupportedException {
        int i = 0;
        int stm = Integer.parseInt(_event.startTime.split(":")[1]);
        int sth = Integer.parseInt(_event.startTime.split(":")[0]);
        int st = sth * 60 + stm;

        int etm = Integer.parseInt(_event.endTime.split(":")[1]);
        int eth = Integer.parseInt(_event.endTime.split(":")[0]);
        int et = eth * 60 + etm;

        int t = Math.abs(st - et);
        int ti = Integer.valueOf(t / 30);

        float ct = availableTimeSlots.get(i);
        while (i < availableTimeSlots.size()) {

            //Finds if the time slots for the amount of time after a time slot is empty
            for (float z = ct; z < ct + (ti * 0.5); z += 0.5) {
                if (!availableTimeSlots.contains(z)) {
                    i += ti;
                    break;
                }
            }

            //Success
            //1: Proper Time format to event
            //2: Remove from Available Time Slot

            int s = availableTimeSlots.indexOf(ct);
            //Index of Starting time
            int e = availableTimeSlots.indexOf(ct + (ti * 0.5f));
            if(s == -1 || e == -1){
                return null;
            }

            //Index of Ending time

            float sh = availableTimeSlots.get(s) ;

            //make exact time sh:sm
            int smt = setDecimal(sh);

            //make exact time eh:em
            int eh = (int) e;
            float em = Math.abs(eh - availableTimeSlots.get(e));

            int emt = setDecimal(em);


            //String times
            String startTime = String.valueOf((int) sh) + ":" + String.valueOf(smt);
            String endTime = String.valueOf(eh) + ":" + String.valueOf(emt);
            //COnverting to datesformat
            DateFormat df = new SimpleDateFormat("hh:mm");
            Date ST = df.parse(startTime);
            String[] sTime= String.valueOf(ST).split(":");
            Date ET = df.parse(endTime);
            String[] eTime= String.valueOf(ET).split(":");

            _event.startTime = sTime[0].split(" ")[3]+":"+sTime[1];
            _event.endTime = eTime[0].split(" ")[3]+":"+eTime[1];
            //Remove from available time slot
            for (float y = ct; y <= (ct + (ti * 0.5)); y++) {
                availableTimeSlots.remove(y);
            }

            //Add times to busy time slot
//            for (float y=ct; y<=(ct + (ti * 0.5)); y++) {
//                busyTimeSlots.remove(y);
//            }
            return _event;
        }
        return null; //This means no time slot available

    }
    public static int setDecimal(float value){
        if(value==0.5){
            return 30;
        }
        else{
            return 0;
        }
    }
}



    

