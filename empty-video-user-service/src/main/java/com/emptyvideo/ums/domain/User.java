package com.emptyvideo.ums.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 3557910046118981308L;
    @Id
    private String id;              //user id
    @Indexed(unique = true)
    private String email;           //email
    @Indexed
    private String pwd;             //password

    private UserSystem system;      //system controlling
    private UserProfile profile;    //user customise

    public User(Map userInfo, PasswordEncoder passwordEncoder) {
        this();
        this.system = new UserSystem();
        this.profile = new UserProfile();
        this.system.setRoles(Collections.singletonList("admin"));
        this.profile.setName(userInfo.get("name").toString());
        this.pwd = passwordEncoder.encode(userInfo.get("pwd").toString());
        this.setEmail(userInfo.get("email").toString());
    }

    public Mono<Map> getPublicUserMapMono() {
        Map<String, Object> res = new HashMap<>();
        res.put("id", this.getId());
        res.put("system", this.getSystem());
        res.put("profile", this.getProfile());
        return Mono.just(res);
    }

    public void updateUserProfile(Map updateForm) {
        this.profile.setName(String.valueOf(updateForm.get("name")));
        this.profile.setAvatar(String.valueOf(updateForm.get("avatar")));
        this.profile.setBanner(String.valueOf(updateForm.get("banner")));
        this.profile.setDescription(String.valueOf(updateForm.get("description")));
        this.profile.setLocation(String.valueOf(updateForm.get("location")));
        this.profile.setWebsite(String.valueOf(updateForm.get("website")));
    }

    public void updateUserSystem(Map updateForm) {
        this.system.setStatus(String.valueOf(updateForm.get("status")));
        this.system.setPoint((Long) updateForm.get("point"));
        this.system.setAchievement((List<String>) updateForm.get("achievement"));
    }
}
