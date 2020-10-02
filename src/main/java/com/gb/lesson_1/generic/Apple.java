package com.gb.lesson_1.generic;

public class Apple extends Fruit {
    public Apple(float weigth) {
        super(weigth);
    }

    @Override
    public String toString() {
        return "Apple{" +
                "weigth=" + weigth +
                '}';
    }
}
