package com.jxnu.blog.reporsity;

import com.jxnu.blog.pojo.reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface rewardReposity extends JpaRepository<reward,Integer> {
    reward findByUserIdAndRewardNo(int userId,String rewardNo);
    reward findByRewardNo(String rewardNo);
    @Modifying
    @Transactional
    @Query(value = "update reward set reward.status=?1 where reward.id=?2", nativeQuery = true)
    int statusUpdateById(int code,int id);
}
