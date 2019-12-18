/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.service;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.supervision.rainbow.rest.model.MobileCardDetail;
import com.supervision.rainbow.rest.model.MobileCardSearch;
import com.supervision.rainbow.rest.repository.MobileCardDetailRepository;
import com.supervision.rainbow.rest.repository.MobileCardSearchRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kavish Manjitha
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MobileCardDetailService {

    @Autowired
    private MobileCardDetailRepository mobileCardDetailRepository;

    @Autowired
    private MobileCardSearchRepository mobileCardSearchRepository;

    public List<MobileCardDetail> getAllDetailBySpId(String spid) {
        return mobileCardDetailRepository.findBySpId(spid);
    }

    public List<MobileCardDetail> getManualCardDetailByCustometName(String data, String type) {
        List<MobileCardSearch> findByCusNameContaining = new VirtualFlow.ArrayLinkedList<>();
        if ("CUSTOMER NAME".equals(type)) {
            findByCusNameContaining = mobileCardSearchRepository.findByCusNameContaining(data, new PageRequest(0, 20, Sort.Direction.ASC, "conNo"));
        } else if ("TELEPHONE NO".equals(type)) {
            findByCusNameContaining = mobileCardSearchRepository.findByTpNoContaining(data, new PageRequest(0, 20, Sort.Direction.ASC, "conNo"));
        } else if ("MOBILE NO".equals(type)) {
            findByCusNameContaining = mobileCardSearchRepository.findByMobNoContaining(data, new PageRequest(0, 20, Sort.Direction.ASC, "conNo"));
        } else if ("NIC NO".equals(type)) {
            findByCusNameContaining = mobileCardSearchRepository.findByIdNoContaining(data, new PageRequest(0, 20, Sort.Direction.ASC, "conNo"));
        }

        List<MobileCardDetail> cardDetails = new ArrayList<>();
        for (MobileCardSearch mobileCardSearch : findByCusNameContaining) {
            MobileCardDetail mobileCardDetail = new MobileCardDetail();
            mobileCardDetail.setConNo(mobileCardSearch.getConNo());
            mobileCardDetail.setSpId(mobileCardSearch.getSpId());
            mobileCardDetail.setSpName(mobileCardSearch.getSpName());
            mobileCardDetail.setmInvNo(mobileCardSearch.getmInvNo());
            mobileCardDetail.setRecFlag(mobileCardSearch.getRecFlag());
            mobileCardDetail.setConDate(mobileCardSearch.getConDate());
            mobileCardDetail.setCusNo(mobileCardSearch.getCusNo());
            mobileCardDetail.setIdNo(mobileCardSearch.getIdNo());
            mobileCardDetail.setCusName(mobileCardSearch.getCusName());
            mobileCardDetail.setAdd1(mobileCardSearch.getAdd1());
            mobileCardDetail.setAdd2(mobileCardSearch.getAdd2());
            mobileCardDetail.setCity(mobileCardSearch.getCity());
            mobileCardDetail.setTpNo(mobileCardSearch.getTpNo());
            mobileCardDetail.setMobNo(mobileCardSearch.getMobNo());
            mobileCardDetail.setTotVal(mobileCardSearch.getTotVal());
            mobileCardDetail.setDpmt(mobileCardSearch.getDpmt());
            mobileCardDetail.setBalVal(mobileCardSearch.getBalVal());
            mobileCardDetail.setTotalDueAmt(mobileCardSearch.getTotalDueAmt());
            mobileCardDetail.setDueAmt(mobileCardSearch.getDueAmt());
            cardDetails.add(mobileCardDetail);
        }
        return cardDetails;
    }

    public MobileCardDetail getManualCardDetail(String data, String type) {
        List<MobileCardSearch> findByDetails = new ArrayList<>();
        if ("CARD NO".equals(type)) {
            findByDetails = mobileCardSearchRepository.findByMInvNo(data);
        } else if ("CON NO".equals(type)) {
            String formatData = data.replace('$', '/');
            findByDetails = mobileCardSearchRepository.findByConNo(formatData);
        }

        if (findByDetails.isEmpty()) {
            return new MobileCardDetail();
        } else {
            MobileCardSearch getMobCardSearch = findByDetails.get(0);
            MobileCardDetail mobileCardDetail = new MobileCardDetail();
            mobileCardDetail.setConNo(getMobCardSearch.getConNo());
            mobileCardDetail.setSpId(getMobCardSearch.getSpId());
            mobileCardDetail.setSpName(getMobCardSearch.getSpName());
            mobileCardDetail.setmInvNo(getMobCardSearch.getmInvNo());
            mobileCardDetail.setRecFlag(getMobCardSearch.getRecFlag());
            mobileCardDetail.setConDate(getMobCardSearch.getConDate());
            mobileCardDetail.setCusNo(getMobCardSearch.getCusNo());
            mobileCardDetail.setIdNo(getMobCardSearch.getIdNo());
            mobileCardDetail.setCusName(getMobCardSearch.getCusName());
            mobileCardDetail.setAdd1(getMobCardSearch.getAdd1());
            mobileCardDetail.setAdd2(getMobCardSearch.getAdd2());
            mobileCardDetail.setCity(getMobCardSearch.getCity());
            mobileCardDetail.setTpNo(getMobCardSearch.getTpNo());
            mobileCardDetail.setMobNo(getMobCardSearch.getMobNo());
            mobileCardDetail.setTotVal(getMobCardSearch.getTotVal());
            mobileCardDetail.setDpmt(getMobCardSearch.getDpmt());
            mobileCardDetail.setBalVal(getMobCardSearch.getBalVal());
            mobileCardDetail.setTotalDueAmt(getMobCardSearch.getTotalDueAmt());
            mobileCardDetail.setDueAmt(getMobCardSearch.getDueAmt());
            return mobileCardDetail;
        }
    }
}
