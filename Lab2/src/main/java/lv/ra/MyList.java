package lv.ra;

public class MyList {

    private MyListElement firstElement;

    public void add(int number) {
        if (firstElement == null) {
            firstElement = new MyListElement(number);
        } else {
            MyListElement element = firstElement;
            while (element.getNextElement() != null) {
                element = element.getNextElement();
            }

            element.setNextElement(new MyListElement(number));
        }
    }

    public int get(int index) {
        if (index < 0 || firstElement == null) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            int currentIndex = 0;
            MyListElement element = firstElement;
            while (currentIndex != index) {
                if (element == null) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                element = element.getNextElement();
                currentIndex++;
            }
            return element.getNumber();
        }
    }

}
