package cn.krl.community.dto;

import lombok.Data;

/**
 * Author:Minamoto
 * Date:2020/11/21,15:24
 */
@Data
public class CommentCreateDTO {

    private Integer parentId;
    private String content;
    private Integer type;
}
