package com.nareshgediya.restapivolleydemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText t1,t2,t3;
    TextView tv,responseTv;
    Button insertBtn,insertDemo;
    ProgressDialog progressDialog;

    private static String url = "http://10.0.2.2/api/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = new ProgressDialog(SignUpActivity.this);

        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        t1 = findViewById(R.id.name);
        t2 = findViewById(R.id.email);
        t3 = findViewById(R.id.pass);
        tv = findViewById(R.id.textView);
        responseTv = findViewById(R.id.textViewTV);
        insertBtn= findViewById(R.id.button);
        insertDemo= findViewById(R.id.buttonDemp);

        insertDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });


        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!t1.getText().toString().isEmpty() && !t2.getText().toString().isEmpty() && !t3.getText().toString().isEmpty()){
                    processData(t1.getText().toString(), t2.getText().toString(),t3.getText().toString());
                    progressDialog.show();
                }

            }
        });

    }
    private void processData(final String name, final String email, final String password) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                t1.setText("");
                t2.setText("");
                t3.setText("");
                responseTv.setText(response);
                Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                t1.setText("");
                t2.setText("");
                t3.setText("");
                responseTv.setText(error.toString());
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name",name);
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 20000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 20000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                tv.setText(error.toString());
            }
        });
    }
}