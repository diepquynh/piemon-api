package com.lillya.piemon.gacha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GachaItemRepository extends JpaRepository<GachaItem, Long> {
    List<GachaItem> findAllByUid(Integer uid);
    List<GachaItem> findAllByUidAndGachaType(Integer uid, Short gacha_type);
}
