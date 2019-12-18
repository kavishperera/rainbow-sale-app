package lk.supervision.saleapp.constant;

/**
 * Created by kavish manjitha on 10/26/2017.
 */

public class IDGenarator {

    public static String newGenaratedId(Integer num) {
        String nextId = "";
        num += 1;
        if (num < 10) {
            nextId = "0000000" + num;
        } else if (num < 100) {
            nextId = "000000" + num;
        } else if (num < 1000) {
            nextId = "00000" + num;
        } else if (num < 10000) {
            nextId = "0000" + num;
        } else if (num < 100000) {
            nextId = "000" + num;
        } else if (num < 1000000) {
            nextId = "00" + num;
        } else if (num < 10000000) {
            nextId = "0" + num;
        } else if (num < 100000000) {
            nextId = String.valueOf(num);
        }
        return "P" + nextId;
    }
}
