package com.jxnu.blog.services;

import com.jxnu.blog.common.ServerResponse;

import java.util.Map;

public interface RewardService {
    ServerResponse<String> createOrder(int articleId, int userId,long payment);
    ServerResponse<String> pay(Integer userId, String rewardNo, String path);
    ServerResponse callback(Map<String,String> param);
    ServerResponse query(String orderNo);
}
