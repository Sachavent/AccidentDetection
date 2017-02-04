package android.mission.accidentdetection.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.mission.accidentdetection.Activity.AddEmergencyContact;
import android.mission.accidentdetection.Intent.GetterContactsPhone;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private HashMap<String, String> contactsAdded;


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
                contactsAdded = (HashMap<String, String>) data.getSerializableExtra("contact_urgent");

                /** Getting the data of the user returned */
                for (Map.Entry<String,String> e : contactsAdded.entrySet()) {
                    Log.d("test", "retour second: "+e.getKey());
                    Log.d("test", "retour second: "+e.getValue());
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
