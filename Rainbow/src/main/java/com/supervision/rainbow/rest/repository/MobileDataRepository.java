/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.repository;

import com.supervision.rainbow.rest.model.MobileData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Kavish Manjitha
 */
public interface MobileDataRepository extends JpaRepository<MobileData, String>{
    
}
