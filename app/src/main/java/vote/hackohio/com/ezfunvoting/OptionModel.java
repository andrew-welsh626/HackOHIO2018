package vote.hackohio.com.ezfunvoting;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OptionModel implements Comparable {

    public String name;
    public Map<String, Integer> rankings;

    public String id;

    public OptionModel() {
        this.name = "";
        this.rankings = new TreeMap<>();
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
        this.rankings = new TreeMap<>();
    }

    public OptionModel(String name, String id, Map<String,Integer> rankings) {
        this.name = name;
        this.rankings = rankings;
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof OptionModel && ((OptionModel) other).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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

    @Override
    public int compareTo(Object o) {
        OptionModel other = (OptionModel)(o);
        return 0;
    }
}
