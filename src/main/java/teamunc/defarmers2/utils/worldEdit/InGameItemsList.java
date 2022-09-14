package teamunc.defarmers2.utils.worldEdit;

import teamunc.defarmers2.Defarmers2;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class InGameItemsList implements Serializable {

    private final HashMap<String,DoubleValue> itemsListWithPrice;

    //getter
    public HashMap<String, Integer> getItemsListWithPrice() {
        HashMap<String,Integer> itemsListWithPriceEnd = new HashMap<>();
        for (String key : itemsListWithPrice.keySet()) {
            itemsListWithPriceEnd.put(key,getRandomValueFromSeed(itemsListWithPrice.get(key)));
        }

        // keep only 5 items in the list
        HashMap<String,Integer> res = new HashMap<>();
        int i = 0;
        ArrayList<String> keys = new ArrayList<>(itemsListWithPriceEnd.keySet());
        Collections.shuffle(keys,new Random(getSeed()));
        for (String key : keys) {
            if (i < Defarmers2.getInstance().getGameManager().getGameOptions().getNumberOfGoalItem()) {
                res.put(key, itemsListWithPriceEnd.get(key));
                i++;
            }
        }

        return res;
    }

    private Integer getRandomValueFromSeed(DoubleValue minMaxValue) {
        Random random = new Random(getSeed());
        return random.nextInt(minMaxValue.getMax() - minMaxValue.getMin()) + minMaxValue.getMin();
    }

    public InGameItemsList(HashMap<String,DoubleValue> itemsListWithPrice) {
        this.itemsListWithPrice = itemsListWithPrice;
    }

    private long getSeed() {
        return Defarmers2.getInstance().getGameManager().getSeedOrGenerate();
    }
}
