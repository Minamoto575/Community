package cn.krl.community.cache;

import cn.krl.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:Minamoto
 * Date:2020/12/3,11:22
 */
public class TagCache {
    public static List<TagDTO> get() {
        ArrayList<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js", "php", "css", "html", "html5","java", "python", "cpp","oc","swift","golang","shell","c#","asp.net","rust","ruby"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("框架");
        framework.setTags(Arrays.asList("spring", "flask", "express"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "nginx", "docker", "ubuntu", "tomcat", "redis"));
        tagDTOS.add(server);

        return tagDTOS;
    }
}
