package vote.hackohio.com.ezfunvoting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, "name 1");
        startActivity(intent);
    }

    public void joinPage(View v) {
        Intent joinActivity = new Intent(MainActivity.this, JoinActivity.class);
        startActivity(joinActivity);
    }
    public void createPage(View v) {
        Intent createPage = new Intent(MainActivity.this, createPage.class);
        startActivity(createPage);
    }
}
