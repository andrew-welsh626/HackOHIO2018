package vote.hackohio.com.ezfunvoting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class Results extends AppCompatActivity {

    RecyclerView resultRecyclerView;
    OptionsAdapter adapter;
    List<OptionModel> options = new ArrayList<>();
    String groupName = getIntent().getStringExtra("GROUP_NAME");
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
        DatabaseReference ref = database.getReference().child("groups/" + groupName);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        reorderRanks((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        System.err.println("AHHHH DATABASE ERROR (Results)");
                    }
                });
    }

    protected void reorderRanks(Map<String, Object> data) {
        options.clear();
        TreeMap<String, Integer> rankings = analyze(data);
        for (String s : rankings.descendingKeySet()) {
            options.add(new OptionModel(s));
        }
    }

    protected TreeMap<String, Integer> analyze(Map<String, Object> data) {
        TreeMap<String, Integer> rankings = new TreeMap<>();
        for (Map.Entry<String, Object> option : data.entrySet()) {
            String name = option.getKey();
            Map<String, Integer> optionMap = (Map<String, Integer>) option.getValue();
            for (Integer i : optionMap.values()) {
                int weight = 4 - i;
                if (rankings.get(name) == null) {
                    rankings.put(name, 0);
                }
                rankings.put(name, rankings.get(weight) + weight);
            }
        }
        return rankings;
    }
}
