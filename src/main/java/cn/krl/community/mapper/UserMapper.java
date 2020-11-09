package cn.krl.community.mapper;

import cn.krl.community.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Author:Minamoto
 * Date:2020/11/8,10:59
 */

public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where token = #{token}")
    User getUserByToken(@Param("token") String token);
}
