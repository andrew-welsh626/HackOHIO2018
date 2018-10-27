package vote.hackohio.com.ezfunvoting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

//https://stackoverflow.com/questions/2d091465/how-do-i-pass-data-between-activities-in-android-application

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final Button button = findViewById(R.id.addbtn);
        final String groupName = getIntent().getStringExtra("GROUP_NAME");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addOption(v,groupName);
            }
        });
    }

    protected void addOption(View v, String groupName){
        String optionName = findViewById(R.id.editText).toString();
        // Add the option to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("groups/" + groupName +"/" + optionName);

        myRef.setValue(new HashMap<String, Integer>());

    }
}
