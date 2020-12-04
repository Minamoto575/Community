package cn.krl.community.service;

import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.User;
import cn.krl.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:Minamoto
 * Date:2020/11/16,20:46
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(example);
        if(users.isEmpty()){
            //插入新用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //更新
            //更新时间需要修改，头像、名字信息可能变化，也需要更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setBio(user.getBio());
          //  updateUser.setAccountId(dbUser.getAccountId());
            updateUser.setToken(user.getToken());
            UserExample example1 = new UserExample();
            example1.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            //为空的数据项不更新
            userMapper.updateByExampleSelective(updateUser, example1);
        }

    }
}
