package ru.sberbank.demo1;

public class Item {

    public String name;
    public String store;
    public int value;
    public int weight;

    public Item(String name, String store, int value, int weight) {
        this.name = name;
        this.store = store;
        this.value = value;
        this.weight = weight;
    }

    public Item(String name, int value, int weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
    }

    public String getStore() {
        return store;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public String str() {
        return name + " [value = " + value + ", weight = " + weight + "]";
    }

}
