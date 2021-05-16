package ru.mike.jpoint.sparkdata2021;


import ru.mike.unsafe_sparkdata.SparkRepository;

import java.util.List;

public interface SpeakerRepo extends SparkRepository<Speaker> {
    List<Speaker> findByAgeBetween(int min, int max);

    long findByAgeGreaterThanCount(int min);
}
