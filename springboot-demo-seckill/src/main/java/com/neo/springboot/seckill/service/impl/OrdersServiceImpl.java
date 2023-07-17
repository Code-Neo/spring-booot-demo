package com.neo.springboot.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.RateLimiter;
import com.neo.springboot.seckill.bean.Goods;
import com.neo.springboot.seckill.bean.Orders;
import com.neo.springboot.seckill.mapper.OrdersMapper;
import com.neo.springboot.seckill.service.GoodsService;
import com.neo.springboot.seckill.service.OrdersService;
import com.neo.springboot.seckill.utils.LeakyBucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author neo
 * @since 2023-07-16
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    /**
     * 创建令牌桶的实例
     */
    private final RateLimiter rateLimiter = RateLimiter.create(100);


    // 漏桶算法
    private final static LeakyBucket LB = new LeakyBucket(0.01,10);

    // 记录请求次数
    private static Integer count = 0;

    @Resource
    private GoodsService goodsService;

    /**
     * 乐观锁
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public String saveOrder(Integer userId, Integer goodsId) {
        // 1.判断当前用户是否已经下过单
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Orders::getUser_id, userId).eq(Orders::getGoods_id, goodsId);

        if (baseMapper.selectCount(wrapper) == 1) {
            return "一个用户只能下一单";
        }
        // 2.判断是否够订单还有库存
        Goods goods = new LambdaQueryChainWrapper<Goods>(goodsService.getBaseMapper())
                .eq(Goods::getId, goodsId)
                .gt(Goods::getStock, 0)
                .one();
        if (Objects.isNull(goods)){
            return "很遗憾你没有秒杀成功";
        }
        // 3.生成订单
        // 乐观锁代码 update goods set stock = stock -1 where id = ? and stock > 0
        // 其中stock > 0可以看作是一个version，因为只有这个条件满足才能更新
        boolean success = goodsService.update(new UpdateWrapper<Goods>()
                .setSql("stock = stock-1")
                .eq("id",goodsId)
                .gt("stock",0));
        if (!success){
            throw new RuntimeException("下单失败");
        }
        Orders orders = new Orders();
        orders.setGoods_id(goodsId)
                .setUser_id(userId);
        // 4.保存订单
        baseMapper.insert(orders);
        return "ok";
    }

    /**
     * synchronized锁
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public synchronized String saveSynchronizedOrder(Integer userId, Integer goodsId) {
        // 1.判断当前用户是否已经下过单
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Orders::getUser_id, userId)
                .eq(Orders::getGoods_id, goodsId);

        if (baseMapper.selectCount(wrapper) == 1) {
            throw new RuntimeException("一个用户只能下一单");
        }
        // 2.判断是否够订单还有库存
        Goods goods = new LambdaQueryChainWrapper<Goods>(goodsService.getBaseMapper())
                .eq(Goods::getId, goodsId)
                .gt(Goods::getStock, 0)
                .one();
        if (Objects.isNull(goods)){
            throw new RuntimeException("库存不足");
        }
        // 3.生成订单
        boolean success = goodsService.update(new UpdateWrapper<Goods>()
                .setSql("stock = stock-1")
                .eq("id",goodsId));
        if (!success){
            throw new RuntimeException("下单失败");
        }
        Orders orders = new Orders();
        orders.setGoods_id(goodsId)
                .setUser_id(userId);
        // 4.保存订单
        baseMapper.insert(orders);
        return "ok";
    }

    /**
     * synchronized锁+乐观锁，降低锁粒度，只锁用户线程，
     * 参考redis高级笔记
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public  String saveSynchronizedDownOrder(Integer userId, Integer goodsId) {
        synchronized (userId.toString().intern()){
            OrdersServiceImpl ordersService = (OrdersServiceImpl) AopContext.currentProxy();
            return ordersService.saveSecKill(userId,goodsId);
        }
    }

    /**
     * 漏桶算法（限流） + 乐观锁
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public String SaveOptimisticLockAndLeakyBucket(Integer userId, Integer goodsId) {
        // 1. 检查漏桶限流情况
        boolean enterBucket = LB.enterBucket();
        if (!enterBucket){
            log.info("系统繁忙，请稍后再试");
            return "系统繁忙，请稍后再试";
        }
        // 记录请求次数
        count++;
        log.info("请求次数：{}",count);
        // 2. 开始秒杀操作
        // 2.1 查询是否已经下过单
        Long orderCount = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(Orders::getUser_id, userId)
                .eq(Orders::getGoods_id, goodsId)
                .count();
        if (orderCount == 1){
            log.info("一个用户只能下一单");
            return "一个用户只能下一单";
        }

        // 2.2 判断是否还有库存
        Goods goods = new LambdaQueryChainWrapper<>(goodsService.getBaseMapper())
                .eq(Goods::getId, goodsId)
                .gt(Goods::getStock,0)
                .one();
        if (Objects.isNull(goods)){
            log.info("秒杀已结束");
            return "秒杀已结束";
        }

        // 2.3 更新库存并且下单
        // 乐观锁
        LambdaUpdateWrapper<Goods> wrapper = new LambdaUpdateWrapper<Goods>()
                .setSql("stock = stock -1")
                .eq(Goods::getId, goodsId)
                .gt(Goods::getStock, 0);
        boolean updateStock = goodsService.update(wrapper);
        if (!updateStock){
            log.info("库存不足");
            return "库存不足";
        }
        // 2.4下订单
        Orders orders = new Orders().setUser_id(userId)
                .setGoods_id(goodsId);
        baseMapper.insert(orders);

        return "ok";
    }

    /**
     * 乐观锁 + 令牌桶漏桶算法限流
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public String SaveOptimisticLockAndTokenBucket(Integer userId, Integer goodsId) {
        // 1.限流
        // acquire等待空闲继续方行
        //double acquire = rateLimiter.acquire();
        //log.info("等待时间：{}", acquire);
        //count++;
        //log.info("请求的次数 = {}", count);

        // tryAcquire设置超时时间，
        boolean acquire = rateLimiter.tryAcquire(1, 1, TimeUnit.SECONDS);
        if (!acquire) {
            log.error("系统繁忙请稍后再试");
            return "系统繁忙请稍后再试";
        }
        // 2. 开始秒杀操作
        // 2.1 查询是否已经下过单
        Long orderCount = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(Orders::getUser_id, userId)
                .eq(Orders::getGoods_id, goodsId)
                .count();
        if (orderCount == 1){
            log.info("一个用户只能下一单");
            return "一个用户只能下一单";
        }

        // 2.2 判断是否还有库存
        Goods goods = new LambdaQueryChainWrapper<>(goodsService.getBaseMapper())
                .eq(Goods::getId, goodsId)
                .gt(Goods::getStock,0)
                .one();
        if (Objects.isNull(goods)){
            log.info("秒杀已结束");
            return "秒杀已结束";
        }

        // 2.3 更新库存并且下单
        // 乐观锁
        LambdaUpdateWrapper<Goods> wrapper = new LambdaUpdateWrapper<Goods>()
                .setSql("stock = stock -1")
                .eq(Goods::getId, goodsId)
                .gt(Goods::getStock, 0);
        boolean updateStock = goodsService.update(wrapper);
        if (!updateStock){
            log.info("库存不足");
            return "库存不足";
        }
        // 2.4下订单
        Orders orders = new Orders().setUser_id(userId)
                .setGoods_id(goodsId);
        baseMapper.insert(orders);

        return "ok";
    }

    public String saveSecKill(Integer userId, Integer goodsId) {
        // 1.判断当前用户是否已经下过单
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Orders::getUser_id, userId)
                .eq(Orders::getGoods_id, goodsId);

        if (baseMapper.selectCount(wrapper) == 1) {
            throw new RuntimeException("一个用户只能下一单");
        }
        // 2.判断是否够订单还有库存
        Goods goods = new LambdaQueryChainWrapper<>(goodsService.getBaseMapper())
                .eq(Goods::getId, goodsId)
                .gt(Goods::getStock, 0)
                .one();
        if (Objects.isNull(goods)){
            throw new RuntimeException("很遗憾你没有秒杀成功");
        }
        // 3.生成订单
        // 乐观锁代码 update goods set stock = stock -1 where id = ? and stock > 0
        // 其中stock > 0可以看作是一个version，因为只有这个条件满足才能更新
        boolean success = goodsService.update(new UpdateWrapper<Goods>()
                .setSql("stock = stock-1")
                .eq("id", goodsId)
                .gt("stock",0));
        if (!success){
            throw new RuntimeException("下单失败");
        }
        Orders orders = new Orders();
        orders.setGoods_id(goodsId)
                .setUser_id(userId);
        // 4.保存订单
        baseMapper.insert(orders);
        return "ok";
    }
}
