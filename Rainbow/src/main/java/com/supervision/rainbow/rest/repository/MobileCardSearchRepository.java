/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.repository;

import com.supervision.rainbow.rest.model.MobileCardSearch;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kavish manjitha
 */
public interface MobileCardSearchRepository extends JpaRepository<MobileCardSearch, String> {

    public List<MobileCardSearch> findByMInvNo(String mInvNo);

    public List<MobileCardSearch> findByTpNoContaining(String tpNo,Pageable pageable);

    public List<MobileCardSearch> findByMobNoContaining(String mobNo,Pageable pageable);

    public List<MobileCardSearch> findByConNo(String conNo);
    
    public List<MobileCardSearch> findByIdNoContaining(String idNo,Pageable pageable);
    
    public List<MobileCardSearch> findByCusNameContaining(String cusName,Pageable pageable);
}
