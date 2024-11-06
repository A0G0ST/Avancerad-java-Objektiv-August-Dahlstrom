package com.example;



public class OrderData {
    private String orderDate;
    private String region;
    private String rep1;
    private String rep2;
    private String item;
    private int units;
    private double unitCost;
    private double total;

    public OrderData(String orderDate, String region, String rep1, String rep2, String item, int units, double unitCost, double total) {
        this.orderDate = orderDate;
        this.region = region;
        this.rep1 = rep1;
        this.rep2 = rep2;
        this.item = item;
        this.units = units;
        this.unitCost = unitCost;
        this.total = total;
    }

    
    public String getOrderDate() { return orderDate; }
    public String getRegion() { return region; }
    public String getRep1() { return rep1; }
    public String getRep2() { return rep2; }
    public String getItem() { return item; }
    public int getUnits() { return units; }
    public double getUnitCost() { return unitCost; }
    public double getTotal() { return total; }
}
