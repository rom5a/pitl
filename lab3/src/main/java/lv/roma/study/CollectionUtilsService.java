package lv.roma.study;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class CollectionUtilsService {

    public void mergeLocalDataWithReceived(Map<String, List<PurchaseItem>> firstMap, Map<String, List<PurchaseItem>> secondMap, Map<String, List<PurchaseItem>> mergedData) {
        for (String owner : firstMap.keySet()) {
            final List<PurchaseItem> firstPurchases = firstMap.get(owner);
            final List<PurchaseItem> secondPurchases = secondMap.get(owner);
            if (secondPurchases != null) {
                if (firstPurchases.isEmpty()) {
                    firstPurchases.addAll(secondPurchases);
                } else {
                    for (PurchaseItem item: secondPurchases) {
                        if (!firstPurchases.contains(item)) {
                            firstPurchases.add(item);
                        }
                    }
                }
            }
            if (mergedData.containsKey(owner)) {
                continue;
            }
            mergedData.put(owner, firstPurchases);
        }
    }

}
