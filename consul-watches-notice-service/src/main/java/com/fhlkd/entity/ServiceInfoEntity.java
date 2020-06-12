package com.fhlkd.entity;

import lombok.*;

/**
 * Created by yanghaiyang on 2020/6/11 9:53
 * 服务信息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceInfoEntity {

    /**
     * 归属人
     */
    private String belonger;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 当前服务实例总数量
     */
    private Integer totalMember;

    /**
     * 当前服务实例可用数量
     */
    private Integer availableMember;
}
