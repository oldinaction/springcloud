package cn.aezo.springcloud.oauthserver.dao;

import cn.aezo.springcloud.common.vo.UserVo;

public interface UserDao {
    UserVo findByUsername(String username);

    UserVo findByWxCode(String wxCode);
}
