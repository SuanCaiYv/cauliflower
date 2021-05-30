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
@Accessors(chain = true)
@Table("sys_role_resources")
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleResources implements Serializable {

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

    @Column("role_id")
    private Long roleId;

    @Column("resource_id")
    private Long resourceId;

}
