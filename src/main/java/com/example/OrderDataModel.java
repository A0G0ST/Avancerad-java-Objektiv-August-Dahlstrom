package com.example;



import java.util.Comparator;
import java.util.List;

public class OrderDataModel {
    private List<OrderData> data;

    public OrderDataModel(List<OrderData> data) {
        this.data = data;
    }

    public void sortBy(String attribute) {
        switch (attribute) {
            case "OrderDate":
                data.sort(Comparator.comparing(OrderData::getOrderDate, Comparator.nullsLast(String::compareTo)));
                break;
            case "Region":
                data.sort(Comparator.comparing(OrderData::getRegion, Comparator.nullsLast(String::compareTo)));
                break;
                case "Item": // Detta sorterar Item-kolumnen alfabetiskt
                data.sort(Comparator.comparing(OrderData::getItem, Comparator.nullsLast(String::compareTo)));
                break;
            default:
                throw new IllegalArgumentException("Ogiltigt attribut för sortering");
        }
    }

    public List<OrderData> getData() {
        return data;
    }
}
