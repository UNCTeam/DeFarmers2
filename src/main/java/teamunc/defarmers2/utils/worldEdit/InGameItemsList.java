package teamunc.defarmers2.utils.worldEdit;

import java.io.Serializable;
import java.util.HashMap;

public class InGameItemsList implements Serializable {

    private HashMap<String,Integer> itemsListWithPrice;

    //getter
    public HashMap<String, Integer> getItemsListWithPrice() {
        return itemsListWithPrice;
    }

    public InGameItemsList(HashMap<String,Integer> itemsListWithPrice) {
        this.itemsListWithPrice = itemsListWithPrice;
    }
}
