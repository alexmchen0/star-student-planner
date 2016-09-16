package testing.ver1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import testing.ver1.ToDoItem.Priority;
import testing.ver1.ToDoItem.Status;

public class AddReminderActivity extends AppCompatActivity {

    private static String timeString;
    private static String dateString;
	private static TextView dateView;
    private static TextView timeView;
    private static Calendar mCalendarDate;

    private Boolean isEdit;
	private CheckBox mPriorityCheckBox;
	private EditText mTitleText;
    private EditText mDetailsText;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reminder);

		mTitleText = (EditText) findViewById(R.id.title);
        mDetailsText = (EditText) findViewById(R.id.details);
		mPriorityCheckBox = (CheckBox) findViewById(R.id.priority_checkBox);
        dateView = (TextView) findViewById(R.id.date);
        timeView = (TextView) findViewById(R.id.time);
        isEdit = false;

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            setDefaultDateTime();
        else {
            isEdit = true;
            fillData(extras);
        }

		final Button datePickerButton = (Button) findViewById(R.id.date_picker_button);
		datePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        final Button timePickerButton = (Button) findViewById(R.id.time_picker_button);
        timePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_todo, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.submit_button:
				Priority priority = getPriority();
				Status status = Status.NOTDONE;
				String title = mTitleText.getText().toString();
                String details = mDetailsText.getText().toString();

				Intent data = new Intent();
                if(isEdit)
                    ToDoItem.packageIntent(data, "EditReminder", title, null, null, priority, status, mCalendarDate, details);
				else
                    ToDoItem.packageIntent(data, "Reminder", title, null, null, priority, status, mCalendarDate, details);

                setResult(RESULT_OK, data);
				finish();
				return true;

			case R.id.cancel_button:
				setResult(RESULT_CANCELED, null);
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

    private void fillData(Bundle extras) {
        mTitleText.setText(extras.getString(ToDoItem.TITLE));
        mCalendarDate.set(Calendar.YEAR, extras.getInt(ToDoItem.YEAR));
        mCalendarDate.set(Calendar.DAY_OF_YEAR, extras.getInt(ToDoItem.DAY_OF_YEAR));
        setDateString(mCalendarDate.get(Calendar.YEAR), mCalendarDate.get(Calendar.MONTH), mCalendarDate.get(Calendar.DAY_OF_MONTH));
        setTimeString(mCalendarDate.get(Calendar.HOUR_OF_DAY), mCalendarDate.get(Calendar.MINUTE));
        dateView.setText(dateString);
        timeView.setText(timeString);
        if(extras.getString(ToDoItem.PRIORITY).equals("HIGH")) mPriorityCheckBox.setChecked(true);
        mDetailsText.setText(extras.getString(ToDoItem.DETAILS));

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Reminder");
        Window window = this.getWindow();
        String subject = extras.getString(ToDoItem.SUBJECT);

        if(subject==null);
        else if(subject.equals("English")){
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.red)));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.redDark));
        }
        else if(subject.equals("Calculus")) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.orange)));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orangeDark));
        }
        else if(subject.equals("Chemistry")){
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yellow)));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.yellowDark));
        }
        else if(subject.equals("Physics")){
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.green)));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.greenDark));
        }
        else if(subject.equals("Economics")){
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.blue)));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blueDark));
        }
        else if(subject.equals("Comp Sci")){
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.purple)));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.purpleDark));
        }
        else;
    }

	private void setDefaultDateTime() {
		Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);

        mCalendarDate = c;
        mCalendarDate.set(Calendar.MINUTE, 0);

		setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dateView.setText(dateString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        timeView.setText(timeString);
	}

	private static void setDateString(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();

        if(c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == monthOfYear && c.get(Calendar.DAY_OF_MONTH) + 1 == dayOfMonth)
            dateString = "Tomorrow";

        else if(c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == monthOfYear && c.get(Calendar.DAY_OF_MONTH) == dayOfMonth)
            dateString = "Today";

        else {
            monthOfYear++;
            String mon = "" + monthOfYear;
            String day = "" + dayOfMonth;
            year -= 2000;

            if (monthOfYear < 10)
                mon = "0" + monthOfYear;
            if (dayOfMonth < 10)
                day = "0" + dayOfMonth;

            dateString = mon + "/" + day + "/" + year;
        }
	}

    private static void setTimeString(int hourOfDay, int minuteOfDay) {
        String ampm = "AM";

        if (hourOfDay >= 12) {
            if (hourOfDay > 12)
                hourOfDay = hourOfDay - 12;
            ampm = "PM";
        }

        if (hourOfDay == 0)
            hourOfDay = 12;

        String hour = "" + hourOfDay;
        String minute = "" + minuteOfDay;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;

        if (minuteOfDay < 10)
            minute = "0" + minuteOfDay;

        timeString = hour + ":" + minute + " " + ampm;
    }

    private Priority getPriority() {
        if(mPriorityCheckBox.isChecked()) {
            return Priority.HIGH;
        }
        else return Priority.LOW;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH) + 1;

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendarDate.set(year, monthOfYear, dayOfMonth);
            setDateString(year, monthOfYear, dayOfMonth);
			dateView.setText(dateString);
        }
	}

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = 0;

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendarDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendarDate.set(Calendar.MINUTE, minute);
            setTimeString(hourOfDay, minute);
            timeView.setText(timeString);
        }
    }

	private void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
}
