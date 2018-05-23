package lv.roma.study;

import java.util.List;
import java.util.Objects;

public class Purchase {

    private String owner;
    private List<PurchaseItem> items;

    public Purchase() {
    }

    public Purchase(String owner, List<PurchaseItem> items) {
        this.owner = owner;
        this.items = items;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(owner, purchase.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "owner='" + owner + '\'' +
                ", items=" + items +
                '}';
    }

}
