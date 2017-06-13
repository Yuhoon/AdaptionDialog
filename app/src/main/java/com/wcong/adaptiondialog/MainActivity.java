package com.wcong.adaptiondialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void top(View view) {
        new AdaptionDialog(this, view, R.layout.layout_window).showAtTop();
    }

    public void bottom(View view) {
        new AdaptionDialog(this, view, R.layout.layout_window).showAtBottom();
    }

    public void list(View view) {
        startActivity(new Intent(this, ListActivity.class));
    }
}
