package cn.krl.community.dto;

import cn.krl.community.model.User;
import lombok.Data;

/**
 * Author:Minamoto
 * Date:2020/11/9,16:07
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Long gmt_create;
    private Long gmt_modified;
    private Integer creator;
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;
    private String tag;
    private User user;
}
