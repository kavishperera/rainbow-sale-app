/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.service;

import com.supervision.rainbow.rest.model.MobileData;
import com.supervision.rainbow.rest.repository.MobileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kavish Manjitha
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MobileDataService {

    @Autowired
    private MobileDataRepository mobileDataRepository;

    public Integer saveMobileData(MobileData mobileData) {
        mobileData.setIndexNo(null);
        MobileData saveMobileData = mobileDataRepository.save(mobileData);
        return saveMobileData.getIndexNo();
    }
}
