package com.example.diogo.discoverytrip.Util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends android.support.v4.app.DialogFragment  implements DatePickerDialog.OnDateSetListener {

    private DatePickerFragmentListener datePickerListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("Logger", "DatePickerFragment onCreateDialog");
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) this, year, month, day);
    }

    public interface DatePickerFragmentListener {
        public void onDateSet(Date date);
    }

    public DatePickerFragmentListener getDatePickerListener() {
        Log.d("Logger", "DatePickerFragment getDatePickerListener");
        return this.datePickerListener;
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        Log.d("Logger", "DatePickerFragment setDatePickerListener");
        this.datePickerListener = listener;
    }

    protected void notifyDatePickerListener(Date date) {
        Log.d("Logger", "DatePickerFragment notifyDatePickerListener");
        if(this.datePickerListener != null) {
            this.datePickerListener.onDateSet(date);
        }
    }

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        Log.d("Logger", "DatePickerFragment DatePickerFragment");
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        return fragment;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("Logger", "DatePickerFragment onDateSet");
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
//        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();

        // Here we call the listener and pass the date back to it.
        notifyDatePickerListener(date);
    }
}
