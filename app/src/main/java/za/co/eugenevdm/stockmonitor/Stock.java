package za.co.eugenevdm.stockmonitor;

import java.io.Serializable;

public class Stock implements Serializable {

    private String id;
    private String e;
    private String t;
    private String name;
    private String l;
    private String type;

    void setName(String name) {
        this.name=name;
    }

    void setPrice(String price) {
        this.l=price;
    }

    public void addStock(String id, String e, String t, String name, String l, String type) {
        //super();
        this.id = id;
        this.e = e;
        this.t = t;
        this.name = name;
        this.l = l;
        this.type = type;
    }

    String getId() {
        return id;
    }

    String getExchange() {
        return e;
    }

    String getTicker() {
        return t;
    }

    String getName() {
        return name;
    }

    String getPrice() {
        return l;
    }

    String getType() { return type; }

}

