package za.co.eugenevdm.stockmonitor;

import java.io.Serializable;

public class Contact implements Serializable {

    private String t;
    private String e;
    private String name;
    private String type;

    public Contact(String t, String e, String name, String type) {
        super();
        this.t = t;
        this.e = e;
        this.name = name;
        this.type = type;
    }

    String getName() {
        return name;
    }

    String getType() {
        return type;
    }

    String getT() {
        return t;
    }

    String getE() {
        return e;
    }
    // get and set methods
}
