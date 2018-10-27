package vote.hackohio.com.ezfunvoting;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {

    private String algorithmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final EditText groupNameET = findViewById(R.id.editTextCreate);
        groupNameET.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        // create the spinner
        final Spinner algorithmSpinner = findViewById(R.id.algSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.SpinnerChoices, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algorithmSpinner.setAdapter(adapter);

        algorithmSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            //Keep spinner variable up to date
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                algorithmName = (String) (parent.getItemAtPosition(position));
            }

            public void onNothingSelected(AdapterView<?> parent) {
                algorithmName = (String) (parent.getItemAtPosition(0));
            }
        });


        final Button button = findViewById(R.id.createbtn);


        button.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {

                // get the group ID
                final String groupName = groupNameET.getText().toString();

                // Generate a unique

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groupCount");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // get the next unique group ID
                        long count = (long) dataSnapshot.getValue();

                        // Create the new group in the database
                        addGroup(Long.toString(count), groupName);

                        // Increment the count
                        ref.setValue(count + 1);

                        // Open the Vote Activity
                        Intent joinVotePage = new Intent(CreateActivity.this, VoteActivity.class);
                        joinVotePage.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, Long.toString(count));
                        startActivity(joinVotePage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void addGroup(String key, String name) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups/" + key);
        ref.child("name").setValue(name);
        ref.child("algorithm").setValue(algorithmName);
    }

}