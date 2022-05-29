package com.kkkzoz.utils;

import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class TimeUtil {

    Calendar calendar;

    public TimeUtil() {
        this.update();
    }

    private void update() {
        this.calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置星期一为一周开始的第一天
        calendar.setMinimalDaysInFirstWeek(4);//可以不用设置
    }

    public void updateToCurrent() {
        this.update();
    }


    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getWeekday(){
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    public int getWeekOfYear() {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public int getDayOfMonthByWeekNumber(int weekNumber) {
        Calendar temp = Calendar.getInstance();
        temp.setFirstDayOfWeek(Calendar.MONDAY);
        temp.setMinimalDaysInFirstWeek(4);


        temp.setWeekDate(this.getYear(), weekNumber, Calendar.MONDAY);
        int month = temp.get(Calendar.MONTH)+1;
        int day = temp.get(Calendar.DAY_OF_MONTH);
//        System.out.println("Month:" + month);
//        System.out.println("Day:" + day);
        return day;
    }

    public int getMonthByWeekNumber(int weekNumber){
        Calendar temp = Calendar.getInstance();
        temp.setFirstDayOfWeek(Calendar.MONDAY);
        temp.setMinimalDaysInFirstWeek(4);


        temp.setWeekDate(this.getYear(), weekNumber, Calendar.MONDAY);
        int month = temp.get(Calendar.MONTH)+1;
        return month;
    }



}
