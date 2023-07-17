package com.neo.springboot.seckill.service;

import com.neo.springboot.seckill.bean.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author neo
 * @since 2023-07-16
 */
public interface OrdersService extends IService<Orders> {

    String saveOrder(Integer userId, Integer orderId);

    String saveSynchronizedOrder(Integer userId, Integer orderId);

    String saveSynchronizedDownOrder(Integer userId, Integer orderId);

    String SaveOptimisticLockAndLeakyBucket(Integer userId, Integer goodsId);

    String SaveOptimisticLockAndTokenBucket(Integer userId, Integer goodsId);
}
