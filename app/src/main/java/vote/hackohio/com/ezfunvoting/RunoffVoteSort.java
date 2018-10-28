package vote.hackohio.com.ezfunvoting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RunoffVoteSort {
    /**
     * Sort option models using the runoff voting system
     * @param listOrig
     * @return
     */
    public static List<OptionModel> sort(List<OptionModel> listOrig) {
        System.out.println(listOrig.size());
        List<OptionModel> sorted = new ArrayList<>();
        List<OptionModel> list = new ArrayList<>(listOrig.size());
        for (int i = 0; i < listOrig.size(); i++) {
            list.add(listOrig.get(i));
        }
        // a map with a key for each user, and a treemap of their option and
        // how much they prefer it
        for (int i = 0; i < listOrig.size(); i++) {
            Map<String, TreeMap<Integer, OptionModel>> preferences = getPrefFromList(list);
            OptionModel best = findBest(preferences, list.size());
            sorted.add(best);
            list.remove(best);
        }

        return sorted;
    }

    /**
     * Transpose the list of option models
     *
     * @param list
     * @return
     */
    private static Map<String, TreeMap<Integer, OptionModel>> getPrefFromList(List<OptionModel> list) {
        HashMap<String, TreeMap<Integer, OptionModel>> preferences = new HashMap<>();
        Set<String> userNames = new HashSet<>();
        for (OptionModel om : list) {
            for (String name : om.rankings.keySet()) {
                userNames.add(name);
            }
        }
        for (String name : userNames) {
            preferences.put(name, new TreeMap<Integer, OptionModel>());
        }

        for (OptionModel om : list) {
            for (Map.Entry<String, Integer> e : om.rankings.entrySet()) {
                preferences.get(e.getKey()).put(e.getValue(), om);
            }
        }
        return preferences;
    }

    /**
     * @param models
     * @return the best choice of the algorithm
     */
    private static OptionModel findBest(Map<String, TreeMap<Integer, OptionModel>> models, int n_choices) {
        //scores for each model
        HashMap<OptionModel, Integer> scores = new HashMap<>();
        OptionModel bestChoice = null;
        boolean majority = false;
        while (!majority) {
            scores.clear();
            //count the number of people who vote for each option, store it in rankings.
            for (TreeMap<Integer, OptionModel> ranking : models.values()) {
                OptionModel val = ranking.firstEntry().getValue();
                scores.put(val, 1 +
                        (scores.get(val) == null ? 0 : scores.get(val)));
            }

            // if there is a majority, then we are done
            OptionModel pluralityOption = maxByValue(scores);
            if (scores.get(pluralityOption) >= n_choices / 2) {
                majority = true;
                bestChoice = pluralityOption;
            }

            // otherwise, remove the lowest-ranked option.
            OptionModel unLovedOption = minByValue(scores);
            for (TreeMap<Integer, OptionModel> rankings : models.values()) {
                rankings.remove(unLovedOption);
            }
            n_choices--;
        }
        return bestChoice;
    }

    /**
     * Return the key in a map corresponding to the map's maximum valued value
     */
    private static OptionModel minByValue(HashMap<OptionModel, Integer> map) {
        OptionModel argMin = null;
        int min = Integer.MAX_VALUE;
        boolean first = true;
        for (Map.Entry<OptionModel, Integer> score : map.entrySet()) {
            if (first) {
                argMin = score.getKey();
                min = score.getValue();
                first = false;
            } else {
                if (score.getValue() < min) {
                    argMin = score.getKey();
                    min = score.getValue();
                }
            }
        }
        return argMin;
    }

    /**
     * Return the key in a map corresponding to the map's maximum valued value
     */
    private static OptionModel maxByValue(HashMap<OptionModel, Integer> map) {
        OptionModel argMax = null;
        int max = 0;
        boolean first = true;
        for (Map.Entry<OptionModel, Integer> score : map.entrySet()) {
            if (first) {
                argMax = score.getKey();
                max = score.getValue();
                first = false;
            } else {
                if (score.getValue() > max) {
                    argMax = score.getKey();
                    max = score.getValue();
                }
            }
        }
        return argMax;
    }
}
