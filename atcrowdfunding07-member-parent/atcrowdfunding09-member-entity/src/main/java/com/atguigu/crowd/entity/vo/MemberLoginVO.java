package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    private String email;
}
