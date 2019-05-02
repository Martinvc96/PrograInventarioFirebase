package com.example.inventario;

import java.io.Serializable;
import java.util.ArrayList;


public class Bodega implements Serializable {
    private ArrayList<Item> Items;

    public Bodega(){
        this.Items = new ArrayList<>();
    }

    public void Add(Item item){
        Items.add(item);
    }

    public Item GetItem(int index){
        return Items.get(index);
    }

    public int GetZise(){
        if(Items.isEmpty()){return 0;}
        else{return Items.size();}
    }

    public void Remove(int index){
        Items.remove(index);
    }
}
