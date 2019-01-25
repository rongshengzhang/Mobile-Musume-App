package com.example.lzy.my_test;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText username = null;
    private EditText password = null;
    private EditText confirm_password = null;
    private Button   Register = null;
    private String RegisterInfoURL =
            "http://vcm-538.vm.duke.edu:8000/rsvp/addUser/";
    private String email = "yl489@duke.edu";
    private String  infotemp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);
        confirm_password = findViewById(R.id.editText3);
        Register = findViewById(R.id.button1);
        Register.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                new Registering().execute();
                break;
            default:
                break;
        }
    }

    private class Registering extends AsyncTask<Void, Void, String> {
        String itemInfo = null;

        /** use POST method, send HTTP request to the server and get its response */
        @Override
        protected String doInBackground(Void... params) {
            String Username = username.getText().toString();
            String Password = password.getText().toString();
            String confirm  = confirm_password.getText().toString();
            if(!confirm.equals(Password)){
                Toast.makeText(RegisterActivity.this, "Have to ensure passwords match!", Toast.LENGTH_SHORT).show();
                return null;
            }
            String target = RegisterInfoURL;
            URL url = null;
            String res = "";

            try {
                url = new URL(target);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConn = null;
            try {
                urlConn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //establish an HTTP connection
                urlConn.setRequestMethod("POST"); //set HTTP request method to post
                urlConn.setDoOutput(true); // enable outputting data
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);


                JSONObject jsonObj = new JSONObject();
                jsonObj.put("username",Username);
                jsonObj.put("password",Password);
                jsonObj.put("email",email);
                String jsonStr = jsonObj.toString();
                byte[] dataSend = jsonStr.getBytes("UTF8");


                // set HTTP post
                // set content type as text, indicating the post data is text data
                urlConn.setRequestProperty("Content-Type", "application/x-www.form-urlencoded");
                // application/x-www.form-urlencoded is the standard format


                // set the length of content
                urlConn.setRequestProperty("Content-Length", String.valueOf(Username) + String.valueOf(Password) + String.valueOf(email));

                //get output stream
                OutputStream outputStream = urlConn.getOutputStream();

                outputStream.write(dataSend); // send data ServiceMode
                outputStream.flush();
                outputStream.close(); // stop output

//--------------------------------------------------------------------------------------------------
                InputStreamReader inputStream = new InputStreamReader(urlConn.getInputStream()); // get data read
                BufferedReader buffer = new BufferedReader(inputStream); // buffer the input stream
                String inputLine;

                while (((inputLine = buffer.readLine()) != null)) {
                    res += inputLine + "\n";
                }

                inputStream.close(); // stop input stream
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                urlConn.disconnect(); // close the connection
            }
            infotemp = res;
            System.out.println(infotemp);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // this is used to display text sent from main activity
            String temp = infotemp;
            System.out.println("temp:"+temp);
            if(temp == null){
                Toast.makeText(RegisterActivity.this, "temp is NULL!", Toast.LENGTH_SHORT).show();
            }
            else {
                if (temp.charAt(0) == 's') {
                    System.out.println("succeed Registered!!!!");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Username already exists, choose another one!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
