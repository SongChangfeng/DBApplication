package com.example.chasong.dbapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TripItemsActivity extends AppCompatActivity {
    ListView listView;
    TripItemsDBManager tripItemsDBManager;

    private static final int CONTEXT_MENU_EDIT_ID = 0;
    private static final int CONTEXT_MENU_DELETE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_trip_items);

        listView = new ListView(TripItemsActivity.this);
        setContentView(listView);

        Intent intent = getIntent();
        int tripDayID = intent.getExtras().getInt(GlobalValues.KEY_TRIP_DAY_ID);

        tripItemsDBManager = new TripItemsDBManager(TripItemsActivity.this, tripDayID);
        Cursor cursor = tripItemsDBManager.queryTheCursor();

        String[] from = {TripDBHelper.TripItemsTable.COLUMN_NAME,
                TripDBHelper.TripItemsTable.COLUMN_INFO};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(TripItemsActivity.this,
                android.R.layout.simple_expandable_list_item_2,
                cursor,
                from,
                new int[]{android.R.id.text1, android.R.id.text2},
                0);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tripItemsDBManager.closeDB();
    }

    private void updateList() {
        Cursor cursor = tripItemsDBManager.queryTheCursor();
        SimpleCursorAdapter adapter = (SimpleCursorAdapter)listView.getAdapter();
        adapter.changeCursor(cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_items, menu);
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
        } else if (id == R.id.action_add) {
            Intent intent = new Intent(TripItemsActivity.this, TripItemAddEditActivity.class);
            intent.putExtra(GlobalValues.REQUEST_CODE, GlobalValues.REQUEST_CODE_TRIP_ITEM_ADD);
            startActivityForResult(intent, GlobalValues.REQUEST_CODE_TRIP_ITEM_ADD);
        } else if (id == android.R.id.home) {
//            Intent intent = new Intent(TripItemsActivity.this, MainActivity.class);
//            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String name = data.getExtras().getString(GlobalValues.KEY_NAME);
            String info = data.getExtras().getString(GlobalValues.KEY_INFO);

            TripItemsDBManager.TripDayItem tripDayItem = tripItemsDBManager.new TripDayItem(name, info);

            if (requestCode == GlobalValues.REQUEST_CODE_TRIP_ITEM_ADD) {
                tripItemsDBManager.add(tripDayItem);

                updateList();
            } else if (requestCode == GlobalValues.REQUEST_CODE_TRIP_ITEM_EDIT) {
                int position = data.getExtras().getInt(GlobalValues.KEY_LIST_POSITION);
                tripItemsDBManager.update(position, tripDayItem);

                updateList();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, CONTEXT_MENU_EDIT_ID, 0, "Edit");
        menu.add(0, CONTEXT_MENU_DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case CONTEXT_MENU_EDIT_ID:
                TextView editTextName = (TextView) menuInfo.targetView.findViewById(android.R.id.text1);
                String name = editTextName.getText().toString();
                TextView editTextInfo = (TextView) menuInfo.targetView.findViewById(android.R.id.text2);
                String info = editTextInfo.getText().toString();

                Intent intent = new Intent(TripItemsActivity.this, TripItemAddEditActivity.class);
                intent.putExtra(GlobalValues.KEY_LIST_POSITION, menuInfo.position);
                intent.putExtra(GlobalValues.KEY_NAME, name);
                intent.putExtra(GlobalValues.KEY_INFO, info);
                intent.putExtra(GlobalValues.REQUEST_CODE, GlobalValues.REQUEST_CODE_TRIP_ITEM_EDIT);
                startActivityForResult(intent, GlobalValues.REQUEST_CODE_TRIP_ITEM_EDIT);

                break;

            case CONTEXT_MENU_DELETE_ID:
                tripItemsDBManager.delete(menuInfo.position);
                updateList();
                break;

            default:
                break;
        }

        return super.onContextItemSelected(item);
    }
}
