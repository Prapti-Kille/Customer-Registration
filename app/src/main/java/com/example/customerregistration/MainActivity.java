package com.example.customerregistration;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "mycustomerdatabase";

    TextView textViewViewCustomers;
    EditText editTextName, editTextContact;
    Spinner spinnerPlatform;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewCustomers = (TextView) findViewById(R.id.textViewViewCustomers);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        spinnerPlatform = (Spinner) findViewById(R.id.spinnerPlatform);

        findViewById(R.id.buttonAddCustomer).setOnClickListener(this);
        textViewViewCustomers.setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        createCustomerTable();
    }


    //this method will create the table
    //as we are going to call this method everytime we will launch the application
    //I have added IF NOT EXISTS to the SQL
    //so it will only create the table when the table is not already created
    private void createCustomerTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS customers (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT customers_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    platform varchar(200) NOT NULL,\n" +
                        "    joiningdate datetime NOT NULL,\n" +
                        "    contact INTEGER  NOT NULL\n" +
                        ");"
        );
    }

    //this method will validate the name and salary
    //dept does not need validation as it is a spinner and it cannot be empty
    private boolean inputsAreCorrect(String name, String contact) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        if (contact.isEmpty() || Integer.parseInt(contact) <= 0) {
            editTextContact.setError("Please enter contact number");
            editTextContact.requestFocus();
            return false;
        }
        return true;
    }

    //In this method we will do the create operation
    private void addCustomer() {

        String name = editTextName.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String platform = spinnerPlatform.getSelectedItem().toString();

        //getting the current time for joining date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

        //validating the inptus
        if (inputsAreCorrect(name, contact)) {

            String insertSQL = "INSERT INTO customers \n" +
                    "(name, platform, joiningdate, contact)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?);";

            //using the same method execsql for inserting values
            //this time it has two parameters
            //first is the sql string and second is the parameters that is to be binded with the query
            mDatabase.execSQL(insertSQL, new String[]{name, platform, joiningDate, contact});

            Toast.makeText(this, "Customer Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddCustomer:

                addCustomer();

                break;
            case R.id.textViewViewCustomers:

                startActivity(new Intent(this, CustomerActivity.class));

                break;
        }
    }
}
