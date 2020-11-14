package cn.krl.community.mapper;

import cn.krl.community.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * Author:Minamoto
 * Date:2020/11/9,13:16
 */
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") Integer userId, @Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer selectCountByUserId(@Param(value = "userId") Integer userId);
}
