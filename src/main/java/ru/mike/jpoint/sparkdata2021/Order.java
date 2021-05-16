package ru.mike.jpoint.sparkdata2021;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mike.unsafe_sparkdata.annotation.Source;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/orders.csv")
public class Order {
    private String name;
    private String desc;
    private int price;
    private long criminalId;
}
