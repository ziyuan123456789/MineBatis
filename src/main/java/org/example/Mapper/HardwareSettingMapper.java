package org.example.Mapper;

import org.example.JavaBeans.HardwareSetting;
import org.example.OrmAnnotations.MyMapper;

/**
 * @author ziyuan
 * @since 2024.04
 */

public interface HardwareSettingMapper {

    HardwareSetting getOneHardware(Integer HardwareSettingId);
}
