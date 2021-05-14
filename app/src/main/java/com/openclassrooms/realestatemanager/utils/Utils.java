package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.openclassrooms.realestatemanager.model.SearchPropertyModel;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars int - money
     * @return euro int - money
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }
    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param euro int - money
     * @return dollars int - money
     */
    public static int convertEuroToDollar(int euro){
        return (int) Math.round(euro * 1.2);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return actual date by specific format
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context context of activity
     * @return - boolean state of internet
     */
    public static Boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if((Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED || Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED )){
            try {
                Process ping = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int valPing = ping.waitFor();
                return (valPing == 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * This method is my additional functionality : To calculate mortgage by month
     * @param price - float money
     * @param bring - int the bring in mortgage
     * @param years - int years to refund the loan
     * @param rate - int rate of the loan
     * @return - float the monthly payment
     */
    public static Float calculateMonthlyPayment (float price, int bring, int years, float rate){
        float rate_on_month = ((rate / 100) / 12);

        float numerator = ((price - bring) * rate_on_month);
        double denominator = 1 - Math.pow((1 / (1 + rate_on_month)), (12 * years));
        return (float) (numerator / denominator);
    }

    /**
     * This method is my additional functionality : To calculate total mortgage
     * @param monthlyPris - float the monthly pris to buy by month
     * @param years - int the years to refund the loan
     * @return - float the total payment
     */
    public static Float calculateTotalPriceOfProperty(float monthlyPris, int years){
        return monthlyPris * years * 12;
    }

    public static Float calculateTotalProfitsOfProperty (float monthlyPrice, int years, float price){
        return monthlyPrice * years * 12 - price;
    }

    /**
     * This method convert a string to an date
     * @param date - String of conversion
     * @return - Date converted
     */
    public static Date getDateFromString (String date){
        DateFormat format = DateFormat.getInstance();
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * This method subtract a time of a date
     * @param date - int
     * @param days - int
     * @param month - int
     * @param year - int
     * @return - Date subtracted
     */
    public static Date subtractTimeToDate(Date date, int days, int month, int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if(days != 0) cal.add(Calendar.DATE, -days);
        if(month != 0) cal.add(Calendar.MONTH, -month);
        if(year != 0) cal.add(Calendar.YEAR, -year);
        return cal.getTime();
    }

    /**
     * This method compress a bitmap of a resolution
     * @param bitmapSource - Bitmap the source
     * @return - Bitmap compressed
     */
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

        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

    }

    /**
     * This method create rounded border on a bitmap
     * @param bitmap - Bitmap source
     * @return - Bitmap rounded
     */
    public static Bitmap createRoundedBorderOfBitmap (Bitmap bitmap){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 15;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF,roundPx,roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * This method write a specific SQL request of a filter
     * @param searchPropertyModel - SearchPropertyModel the filter
     * @return SimpleSQLiteQuery the query
     */
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

    /**
     * This method write a specific SQL request of a filter for POI
     * @param searchPropertyModel - SearchPropertyModel the filter
     * @return SimpleSQLiteQuery the query
     */
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
