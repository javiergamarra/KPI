package com.nhpatt.kpi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "KPI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.d(TAG, "Starting app...");

        findViewById(R.id.daily_objective).setMinimumHeight(200);

        findViewById(R.id.star).setOnClickListener(this);

        Toast.makeText(this, "2 tasks pending", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stopping");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "starting");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resuming");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "pausing");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }
}
