package teamunc.defarmers2.utils;

import java.util.Set;

public class Utils {
    public static <T> int getIndex(Set<T> set, T value) {
        int result = 0;
        for (T entry:set) {
            if (entry.equals(value)) return result;
            result++;
        }
        return -1;
    }

    public static <T> T getValue(Set<T> set, int index) {
        int i = 0;
        for (T entry:set) {
            if (i == index) return entry;
            i++;
        }
        return null;
    }
}
