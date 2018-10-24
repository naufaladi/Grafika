package com.wordpress.devkappa.grafikav3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

public class CompareActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Toolbar mToolbar = findViewById(R.id.toolbar_compare);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("GPU Comparison");
    }

    public void gpuList(View view){
        Intent intent = new Intent(CompareActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
