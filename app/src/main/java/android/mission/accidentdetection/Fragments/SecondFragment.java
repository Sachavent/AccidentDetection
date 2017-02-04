package android.mission.accidentdetection.Fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onStart() {
        super.onStart();

        /*ArrayList<String> phoneNumber = new ArrayList<>();
        phoneNumber.add("0678681496");
        phoneNumber.add("0667391286");

        String smsBody = "Test envoie liste de contact";

        SmsDeliever smsDeliever = new SmsDeliever(getContext(), phoneNumber, smsBody);
        smsDeliever.SendingMessage();*/

        Hashtable<String,String> contactList = new Hashtable<>();

        
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactList.put(name,phoneNumber);
        }
        phones.close();

       Log.d("hashmap", "floriane: "+contactList.get("Floriane"));

    }
}
