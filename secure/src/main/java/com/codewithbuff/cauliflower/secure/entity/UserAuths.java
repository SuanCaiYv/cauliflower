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
@Table("user_auths")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserAuths implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("created_time")
    private LocalDateTime createdTime;

    @Column("updated_time")
    private LocalDateTime updatedTime;

    @Column("is_available")
    private Boolean available;

    @Column("user_id")
    private Long userId;

    @Column("identity_type")
    private String identityType;

    /**
     * 登录名
     */
    @Column("identifier")
    private String identifier;

    /**
     * 登录密码
     */
    @Column("credential")
    private String credential;

    public static final String IDENTITY_TYPE_EMAIL = "EMAIL";

    public static final String IDENTITY_TYPE_USERNAME = "USERNAME";

}
