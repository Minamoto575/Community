package cn.krl.community.mapper;

import cn.krl.community.model.Comment;
import cn.krl.community.model.Question;

/**
 * Author:Minamoto
 * Date:2020/11/21,16:04
 */
public interface CommentExtMapper {
    void incComment(Comment comment);
}
