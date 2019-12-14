package com.titan.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

public class CrashActivity extends AppCompatActivity {

    TextView dummyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);


        (findViewById(R.id.btn_test_error)).setOnClickListener(btn_test_error__OnClickListener);
        (findViewById(R.id.btn_fatal_error_1)).setOnClickListener(btn_fatal_error_1__OnClickListener);
        (findViewById(R.id.btn_fatal_error_2)).setOnClickListener(btn_fatal_error_2__OnClickListener);
        (findViewById(R.id.btn_non_fatal_error)).setOnClickListener(btn_non_fatal_error__OnClickListener);
    }

    Button.OnClickListener btn_test_error__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Crashlytics.setUserIdentifier("user123456789");
            Crashlytics.setInt("current_level", 3);
            Crashlytics.setString("last_UI_action", "logged_in");
            Crashlytics.log("test message");
            Crashlytics.getInstance().crash();

        }
    };

    Button.OnClickListener btn_fatal_error_1__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            dummyTextView.setText("This will fire NullPointerException");
        }
    };

    Button.OnClickListener btn_fatal_error_2__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            throw new ArrayIndexOutOfBoundsException();
        }
    };

    Button.OnClickListener btn_non_fatal_error__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                dummyTextView.setText("This will fire NullPointerException");
            }
            catch (NullPointerException e) {

                Crashlytics.log("This is a log message: firebase app");
                Crashlytics.log("This is a log message from the app");
                Crashlytics.log(e.toString());
                Crashlytics.logException(e);
                Toast.makeText(CrashActivity.this, "Custom exception is thrown", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
