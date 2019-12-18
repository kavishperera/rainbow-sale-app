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
 * @author Kavish Manjitha
 */
@Entity
@Table(name = "sperson")
public class SPerson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "spid")
    private String spId;

    @Column(name = "spname")
    private String spName;

    @Column(name = "pword")
    private String password;

    @Column(name = "colf")
    private String colF;
    
    @Column(name = "lastserial")
    private Integer lastSerial;

    public SPerson() {
    }

    public SPerson(String spId, String spName, String password, String colF, Integer lastSerial) {
        this.spId = spId;
        this.spName = spName;
        this.password = password;
        this.colF = colF;
        this.lastSerial = lastSerial;
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
        return "SPerson{" + "spId=" + spId + ", spName=" + spName + ", password=" + password + ", colF=" + colF + ", lastSerial=" + lastSerial + '}';
    }

}
