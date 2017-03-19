package com.example.diogo.discoverytrip.Util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends android.support.v4.app.DialogFragment  implements TimePickerDialog.OnTimeSetListener {

    private TimePickerFragmentListener timePickerListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("Logger", "TimePickerFragment onCreateDialog");
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) this, hour, minute, true);
    }

    public interface TimePickerFragmentListener {
        public void onTimeSet(int hour,int minute);
    }

    public TimePickerFragmentListener getDatePickerListener() {
        Log.d("Logger", "TimePickerFragment getDatePickerListener");
        return this.timePickerListener;
    }

    public void setTimePickerListener(TimePickerFragmentListener listener) {
        Log.d("Logger", "TimePickerFragment setDatePickerListener");
        this.timePickerListener = listener;
    }

    protected void notifyTimePickerListener(int hour,int minute) {
        Log.d("Logger", "TimePickerFragment notifyDatePickerListener");
        if(this.timePickerListener != null) {
            this.timePickerListener.onTimeSet(hour, minute);
        }
    }

    public static TimePickerFragment newInstance(TimePickerFragmentListener listener) {
        Log.d("Logger", "TimePickerFragment newInstance");
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setTimePickerListener(listener);
        return fragment;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("Logger", "TimePickerFragment onTimeSet");

        // Here we call the listener and pass the time back to it.
        notifyTimePickerListener(hourOfDay, minute);

    }
}
