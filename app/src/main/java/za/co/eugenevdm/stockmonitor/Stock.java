package za.co.eugenevdm.stockmonitor;

import java.io.Serializable;

public class Stock implements Serializable {

    private String id;
    private String e;
    private String t;
    private String name;
    private String l;
    private String type;

    public Stock(String id, String e, String t, String name, String l, String type) {
        super();
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

/*
    06-02 21:08:24.341  24907-24907/za.co.eugenevdm.stockmonitor D/sm_StockDetailFragment﹕ [{id=1019083932318899, t=BAT, e=JSE, l=10,682.00, l_fix=10682.00, l_cur=ZAC10,682.00, s=0, ltt=5:00PM GMT 2, lt=Jun 2, 5:00PM GMT 2, lt_dts=2015-06-02T17:00:22Z, c= 182.00, c_fix=182.00, cp=1.73, cp_fix=1.73, ccol=chg, pcls_fix=10500, eo=, delay=15, op=10,500.00, hi=10,711.00, lo=10,250.00, vo=485,561.00, avvo=642,997.00, hi52=11,665.00, lo52=5,600.00, mc=55.17B, pe=20.39, fwpe=, beta=, eps=5.24, shares=516.49M, inst_own=, name=Brait SE, type=Company}, {id=918586879122938, t=SAB, e=JSE, l=64,859.00, l_fix=64859.00, l_cur=ZAC64,859.00, s=0, ltt=5:00PM GMT 2, lt=Jun 2, 5:00PM GMT 2, lt_dts=2015-06-02T17:00:25Z, c= 324.00, c_fix=324.00, cp=0.50, cp_fix=0.50, ccol=chg, pcls_fix=64535, eo=, delay=15, op=64,100.00, hi=64,859.00, lo=63,368.00, vo=955,935.00, avvo=819,223.00, hi52=68,907.00, lo52=55,403.00, mc=1.05T, pe=26.01, fwpe=, beta=, eps=24.94, shares=1.62B, inst_own=, name=SABMiller plc (S. Africa), type=Company}]
     */
