package vote.hackohio.com.ezfunvoting;

import java.util.HashMap;
import java.util.Map;

public class OptionModel {

    public String name;
    public Map<String, Integer> rankings;

    public String id;

    public OptionModel() {
        this.name = "";
        this.rankings = new HashMap<>();
        this.id = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OptionModel(String name) {
        this.name = name;
        this.rankings = new HashMap<>();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof OptionModel && ((OptionModel) other).name == this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getRankings() {
        return rankings;
    }

    public void setRankings(Map<String, Integer> rankings) {
        this.rankings = rankings;
    }
}
