package vn.ghtk.demo.catalog.common.utils;

import java.util.HashSet;
import java.util.List;

public class CollectionUtil {
    public static <T> boolean equals(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null) return false;
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

}
