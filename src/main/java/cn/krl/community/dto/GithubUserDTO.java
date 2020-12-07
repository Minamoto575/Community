package cn.krl.community.dto;

import lombok.Data;

@Data
public class GithubUserDTO {
    private String name;
    private String id;
    private String bio;
    private String avatar_url;
}
