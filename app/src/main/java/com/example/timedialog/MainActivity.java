package com.example.timedialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TimeBottomDialog.OnUpdateMyTime {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void timeClick(View view) {
        TimeBottomDialog dialog = new TimeBottomDialog(this);
        dialog.setMyTime(this);
        dialog.show();
    }

    @Override
    public void updateMyTime(StringBuilder builder) {
        Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
    }
}
