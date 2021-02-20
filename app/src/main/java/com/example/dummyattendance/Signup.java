package com.example.dummyattendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;



public class Signup extends AppCompatActivity {


    EditText edtEmailAddress, edtPassword, edtConfirmPassword, edtName,edtMobile;
    Button btnSignUp,_login;
    TextView rtol;
    ProgressBar progressBar;
    LinearLayout lvparent;
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        edtName=findViewById(R.id.edtName);
        edtMobile=findViewById(R.id.edtMobile);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        rtol=findViewById(R.id.rtol);
        progressBar = findViewById(R.id.pbbar);
        lvparent = findViewById(R.id.lvparent);
        this.setTitle("User SignUp");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isEmpty(edtEmailAddress.getText().toString()) ||
                        isEmpty(edtPassword.getText().toString()) ||
                        isEmpty(edtConfirmPassword.getText().toString())||
                         isEmpty(edtName.getText().toString())||
                          isEmpty(edtMobile.getText().toString()))
                    ShowSnackBar("Please enter all fields");
                else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString()))
                    ShowSnackBar("Password does not match");
                else {
                    AddUsers addUsers = new AddUsers();
                    addUsers.execute("");
                }

            }
        });



           rtol.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(Signup.this,Login.class);
                   startActivity(intent);
               }
           });










    }

    public void ShowSnackBar(String message) {
        Snackbar.make(lvparent, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public Boolean isEmpty(String strValue) {
        if (strValue == null || strValue.trim().equals(("")))
            return true;
        else
            return false;
    }

    private class AddUsers extends AsyncTask<String, Void, String> {
        String name,mobile,email, password;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name=edtName.getText().toString();
            mobile=edtMobile.getText().toString();
            email = edtEmailAddress.getText().toString();
            password = edtPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionHelper con = new ConnectionHelper();
                Connection connect = ConnectionHelper.CONN();

                String queryStmt = "Insert into userInfo " +
                        " (name,email,mobile,Password) values "
                        + "('"
                        +name
                        + "','"
                        +email
                        +"','"
                        +mobile
                        +"','"
                        +password
                        + "')";

                PreparedStatement preparedStatement = connect
                        .prepareStatement(queryStmt);

                preparedStatement.executeUpdate();

                preparedStatement.close();

                return "Registration successfully";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage().toString();
            } catch (Exception e) {
                return "Server under maintenance.";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(signup.this, result, Toast.LENGTH_SHORT).show();
            ShowSnackBar(result);
            progressBar.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.VISIBLE);
            if (result.equals("Registration successfully")) {
                // Clear();
            }

        }

        

    }

}

