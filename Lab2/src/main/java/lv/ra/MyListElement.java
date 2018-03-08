package lv.ra;

public class MyListElement {

    private int number;
    private MyListElement nextElement;

    public MyListElement() {}

    public MyListElement(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public MyListElement getNextElement() {
        return nextElement;
    }

    public void setNextElement(MyListElement nextElement) {
        this.nextElement = nextElement;
    }
}
