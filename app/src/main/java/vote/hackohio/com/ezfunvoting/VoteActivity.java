package vote.hackohio.com.ezfunvoting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoteActivity extends AppCompatActivity {

    RecyclerView votingRecyclerView;
    OptionsAdapter adapter;
    List<OptionModel> options = new ArrayList<>();
    String groupName;
    String userID;

    public static final String GROUP_NAME_EXTRA_KEY = "GROUP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        votingRecyclerView = findViewById(R.id.rv_vote);

        /* TODO store this in the config fi;e */
        this.userID = UUID.randomUUID().toString();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getItemTouchHelper());

        /* Create the adapter and setup the recyclerview */
        adapter = new OptionsAdapter(options);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        votingRecyclerView.setLayoutManager(mLayoutManager);
        votingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        votingRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        votingRecyclerView.setAdapter(adapter);

        /* Dummy Data */
        options.add(new OptionModel("Option 1"));
        options.add(new OptionModel("Option 2"));
        options.add(new OptionModel("Option 3"));
        adapter.notifyDataSetChanged();

        itemTouchHelper.attachToRecyclerView(votingRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_voting, menu);
        return true;
    }

    public void sendVotesToDatabase() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/groups/" + groupName);
        for (int i = 0; i < options.size(); i++) {
            ref.child(options.get(i).name + "/" + this.userID).setValue(i + 1);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_option:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra(GROUP_NAME_EXTRA_KEY, groupName);
                startActivity(intent);
                return true;
            case R.id.send_votes:
                sendVotesToDatabase();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public ItemTouchHelper.SimpleCallback getItemTouchHelper() {

        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();

                OptionModel movedOptions = options.remove(fromPosition);
                options.add(toPosition, movedOptions);
                adapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {}

        };

    }




}
