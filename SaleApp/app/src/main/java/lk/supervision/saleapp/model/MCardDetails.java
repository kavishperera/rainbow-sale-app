package lk.supervision.saleapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by kavish manjitha on 10/7/2017.
 */

public class MCardDetails implements Serializable {

    private Integer indexNo;
    private String conNo;
    private String spId;
    private String spName;
    private String mInvNo;
    private String recFlag;
    private String conDate;
    private String cusNo;
    private String idNo;
    private String cusName;
    private String add1;
    private String add2;
    private String city;
    private String tpNo;
    private String mobNo;
    private BigDecimal totVal;
    private BigDecimal dpmt;
    private BigDecimal balVal;
    private BigDecimal totalDueAmt;
    private BigDecimal dueAmt;

    public MCardDetails() {
    }

    public MCardDetails(Integer indexNo, String conNo, String spId, String spName, String mInvNo, String recFlag, String conDate, String cusNo, String idNo, String cusName, String add1, String add2, String city, String tpNo, String mobNo, BigDecimal totVal, BigDecimal dpmt, BigDecimal balVal, BigDecimal totalDueAmt, BigDecimal dueAmt) {
        this.indexNo = indexNo;
        this.conNo = conNo;
        this.spId = spId;
        this.spName = spName;
        this.mInvNo = mInvNo;
        this.recFlag = recFlag;
        this.conDate = conDate;
        this.cusNo = cusNo;
        this.idNo = idNo;
        this.cusName = cusName;
        this.add1 = add1;
        this.add2 = add2;
        this.city = city;
        this.tpNo = tpNo;
        this.mobNo = mobNo;
        this.totVal = totVal;
        this.dpmt = dpmt;
        this.balVal = balVal;
        this.totalDueAmt = totalDueAmt;
        this.dueAmt = dueAmt;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getConNo() {
        return conNo;
    }

    public void setConNo(String conNo) {
        this.conNo = conNo;
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

    public String getmInvNo() {
        return mInvNo;
    }

    public void setmInvNo(String mInvNo) {
        this.mInvNo = mInvNo;
    }

    public String getRecFlag() {
        return recFlag;
    }

    public void setRecFlag(String recFlag) {
        this.recFlag = recFlag;
    }

    public String getConDate() {
        return conDate;
    }

    public void setConDate(String conDate) {
        this.conDate = conDate;
    }

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTpNo() {
        return tpNo;
    }

    public void setTpNo(String tpNo) {
        this.tpNo = tpNo;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public BigDecimal getTotVal() {
        return totVal;
    }

    public void setTotVal(BigDecimal totVal) {
        this.totVal = totVal;
    }

    public BigDecimal getDpmt() {
        return dpmt;
    }

    public void setDpmt(BigDecimal dpmt) {
        this.dpmt = dpmt;
    }

    public BigDecimal getBalVal() {
        return balVal;
    }

    public void setBalVal(BigDecimal balVal) {
        this.balVal = balVal;
    }

    public BigDecimal getTotalDueAmt() {
        return totalDueAmt;
    }

    public void setTotalDueAmt(BigDecimal totalDueAmt) {
        this.totalDueAmt = totalDueAmt;
    }

    public BigDecimal getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(BigDecimal dueAmt) {
        this.dueAmt = dueAmt;
    }

    @Override
    public String toString() {
        return "CARD NO - " + mInvNo + " - " + cusName;
    }
}
