package com.dengpan.pan.speedss.utils;

public class TimeRestUtil {
    public static final long DAY_TIME = 24 *60 * 60 *1000;
    public static final long HOUR_TIME = 60 * 60 * 1000;
    public static final long MIN_TIME = 60 *1000;
    public static String RestTime(String time){
        long  LongTime = strToLong(time);

        long restTime = System.currentTimeMillis() - LongTime;
        int endTime = 3*24*60*60*1000;
        if(restTime >endTime){
            return "已经过期";
        }else {
            long lastTime  =endTime - restTime;
            long day = lastTime/DAY_TIME;
            long hour = lastTime%DAY_TIME/HOUR_TIME;
            long min = lastTime%HOUR_TIME/MIN_TIME;
            long sec = lastTime%MIN_TIME/1000;
            return day+"天"+hour+"小时"+min+"分"+sec+"秒";
        }
    }
    public static long restTimeLong(String time){
        long  LongTime = strToLong(time);

        long restTime = System.currentTimeMillis() - LongTime;
        long endTime = 3*24*60*60*1000;
        return LongTime +endTime -System.currentTimeMillis() ;
    }

    public static String RestTime(long restTime){

        long endTime = 3*24*60*60*1000;
        if(restTime <1000){
            return "已过期";
        }else {
            long lastTime  =restTime;
            long day = lastTime/DAY_TIME;
            long hour = lastTime%DAY_TIME/HOUR_TIME;
            long min = lastTime%HOUR_TIME/MIN_TIME;
            long sec = lastTime%MIN_TIME/1000;
            return day+"天"+hour+"小时"+min+"分"+sec+"秒";
        }
    }

    private static long strToLong(String time) {
        try {
            return Long.parseLong(time);
        }catch (Exception e){
            return 0;
        }
    }
}
