package teamunc.defarmers2.utils.worldEdit;

import java.io.Serializable;

public class DoubleValue implements Serializable {
    int min;
    int max;

    //getter
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public DoubleValue(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return "DoubleValue{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
