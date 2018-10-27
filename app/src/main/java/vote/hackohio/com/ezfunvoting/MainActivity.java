package vote.hackohio.com.ezfunvoting;
import android.content.Intent;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create = findViewById(R.id.button2);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent CreateActivity = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(CreateActivity);
            }
        });
        Button join = findViewById(R.id.button3);
        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent JoinActivity = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(JoinActivity);
            }
        });
    }


    }
