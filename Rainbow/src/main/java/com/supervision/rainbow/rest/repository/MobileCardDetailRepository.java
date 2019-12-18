/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.rainbow.rest.repository;

import com.supervision.rainbow.rest.model.MobileCardDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Kavish Manjitha
 */
public interface MobileCardDetailRepository extends JpaRepository<MobileCardDetail, String> {

    public List<MobileCardDetail> findBySpId(String spid);

}
