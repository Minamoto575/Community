package cn.krl.community.dto;

import lombok.Data;

/**
 * Author:Minamoto
 * Date:2020/12/3,15:03
 */
@Data
public class NotificationDTO {
    private Integer id;
    private Long gmtCreate;
    private Integer status;
    private Integer notifier;
    private String notifierName;
    private String outerTitle;
    private Integer outerId;
    private String typeName;    //类型，回复问题还是回复评论
    private Integer type;
}
