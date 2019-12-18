package lk.supervision.saleapp.constant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;

import lk.supervision.saleapp.R;

/**
 * Created by kavish manjitha on 12/26/2017.
 */

public class  DailySummryDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener  {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        try {
            String createdate = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();
            TextView textNewPaymentDate = (TextView) getActivity().findViewById(R.id.text_select_date);
            textNewPaymentDate.setText(AppEnvironmentValues.SIMPLE_DATE_FORMAT.format(AppEnvironmentValues.dateFormat(createdate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
