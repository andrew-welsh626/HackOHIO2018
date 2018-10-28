package vote.hackohio.com.ezfunvoting;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final Button button = findViewById(R.id.joinbtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                joinGroup(v);
            }
        });
    }

    public void joinGroup (View v) {
        EditText groupNameET = findViewById(R.id.editTextJoin);
        String groupName = groupNameET.getText().toString();
        Intent joinVotePage = new Intent(JoinActivity.this, VoteActivity.class);
        joinVotePage.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(joinVotePage);
    }
}
