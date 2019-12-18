 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Kavish Manjitha
 */
@Entity
@Table(name = "mobdata")
public class MobileData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "indexno")
    private Integer indexNo;

    @Column(name = "spid")
    private String spId;

    @Column(name = "pmtno")
    private String pmtNo;
    
    @Column(name = "minvno")
    private String mInvNo;

    @Column(name = "conno")
    private String conNo;
    
    @Column(name = "pmtdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pmtDate;

    @Column(name = "pmttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pmtTime;

    @Column(name = "pmttype")
    private String pmtType;

    @Column(name = "pmtamt")
    private BigDecimal pmtAmt;

    @Column(name = "pmtremark")
    private String pmtRemark;

    @Column(name = "npmtdate")
    private String npmtDate;

    @Column(name = "phoneid")
    private Integer phoneId;

    @Column(name = "sysupdate")
    private Integer sysUpdate;

    @Column(name = "sysupdatedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysUpdateDate;

    public MobileData() {
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
    
    public String getmInvNo() {
        return mInvNo;
    }

    public void setmInvNo(String mInvNo) {
        this.mInvNo = mInvNo;
    }
    
    @Override
    public String toString() {
        return "MobileData{" + "indexNo=" + indexNo + ", spId=" + spId + ", pmtNo=" + pmtNo + ", conNo=" + conNo + ", pmtDate=" + pmtDate + ", pmtTime=" + pmtTime + ", pmtType=" + pmtType + ", pmtAmt=" + pmtAmt + ", pmtRemark=" + pmtRemark + ", npmtDate=" + npmtDate + ", phoneId=" + phoneId + ", sysUpdate=" + sysUpdate + ", sysUpdateDate=" + sysUpdateDate + '}';
    }


}
