package android.mission.accidentdetection.Activity;

import android.content.Intent;
import android.mission.accidentdetection.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
    }

    public void btn_lauch_home_activity(View v) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

    }
}
