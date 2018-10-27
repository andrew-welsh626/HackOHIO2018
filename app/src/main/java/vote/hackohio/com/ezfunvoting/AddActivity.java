package vote.hackohio.com.ezfunvoting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
        EditText optionNameET = findViewById(R.id.editText);
        String optionName = optionNameET.getText().toString();
        // Add the option to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("groups/" + groupName);
        OptionModel option = new OptionModel(optionName);
        myRef.push().setValue(option);

        Intent intent = new Intent();
        intent.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(intent);

    }
}
