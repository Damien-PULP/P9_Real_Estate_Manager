package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.SearchPropertyModel;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }
    public static int convertEuroToDollar(int euro){
        return (int) Math.round(euro * 1.2);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED );
    }

    /**
     * This method is my additional functionality : To calculate mortgage by month
     * @param pris
     * @param bring
     * @param years
     * @param rate
     * @return
     */
    public static Float calculateMonthlyPayment (float pris, int bring, int years, float rate){
        return ((pris - bring) / (years * 12)) * rate;
    }

    /**
     * This method is my additional functionality : To calculate total mortgage
     * @param monthlyPris
     * @param years
     * @return
     */
    public static Float calculateTotalPrisOfProperty (float monthlyPris, int years){
        return monthlyPris * years * 12;
    }


    public static Date subtractTimeToDate(Date date, int days, int month, int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if(days != 0) cal.add(Calendar.DATE, -days);
        if(month != 0) cal.add(Calendar.MONTH, -month);
        if(year != 0) cal.add(Calendar.YEAR, -year);
        return cal.getTime();
    }

    public static Bitmap compressImage(Bitmap bitmapSource){

        ByteArrayOutputStream streamSource = new ByteArrayOutputStream();
        bitmapSource.compress(Bitmap.CompressFormat.PNG, 100, streamSource);
        byte[] bitmapArray = streamSource.toByteArray();

        while (bitmapArray.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            bitmapArray = stream.toByteArray();
        }

        Bitmap bitmapCompressed = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        return bitmapCompressed;

    }

    public static SimpleSQLiteQuery getQueriesByFilter (SearchPropertyModel searchPropertyModel){
        String request = "SELECT * FROM Property";

        if(searchPropertyModel.isSoldProperty()){
            request += " WHERE state = 'IS_SELL'";
        }else{
            request += " WHERE state = 'NOT_SELL'";
        }
        if(searchPropertyModel.getTypeProperty() != null){
            request += " AND type LIKE '%" + searchPropertyModel.getTypeProperty() + "%'";
        }
        if(searchPropertyModel.getMaxSurfaceProperty() > 0 ){
            request += " AND area BETWEEN '" + searchPropertyModel.getMinSurfaceProperty() + "' AND '" + searchPropertyModel.getMaxSurfaceProperty() + "'";
        }
        if(searchPropertyModel.getMaxPrisProperty() > 0){
            request += " AND pris BETWEEN '" + searchPropertyModel.getMinPrisProperty() + "' AND '" + searchPropertyModel.getMaxPrisProperty() + "'";
        }
        return new SimpleSQLiteQuery(request);
    }

    public static SimpleSQLiteQuery getQueriesByFilterForPointOfInterest (SearchPropertyModel searchPropertyModel){
        StringBuilder request = new StringBuilder("SELECT * FROM PointOfInterest");
        List<String> points = searchPropertyModel.getPointOfInterestProperty();
        if(points.size() > 0){
            request.append(" WHERE name IN (");
            for(int i= 0; i< points.size() - 1; i++){
                request.append("'").append(points.get(i)).append("',");
            }
            request.append("'").append(points.get(points.size() - 1)).append("')");
        }
        return new SimpleSQLiteQuery(request.toString());
    }

}
