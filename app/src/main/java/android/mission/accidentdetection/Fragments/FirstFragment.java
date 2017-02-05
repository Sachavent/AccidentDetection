package android.mission.accidentdetection.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.mission.accidentdetection.Intent.SmsDeliever;
import android.mission.accidentdetection.Listener.GpsListener;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

//Import for device sensors
import android.hardware.Sensor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Annick on 03/02/2017.
 */

public class FirstFragment extends Fragment {

    private boolean isCrack = false;

    //Just textview
    TextView percentage;
    ImageView egg;
    Toast totoast;
    SeekBar seekbar;

    // Listener
    private GpsListener gpsListener;

    // Permission
    private boolean permissionsEnabled;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check permission
        ActivatePermissions();
    }

    public static FirstFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        FirstFragment firstFragment = new FirstFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onStart() {
        super.onStart();

        percentage = (TextView) getActivity().findViewById(R.id.percentageID);
        egg = (ImageView) getActivity().findViewById(R.id.eggimgID);
        egg.setImageResource(R.drawable.egg);
        totoast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        isCrack = false;
        //seekbar = (SeekBar) getActivity().findViewById(R.id.detectparamID);
        //seekbar.setMax(100);
        //seekbar.setProgress(5);

        /** Using GPS */
        gpsListener = new GpsListener(getContext(), new GpsListener.GpsCallBack() {
            @Override
            public void onSpeedRecieved(Float vitesse) {
                Log.d("vitesse", "vitesse: " + vitesse);
            }

            @Override
            public void onSensorEventRecieved(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    Log.d("sensor", "Accelerometer :\nx:" + event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
                }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                    Log.d("sensor", "Gyrometer :\nx:" + event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
                }
            }

            @Override
            public void onChocEventRecieved(Float AccidentProba) {

                if(!isCrack) {
                    percentage.setText("Probabilitée d'accident : " + Math.floor(AccidentProba * 100) / 100  +"%");
                }

                if (AccidentProba > 5) {
                    isCrack = true;
                    egg.setImageResource(R.drawable.newcrack);

                    //Send emergence sms
                    ArrayList<String> phoneNumber = new ArrayList<>();
                    phoneNumber.add("0678681496");
                    phoneNumber.add("0667391286");
                    String contacName = "Tanguy";

                    String smsBody = contacName + " Viens d'avoir un terrible accident ! D:";
                    SmsDeliever smsDeliever = new SmsDeliever(getContext(), phoneNumber, smsBody);
                    smsDeliever.SendingMessage();
                }

            }

            @Override
            public void onWarningEventRecieved(Float AccidentProba) {

                if(!isCrack) {
                    percentage.setText(Math.floor(AccidentProba * 10) / 10 + "%");

                    //Show toast
                    totoast.setText("GPS data are more that 30sec old");
                    totoast.show();

                    Random rand = new Random();
                    int n = rand.nextInt(3) - 1;
                    egg.setRotation(n * AccidentProba * 5);
                }

                if (AccidentProba > 5) {
                    isCrack = true;
                    egg.setImageResource(R.drawable.newcrack);
                }

            }

        });
    }


    @Override
    public void onPause() {
        totoast.cancel();
        //mSensorManager.unregisterListener(gpsListener, accelerometer);
        //mSensorManager.unregisterListener(gpsListener, gyroscope);
        super.onPause();
    }

    @Override
    public void onResume() {
        totoast.cancel();
        //mSensorManager.registerListener(gpsListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        //mSensorManager.registerListener(gpsListener, gyroscope, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsEnabled = true;
                } else {
                    Log.d("test ", "position refusée");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("Activating position permissions is mandatory");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivatePermissions();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }

        }
    }

    private void ActivatePermissions() {
        if (permissionsEnabled != true) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.SEND_SMS
            }, 10);
        }
    }

}
