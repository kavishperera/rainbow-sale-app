/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.controller;

import com.supervision.rainbow.rest.model.MobileCardDetail;
import com.supervision.rainbow.rest.service.MobileCardDetailService;
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
@RequestMapping(value = "/api/v1/rainbow/card-detail")
public class MobileCardDetailController {

    @Autowired
    private MobileCardDetailService mobileCardDetailService;

    @RequestMapping(value = "/get-card-details/{spid}", method = RequestMethod.GET)
    public List<MobileCardDetail> getCardDetail(@PathVariable("spid") String spid) {
        return mobileCardDetailService.getAllDetailBySpId(spid);
    }

    @RequestMapping(value = "/manual-card-details-search/{data}/{type}", method = RequestMethod.GET)
    public MobileCardDetail getManualCardDetail(
            @PathVariable("data") String data,
            @PathVariable("type") String type) {
        return mobileCardDetailService.getManualCardDetail(data, type);
    }

    
    @RequestMapping(value = "/manual-card-details-search-by-customer-name/{data}/{type}", method = RequestMethod.GET)
    public List<MobileCardDetail> getManualCardDetailByCustometName(
            @PathVariable("data") String data,
            @PathVariable("type") String type) {
        return mobileCardDetailService.getManualCardDetailByCustometName(data,type);
    }
}
