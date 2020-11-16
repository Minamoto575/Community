package cn.krl.community.service;

import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:Minamoto
 * Date:2020/11/16,20:46
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        User dbUser = userMapper.selectByAccountId(user.getAccount_id());
        if(dbUser==null){
            //插入新用户
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.insert(user);
        }else {
            //更新信息
            dbUser.setAvatar_url(user.getAvatar_url());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            dbUser.setBio(user.getBio());
            dbUser.setGmt_modified(System.currentTimeMillis());
            userMapper.updateById(dbUser);
        }

    }
}
