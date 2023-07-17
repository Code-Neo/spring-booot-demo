package com.neo.springboot.seckill.controller;


import com.neo.springboot.seckill.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author neo
 * @since 2023-07-16
 */
@RestController
@Slf4j
@RequestMapping("/seckill/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 乐观锁
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("/saveOrder/{userId}/{goodsId}")
    public String saveOrder(@PathVariable("userId") Integer userId, @PathVariable("goodsId") Integer goodsId){
        log.info("用户Id = {},商品Id = {}", userId, goodsId);
        String res = ordersService.saveOrder(userId, goodsId);
        return res;
    }

    /**
     * Synchronized锁
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("/saveSynchronizedOrder/{userId}/{goodsId}")
    public  String saveSynchronizedOrder(@PathVariable("userId") Integer userId, @PathVariable("goodsId") Integer goodsId){
        try{
            log.info("用户Id = {},商品Id = {}", userId, goodsId);
            String res = ordersService.saveSynchronizedOrder(userId, goodsId);
            return res;
        }catch (Exception e){
            log.info(e.getMessage());
            return "error";
        }
    }


    /**
     * Synchronized降低锁粒度 + 乐观锁
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("/saveSynchronizedDownOrder/{userId}/{goodsId}")
    public  String saveSynchronizedDownOrder(@PathVariable("userId") Integer userId, @PathVariable Integer goodsId){
        try{
            log.info("用户Id = {},商品Id = {}", userId, goodsId);
            String res = ordersService.saveSynchronizedDownOrder(userId, goodsId);
            return res;
        }catch (Exception e){
            log.info(e.getMessage());
            return "error";
        }
    }

    /**
     * 乐观锁 + 漏桶算法限流
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("/SaveOptimisticLockAndLeakyBucket/{userId}/{goodsId}")
    public String SaveOptimisticLockAndLeakyBucket(@PathVariable("userId") Integer userId, @PathVariable Integer goodsId){
        try{
            log.info("用户Id = {},商品Id = {}", userId, goodsId);
            String res = ordersService.SaveOptimisticLockAndLeakyBucket(userId, goodsId);
            return res;
        }catch (Exception e){
            log.info(e.getMessage());
            return "error";
        }
    }

    /**
     * 乐观锁 + 令牌桶漏桶算法限流
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("/SaveOptimisticLockAndTokenBucket/{userId}/{goodsId}")
    public String SaveOptimisticLockAndTokenBucket(@PathVariable("userId") Integer userId, @PathVariable Integer goodsId){
        try{
            log.info("用户Id = {},商品Id = {}", userId, goodsId);
            String res = ordersService.SaveOptimisticLockAndTokenBucket(userId, goodsId);
            return res;
        }catch (Exception e){
            log.info(e.getMessage());
            return "error";
        }
    }

}

