package com.example.laptophome.sufra.data.local.RoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MenuItemEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    int item_id;
    int Quantity,Totalprice,Price;
    String Title,ImagePath,ItemNote;

    public MenuItemEntity() {
    }

    public MenuItemEntity(int item_id, int quantity, String title, int price, String imagePath, int totalprice, String itemNote) {
        this.item_id = item_id;
        Quantity = quantity;
        Title = title;
        Price = price;
        ImagePath = imagePath;
        Totalprice = totalprice;
        ItemNote = itemNote;
    }

    public String getItemNote() {
        return ItemNote;
    }

    public void setItemNote(String itemNote) {
        ItemNote = itemNote;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(int totalprice) {
        Totalprice = totalprice;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
