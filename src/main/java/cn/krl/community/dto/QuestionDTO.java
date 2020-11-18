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
    private String tag;
    private long gmtCreate;
    private long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private User user;
}
