package com.example.dummyattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {
    EditText edtEmailAddress, edtPassword;
    Button btnLogin;
    TextView lror;
    ProgressBar progressBar;
    LinearLayout lvparent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        lvparent = findViewById(R.id.lvparent);
        lror=findViewById(R.id.ltor);
        progressBar = findViewById(R.id.progressBar);


        lror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });

    }


    private class DoLoginForUser extends AsyncTask<String, Void, String> {
        String email, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            email = edtEmailAddress.getText().toString();
            password = edtPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionHelper con = new ConnectionHelper();
                Connection connect = ConnectionHelper.CONN();

                String query = "Select * from userInfo where email='" + email + "'";
                PreparedStatement ps = connect.prepareStatement(query);

                Log.e("query",query);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String passcode = rs.getString("password");
                    connect.close();
                    rs.close();
                    ps.close();
                    if (passcode != null && !passcode.trim().equals("") && passcode.equals(password))
                        return "success";
                    else
                        return "Invalid Credentials";

                } else
                    return "User does not exists.";
            } catch (SQLException e) {

                return "Error:" + e.getMessage().toString();
            } catch (Exception e) {
                return "Error:" + e.getMessage().toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(signup.this, result, Toast.LENGTH_SHORT).show();
            ShowSnackBar(result);
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            if (result.equals("success")) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences
                        ("userdetails",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("email",edtEmailAddress.getText().toString());

                editor.commit();

                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);

            } else {
                ShowSnackBar(result);
            }
        }
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

    public void DoLogin(View v)
    {
        DoLoginForUser login = new DoLoginForUser();
        login.execute("");
    }

}