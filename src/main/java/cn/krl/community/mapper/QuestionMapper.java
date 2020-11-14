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

}
