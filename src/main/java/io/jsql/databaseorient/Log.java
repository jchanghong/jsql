/*
 * Copyright (c) 2016.
 * changhong@uestc
 */

package io.jsql.databaseorient;

public interface Log {
    default void info(Object info) {
        System.out.println(info);
    }
}
