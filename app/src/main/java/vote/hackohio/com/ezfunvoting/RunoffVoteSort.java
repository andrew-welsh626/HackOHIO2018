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
     *
     * @param listOrig
     * @return
     */
    public static List<OptionModel> sort(List<OptionModel> listOrig) {
        List<OptionModel> sorted = new ArrayList<>();
        List<OptionModel> list = new ArrayList<>(listOrig.size());
        for (int i = 0; i < listOrig.size(); i++) {
            list.add(listOrig.get(i));
        }
        // a map with a key for each user, and a treemap of their options and
        // how much they prefer it
        Map<String, TreeMap<Integer, OptionModel>> preferences = getPrefFromList(list);
        if (allDifferent(preferences, list.size())) {
            for (int i = 0; i < listOrig.size(); i++) {
                OptionModel best = findBest(preferences, list.size());
                sorted.add(best);
                removeFromList(preferences, best);
                list.remove(best);
            }
            return sorted;
        } else {
            return listOrig;
        }
    }

    protected static boolean allDifferent(Map<String, TreeMap<Integer, OptionModel>> pref, int n) {
        for (TreeMap ranking : pref.values()) {
            if (ranking.size() != n) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove an element of the list, decreasing the rankings of all elements above it to
     * maintain voting order
     *
     * @Updates preferences
     */
    private static void removeFromList(Map<String, TreeMap<Integer, OptionModel>> preferences,
                                       OptionModel best) {
        for (TreeMap<Integer, OptionModel> rankings : preferences.values()) {
            int bestRank = findValue(rankings, best);
            for (Map.Entry<Integer, OptionModel> e : rankings.entrySet()) {
                if (e.getKey() > bestRank) {
                    //should not overwrite data because the list is sorted
                    rankings.put(e.getKey() - 1, e.getValue());
                }
            }
            //last entry has 2 locations by the last loop logic
            rankings.pollLastEntry();
        }
    }

    /**
     * returns the key associated with a value in a map
     */
    private static Integer findValue(TreeMap<Integer, OptionModel> map, OptionModel value) {
        for (Map.Entry<Integer, OptionModel> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Could not find value for RunoffVoteSort");
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
     * @return the best choice of 1 loop of the algorithm
     */
    private static OptionModel findBest(Map<String, TreeMap<Integer, OptionModel>> modelsOrig, int n_choices) {
        //scores for each model
        Map<String, TreeMap<Integer, OptionModel>> models = copy(modelsOrig);
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
                int v = findValue(rankings, unLovedOption);
                rankings.remove(v);
            }
            n_choices--;
        }
        return bestChoice;
    }

    //copy it a bit
    private static Map<String, TreeMap<Integer, OptionModel>> copy(Map<String, TreeMap<Integer, OptionModel>> modelsOrig) {
        Map<String, TreeMap<Integer, OptionModel>> copy = new HashMap<>();
        for (Map.Entry<String, TreeMap<Integer, OptionModel>> entry : modelsOrig.entrySet()) {
            TreeMap<Integer, OptionModel> copyMap = new TreeMap<>();
            for (Map.Entry<Integer, OptionModel> e : entry.getValue().entrySet()) {
                copyMap.put(e.getKey(), e.getValue());
            }
            copy.put(entry.getKey(), copyMap);
        }
        return copy;
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
