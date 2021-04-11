package com.example.dcloud.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总条数
     */
    private Long total;

    /**
     * 数据
     */
    private List<?> data;
}
