package ru.mike.jpoint.sparkdata2021;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mike.unsafe_sparkdata.annotation.ForeignKey;
import ru.mike.unsafe_sparkdata.annotation.Source;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/criminals.csv")
public class Criminal {
    private long id;
    private String name;
    private int number;

    @ForeignKey("criminalId")
    private List<Order> orders;

    public void printAllOrders() {
        orders.forEach(System.out::println);
    }
}
