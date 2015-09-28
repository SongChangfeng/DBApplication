package com.example.chasong.dbapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class TripDayAddEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_day_add_edit);

        Intent intent = getIntent();
        int requestCode = intent.getExtras().getInt(GlobalValues.REQUEST_CODE);
        switch (requestCode) {
            case GlobalValues.REQUEST_CODE_TRIP_DAY_ADD:
                break;

            case GlobalValues.REQUEST_CODE_TRIP_DAY_EDIT:
                String name = intent.getExtras().getString(GlobalValues.KEY_NAME);
                String info = intent.getExtras().getString(GlobalValues.KEY_INFO);
                EditText editTextName = (EditText) findViewById(R.id.trip_day_name);
                EditText eidtTextInfo = (EditText) findViewById(R.id.trip_day_info);
                editTextName.setText(name);
                eidtTextInfo.setText(info);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_day_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
        } else if (id == R.id.action_done) {
            String name = ((EditText) findViewById(R.id.trip_day_name)).getText().toString();
            String info = ((EditText) findViewById(R.id.trip_day_info)).getText().toString();

            Intent intent = getIntent();
            intent.putExtra(GlobalValues.KEY_NAME, name);
            intent.putExtra(GlobalValues.KEY_INFO, info);

            setResult(RESULT_OK, intent);

            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
