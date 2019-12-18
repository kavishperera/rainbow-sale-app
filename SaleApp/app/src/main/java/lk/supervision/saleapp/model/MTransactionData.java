package lk.supervision.saleapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kavish manjitha on 10/7/2017.
 */

public class MTransactionData implements Serializable {

    private Integer indexNo;
    private String spId;
    private String pmtNo;
    private String mInvNo;
    private String conNo;
    private Date pmtDate;
    private Date pmtTime;
    private String pmtType;
    private BigDecimal pmtAmt;
    private String pmtRemark;
    private String npmtDate;
    private Integer phoneId;
    private Integer sysUpdate;
    private Date sysUpdateDate;

    public MTransactionData() {
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

    public String getPmtNo() {
        return pmtNo;
    }

    public void setPmtNo(String pmtNo) {
        this.pmtNo = pmtNo;
    }

    public String getConNo() {
        return conNo;
    }

    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    public Date getPmtDate() {
        return pmtDate;
    }

    public void setPmtDate(Date pmtDate) {
        this.pmtDate = pmtDate;
    }

    public Date getPmtTime() {
        return pmtTime;
    }

    public void setPmtTime(Date pmtTime) {
        this.pmtTime = pmtTime;
    }

    public String getPmtType() {
        return pmtType;
    }

    public void setPmtType(String pmtType) {
        this.pmtType = pmtType;
    }

    public BigDecimal getPmtAmt() {
        return pmtAmt;
    }

    public void setPmtAmt(BigDecimal pmtAmt) {
        this.pmtAmt = pmtAmt;
    }

    public String getPmtRemark() {
        return pmtRemark;
    }

    public void setPmtRemark(String pmtRemark) {
        this.pmtRemark = pmtRemark;
    }

    public String getNpmtDate() {
        return npmtDate;
    }

    public void setNpmtDate(String npmtDate) {
        this.npmtDate = npmtDate;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getSysUpdate() {
        return sysUpdate;
    }

    public void setSysUpdate(Integer sysUpdate) {
        this.sysUpdate = sysUpdate;
    }

    public Date getSysUpdateDate() {
        return sysUpdateDate;
    }

    public void setSysUpdateDate(Date sysUpdateDate) {
        this.sysUpdateDate = sysUpdateDate;
    }

    public String getmInvNo() {return mInvNo;}

    public void setmInvNo(String mInvNo) {this.mInvNo = mInvNo;}

    @Override
    public String toString() {
        return "MTransactionData{" +
                "indexNo=" + indexNo +
                ", spId='" + spId + '\'' +
                ", pmtNo='" + pmtNo + '\'' +
                ", conNo='" + conNo + '\'' +
                ", pmtDate=" + pmtDate +
                ", pmtTime=" + pmtTime +
                ", pmtType='" + pmtType + '\'' +
                ", pmtAmt=" + pmtAmt +
                ", pmtRemark='" + pmtRemark + '\'' +
                ", npmtDate='" + npmtDate + '\'' +
                ", phoneId=" + phoneId +
                ", sysUpdate=" + sysUpdate +
                ", sysUpdateDate=" + sysUpdateDate +
                '}';
    }
}
