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
@Table("sys_role")
public class SysRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 其实不应该用bigint，但是为了统一化处理，我偷懒了。
     */
    @Id
    private Long id;

    @Column("created_time")
    private LocalDateTime createdTime;

    @Column("updated_time")
    private LocalDateTime updatedTime;

    @Column("is_available")
    private Boolean available;

    @Column("name")
    private String name;

    @Column("desc")
    private String desc;

    /**
     * 0 特罗姆瑟(Admin)
     */
    public static final String TROMS = "TROMS";

    /**
     * 1 巴尔斯菲尤尔
     */
    public static final String BALSFJORD = "BALSFJORD";

    /**
     * 2 巴尔迪
     */
    public static final String BARDU = "BARDU";

    /**
     * 3 贝格
     */
    public static final String BERG = "BERG";

    /**
     * 4 迪略
     */
    public static final String DYROY = "DYROY";

    /**
     * 5 格拉唐恩
     */
    public static final String GRATANGEN = "GRATANGEN";

    /**
     * 6 哈尔斯塔
     */
    public static final String HARSTAD = "HARSTAD";

    /**
     * 7 伊伯斯塔
     */
    public static final String IBESTAD = "IBESTAD";

    /**
     * 8 科菲尤尔
     */
    public static final String KAFJORD = "KAFJORD";

    /**
     * 9 卡尔绥
     */
    public static final String KARLSOY = "KARLSOY";

}
