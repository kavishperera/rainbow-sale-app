package lk.supervision.saleapp.constant;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kavish manjitha on 12/30/2017.
 */

public class ServerHostAvailabilityTask {

    public static boolean isConnectedToServerOnline() {
        try {
            URL myUrl = new URL(AppEnvironmentValues.SERVER_ADDRESS_ONLINE);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(1000);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isConnectedToServerLocalHost() {
        try {
            URL myUrl = new URL(AppEnvironmentValues.SERVER_ADDRESS_LOCAL);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(1000);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
