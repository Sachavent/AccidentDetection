package android.mission.accidentdetection.Activity;

import android.content.Intent;
import android.mission.accidentdetection.Adapter.ListViewAdapter;
import android.mission.accidentdetection.Intent.GetterContactsPhone;
import android.mission.accidentdetection.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.HashMap;

public class AddEmergencyContact extends AppCompatActivity {

    private ListView contactListView;
    private ListView saveContactListView;
    private HashMap<String, String> contacts;
    EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emergency_contact);

        /** Getting contact list from Second Fragment */
        Intent intent = getIntent();
        contacts = (HashMap<String, String>) intent.getSerializableExtra("hashmap_contact");

        contactListView = (ListView) findViewById(R.id.listView_contacts);
        final ListViewAdapter adapter  = new ListViewAdapter(contacts);
        contactListView.setAdapter(adapter);
        saveContactListView = contactListView;

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("test", "result: " + adapter.getItem(i));

                /**Creating Hashmap to return */
                HashMap<String,String> contactAdded = new HashMap<String, String>();
                contactAdded.put(adapter.getItem(i).getKey(), adapter.getItem(i).getValue());

                /** Sending the emergency contact added to Second Fragment*/
                Intent intent = new Intent();
                intent.putExtra("contact_urgent", contactAdded);
                setResult(RESULT_OK, intent);

                finish();
            }
        });


        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                contactListView = saveContactListView;
                for (int i = 0; i < contactListView.getCount(); i++) {

                    String nom = contactListView.getItemAtPosition(i).toString();

                    if ( nom.toString().toLowerCase().contains(s.toString().toLowerCase()) ){
                    }else{
                        contactListView.removeViewInLayout(contactListView.getChildAt(i));
                    }
                }

            }

        });



    }

}
