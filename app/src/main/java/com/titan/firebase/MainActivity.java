package com.titan.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.btn_images)).setOnClickListener(btn_images__OnClickListener);
        (findViewById(R.id.btn_notes)).setOnClickListener(btn_notes__OnClickListener);
        (findViewById(R.id.btn_crash)).setOnClickListener(btn_crash__OnClickListener);
    }

    Button.OnClickListener btn_images__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(MainActivity.this, ImagesActivity.class));
        }
    };

    Button.OnClickListener btn_notes__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }
    };

    Button.OnClickListener btn_crash__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(MainActivity.this, CrashActivity.class));

        }
    };
}
