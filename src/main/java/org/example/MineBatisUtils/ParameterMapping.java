package org.example.MineBatisUtils;

import lombok.Data;

/**
 * @author ziyuan
 * @since 2024.04
 */
@Data
public class ParameterMapping {
    private String property;
    ParameterMapping(String property){
        this.property = property;
    }

}
