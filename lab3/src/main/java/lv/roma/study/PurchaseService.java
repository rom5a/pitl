package lv.roma.study;

import javax.inject.Inject;
import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class PurchaseService {

	final static int MODEL_CAPACITY = 2;

    private final RestService restService;
    private final CollectionUtilsService collectionUtilsService;
    private final Map<String, List<PurchaseItem>> purchases = new HashMap<>();

    @Inject
    public PurchaseService(RestService restService, CollectionUtilsService collectionUtilsService) {
        this.restService = restService;
		this.collectionUtilsService = collectionUtilsService;
	}

    public void synchronize(String token) {
		final Map<String, List<PurchaseItem>> receivedPurchases = restService.get(token);
		final Map<String, List<PurchaseItem>> mergedData = new HashMap<>();

		collectionUtilsService.mergeLocalDataWithReceived(purchases, receivedPurchases, mergedData);
		collectionUtilsService.mergeLocalDataWithReceived(receivedPurchases, purchases, mergedData);

		purchases.clear();
		purchases.putAll(mergedData);
		restService.post(purchases, token);
    }

	public Map<String, Object> initialData() {
    	final Map<String, Object> initialModel = new HashMap<>(MODEL_CAPACITY);

	    final List<String> owners = getOwners();
	    if (owners.size() > 0) {
		    final String firstOwner = owners.get(0);
		    initialModel.put("owners", owners);
		    final List<PurchaseItem> firstOwnerPurchases = purchases.get(firstOwner);
		    initialModel.put("purchase", new Purchase(firstOwner, firstOwnerPurchases));
	    } else {
		    initialModel.put("owners", singletonList("Add list owner"));
	    }
	    return initialModel;
    }

    public Purchase findByOwner(String owner) {
		List<PurchaseItem> purchaseItems = purchases.get(owner);
		if (purchaseItems != null) {
			return new Purchase(owner, purchaseItems);
		}
		return null;
    }

    public Purchase addPurchase(Purchase purchase) {
		List<PurchaseItem> purchaseItems = purchases.get(purchase.getOwner());
		if (purchaseItems != null) {
			purchaseItems.addAll(purchase.getItems());
		} else {
			purchases.put(purchase.getOwner(), purchase.getItems());
		}
        return new Purchase(purchase.getOwner(), purchases.get(purchase.getOwner()));
    }

	public Purchase newOwner(String owner) {
		if (!purchases.containsKey(owner)) {
			purchases.put(owner, new ArrayList<>());
		}
		return new Purchase(owner, purchases.get(owner));
	}

    public Purchase deletePurchaseItem(String owner, PurchaseItem purchaseItem) {
		List<PurchaseItem> purchaseItems = purchases.get(owner);
		if (purchaseItems != null) {
			purchaseItems.remove(purchaseItem);
			return new Purchase(owner, purchaseItems);
		}
        return null;
    }


	private List<String> getOwners() {
		return new ArrayList<>(purchases.keySet());
	}

}
