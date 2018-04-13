package com.holly.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lombok.extern.log4j.Log4j;

@Log4j
public class TimeUtils {

	public static final String DEFAULT_DATETIME_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_FORMAT_YEAR = "yyyy";
	public static final String DATETIME_FORMAT_YEAR_MONTH = "yyyy-MM";
	public static final String DATETIME_FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT_YEAR_MONTH_DAY_HOUR = "yyyy-MM-dd HH";
	public static final String DATETIME_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";

	public static Date stringToDate(Object dateString, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		Date d = null;
		if (dateString != null && !"".equals(dateString)) {
			try {
				d = df.parse((String) dateString);
			} catch (ParseException e) {
				e.printStackTrace();
				log.error("TimeUtils.class, 解析时间出错..."+dateString);
			}
		}
		return d;
	}

	public static String dateToString(Date date) {
		return dateToString(date, DEFAULT_DATETIME_FORMAT_SEC);
	}

	public static String dateToString(Date date, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		String d = null;
		if (date != null) {
			d = df.format(date);
		}
		return d;
	}

	public static String getTodayString() {
		GregorianCalendar today = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String todayString = df.format(today.getTime());

		return todayString;
	}

	/*
	 * 根据传入的日期格式(如yyyy-MM-dd)返回今天日期的字符串
	 */
	public static String getTodayString(String format) {
		GregorianCalendar today = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat(format);
		String todayString = df.format(today.getTime());

		return todayString;
	}

	public static String getTomorrowString() {
		GregorianCalendar tomorrow = new GregorianCalendar();
		tomorrow.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrowStringg = df.format(tomorrow.getTime());

		return tomorrowStringg;
	}

	public static String getYesterdayString() {
		GregorianCalendar yesterday = new GregorianCalendar();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setCalendar(yesterday);
		String yesterdayString = df.format(yesterday.getTime());

		return yesterdayString;
	}

	public static String getPrevMonthString(Date oneday) {
		Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(oneday);
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 指定时间 天 向前 或 向后 平移
	 * 
	 * @param date
	 * @param dayNum
	 * @return
	 */
	public static Date setDateDay(Date date, int dayNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.DATE, dayNum);
		return now.getTime();
	}

	/**
	 * 指定时间 分钟 向前 或 向后 平移
	 * 
	 * @param date
	 * @param dayNum
	 * @return
	 */
	public static Date setDateMinute(Date date, int minute) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.MINUTE, minute);
		return now.getTime();
	}

	/**
	 * 指定时间 月 向前 或 向后 平移
	 * 
	 * @param date
	 * @param dayNum
	 * @return
	 */
	public static Date setDateMonth(Date date, int MonthNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.MONTH, MonthNum);
		return now.getTime();
	}

	public static Long getTimeDiff(Date date1, Date date2) {
		Long days = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);

		return days;
	}

	public static Long getTimeDiff(String date1, String date2, String format) {
		Date a = TimeUtils.stringToDate(date1, format == null ? DATETIME_FORMAT_YEAR_MONTH_DAY : format);
		Date b = TimeUtils.stringToDate(date2, format == null ? DATETIME_FORMAT_YEAR_MONTH_DAY : format);
		Long days = TimeUtils.getTimeDiff(a, b);
		return days;
	}

	public static String formatTime(long ms) {// 将毫秒数换算成x天x时x分x秒x毫秒
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day;
		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		return strDay + "天" + strHour + "时" + strMinute + "分" + strSecond + "秒";
	}

}
