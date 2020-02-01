package com.rohim.rohimmodule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.loader.content.CursorLoader;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ItemValidation {

    private final String TAG = "Item.Validation";

    public String getToday(String format){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public boolean validateCustomDate(final EditText edt, final String format){
        String textDate = edt.getText().toString();
        if(!isValidFormat(format,textDate)){
            edt.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public String getCurrentDate(String format){
        return new SimpleDateFormat(format).format(new Date());
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public void CustomDateFormatCorrection(final EditText edt){

        edt.addTextChangedListener(new TextWatcher() {
            boolean changeChar = true;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edt.getText().length() == 5 || edt.getText().length() == 8){
                    if(edt.getText().toString().charAt(edt.getText().length() - 1) == '-' ){
                        changeChar = false;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if((edt.getText().length() == 4 || edt.getText().length() == 7) && changeChar){
                    edt.setText(edt.getText().toString() + "-");
                    edt.setSelection(edt.getText().length());
                }
                changeChar = true;
            }
        });
    }

    public String ChangeFormatDateString(String date, String formatDateFrom, String formatDateTo){

        if (!date.isEmpty()){

            String result = date;
            SimpleDateFormat sdf = new SimpleDateFormat(formatDateFrom);
            SimpleDateFormat sdfCustom = new SimpleDateFormat(formatDateTo);

            Date date1 = null;
            try {
                date1 = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return (date1 == null || date.isEmpty()) ? "" : sdfCustom.format(date1);
        }else{

            return "";
        }
    }

    public int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public int[] getScreenResolution(Context context){

        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int[] sizeArray = new int[2];
        sizeArray[0] =  width;
        sizeArray[1] = height;
        return sizeArray;
    }

    public boolean isMoreThanCurrentDate(EditText edt, EditText edt2, String format){

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dateToCompare = null;
        Date dateCompare = null;

        try {
            dateToCompare = sdf.parse(edt.getText().toString());
            dateCompare = sdf.parse(edt2.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(dateToCompare.after(dateCompare) || dateToCompare.equals(dateCompare)){
            return true;
        }else{
            return false;
        }
    }

    public String sumDate(String date1, int numberOfDay, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, numberOfDay);
        return sdf.format(c.getTime());
    }

    public String sumMinutes(String date1, int numberOfMinutes, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MINUTE, numberOfMinutes);
        return sdf.format(c.getTime());
    }
    //endregion

    //region Datepicker
    public void datePickerEvent(final Context context, final EditText edt, final String drawablePosition, final String formatDate){
        edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int prePosition;
                final Calendar customDate;

                switch (drawablePosition.toUpperCase()){
                    case "LEFT":
                        prePosition = 0;
                        break;
                    case "TOP":
                        prePosition = 1;
                        break;
                    case "RIGHT":
                        prePosition = 2;
                        break;
                    case "Bottom":
                        prePosition = 3;
                        break;
                    default:
                        prePosition = 2;
                }

                final int position = prePosition;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edt.getRight() - edt.getCompoundDrawables()[position].getBounds().width())) {

                        /*Log.d(TAG, "onTouch: ");
                        // set format date
                        customDate = Calendar.getInstance();
                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                                customDate.set(Calendar.YEAR,year);
                                customDate.set(Calendar.MONTH,month);
                                customDate.set(Calendar.DATE,date);

                                SimpleDateFormat sdFormat = new SimpleDateFormat(formatDate, Locale.US);
                                edt.setText(sdFormat.format(customDate.getTime()));
                            }
                        };

                        new DatePickerDialog(context,date,customDate.get(Calendar.YEAR),customDate.get(Calendar.MONTH),customDate.get(Calendar.DATE)).show();
                        return true;*/
                    }

                    customDate = Calendar.getInstance();
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                            customDate.set(Calendar.YEAR,year);
                            customDate.set(Calendar.MONTH,month);
                            customDate.set(Calendar.DATE,date);

                            SimpleDateFormat sdFormat = new SimpleDateFormat(formatDate, Locale.US);
                            edt.setText(sdFormat.format(customDate.getTime()));
                        }
                    };

                    new DatePickerDialog(context,date,customDate.get(Calendar.YEAR),customDate.get(Calendar.MONTH),customDate.get(Calendar.DATE)).show();
                    return true;
                }
                return false;
            }
        });
    }

    public void datePickerEvent(final Context context, final EditText edt, final String drawablePosition, final String formatDate, final String value){
        edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int prePosition;
                final Calendar customDate;

                switch (drawablePosition.toUpperCase()){
                    case "LEFT":
                        prePosition = 0;
                        break;
                    case "TOP":
                        prePosition = 1;
                        break;
                    case "RIGHT":
                        prePosition = 2;
                        break;
                    case "Bottom":
                        prePosition = 3;
                        break;
                    default:
                        prePosition = 2;
                }

                final int position = prePosition;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edt.getRight() - edt.getCompoundDrawables()[position].getBounds().width())) {

                        /*Log.d(TAG, "onTouch: ");
                        // set format date
                        customDate = Calendar.getInstance();
                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                                customDate.set(Calendar.YEAR,year);
                                customDate.set(Calendar.MONTH,month);
                                customDate.set(Calendar.DATE,date);

                                SimpleDateFormat sdFormat = new SimpleDateFormat(formatDate, Locale.US);
                                edt.setText(sdFormat.format(customDate.getTime()));
                            }
                        };

                        new DatePickerDialog(context,date,customDate.get(Calendar.YEAR),customDate.get(Calendar.MONTH),customDate.get(Calendar.DATE)).show();
                        return true;*/
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat(formatDate);

                    Date dateValue = null;

                    try {
                        dateValue = sdf.parse(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    customDate = Calendar.getInstance();
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                            customDate.set(Calendar.YEAR,year);
                            customDate.set(Calendar.MONTH,month);
                            customDate.set(Calendar.DATE,date);

                            SimpleDateFormat sdFormat = new SimpleDateFormat(formatDate, Locale.US);
                            edt.setText(sdFormat.format(customDate.getTime()));
                        }
                    };


                    SimpleDateFormat yearOnly = new SimpleDateFormat("yyyy");
                    new DatePickerDialog(context,date, parseNullInteger(yearOnly.format(dateValue)),dateValue.getMonth(),dateValue.getDate()).show();
                    return true;
                }
                return false;
            }
        });
    }
    //endregion

    //endregion

    //region Number
    /* Change Number to Rupiah*/
    public String ChangeToCurrencyFormat(String number){

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(parseNullDouble(number)));

        /*String stringConvert = "0";
        try {
            stringConvert = format.format(1000);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


        if(!stringConvert.contains(",")){
            hasil += ",00";
        }*/

        return hasil;
    }

    public String ChangeToCurrencyFormat(Double number){

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(number));

        /*String stringConvert = "0";
        try {
            stringConvert = format.format(1000);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


        if(!stringConvert.contains(",")){
            hasil += ",00";
        }*/

        return hasil;
    }

    public String ChangeToCurrencyFormat(int number){

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(number));

        /*String stringConvert = "0";
        try {
            stringConvert = format.format(1000);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


        if(!stringConvert.contains(",")){
            hasil += ",00";
        }*/

        return hasil;
    }

    public String ChangeToRupiahFormat(String number){

        double value = parseNullDouble(number);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("Rp ");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(value));

        return hasil;
    }

    public String ChangeToRupiahFormat(Float number){

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("Rp ");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(number));

        /*String stringConvert = "0";
        try {
            stringConvert = format.format(1000);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


        if(!stringConvert.contains(",")){
            hasil += ",00";
        }*/

        return hasil;
    }

    public String ChangeToRupiahFormat(Double number){

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("Rp ");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(number));

        return hasil;
    }

    public String ChangeToRupiahFormat(Long number){

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("Rp ");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(number));

        return hasil;
    }
    //endregion

    //region Progressbar
    public void ProgressbarEvent(final LinearLayout llItem, final ProgressBar pbItem, final Button btnItem, String condition){

        if (condition.toUpperCase().equals("SHOW")){
            llItem.setVisibility(View.VISIBLE);
            pbItem.setVisibility(View.VISIBLE);
            btnItem.setVisibility(View.GONE);
        }else if(condition.toUpperCase().equals("ERROR")){
            llItem.setVisibility(View.VISIBLE);
            pbItem.setVisibility(View.GONE);
            btnItem.setVisibility(View.VISIBLE);
        }else if(condition.toUpperCase().equals("GONE")){
            llItem.setVisibility(View.GONE);
            pbItem.setVisibility(View.GONE);
            btnItem.setVisibility(View.GONE);
        }

    }
    //endregion

    //region Nullable value
    public int parseNullInteger(String s){

        int result = 0;
        if(s != null && s.length() > 0){
            try {
                result = Integer.parseInt(s);
            }catch (Exception e){
                result = 0;
                e.printStackTrace();
            }
        }
        return result;
    }

    //region Nullable value
    public long parseNullLong(String s){
        long result = 0;
        if(s != null && s.length() > 0){
            try {
                result = Long.parseLong(s);
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        return result;
    }

    //nullable value
    public float parseNullFloat(String s){
        float result = 0;
        if(s != null && s.length() > 0){
            try {
                result = Float.parseFloat(s);
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        return result;
    }

    public Double parseNullDouble(String s){
        double result = 0;
        if(s != null && s.length() > 0){
            try {
                result = Double.parseDouble(s);
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        return result;
    }

    public String doubleToString(Double number, String numberOfDouble){
        return String.format("%."+ numberOfDouble+"f", number).replace(",",".");
    }

    public String doubleToString(Double number){
        return String.format("%.1f", number).replace(",",".");
    }

    public String doubleToStringRound(Double number){
        return String.format("%.0f", number).replace(",",".");
    }

    public String doubleToStringFull(Double number){
        return String.format("%s", number).replace(",",".");
    }

    public String parseNullString(String s){
        String result = "";
        if(s != null){
            result = s;
        }
        return result;
    }

    public int floatToInteger(float f){
        int result = 0;
        String temp = "0";
        temp = String.format("%.0f", f);
        if(temp != null){
            try {
                result = Integer.parseInt(temp);
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        return result;
    }

    public String toPercent(String value){
        return String.valueOf(Float.parseFloat(value) * 100) + " %";
    }
    //endregion

    public TextWatcher textChangeListenerCurrency(final EditText editText) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                editText.removeTextChangedListener(this);
                String originalString = s.toString();

                /*if(originalString.contains(",")){
                    originalString = originalString.replaceAll(",", "");
                }

                if(originalString.contains(".")){
                    originalString = originalString.replaceAll("\\.", "");
                }*/
                originalString = originalString.replace(",", "").replace(".","");

                DecimalFormat formatter = new DecimalFormat();
                String stringConvert = "0";
                try {
                    stringConvert = formatter.format(Double.parseDouble(originalString));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }

                editText.setText(stringConvert);
                editText.setSelection(editText.getText().length());
                editText.addTextChangedListener(this);
            }
        };
    }

    public void setCurrencyFormatOnFocus(final EditText edt){

        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){

                    String originalString = edt.getText().toString();
                    originalString = originalString.replaceAll(",", "");
                    edt.setText(originalString);
                }else{

                    String originalString = edt.getText().toString();
                    if(originalString.length() > 0 && !originalString.trim().equals("0")){
                        String pattern = "#,##0.00";
                        DecimalFormat formatter = new DecimalFormat(pattern);
                        String formattedString = formatter.format(parseNullFloat(edt.getText().toString()));

                        //setting text after format to EditText
                        edt.setText(formattedString);
                    }
                }
            }
        });
    }
    //endregion

    public void hideSoftKey(Context context){
        try {

            InputMethodManager inputManager =
                    (InputMethodManager) context.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    ((Activity) context).getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS); ((Activity) context).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        @SuppressLint("RestrictedApi") Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public String sha256(String message, String key) {

        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        try {
            sha256_HMAC.init(secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        String hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.DEFAULT);
        return hash;
    }

    public String encodeBase64(String value){

        String hasil = value;
        try {
            byte[] encodeValue = Base64.encode(value.getBytes(), Base64.DEFAULT);
            hasil = new String(encodeValue);
        }catch (Exception e){
            e.printStackTrace();
            hasil = "";
        }
        return hasil;
    }

    public String decodeBase64(String value){

        String hasil = value;
        try {
            byte[] decodeValue = Base64.decode(value.getBytes(), Base64.DEFAULT);
            hasil = new String(decodeValue);
        }catch (Exception e){
            hasil = "";
        }

        return hasil;
    }

    public String encodeMD5(String text) {

        String result = "";
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(text.getBytes(), 0, text.length());
            text = new BigInteger(1, mdEnc.digest()).toString(16);
            while (text.length() < 32) {
                text = "0" + text;
            }
            result = text;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public int getActionBarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        return  mActionBarSize;
    }

    public String getPathFromUri(Context context, Uri fileUri) {
        String realPath;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            realPath = getRealPathFromURI_BelowAPI11(context, fileUri);
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            realPath = getRealPathFromURI_API11to18(context, fileUri);
        }
        // SDK > 19 (Android 4.4) and up
        else {
            realPath = getRealPathFromURI_API19(context, fileUri);
        }
        return realPath;
    }


    @SuppressLint("NewApi")
    public String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        }
        return result;
    }

    public String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = 0;
        String result = "";
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return result;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public boolean isImage(String extiontion){
        return (extiontion.equals(".jpeg") || extiontion.equals(".jpg") || extiontion.equals(".png") || extiontion.equals(".bmp"));
    }

    public static ArrayList<String> getIMEI(Context context) {

        ArrayList<String> imeiList = new ArrayList<>();
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
                Class<?>[] parameter = new Class[1];
                parameter[0] = int.class;
                Method getFirstMethod = telephonyClass.getMethod("getDeviceId", parameter);
                //Log.d("SimData", getFirstMethod.toString());
                Object[] obParameter = new Object[1];
                obParameter[0] = 0;
                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String first = (String) getFirstMethod.invoke(telephony, obParameter);
                if (first != null && !first.equals("")) imeiList.add(first);
                //Log.d("SimData", "first :" + first);
                obParameter[0] = 1;
                String second = (String) getFirstMethod.invoke(telephony, obParameter);
                if (second != null && !second.equals("")) imeiList.add(second);
                //Log.d("SimData", "Second :" + second);
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                @SuppressLint("MissingPermission") String first = telephony.getDeviceId();
                imeiList.add(first);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return imeiList;
    }

    public int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}
