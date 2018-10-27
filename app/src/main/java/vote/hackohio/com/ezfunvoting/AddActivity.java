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

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final Button button = findViewById(R.id.addbtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addOption(v);
            }
        });
    }

    protected void addOption(View v){
        String optionName = findViewById(R.id.editText).toString();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("groups/swanky-unarmed-heptagons/Sent From Andrio Studio");

        myRef.setValue("Food Eat Yum");
    }
}
