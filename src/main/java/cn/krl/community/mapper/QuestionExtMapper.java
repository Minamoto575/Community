package cn.krl.community.mapper;

import cn.krl.community.model.Question;

/**
 * Author:Minamoto
 * Date:2020/11/19,16:02
 */
public interface QuestionExtMapper {
    void incView(Question question);
    void incComment(Question question);
}
