package vote.hackohio.com.ezfunvoting;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class OptionComparator implements Comparator<OptionModel> {

    @Override
    public int compare(OptionModel option1, OptionModel option2) {

        int opt1Sum = 0, opt2Sum = 0;

        /*
        * Adds up the rankings each user gave to one option and compares the sum
        */

        for (Map.Entry <String, Integer> option : option1.rankings.entrySet()) {
            opt1Sum += option.getValue();
        }
        for (Map.Entry <String, Integer> option : option2.rankings.entrySet()) {
            opt2Sum += option.getValue();
        }
        if(opt1Sum == opt2Sum) {
            return 0;
        }
        return opt1Sum - opt2Sum; // positive = option 1 is better, negative = option 2
    }

}
