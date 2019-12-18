/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.controller;

import com.supervision.rainbow.rest.model.MobileData;
import com.supervision.rainbow.rest.service.MobileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Kavish Manjitha
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/rainbow/mobile-data")
public class MobileDataController {

    @Autowired
    private MobileDataService mobileDataService;

    @RequestMapping(value = "/save-mobile-transaction-data", method = RequestMethod.POST)
    public Integer saveMobileData(@RequestBody MobileData mobileData) {
        return mobileDataService.saveMobileData(mobileData);
    }
}
