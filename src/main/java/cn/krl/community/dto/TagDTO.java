package cn.krl.community.dto;

import lombok.Data;

import java.util.List;

/**
 * Author:Minamoto
 * Date:2020/12/3,11:23
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
