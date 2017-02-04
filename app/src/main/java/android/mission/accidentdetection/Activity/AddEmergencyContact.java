package android.mission.accidentdetection.Activity;

import android.content.Intent;
import android.mission.accidentdetection.Adapter.ListViewAdapter;
import android.mission.accidentdetection.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

public class AddEmergencyContact extends AppCompatActivity {

    private ListView contactListView;
    private HashMap<String,String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emergency_contact);

        /** Getting contact list from Second Fragment */
        Intent intent = getIntent();
        contacts = (HashMap<String, String>)intent.getSerializableExtra("hashmap_contact");

        Log.d("hashmap", "floriane: " + contacts.get("Floriane"));


        contactListView = (ListView) findViewById(R.id.listView_contacts);
        final ListViewAdapter adapter = new ListViewAdapter(contacts);
        contactListView.setAdapter(adapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("test","result: "+adapter.getItem(i));
            }
        });
    }

}
