package com.example.customerregistration;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class CustomerAdapter extends ArrayAdapter<Customer> {

    Context mCtx;
    int listLayoutRes;
    List<Customer> customerList;
    SQLiteDatabase mDatabase;

    public CustomerAdapter(Context mCtx, int listLayoutRes, List<Customer> customerList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, customerList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.customerList = customerList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Customer customer = customerList.get(position);


        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewPlatform = view.findViewById(R.id.textViewPlatform);
        TextView textViewContact = view.findViewById(R.id.textViewContact);
        TextView textViewJoiningDate = view.findViewById(R.id.textViewJoiningDate);


        textViewName.setText(customer.getName());
        textViewPlatform.setText(customer.getPlatform());
        textViewContact.setText(String.valueOf(customer.getContact()));
        textViewJoiningDate.setText(customer.getJoiningDate());


        Button buttonDelete = view.findViewById(R.id.buttonDeleteCustomer);
        /*Button buttonEdit = view.findViewById(R.id.buttonEditCustomer);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCustomer(customer);
            }
        }); */

        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM customers WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{customer.getId()});
                        reloadCustomersFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void updateCustomer(final Customer customer) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_customers, null);
        builder.setView(view);


        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextContact = view.findViewById(R.id.editTextContact);
        final Spinner spinnerPlatform = view.findViewById(R.id.spinnerPlatform);

        editTextName.setText(customer.getName());
        editTextContact.setText(String.valueOf(customer.getContact()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String contact = editTextContact.getText().toString().trim();
                String platform = spinnerPlatform.getSelectedItem().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (contact.isEmpty()) {
                    editTextContact.setError("Salary can't be blank");
                    editTextContact.requestFocus();
                    return;
                }

                String sql = "UPDATE customers \n" +
                        "SET name = ?, \n" +
                        "platform = ?, \n" +
                        "contact = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{name, platform, contact, String.valueOf(customer.getId())});
                Toast.makeText(mCtx, "Customer Updated", Toast.LENGTH_SHORT).show();
                reloadCustomersFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadCustomersFromDatabase() {
        Cursor cursorCustomers = mDatabase.rawQuery("SELECT * FROM customers", null);
        if (cursorCustomers.moveToFirst()) {
            customerList.clear();
            do {
                customerList.add(new Customer(
                        cursorCustomers.getInt(0),
                        cursorCustomers.getString(1),
                        cursorCustomers.getString(2),
                        cursorCustomers.getString(3),
                        cursorCustomers.getInt(4)
                ));
            } while (cursorCustomers.moveToNext());
        }
        cursorCustomers.close();
        notifyDataSetChanged();
    }

}
