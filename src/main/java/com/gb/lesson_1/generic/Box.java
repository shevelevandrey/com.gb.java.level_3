package com.gb.lesson_1.generic;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private final List<T> elements;

    public Box(T element, int count) {
        elements = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            elements.add(element);
        }
    }

    public float getWeight() {
        float weight = 0F;
        for (T e: elements) {
            weight += e.getWeigth();
        }
        return weight;
    }

    public boolean compare (Box box) {
        return box.getWeight() == this.getWeight();
    }

    public void add (T element) {
        if (elements.iterator().hasNext()) {
            if (elements.iterator().next().getClass() == element.getClass()) {
                elements.add(element);
            } else {
                System.out.println("В данной коробке могут находяться только элементы типа [" + elements.iterator().next().getClass().getSimpleName()
                        + "], поэтому добавить элемент типа [" + element.getClass().getSimpleName() + "] не представляется возможным.");
                return;
            }
        } else {
            elements.add(element);
        }
        System.out.println("Элемент [" + element + "], добавлен в коробку.");
    }

    public void engulf(Box<T> box) {
        T element;
        while (box.getWeight() > 0) {
            element = box.takeOut();
            this.add(element);
        }
    }

    public T takeOut() {
        T element = null;
        if (elements.size() > 0) {
            int index = elements.size() - 1;
            element = elements.get(index);
            elements.remove(index);
        }
        return element;
    }

    @Override
    public String toString() {
        return "Box{" +
                "elements=" + elements +
                '}';
    }
}
