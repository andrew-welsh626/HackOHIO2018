package vote.hackohio.com.ezfunvoting;

import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final Button button = findViewById(R.id.createbtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // get the group ID
                EditText groupNameET = findViewById(R.id.editTextCreate);
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

    }
}
