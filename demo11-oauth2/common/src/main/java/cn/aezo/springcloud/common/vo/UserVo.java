package cn.aezo.springcloud.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo implements Serializable {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String phone;

    private String avatar;

    private String name;

    private Integer sex;

    private Integer age;

    private Date birthday;

    private String roleCode;

    private Integer validStatus;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 3881610071550902762L;
}
