package com.macro.nativeInterfaceJS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.tv_a_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WebViewActivityNativeCallJS.class).putExtra("offer", 1);
                startActivity(i);
            }
        });
        findViewById(R.id.tv_a_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WebViewActivityNativeCallJS.class).putExtra("offer", 2);
                startActivity(i);
            }
        });
        findViewById(R.id.tv_a_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WebViewActivityNativeCallJS.class).putExtra("offer", 32);
                startActivity(i);
            }
        });



        findViewById(R.id.tv_b_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebViewActivityJSCallNativeIntercept.class));
            }
        });

        findViewById(R.id.tv_b_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebViewActivityJSCallNativeIJSInterface.class));
            }
        });
        findViewById(R.id.tv_b_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebViewActivityJsCallNativeJSBridge.class));
            }
        });



    }
}
