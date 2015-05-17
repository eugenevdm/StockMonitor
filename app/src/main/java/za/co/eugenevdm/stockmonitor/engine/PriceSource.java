package za.co.eugenevdm.stockmonitor.engine;

public enum PriceSource {
    // It is important that the used name shall be tally with StockServerFactory
    // concrete class name.
    Yahoo,
    Google,
    KLSEInfo;
}
