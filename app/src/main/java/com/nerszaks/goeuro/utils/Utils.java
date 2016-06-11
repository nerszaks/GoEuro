package com.nerszaks.goeuro.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nerszaks.goeuro.GoEuroApp;
import com.nerszaks.goeuro.MainActivity;
import com.nerszaks.goeuro.fragments.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 11.06.2016.
 */
public class Utils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static void showDatePickerDialog(MainActivity activity, long selectedDate,
                                            DatePickerDialog.OnDateSetListener onDateSetListener) {
        DialogFragment pickerFragment = new DatePickerFragment();
        ((DatePickerFragment) pickerFragment).setDateSetListener(onDateSetListener);
        ((DatePickerFragment) pickerFragment).setDate(selectedDate);
        pickerFragment.show(activity.getSupportFragmentManager(), "datePicker");
    }

    public static void updateCalendar(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public static String formatDateToDisplay(long date) {
        return simpleDateFormat.format(new Date(date));
    }

    public static void clearTimeCalendar(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void hideKeyboard(final View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    public static boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) GoEuroApp.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        } catch (Exception exception) {
            return true;
        }
    }


}
