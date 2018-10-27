package vote.hackohio.com.ezfunvoting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vote_page);

        final Button button = findViewById(R.id.addbtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                joinGroup(v);
            }
        });
    }

    protected void joinGroup (View v) {
        String groupName = findViewById(R.id.editText).toString();
        Intent joinGroupPage = new Intent(JoinActivity.this, VoteActivity.class);
        startActivity(joinGroupPage);
    }
}
