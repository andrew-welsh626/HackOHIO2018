package vote.hackohio.com.ezfunvoting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class createPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /** Called when the user touches the join button */
    public void createGroup(View view) {
        String groupName = findViewById(R.id.editText).toString();
        Intent createGroupPage = new Intent(createPage.this, VoteActivity.class);
        startActivity(createGroupPage);
    }
}
