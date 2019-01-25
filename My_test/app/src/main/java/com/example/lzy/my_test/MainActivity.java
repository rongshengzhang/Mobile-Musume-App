package com.example.lzy.my_test;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username=null;
    private EditText password=null;
    private Button      login=null;
    private Button   Register=null;
    private String  infotemp = "";
    static final String loginInfoURL =
            "http://vcm-538.vm.duke.edu:8000/rsvp/authentication/";
    private String succeed_msg = "login succeed! welcome!";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);
        login    = findViewById(R.id.button1);
        login.setOnClickListener(this);
        Register = findViewById(R.id.button2);
        Register.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                new Logging().execute();
                break;
            case R.id.button2:
                register(view);
                break;
            default:
                break;
        }
    }

    public void login(View view) {
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        String target   = loginInfoURL;
        URL url = null;
        String res = null;

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
            jsonObj.put("username", Username);
            jsonObj.put("password",Password);
            String jsonStr = jsonObj.toString();
            byte[] dataSend = jsonStr.getBytes("UTF8");


            // set HTTP post
            // set content type as text, indicating the post data is text data
            urlConn.setRequestProperty("Content-Type", "application/x-www.form-urlencoded");
            // application/x-www.form-urlencoded is the standard format


            // set the length of content
            urlConn.setRequestProperty("Content-Length", String.valueOf(Username) + String.valueOf(Password));

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

        if(res.equals(succeed_msg)){

        }

    }

    public void register(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }


    private class Logging extends AsyncTask<Void, Void, String> {
        String itemInfo = null;

        /** use POST method, send HTTP request to the server and get its response */
        @Override
        protected String doInBackground(Void... params) {
            String Username = username.getText().toString();
            String Password = password.getText().toString();
            System.out.println(Username);
            System.out.println(Password);
            String target = loginInfoURL;
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
                String jsonStr = jsonObj.toString();
                byte[] dataSend = jsonStr.getBytes("UTF8");


                // set HTTP post
                // set content type as text, indicating the post data is text data
                urlConn.setRequestProperty("Content-Type", "application/x-www.form-urlencoded");
                // application/x-www.form-urlencoded is the standard format


                // set the length of content
                urlConn.setRequestProperty("Content-Length", String.valueOf(Username) + String.valueOf(Password));

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
                Toast.makeText(MainActivity.this, "temp is NULL!", Toast.LENGTH_SHORT).show();
            }
            else {
                if (temp.charAt(0) == 'l') {
                    System.out.println("succeed checking!!!!");
                    Intent intent = new Intent(MainActivity.this, SucceedActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
