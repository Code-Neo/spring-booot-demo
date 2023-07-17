package com.neo.springboot.seckill.service.impl;

import com.neo.springboot.seckill.bean.Goods;
import com.neo.springboot.seckill.mapper.GoodsMapper;
import com.neo.springboot.seckill.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author neo
 * @since 2023-07-16
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

}
