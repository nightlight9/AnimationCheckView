package com.hn481.tianhui.widgettestdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hn481.tianhui.widgettestdemo.ui.MyCheckView;

public class MainActivity extends AppCompatActivity {
    private MyCheckView checkView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkView = (MyCheckView) findViewById(R.id.checkview);
    }
}
