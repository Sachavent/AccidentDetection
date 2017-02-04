package android.mission.accidentdetection.Fragments;

import android.database.Cursor;
import android.mission.accidentdetection.Intent.GetterContactsPhone;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Annick on 03/02/2017.
 */

public class SecondFragment extends Fragment {

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
        HashMap<String, String> contactList = new HashMap<>();


        /*
        Hashtable<String, String> contactList = new Hashtable<>();

        GetterContactsPhone getterContactsPhone = new GetterContactsPhone(getContext());
        contactList = getterContactsPhone.getAllContact();

        Log.d("hashmap", "floriane: " + contactList.get("Floriane"));

        */


    }
}
