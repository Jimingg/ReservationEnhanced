package sg.edu.rp.c346.reservation;

        import android.app.DatePickerDialog;
        import android.app.TimePickerDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import java.sql.Time;
        import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView tvname;
    TextView tvnumber;
    TextView tvsize;
    EditText etinput1;
    EditText etinput2;
    EditText etinput3;
    CheckBox cbsmoking;
    EditText etDay;
    EditText etTime;
    Button btnres;
    Button btnreset;
     int thisyear;
    int thismonth;
    int thisday;
    int thishour;
    int thisminute;

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("date",etDay.getText().toString());
        prefEdit.putString("time",etTime.getText().toString());
        prefEdit.putString("name",etinput1.getText().toString());
        prefEdit.putString("number",etinput2.getText().toString());
        prefEdit.putString("size",etinput3.getText().toString());
        if (cbsmoking.isChecked()) {
            prefEdit.putString("smoking", "true");
        }else{
            prefEdit.putString("smoking", "false");
        }

        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lastDate =prefs.getString("date","");
        String lastTime=prefs.getString("time","");
        String lastName=prefs.getString("name","");
        String lastNum=prefs.getString("number","");
        String lastSize=prefs.getString("size","");
        String lastsmoking=prefs.getString("smoking","");

        etTime.setText(lastTime);
        etDay.setText(lastDate);
        etinput1.setText(lastName);
        etinput2.setText(lastNum);
        etinput3.setText(lastSize);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        tvname=findViewById(R.id.textViewName);
        tvnumber=findViewById(R.id.textViewNumber);
        tvsize=findViewById(R.id.textViewSize);
        etinput1=findViewById(R.id.editTextName);
        etinput2=findViewById(R.id.editTextNumber);
        etinput3=findViewById(R.id.editTextSize);
        cbsmoking=findViewById(R.id.CheckboxSmoking);
        etDay=findViewById(R.id.editTextDay);
        etTime=findViewById(R.id.editTextTime);
        btnres=findViewById(R.id.buttonRes);
        btnreset=findViewById(R.id.button2Reset);
        if (thisyear == 0 || thismonth ==0 || thisday == 0 || thishour == 0 || thisminute == 0);
        Calendar calendar= Calendar.getInstance();
        thisyear=calendar.get(Calendar.YEAR);
         thismonth=calendar.get(Calendar.MONTH);
         thisday=calendar.get(Calendar.DAY_OF_MONTH);
        thishour=calendar.get(calendar.HOUR_OF_DAY);
        thisminute=calendar.get(calendar.MINUTE);
        etDay.setText("Date : "+thisday+"/"+(thismonth+1)+"/"+ thisyear);
        etTime.setText("Time : "+ thishour+" : "+thisminute);


        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etTime.setText("Time : "+ hourOfDay+" : "+minute);
                        thishour=hourOfDay;
                        thisminute=minute;
                    }
                };
    TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this, myTimeListener, thishour, thisminute, true);
    myTimeDialog.show();

            }
        });
        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etDay.setText("Date : "+dayOfMonth+"/"+(month+1)+"/"+ year);
                        thisyear = year;
                        thismonth = month;
                        thisday = dayOfMonth;
                    }
                };



    DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this,
            myDateListener, thisyear,thismonth, thisday);
    myDateDialog.show();



            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etinput1.getText().clear();
                etinput2.getText().clear();
                etinput3.getText().clear();
                cbsmoking.setChecked(false);
                Calendar calendar= Calendar.getInstance();
                int Y=calendar.get(Calendar.YEAR);
                int M=calendar.get(Calendar.MONTH);
                int D=calendar.get(Calendar.DAY_OF_MONTH);
                int hour=calendar.get(calendar.HOUR_OF_DAY);
                int minute=calendar.get(calendar.MINUTE);
                etDay.setText("Date : "+D+"/"+(M+1)+"/"+ Y);
                etTime.setText("Time : "+ hour+" : "+minute);
            }
        });
        btnres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbsmoking.isChecked()) {
                    String name = etinput1.getText().toString();
                    final String number = etinput2.getText().toString();
                    String size = etinput3.getText().toString();
                    String date = etDay.getText().toString();
                    String time = etTime.getText().toString();
                  final  String text = "New Reservation"+System.getProperty("line.separator") +
                            "Name:" + name + System.getProperty("line.separator") +
                            "Smoking: Yes" + System.getProperty("line.separator") +
                            "size:" + size + System.getProperty("line.separator") +
                            date + System.getProperty("line.separator") +
                            time + System.getProperty("line.separator");
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);

                    //set the dialog details
                    myBuilder.setTitle("Confirm Your Order");
                    myBuilder.setMessage(text);
                    myBuilder.setCancelable(false);
                    myBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"Message send",Toast.LENGTH_LONG).show();
                        }
                    });
                    myBuilder.setNegativeButton("CANCEL", null);
                    AlertDialog myDialog = myBuilder.create();
                    myDialog.show();

                }else{
                    String name = etinput1.getText().toString();
                    final String number = etinput2.getText().toString();
                    String size = etinput3.getText().toString();
                    String date = etDay.getText().toString();
                    String time = etTime.getText().toString();
                    final String text = "New Reservation"+System.getProperty("line.separator") +
                            "Name:" + name + System.getProperty("line.separator") +
                            "Smoking:No" + System.getProperty("line.separator") +
                            "size:" + size + System.getProperty("line.separator") +
                            date + System.getProperty("line.separator") +
                            time + System.getProperty("line.separator");
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);

                    //set the dialog details
                    myBuilder.setTitle("Confirm Your Order");
                    myBuilder.setMessage(text);
                    myBuilder.setCancelable(false);
                    myBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                               Toast.makeText(getApplicationContext(),"Message send",Toast.LENGTH_LONG).show();
                        }
                    });
                    myBuilder.setNegativeButton("CANCEL", null);
                    AlertDialog myDialog = myBuilder.create();
                    myDialog.show();
                }
            }
        });
    }

}
