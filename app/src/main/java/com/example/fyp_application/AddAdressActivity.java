package com.example.fyp_application;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAdressActivity extends AppCompatActivity {

    private EditText city;
    private EditText locality;
    private EditText faltNo;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternativeMobileNo;
    private Spinner stateSpinner;
    private Button saveBtn;

    private Dialog loadingDialog;

    private String[] districtList;
    private String selectedDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //loading diaglog
        loadingDialog = new Dialog(AddAdressActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialogue);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.input_field));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        districtList = getResources().getStringArray(R.array.nepal_districts);

        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        faltNo = findViewById(R.id.flat_numberORBuilding_no);
        pincode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_number);
        alternativeMobileNo = findViewById(R.id.alternate_mobile_number);
        stateSpinner= findViewById(R.id.state_spinner);
        saveBtn = findViewById(R.id.save_btn);

        ArrayAdapter spinnerAdaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, districtList);
        spinnerAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(spinnerAdaptor);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = districtList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())){
                    if (!TextUtils.isEmpty(locality.getText())){
                        if (!TextUtils.isEmpty(faltNo.getText())){
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6){
                                if (!TextUtils.isEmpty(name.getText())){
                                    if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 10){

                                        loadingDialog.show();
                                        final String fullAddress = faltNo.getText().toString()+" "+locality.getText().toString()+" "+landmark.getText().toString()+" "+city.getText().toString()+" "+ selectedDistrict;
                                        Map<String, Object> addAddress = new HashMap();
                                        addAddress.put("list_size", (long)DBqueries.addressesModelList.size() + 1);
                                        if (TextUtils.isEmpty(alternativeMobileNo.getText())) {
                                            addAddress.put("fullname_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString());
                                        }else {
                                            addAddress.put("fullname_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString() + " OR "+ alternativeMobileNo.getText().toString());
                                        }
                                        addAddress.put("address_"+ String.valueOf((long)DBqueries.addressesModelList.size() + 1), fullAddress);
                                        addAddress.put("pincode_"+ String.valueOf((long)DBqueries.addressesModelList.size() + 1), pincode.getText().toString().trim());
                                        addAddress.put("selected_"+ String.valueOf((long)DBqueries.addressesModelList.size() + 1), true);
                                        if (DBqueries.addressesModelList.size() > 0) {
                                            addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                        }
                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_DATA").document("MY_ADDRESSES").update(addAddress)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            if (DBqueries.addressesModelList.size() > 0) {
                                                                DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                            }
                                                            if (TextUtils.isEmpty(alternativeMobileNo.getText())) {
                                                                DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString(), fullAddress, pincode.getText().toString(), true));
                                                            } else{
                                                                DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString() + " OR "+ alternativeMobileNo.getText().toString(), fullAddress, pincode.getText().toString(), true));
                                                            }
                                                            if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                                Intent intent = new Intent(AddAdressActivity.this, DeliveryActivity.class);
                                                                startActivity(intent);
                                                            }else {
                                                                MyAddressActivity.refreshItem(DBqueries.selectedAddress, DBqueries.addressesModelList.size() - 1);
                                                            }
                                                            DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                            finish();

                                                        }else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(AddAdressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                        }
                                                        loadingDialog.dismiss();
                                                    }
                                                });
                                    } else {
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddAdressActivity.this, "Please provide valid mobile number.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    name.requestFocus();
                                }
                            } else {
                                pincode.requestFocus();
                                Toast.makeText(AddAdressActivity.this, "Please provide valid pincode.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            faltNo.requestFocus();
                        }
                    } else {
                        locality.requestFocus();
                    }
                } else{
                    city.requestFocus();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
