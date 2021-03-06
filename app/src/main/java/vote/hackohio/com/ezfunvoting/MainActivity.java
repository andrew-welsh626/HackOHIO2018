package vote.hackohio.com.ezfunvoting;
import android.content.Intent;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.crashlytics.android.core.CrashlyticsCore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create = findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent createActivity = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(createActivity);
            }
        });
        Button join = findViewById(R.id.button_join);
        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent joinActivity = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(joinActivity);

            }
        });
    }
}
