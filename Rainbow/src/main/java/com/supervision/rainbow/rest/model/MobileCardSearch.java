/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author kavish manjitha
 */
@Entity
@Table(name = "mcardsearch")
public class MobileCardSearch implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conno")
    private String conNo;
    
    @Basic(optional = false)
    @Column(name = "spid")
    private String spId;
    
    @Column(name = "spname")
    private String spName;
    
    @Column(name = "minvno")
    private String mInvNo;
    
    @Column(name = "recflag")
    private String recFlag;
    
    @Column(name = "condate")
    private String conDate;
    
    @Column(name = "cusno")
    private String cusNo;
    
    @Column(name = "idno")
    private String idNo;
    
    @Column(name = "cusname")
    private String cusName;
    
    @Column(name = "add1")
    private String add1;
    
    @Column(name = "add2")
    private String add2;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "tpno")
    private String tpNo;
    
    @Column(name = "mobno")
    private String mobNo;
    
    @Column(name = "totval")
    private String totVal;
    
    @Column(name = "dpmt")
    private String dpmt;
    
    @Column(name = "balval")
    private String balVal;
    
    @Column(name = "totaldueamt")
    private String totalDueAmt;
    
    @Column(name = "dueamt")
    private String dueAmt;

    public MobileCardSearch() {
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

    public String getTotVal() {
        return totVal;
    }

    public void setTotVal(String totVal) {
        this.totVal = totVal;
    }

    public String getDpmt() {
        return dpmt;
    }

    public void setDpmt(String dpmt) {
        this.dpmt = dpmt;
    }

    public String getBalVal() {
        return balVal;
    }

    public void setBalVal(String balVal) {
        this.balVal = balVal;
    }

    public String getTotalDueAmt() {
        return totalDueAmt;
    }

    public void setTotalDueAmt(String totalDueAmt) {
        this.totalDueAmt = totalDueAmt;
    }

    public String getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getConNo() {
        return conNo;
    }

    public void setConNo(String conNo) {
        this.conNo = conNo;
    } 
}
