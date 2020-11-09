package cn.krl.community.dto;

import lombok.Data;

@Data
public class GithubUserDTO {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;

}
