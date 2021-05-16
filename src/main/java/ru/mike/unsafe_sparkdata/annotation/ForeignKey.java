package ru.mike.unsafe_sparkdata.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface ForeignKey {
    String value();
}
