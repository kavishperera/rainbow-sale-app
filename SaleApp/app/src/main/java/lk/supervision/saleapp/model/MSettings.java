package lk.supervision.saleapp.model;

/**
 * Created by kavish manjitha on 12/18/2017.
 */

public class MSettings {

    private Integer indexNo;
    private String bluetoothPrinter;
    private String bluetoothPrinterMac;
    private String clearCardLastDateTime;
    private String beforSyncLastDateTime;
    private String afterSyncLastDateTime;

    public MSettings() {
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getBluetoothPrinter() {
        return bluetoothPrinter;
    }

    public void setBluetoothPrinter(String bluetoothPrinter) {
        this.bluetoothPrinter = bluetoothPrinter;
    }

    public String getBluetoothPrinterMac() {
        return bluetoothPrinterMac;
    }

    public void setBluetoothPrinterMac(String bluetoothPrinterMac) {
        this.bluetoothPrinterMac = bluetoothPrinterMac;
    }

    public String getClearCardLastDateTime() {
        return clearCardLastDateTime;
    }

    public void setClearCardLastDateTime(String clearCardLastDateTime) {
        this.clearCardLastDateTime = clearCardLastDateTime;
    }

    public String getBeforSyncLastDateTime() {
        return beforSyncLastDateTime;
    }

    public void setBeforSyncLastDateTime(String beforSyncLastDateTime) {
        this.beforSyncLastDateTime = beforSyncLastDateTime;
    }

    public String getAfterSyncLastDateTime() {
        return afterSyncLastDateTime;
    }

    public void setAfterSyncLastDateTime(String afterSyncLastDateTime) {
        this.afterSyncLastDateTime = afterSyncLastDateTime;
    }

    @Override
    public String toString() {
        return "MSettings{" +
                "indexNo=" + indexNo +
                ", bluetoothPrinter='" + bluetoothPrinter + '\'' +
                ", bluetoothPrinterMac='" + bluetoothPrinterMac + '\'' +
                ", clearCardLastDateTime='" + clearCardLastDateTime + '\'' +
                ", beforSyncLastDateTime='" + beforSyncLastDateTime + '\'' +
                ", afterSyncLastDateTime='" + afterSyncLastDateTime + '\'' +
                '}';
    }
}
