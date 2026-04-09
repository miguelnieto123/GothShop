package com.GothWearShop.GothShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GothWearShop.GothShop.entity.order;

@Repository
public interface sellorderRepository extends JpaRepository<order, Long> {
    List<order> findByUserId(Long userId);
}
