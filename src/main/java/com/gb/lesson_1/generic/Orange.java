package com.gb.lesson_1.generic;

import com.gb.lesson_1.generic.Fruit;

public class Orange extends Fruit {
    public Orange(float weigth) {
        super(weigth);
    }

    @Override
    public String toString() {
        return "Orange{" +
                "weigth=" + weigth +
                '}';
    }
}
