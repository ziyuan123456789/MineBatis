package org.example.MineBatisUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ziyuan
 * @since 2024.04
 */
public  class ParameterMappingTokenHandler implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<>();


    @Override
    public String handleToken(String content) {
        parameterMappings.add(new ParameterMapping(content));
        return "?";
    }

    public List<ParameterMapping> getParameterMapping() {
        return parameterMappings;
    }
    public void resetParameterMappings(){
        parameterMappings.clear();
    }


}
