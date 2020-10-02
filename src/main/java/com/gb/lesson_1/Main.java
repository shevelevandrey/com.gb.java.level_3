package com.gb.lesson_1;

import com.gb.lesson_1.generic.Apple;
import com.gb.lesson_1.generic.Box;
import com.gb.lesson_1.generic.Fruit;
import com.gb.lesson_1.generic.Orange;

public class Main {
    public static void main(String[] args) {

        System.out.println("Коробка с яблоками:");
        Box<Apple> apples = new Box<>(new Apple(1F), 3);
        System.out.println(apples);
        System.out.println();

        System.out.println("Коробка с апельсинами:");
        Box<Orange> oranges = new Box<>(new Orange(1.5F), 2);
        System.out.println(oranges);
        System.out.println();

        System.out.println("Коробка с фруктами:");
        Box<Fruit> fruits = new Box<>(new Apple(1F), 1);
        System.out.println(fruits);
        System.out.println();

        System.out.println("Пробуем в коробку с фруктами положить яблоко:");
        fruits.add(new Apple(1F));
        System.out.println();

        System.out.println("Пробуем в коробку с фруктами положить апельсин, если там уже лежит яблоко:");
        fruits.add(new Orange(1.5F));
        System.out.println();

        System.out.println("Смотрим, что лежит в коробке с фруктами, после наших манипуляций:");
        System.out.println(fruits);
        System.out.println();

        System.out.println("Пробуем в коробку с яблоками пересыпать коробку с апельсинами:");
//        apples.engulf(oranges); // ошибка
        System.out.println();

        System.out.println("Пробуем в коробку с яблоками, пересыпать другую коробку с яблоками:");
        Box<Apple> apples2 = new Box<>(new Apple(1F), 5);
        apples.engulf(apples2);
        System.out.println("Коробка [apples2] " + apples2);
        System.out.println("Коробка [apples] " + apples);

    }
}
