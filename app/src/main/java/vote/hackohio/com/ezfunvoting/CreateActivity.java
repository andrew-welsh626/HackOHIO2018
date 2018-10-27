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

        final Button button = findViewById(R.id.addbtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createGroup(v);
            }
        });
    }
    /** Called when the user touches the join button */
    public void createGroup(View view) {
        EditText groupNameET = findViewById(R.id.editText);
        String groupName = groupNameET.getText().toString();
        Intent createGroupPage = new Intent(CreateActivity.this, VoteActivity.class);
        createGroupPage.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(createGroupPage);
    }
}
