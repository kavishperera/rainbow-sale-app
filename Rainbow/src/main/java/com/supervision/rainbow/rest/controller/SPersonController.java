/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.controller;

import com.supervision.rainbow.rest.model.SPerson;
import com.supervision.rainbow.rest.service.SPersonService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Kavish Manjitha
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/rainbow/sperson")
public class SPersonController {

    @Autowired
    private SPersonService sPersonService;

    @RequestMapping(value = "/get-active-users", method = RequestMethod.GET)
    public List<SPerson> getActiveUsers() {
        return sPersonService.getActiveUsers();
    }

    @RequestMapping(value = "/save-sperson-last-payment-serial-sync/{spId}/{lastPaymentSerial}", method = RequestMethod.GET)
    public String saveSPersonLastPaymentSrialData(@PathVariable String spId, @PathVariable Integer lastPaymentSerial) {
        return sPersonService.saveSPersonLastPaymentSrialData(spId, lastPaymentSerial);
    }
}
