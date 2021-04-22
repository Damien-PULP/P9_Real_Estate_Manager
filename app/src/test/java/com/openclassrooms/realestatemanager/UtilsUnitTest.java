/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager;

import com.bumptech.glide.util.Util;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilsUnitTest {

    // Converter money
    @Test
    public void convertDollarToEuroTest (){
        int valueDollar = 10;
        int convertedValue = Utils.convertDollarToEuro(valueDollar);
        assertEquals(8, convertedValue);
    }
    @Test
    public void convertEuroToDollarTest (){
        int valueEuro = 10;
        int convertedValue = Utils.convertEuroToDollar(valueEuro);
        assertEquals(12, convertedValue);
    }

    // Get Date by format
    @Test
    public void getDateTodayTest () throws ParseException {
        String today = Utils.getTodayDate();
        Date dateToday = new SimpleDateFormat("dd/MM/yyyy").parse(today);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToday);
        assertTrue(cal.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)  && cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR));
    }
}
