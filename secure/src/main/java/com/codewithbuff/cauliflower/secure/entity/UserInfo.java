package com.codewithbuff.cauliflower.secure.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table("user_info")
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column("username")
    private String username;

    /**
     * 邮箱
     */
    @Column("email")
    private String email;

    /**
     * 手机号：+<地区>:xxxx
     */
    @Column("phone_num")
    private String phoneNum;

    /**
     * 0：未知，1：男，2：女
     */
    @Column("gender")
    private Integer gender;

    /**
     * 出生日期
     */
    @Column("birth_date")
    private LocalDateTime birthDate;

    /**
     * 头像
     */
    @Column("avatar")
    private String avatar;

    /**
     * 主页背景图
     */
    @Column("bkg_img")
    private String bkgImg;

    /**
     * 签名
     */
    @Column("talking")
    private String talking;

    /**
     * 地址
     */
    @Column("location")
    private Long location;

    /**
     * 个人网站
     */
    @Column("website")
    private String website;

}
