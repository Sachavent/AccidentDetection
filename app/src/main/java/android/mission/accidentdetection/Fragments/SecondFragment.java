package android.mission.accidentdetection.Fragments;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.mission.accidentdetection.Activity.AddEmergencyContact;
import android.mission.accidentdetection.Adapter.ListViewAdapter;
import android.mission.accidentdetection.Adapter.ListViewAddedContactAdapter;
import android.mission.accidentdetection.Helper.DBHelper;
import android.mission.accidentdetection.Intent.GetterContactsPhone;
import android.mission.accidentdetection.R;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Annick on 03/02/2017.
 */

public class SecondFragment extends Fragment {

    private Button butonAddContact;
    private HashMap<String, String> contactList;
    private HashMap<String, String> contactsJustAdded;
    private ListView contactAdded;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SecondFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        SecondFragment secondFragment = new SecondFragment();
        secondFragment.setArguments(args);
        return secondFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onStart() {
        super.onStart();

        /** Showing the list of emergency contact in the list view */
        HashMap<String,String> contactsallreadyAdded = new HashMap<>();
        contactsallreadyAdded = getAllEmergencyContacts();

        ArrayList<String> nameofcontacts = new ArrayList<>();
        for (Map.Entry<String,String> e : contactsallreadyAdded.entrySet()) {
            nameofcontacts.add(e.getKey());
        }

        Log.d("lecture", "nameofcontacts: "+nameofcontacts);

        contactAdded = (ListView) getActivity().findViewById(R.id.listView_Contactsaddded);
        ListViewAddedContactAdapter arrayAdapter = new ListViewAddedContactAdapter(nameofcontacts);
        contactAdded.setAdapter(arrayAdapter);

        /** Button to add contact */
        butonAddContact = (Button) getActivity().findViewById(R.id.addContact);
        butonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_add_urgent_contact();
            }
        });


        GetterContactsPhone getterContactsPhone = new GetterContactsPhone(getContext());
        contactList = getterContactsPhone.getAllContact();

        //Log.d("hashmap", "floriane: " + contactList.get("Floriane"));
    }

    /**
     * Launching Activity to add emergency contact
     * */

    public void btn_add_urgent_contact() {
        Intent intent_add_contact = new Intent(getContext(), AddEmergencyContact.class);
        intent_add_contact.putExtra("hashmap_contact", contactList);
        startActivityForResult(intent_add_contact,2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** Recieve the emergency contact from AddEmergency Contact  */
        if (requestCode == 2000) {
            if (resultCode == RESULT_OK) {
                contactsJustAdded = (HashMap<String, String>) data.getSerializableExtra("contact_urgent");

                /** Getting the data of the user returned */
                for (Map.Entry<String,String> e : contactsJustAdded.entrySet()) {

                    /**Saving User in the database */
                    Log.d("result", "result: "+insertEmergencyContact (e.getKey(), e.getValue()));
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**Saving Emergency Contact in the SQLite Database */
    public Uri insertEmergencyContact (String name, String telephone) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COL_2, name);
        contentValues.put(DBHelper.COL_3, telephone);

        Uri result = getActivity().getContentResolver().insert(android.mission.accidentdetection.Provider.ContentProvider.CONTENT_URL, contentValues);
        return result;
    }

    /**Getting all emergency contacts */
    public HashMap<String,String> getAllEmergencyContacts() {
        HashMap<String, String> contactsAllreadyAdded = new HashMap<>();
        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://android.mission.accidentdetection/elements/"), null, null, null, null);
        while (cursor.moveToNext()) {
            Log.d("result", "key: "+cursor.getString(1));
            Log.d("result", "value: "+cursor.getString(2));
            contactsAllreadyAdded.put(cursor.getString(1), cursor.getString(2));
        }
        return contactsAllreadyAdded;
    }

}
