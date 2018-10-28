package vote.hackohio.com.ezfunvoting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {

    private List<OptionModel> optionsList;

    public ResultsAdapter(List<OptionModel> optionsList) {
        this.optionsList = optionsList;
    }
    public void setOptionsList(List<OptionModel> o){
        optionsList = o;
    }
    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_option_results, parent, false);

        return new ResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsViewHolder holder, int position) {
        OptionModel option = optionsList.get(position);
        holder.tvOptionName.setText(option.name);
        holder.tvRank.setText((position + 1) + ".");
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOptionName;
        public TextView tvRank;

        public ResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvOptionName = itemView.findViewById(R.id.tv_option_name);
            this.tvRank = itemView.findViewById(R.id.tv_rank);
        }

    }

}