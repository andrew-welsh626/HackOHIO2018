package vote.hackohio.com.ezfunvoting;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import java.util.TreeSet;

public class Results extends AppCompatActivity {

    RecyclerView resultRecyclerView;
    OptionsAdapter adapter;
    List<OptionModel> options = new ArrayList<>();
    String groupName = "1004"; //getIntent().getStringExtra("GROUP_NAME");
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultRecyclerView = findViewById(R.id.rv_results);
        adapter = new OptionsAdapter(options);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        resultRecyclerView.setLayoutManager(mLayoutManager);
        resultRecyclerView.setItemAnimator(new DefaultItemAnimator());
        resultRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        resultRecyclerView.setAdapter(adapter);

        generateRankings();

    }

    protected void generateRankings() {
        DatabaseReference ref = database.getReference("groups/" + groupName);
        ref.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(!dataSnapshot.getKey().equals("name")) {
                            OptionModel o = dataSnapshot.getValue(OptionModel.class);
                            options.add(o);
                            reorderRanks();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        reorderRanks();
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
    * Sorts the options
    */
    protected void reorderRanks() {
        OptionComparator sortRankings = new OptionComparator();
        Collections.sort(options, sortRankings);
    }

}
