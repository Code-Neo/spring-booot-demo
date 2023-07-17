package com.neo.springboot.seckill.mapper;

import com.neo.springboot.seckill.bean.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author neo
 * @since 2023-07-16
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
