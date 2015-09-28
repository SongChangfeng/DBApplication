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

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TripListDBManager tripListDBManager;

    private static final int CONTEXT_MENU_EDIT_ID = 0;
    private static final int CONTEXT_MENU_DELETE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        listView = new ListView(MainActivity.this);
        setContentView(listView);

        tripListDBManager = new TripListDBManager(MainActivity.this);
        Cursor cursor = tripListDBManager.queryTheCursor();

        String[] from = {TripDBHelper.TripListTable.COLUMN_NAME,
                TripDBHelper.TripListTable.COLUMN_INFO};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this,
                android.R.layout.simple_expandable_list_item_2,
                cursor,
                from,
                new int[]{android.R.id.text1, android.R.id.text2},
                0);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TripDaysActivity.class);
                intent.putExtra(GlobalValues.KEY_TRIP_ID, tripListDBManager.getID(position));
                startActivityForResult(intent, GlobalValues.REQUEST_CODE_TRIP_DETAIL);
            }
        });

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return false;
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tripListDBManager.closeDB();
    }

    private void updateList() {
        Cursor cursor = tripListDBManager.queryTheCursor();
        SimpleCursorAdapter adapter = (SimpleCursorAdapter)listView.getAdapter();
        adapter.changeCursor(cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(MainActivity.this, TripAddEditActivity.class);
            intent.putExtra(GlobalValues.REQUEST_CODE, GlobalValues.REQUEST_CODE_TRIP_ADD);
            startActivityForResult(intent, GlobalValues.REQUEST_CODE_TRIP_ADD);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String name = data.getExtras().getString(GlobalValues.KEY_NAME);
            String info = data.getExtras().getString(GlobalValues.KEY_INFO);

            TripListDBManager.Trip trip = tripListDBManager.new Trip(name, info);

            if (requestCode == GlobalValues.REQUEST_CODE_TRIP_ADD) {
                tripListDBManager.add(trip);

                updateList();

                listView.smoothScrollToPosition(listView.getCount() - 1);

            } else if (requestCode == GlobalValues.REQUEST_CODE_TRIP_EDIT) {
                int position = data.getExtras().getInt(GlobalValues.KEY_LIST_POSITION);
                tripListDBManager.update(position, trip);

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

                Intent intent = new Intent(MainActivity.this, TripAddEditActivity.class);
                intent.putExtra(GlobalValues.KEY_LIST_POSITION, menuInfo.position);
                intent.putExtra(GlobalValues.KEY_NAME, name);
                intent.putExtra(GlobalValues.KEY_INFO, info);
                intent.putExtra(GlobalValues.REQUEST_CODE, GlobalValues.REQUEST_CODE_TRIP_EDIT);
                startActivityForResult(intent, GlobalValues.REQUEST_CODE_TRIP_EDIT);

                break;

            case CONTEXT_MENU_DELETE_ID:
                tripListDBManager.delete(menuInfo.position);
                updateList();
                break;

            default:
                break;
        }

        return super.onContextItemSelected(item);
    }
}
