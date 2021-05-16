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
@Source("data/speakers.json")
public class Speaker {
    private String name;
    private long age;
}
