package lk.supervision.saleapp.model;

/**
 * Created by kavish manjitha on 10/23/2017.
 */

public class MSperson {

    private Integer indexNo;
    private String spId;
    private String spName;
    private String password;
    private String colF;
    private Integer lastSerial;

    public MSperson() {
    }

    public MSperson(Integer indexNo, String spId, String spName, String password, String colF, Integer lastSerial) {
        this.indexNo = indexNo;
        this.spId = spId;
        this.spName = spName;
        this.password = password;
        this.colF = colF;
        this.lastSerial = lastSerial;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getColF() {
        return colF;
    }

    public void setColF(String colF) {
        this.colF = colF;
    }

    public Integer getLastSerial() {
        return lastSerial;
    }

    public void setLastSerial(Integer lastSerial) {
        this.lastSerial = lastSerial;
    }

    @Override
    public String toString() {
        return "MSperson{" +
                "indexNo=" + indexNo +
                ", spId='" + spId + '\'' +
                ", spName='" + spName + '\'' +
                ", password='" + password + '\'' +
                ", colF='" + colF + '\'' +
                ", lastSerial=" + lastSerial +
                '}';
    }
}
