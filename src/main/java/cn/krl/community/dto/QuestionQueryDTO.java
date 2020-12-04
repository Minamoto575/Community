package cn.krl.community.dto;

import lombok.Data;

/**
 * Author:Minamoto
 * Date:2020/12/4,19:28
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
