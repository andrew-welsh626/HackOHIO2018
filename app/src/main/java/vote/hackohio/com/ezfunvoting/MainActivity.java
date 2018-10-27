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
        Intent joinPage = new Intent(MainActivity.this, joinPage.class);
        startActivity(joinPage);
    }
    public void createPage(View v) {
        Intent createPage = new Intent(MainActivity.this, createPage.class);
        startActivity(createPage);
    }
}
