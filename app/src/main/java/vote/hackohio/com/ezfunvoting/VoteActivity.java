package vote.hackohio.com.ezfunvoting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoteActivity extends AppCompatActivity {

    RecyclerView votingRecyclerView;
    OptionsAdapter adapter;
    List<OptionModel> options = new ArrayList<>();
    String groupName = "new group";
    String userID;

    public static final String GROUP_NAME_EXTRA_KEY = "GROUP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        votingRecyclerView = findViewById(R.id.rv_vote);

        /* TODO store this in the config file */
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
        adapter.notifyDataSetChanged();

        setupDataListener();

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
            ref.child(options.get(i).getId()).setValue(options.get(i));
        }

    }

    private void setupDataListener() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups/" + this.groupName);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                OptionModel option = dataSnapshot.getValue(OptionModel.class);
                option.setId(dataSnapshot.getKey());
                options.add(option);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                OptionModel option = dataSnapshot.getValue(OptionModel.class);
                options.set(options.indexOf(option), option);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VoteActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
