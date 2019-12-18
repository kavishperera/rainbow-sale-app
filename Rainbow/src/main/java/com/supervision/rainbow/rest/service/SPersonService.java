/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.service;

import com.supervision.rainbow.rest.model.MobileData;
import com.supervision.rainbow.rest.model.SPerson;
import com.supervision.rainbow.rest.repository.SPersonRepository;
import java.util.ArrayList;
import java.util.List;
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
public class SPersonService {

    @Autowired
    private SPersonRepository sPersonRepository;

    public List<SPerson> getActiveUsers() {
        List<SPerson> findByColF = sPersonRepository.findByColF("1");
        if (findByColF.isEmpty()) {
            List<SPerson> emptySpersonList = new ArrayList<>();
            return emptySpersonList;
        } else {
            return findByColF;
        }
    }

    public String saveSPersonLastPaymentSrialData(String spId, Integer lastPaymentSerial) {
        List<SPerson> findBySpId = sPersonRepository.findBySpId(spId);
        SPerson sPerson = findBySpId.get(0);
        sPerson.setLastSerial(lastPaymentSerial);
        SPerson savepersonData = sPersonRepository.save(sPerson);
        return savepersonData.getSpId();
    }

}
