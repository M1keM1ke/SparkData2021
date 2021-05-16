package ru.mike.jpoint.sparkdata2021;


import ru.mike.unsafe_sparkdata.SparkRepository;

import java.util.List;

public interface CriminalRepo extends SparkRepository<Criminal> {
    List<Criminal> findByNumberGreaterThanOrderByNumber(int min);

    List<Criminal> findByNameContains(String s);

    long count();

    Criminal findByIdWhere(long idNumber);
}
