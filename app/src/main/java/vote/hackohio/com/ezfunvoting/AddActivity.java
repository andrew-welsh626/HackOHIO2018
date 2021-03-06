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

public class AddActivity extends AppCompatActivity {

    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final Button button = findViewById(R.id.add_button);
        groupName = getIntent().getStringExtra("GROUP_NAME");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addOption(v,groupName);
            }
        });
    }

    /*
     * Define behavior for the ActionBar home button
     */
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(intent);
        finish();

        return true;
    }

    @Override
    public boolean onNavigateUp() {

        Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(intent);
        finish();

        return true;
    }

    protected void addOption(View v, String groupName){
        EditText optionNameET = findViewById(R.id.et_add);
        String optionName = optionNameET.getText().toString();

        // Add the option to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("groups/" + groupName);
        OptionModel option = new OptionModel(optionName);
        myRef.push().setValue(option);

        // Navigate back to the Voting Activity
        Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(intent);

    }
}
