package com.example.customerregistration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    List<Customer> customerList;
    SQLiteDatabase mDatabase;
    ListView listViewCustomers;
    CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        listViewCustomers = (ListView) findViewById(R.id.listViewCustomers);
        customerList = new ArrayList<>();

        //opening the database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the employees in the list
        showCustomersFromDatabase();
    }

    private void showCustomersFromDatabase() {
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorCustomers = mDatabase.rawQuery("SELECT * FROM customers", null);

        //if the cursor has some data
        if (cursorCustomers.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                customerList.add(new Customer(
                        cursorCustomers.getInt(0),
                        cursorCustomers.getString(1),
                        cursorCustomers.getString(2),
                        cursorCustomers.getString(3),
                        cursorCustomers.getInt(4)
                ));
            } while (cursorCustomers.moveToNext());
        }
        //closing the cursor
        cursorCustomers.close();

        //creating the adapter object
        adapter = new CustomerAdapter(this, R.layout.list_layout_customer,customerList, mDatabase);

        //adding the adapter to listview
        listViewCustomers.setAdapter(adapter);
    }

}

