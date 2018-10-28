package vote.hackohio.com.ezfunvoting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Results extends AppCompatActivity {

    private RecyclerView resultRecyclerView;
    private ResultsAdapter adapter;
    private List<OptionModel> options = new ArrayList<>();
    private String groupName;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView winnerTextView;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        winnerTextView = findViewById(R.id.tv_winner);
        groupName = getIntent().getStringExtra("GROUP_NAME");

        userID = getSharedPreferences(VoteActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(VoteActivity.SP_UID_KEY, "");
        
        /* Create the Recycler view and its adapter */
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        resultRecyclerView = findViewById(R.id.rv_results);
        resultRecyclerView.setLayoutManager(mLayoutManager);
        resultRecyclerView.setItemAnimator(new DefaultItemAnimator());
        resultRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /* Attach and populate the adapter */
        adapter = new ResultsAdapter(options);
        resultRecyclerView.setAdapter(adapter);
        generateRankings();

    }

    /*
     * Define behavior for the ActionBar home button
     */
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(intent);
        finish();

        return true;
    }

    @Override
    public boolean onNavigateUp() {

        Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra(VoteActivity.GROUP_NAME_EXTRA_KEY, groupName);
        startActivity(intent);
        finish();

        return true;
    }

    protected void generateRankings() {
        final DatabaseReference ref = database.getReference("groups/" + groupName);
        ref.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(!dataSnapshot.getKey().equals("name") && !dataSnapshot.getKey().equals("algorithm")) {
                            OptionModel o = dataSnapshot.getValue(OptionModel.class);
                            o.setId(dataSnapshot.getKey());
                            if (!userID.isEmpty() && !o.rankings.containsKey(userID)) {
                                o.rankings.put(userID, options.size() + 1);
                                ref.child(dataSnapshot.getKey()).setValue(o);
                            }
                            options.add(o);
                            reorderRanks();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(!dataSnapshot.getKey().equals("name") && !dataSnapshot.getKey().equals("algorithm")) {
                            OptionModel o = dataSnapshot.getValue(OptionModel.class);
                            o.setId(dataSnapshot.getKey());
                            options.set(options.indexOf(o), o);
                            reorderRanks();
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
                    }
                });
    }

    /*
     * Sorts the options and updates the page
     */
    protected void reorderRanks() {
        OptionComparator sortRankings = new OptionComparator();
        Collections.sort(options, sortRankings);
        adapter.notifyDataSetChanged();

        winnerTextView.setText(options.get(0).name);
    }

}
