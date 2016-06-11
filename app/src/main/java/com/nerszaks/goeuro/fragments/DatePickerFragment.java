package com.nerszaks.goeuro.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener {

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private Calendar c = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (onDateSetListener != null) {
            onDateSetListener.onDateSet(view, year, month, day);
        }
    }

    public void setDate(long date) {
        c = Calendar.getInstance();
        c.setTimeInMillis(date);
        if (datePickerDialog != null) {
            datePickerDialog.getDatePicker().init(c.get(Calendar.YEAR), c.get(Calendar.YEAR),
                    c.get(Calendar.YEAR), this);
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // Do something with the date changed

    }

    public void setDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

}