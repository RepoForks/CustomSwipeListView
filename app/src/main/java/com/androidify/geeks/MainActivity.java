package com.androidify.geeks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.androidify.geeks.Adapter.PersonAdapter;
import com.androidify.geeks.BusinessObjects.Person;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView playlistView = (ListView) findViewById(R.id.list);
        ArrayList<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 40; i++) {
            Person aud = new Person("value : "+i, i);
            persons.add(aud);
        }

        PersonAdapter playList = new PersonAdapter(this, R.layout.swipeable_list_item, persons);
        playlistView.setAdapter(playList);
        playList.setListView(playlistView);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
