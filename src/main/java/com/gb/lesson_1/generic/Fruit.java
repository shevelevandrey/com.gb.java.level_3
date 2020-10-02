package com.gb.lesson_1.generic;

abstract public class Fruit {
    public float weigth;

    public Fruit(float weigth) {
        this.weigth = weigth;
    }

    public float getWeigth() {
        return weigth;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "weigth=" + weigth +
                '}';
    }
}
