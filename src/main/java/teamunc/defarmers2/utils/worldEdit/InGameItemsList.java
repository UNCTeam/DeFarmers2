package teamunc.defarmers2.utils.worldEdit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class InGameItemsList implements Serializable {

    private HashMap<String,DoubleValue> itemsListWithPrice;
    private long seed;

    //getter
    public HashMap<String, Integer> getItemsListWithPrice() {
        this.seed = new Random().nextInt(1000000);
        HashMap<String,Integer> itemsListWithPriceEnd = new HashMap<>();

        for (String key : itemsListWithPrice.keySet()) {
            itemsListWithPriceEnd.put(key,getRandomValueFromSeed(itemsListWithPrice.get(key)));
        }

        return itemsListWithPriceEnd;
    }

    private Integer getRandomValueFromSeed(DoubleValue minMaxValue) {
        Random random = new Random(seed);
        return random.nextInt(minMaxValue.getMax() - minMaxValue.getMin()) + minMaxValue.getMin();
    }

    public InGameItemsList(HashMap<String,DoubleValue> itemsListWithPrice) {
        this.itemsListWithPrice = itemsListWithPrice;
    }
}
