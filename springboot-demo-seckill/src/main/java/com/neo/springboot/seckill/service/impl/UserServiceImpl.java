package com.neo.springboot.seckill.service.impl;

import com.neo.springboot.seckill.bean.User;
import com.neo.springboot.seckill.mapper.UserMapper;
import com.neo.springboot.seckill.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
