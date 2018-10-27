package vote.hackohio.com.ezfunvoting;

import java.util.List;

public class OptionModel {

    public String name;
    public List<Integer> rankings;

    public OptionModel(String name) {
        this.name = name;
    }

}
