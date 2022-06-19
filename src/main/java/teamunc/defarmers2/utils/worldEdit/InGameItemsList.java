package teamunc.defarmers2.utils.worldEdit;

import java.io.Serializable;
import java.util.HashMap;

public class InGameItemsList implements Serializable {

    public HashMap<String,Integer> itemsListWithPrice;
    public InGameItemsList(HashMap<String,Integer> itemsListWithPrice) {
        this.itemsListWithPrice = itemsListWithPrice;
    }
}
