package android.mission.accidentdetection.Fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.mission.accidentdetection.Intent.SmsDeliever;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

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

    }
}
