package in.realtech.ibike_dealer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
public class Datepicker extends AppCompatActivity {

    //UI References
//    private EditText fromDateEtxt;
//    private EditText toDateEtxt;
//    private DatePickerDialog fromDatePickerDialog;
//    private DatePickerDialog toDatePickerDialog;
//    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);
//
//        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//
//        findViewsById();
//
//        setDateTimeField();
//    }
//
//    private void findViewsById() {
////        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
////        fromDateEtxt.setInputType(InputType.TYPE_NULL);
////        fromDateEtxt.requestFocus();
//
////        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
////        toDateEtxt.setInputType(InputType.TYPE_NULL);
//    }
//
//    private void setDateTimeField() {
//        fromDateEtxt.setOnClickListener(this);
//        toDateEtxt.setOnClickListener(this);
//
//        Calendar newCalendar = Calendar.getInstance();
//        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
//                String a = fromDateEtxt.getText().toString();
//                Log.i("datepicker",a);
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//        toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
//                String b = toDateEtxt.getText().toString();
//                Log.i("datepicker",b);
//
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//    }
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.main, menu);
////        return true;
////    }
//
//    @Override
//    public void onClick(View view) {
//        if(view == fromDateEtxt) {
//            fromDatePickerDialog.show();
//           String a = fromDateEtxt.getText().toString();
//            Log.i("datepicker",a);
//        } else if(view == toDateEtxt) {
//            toDatePickerDialog.show();
//           String b = toDateEtxt.getText().toString();
//            Log.i("datepicker",b);
//        }
//
    }
}
