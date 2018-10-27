package vote.hackohio.com.ezfunvoting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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