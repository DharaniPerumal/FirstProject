package in.realtech.ibike_dealer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class Address_dealer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText inputName, inputEmail,input_mobile,input_address,input_pin_code,input_postal_Address,input_District;
    private TextInputLayout inputLayoutName, inputLayoutEmail,input_layout_Mobile,input_layout_address,input_layout_pin_code,input_layout_Postal_Address,input_layout_District;
    private Button btnSignUp;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_dealer);
            setTitle("Add Address");


        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Andra Pradesh");
        categories.add("Arunachal Pradesh");
        categories.add("Assam");
        categories.add("Bihar");
        categories.add("Chhattisgarh");
        categories.add("Goa");
        categories.add("Gujarat");
        categories.add("Haryana");
        categories.add("Himachal Pradesh");
        categories.add("Jammu and Kashmir");
        categories.add("Jharkhand");
        categories.add("Karnataka");
        categories.add("Kerala");
        categories.add("Madya Pradesh");
        categories.add("Maharashtra");
        categories.add("Manipur");
        categories.add("Meghalaya");
        categories.add("Mizoram");
        categories.add("Nagaland");
        categories.add("Orissa");
        categories.add("Punjab");
        categories.add("Rajasthan");
        categories.add("Sikkim");
        categories.add("Tamil Nadu");
        categories.add("Telagana");
        categories.add("Tripura");
        categories.add("Uttaranchal");
        categories.add("Uttar Pradesh");
        categories.add("West Bengal");




        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
       state = spinner.getSelectedItem().toString();

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        input_layout_Mobile = (TextInputLayout) findViewById(R.id.input_layout_Mobile);
        input_layout_address = (TextInputLayout) findViewById(R.id.input_layout_address);
        input_layout_pin_code = (TextInputLayout) findViewById(R.id.input_layout_pin_code);
        input_layout_District = (TextInputLayout) findViewById(R.id.input_layout_District);

        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        input_mobile = (EditText) findViewById(R.id.input_mobile);
        input_address = (EditText) findViewById(R.id.input_address);
        input_pin_code = (EditText) findViewById(R.id.input_pin_code);
        input_District = (EditText) findViewById(R.id.input_District);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        input_address.addTextChangedListener(new MyTextWatcher(input_address));
        input_pin_code.addTextChangedListener(new MyTextWatcher(input_pin_code));
        input_District.addTextChangedListener(new MyTextWatcher(input_District));


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }
        if (!validateAddress()) {
            return;
        }
        if (!validateMobile()) {
            return;
        }
        if (!validatePincode()) {
            return;
        }
        if (!validateDistrict()) {
            return;
        }

        Intent intent=new Intent(Address_dealer.this,purchase_product.class);
        intent.putExtra("name",inputName.getText().toString());
        intent.putExtra("email",inputEmail.getText().toString());
        intent.putExtra("address",input_address.getText().toString());
        intent.putExtra("mobile",input_mobile.getText().toString());
        intent.putExtra("pincode",input_pin_code.getText().toString());
        intent.putExtra("state",state);
        intent.putExtra("District",input_District.getText().toString());





        startActivity(intent);
            finish();
        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter your full name");
            requestFocus(inputName);
            return false;

        } else {

            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }   private boolean validateAddress() {
        if (input_address.getText().toString().trim().isEmpty()) {
            input_layout_address.setError("Enter your address");
            requestFocus(input_address);
            return false;
        } else {
            input_layout_address.setErrorEnabled(false);
        }
        return true;
    } private boolean validateMobile() {
        if (input_mobile.getText().toString().length()!= 10) {
            input_layout_Mobile.setError("Enter your mobile correctly");
            requestFocus(input_mobile);
            return false;
        } else {
            input_layout_Mobile.setErrorEnabled(false);
        }
        return true;
    }private boolean validatePincode() {
        if (input_pin_code.getText().toString().length()!= 6) {
            input_layout_pin_code.setError("Enter your pincode correctly");
            requestFocus(input_pin_code);
            return false;
        } else {
            input_layout_pin_code.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDistrict() {
        if (input_District.getText().toString().trim().isEmpty()) {
            input_layout_District.setError("Enter your District");
            requestFocus(input_District);
            return false;
        } else {
            input_layout_District.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError("Enter valid email address");
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_mobile:
                    validateMobile();
                    break;
                case R.id.input_address:
                    validateAddress();
                    break;
                case R.id.input_pin_code:
                    validatePincode();
                    break;
                case R.id.input_District:
                    validateDistrict();
                    break;

            }
        }

        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.input_name:
//                    validateName();
//                    break;
//                case R.id.input_email:
//                    validateEmail();
//                    break;
//                case R.id.input_mobile:
//                    validateMobile();
//                    break;
//                case R.id.input_address:
//                    validateAddress();
//                    break;
//                case R.id.input_pin_code:
//                    validatePincode();
//                    break;
//                case R.id.input_num_Nos:
//                    validateTotelnoofnos();
//                    break;
//                case R.id.input_num_Nos_text:
//                    validateTotelnoofnostext();
//                    break;
//
//            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon action bar is clicked; go to parent activity
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}