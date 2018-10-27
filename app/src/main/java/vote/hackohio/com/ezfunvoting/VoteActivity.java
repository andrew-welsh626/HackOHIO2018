package vote.hackohio.com.ezfunvoting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VoteActivity extends AppCompatActivity {

    RecyclerView votingRecyclerView;
    OptionsAdapter adapter;
    List<OptionModel> options = new ArrayList<>();
    String groupName;

    public static final String GROUP_NAME_EXTRA_KEY = "GROUP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);


        votingRecyclerView = findViewById(R.id.rv_vote);

        adapter = new OptionsAdapter(options);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        votingRecyclerView.setLayoutManager(mLayoutManager);
        votingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        votingRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        votingRecyclerView.setAdapter(adapter);

        options.add(new OptionModel("Option 1"));
        options.add(new OptionModel("Option 2"));
        options.add(new OptionModel("Option 3"));
        adapter.notifyDataSetChanged();
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
                intent.putExtra(GROUP_NAME_EXTRA_KEY, groupName);
                startActivity(intent);
                return true;
            case R.id.send_votes:
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionViewHolder> {

        private List<OptionModel> optionsList;

        public OptionsAdapter(List<OptionModel> optionsList) {
            this.optionsList = optionsList;
        }

        @NonNull
        @Override
        public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_option, parent, false);

            return new OptionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
            OptionModel option = optionsList.get(position);
            holder.tvOptionName.setText(option.name);
        }

        @Override
        public int getItemCount() {
            return optionsList.size();
        }

        public class OptionViewHolder extends RecyclerView.ViewHolder {

            public TextView tvOptionName;

            public OptionViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tvOptionName = itemView.findViewById(R.id.tv_option_name);
            }

        }

    }


}
