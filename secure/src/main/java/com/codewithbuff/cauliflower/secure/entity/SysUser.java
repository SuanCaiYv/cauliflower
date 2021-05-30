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
@Table("sys_user")
@Accessors(chain = true)
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("created_time")
    private LocalDateTime createdTime;

    @Column("updated_time")
    private LocalDateTime updatedTime;

    @Column("state")
    private Boolean state;

    @Column("flag")
    private Integer flag;

    @Column("username")
    private String username;

    @Column("salt")
    private String salt;

}
