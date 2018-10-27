package vote.hackohio.com.ezfunvoting;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final Button button = findViewById(R.id.createbtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText groupNameET = findViewById(R.id.editTextCreate);
                String groupName = groupNameET.getText().toString();
                Intent joinVotePage = new Intent(CreateActivity.this, VoteActivity.class);
                joinVotePage.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
                startActivity(joinVotePage);
            }
        });
    }
}
