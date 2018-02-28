package lab1;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

public class Assignment1
{
    public static String getDayOfWeek(LocalDate date)
    {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.getDefault());
    }
    public static void main(String[] args)
    {
        String input = "2007-02-28T10:15:30+01:00";
        OffsetDateTime dateTime = OffsetDateTime.parse(input);
        LocalDate date = dateTime.toLocalDate();

        System.out.printf("Date %s %s%n", date, getDayOfWeek(date));
        LocalDate nextDay = date.plusDays(1);
        System.out.printf("Next day %s%n",nextDay);

        LocalDate weekStart = date.minusDays(date.getDayOfWeek().ordinal());

        LocalDate weekEnd = date.plusDays(7 - date.getDayOfWeek().ordinal());
        System.out.printf(
                "Week beginning %s %s%n" +
                "Week end %s %s%n",
                weekStart,getDayOfWeek(weekStart),
                weekEnd,getDayOfWeek(weekEnd));
        LocalDate nextMonth = date.plusDays(date.getMonth().length(date.isLeapYear()) - date.getDayOfMonth()+1);
        System.out.printf(
                "Was month %s %s%n" +
                "Next month %s %s%n",
                date.getMonth(),date,
                nextMonth.getMonth(),nextMonth);
    }
}
