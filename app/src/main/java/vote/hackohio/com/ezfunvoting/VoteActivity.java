package vote.hackohio.com.ezfunvoting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class VoteActivity extends AppCompatActivity {

    /**
     * Instance variables
     */
    private RecyclerView votingRecyclerView;
    private TextView groupIdTextView;
    private OptionsAdapter adapter;
    private List<OptionModel> options = new ArrayList<>();
    private String groupID;
    private String userID;

    /**
     * Constants and map keys
     */
    public static final String GROUP_NAME_EXTRA_KEY = "GROUP_NAME";
    public static final String SHARED_PREF_NAME = "sharedprefs";
    public static final String SP_UID_KEY = "shared preferences user id key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        votingRecyclerView = findViewById(R.id.rv_vote);
        groupIdTextView = findViewById(R.id.group_id_tv);

        createOrReadUID();

        this.groupID = getIntent().getStringExtra(GROUP_NAME_EXTRA_KEY);
        groupIdTextView.setText(groupID);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_option:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra(GROUP_NAME_EXTRA_KEY, groupID);
                startActivity(intent);
                return true;
            case R.id.send_votes:
                sendVotesToDatabase();
                goToResultsPage();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * Attempts to read the user ID from the shared preferences file. If it does not exist, the user
     * is given a new UID and this value is stored in the Shared Preferences file.
     */
    private void createOrReadUID() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(SP_UID_KEY)) {
            this.userID = sharedPreferences.getString(SP_UID_KEY, "");
        } else {
            this.userID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SP_UID_KEY, userID);
            editor.apply();
        }
    }

    /**
     * Sets up the Firebase database event listener to keep data up to date with the database.
     */
    private void setupDataListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups/" + this.groupID);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.getKey().equals("name")) {
                    getSupportActionBar().setTitle((String) dataSnapshot.getValue());
                } else if (!dataSnapshot.getKey().equals("id") && !dataSnapshot.getKey().equals("algorithm")) {
                    OptionModel option = dataSnapshot.getValue(OptionModel.class);
                    option.setId(dataSnapshot.getKey());
                    options.add(option);
                    adapter.notifyDataSetChanged();
                }

                Collections.sort(options, new SingleUserOptionComparator(userID));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (!dataSnapshot.getKey().equals("name") && !dataSnapshot.getKey().equals("id")) {
                    OptionModel option = dataSnapshot.getValue(OptionModel.class);
                    options.set(options.indexOf(option), option);
                    Collections.sort(options, new SingleUserOptionComparator(userID));
                }

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

    /**
     * Update each of the options with this user's ranking, then put each of the options in the
     * database with those rankings.
     */
    private void sendVotesToDatabase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/groups/" + groupID);
        for (int i = 0; i < options.size(); i++) {
            OptionModel option = options.get(i);
            option.rankings.put(this.userID, i + 1);
            ref.child(option.getId()).setValue(options.get(i));
        }

    }

    public void goToResultsPage(){
        Intent createActivity = new Intent(this, Results.class);
        createActivity.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, this.groupID);
        startActivity(createActivity);
    }

    /**
     * Provides the gesture controls for moving the options up and down in the {@link RecyclerView}.
     */
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

    public class SingleUserOptionComparator implements Comparator<OptionModel> {

        private String userID;

        public SingleUserOptionComparator(String uid) {
            userID = uid;
        }

        @Override
        public int compare(OptionModel optionModel1, OptionModel optionModel2) {
            int option1Rank = (optionModel1.rankings.containsKey(userID)) ? optionModel1.rankings.get(userID) : Integer.MAX_VALUE;
            int option2Rank = (optionModel2.rankings.containsKey(userID)) ? optionModel2.rankings.get(userID) : Integer.MAX_VALUE;

            return option1Rank - option2Rank;

        }

    }
}
