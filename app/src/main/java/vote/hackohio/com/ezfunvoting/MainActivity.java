package vote.hackohio.com.ezfunvoting;
import android.content.Intent;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
