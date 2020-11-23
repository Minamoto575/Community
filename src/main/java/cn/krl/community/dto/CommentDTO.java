package cn.krl.community.dto;

import cn.krl.community.model.User;
import lombok.Data;

/**
 * Author:Minamoto
 * Date:2020/11/19,17:01
 */
@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private Integer commentCount;
    private User user;
}

