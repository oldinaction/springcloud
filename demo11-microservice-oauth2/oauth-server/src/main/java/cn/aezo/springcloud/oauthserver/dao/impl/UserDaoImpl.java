package cn.aezo.springcloud.oauthserver.dao.impl;

import cn.aezo.springcloud.common.vo.UserVo;
import cn.aezo.springcloud.oauthserver.dao.UserDao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserDaoImpl implements UserDao {
    private static final Map<String, UserVo> userDb = new ConcurrentHashMap<>();

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static {
        System.out.println("passwordEncoder.encode(\"123\") = " + passwordEncoder.encode("123"));

        userDb.put("smalle", UserVo.builder()
                .id(1L)
                .username("smalle")
                .password(passwordEncoder.encode("123"))
                .roleCode("USER")
                .build());
        userDb.put("admin", UserVo.builder()
                .id(2L)
                .username("admin")
                .password(passwordEncoder.encode("123"))
                .roleCode("ADMIN")
                .build());
    }

    @Override
    public UserVo findByUsername(String username) {
        return userDb.get(username);
    }

    @Override
    public UserVo findByWxCode(String wxCode) {
        return null;
    }
}
