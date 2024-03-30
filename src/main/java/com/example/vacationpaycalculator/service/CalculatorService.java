package com.example.vacationpaycalculator.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalculatorService {
    private static final HashMap<Integer, Integer[]> weekends = new HashMap<>();

    static {
        weekends.put(1, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        weekends.put(2, new Integer[]{23, 24, 25});
        weekends.put(3, new Integer[]{8, 9, 10});
        weekends.put(4, new Integer[]{28, 29, 30});
        weekends.put(5, new Integer[]{1, 9, 10, 11, 12});
        weekends.put(6, new Integer[]{12});
        weekends.put(7, new Integer[]{});
        weekends.put(8, new Integer[]{});
        weekends.put(9, new Integer[]{});
        weekends.put(10, new Integer[]{});
        weekends.put(11, new Integer[]{3, 4});
        weekends.put(12, new Integer[]{});
    }

    /**
     * Function for calculating vacation payment
     *
     * @param salary - average salary for 12 months
     * @param days   - vacation days amount
     * @return - vacation pay amount
     **/
    public double vacationPayCalculate(int salary, short days) {
        //29.3 - middle amount of days. number after "-" - Personal income tax (13%)
        return Math.ceil(((salary / 29.3 * days) - (salary / 29.3 * days / 100 * 13)) * 100) / 100;
    }

    /**
     * Function for calculating vacation payment with knowing day of which vacation begins
     *
     * @param salary    - average salary for 12 months
     * @param days      - vacation days amount
     * @param startDate - day on which vacation begins
     * @return - vacation pay amount
     **/
    public double vacationPayCalculateWithWeekends(int salary, short days, Date startDate) {
        //number of vacation days including weekends
        int realDaysAmount = days;
        //Used to parse the date into parts
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        //month of start vacations. Used to find weekends in this month
        int startMonth = (calendar.get(Calendar.MONTH)) + 1;
        //day of start vacations
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        //Takes weekends for current month
        ArrayList<Integer> currentWeekends = new ArrayList<>(Arrays.asList(weekends.get(startMonth)));
        //Decrease current vacation days, if there are weekends in vacation
        for (int i = 0; i < days; ++i) {
            //if list of weekends contains day - decrease our amount of calculated days
            if (currentWeekends.contains(startDay))
                realDaysAmount--;
            startDay++;
        }
        //days in next month
        int additionDay = 0;
        //weekends days in next month
        ArrayList<Integer> nextMonthWeekends = new ArrayList<>();
        //calculate additions days and takes weekends for next month
        switch (startMonth) {
            case 1:
                additionDay = startDay + days - 28;
                if (additionDay > 0)
                    nextMonthWeekends = new ArrayList<>(Arrays.asList(weekends.get(startMonth + 1)));
                break;
            case 2, 4, 6, 8, 9, 11:
                additionDay = startDay + days - 31;
                if (additionDay > 0)
                    nextMonthWeekends = new ArrayList<>(Arrays.asList(weekends.get(startMonth + 1)));
                break;
            case 3, 5, 7, 10, 12:
                additionDay = startDay + days - 30;
                if (additionDay > 0)
                    if (startMonth != 12)
                        nextMonthWeekends = new ArrayList<>(Arrays.asList(weekends.get(startMonth + 1)));
                    else
                        nextMonthWeekends = new ArrayList<>(Arrays.asList(weekends.get(1)));
                break;
        }
        //Decrease current vacation days, if there are weekends from next month in vacation
        if (additionDay > 0)
            for (int i = 0; i < additionDay; ++i) {
                //if list of weekends contains day - decrease our amount of calculated days
                if (nextMonthWeekends.contains(i))
                    realDaysAmount--;
            }
        //Return our vacation pay amount. 29.3 - middle amount of days. number after "-" - Personal income tax (13%)
        return Math.ceil(((salary / 29.3 * realDaysAmount) - (salary / 29.3 * realDaysAmount / 100 * 13)) * 100) / 100;
    }
}
