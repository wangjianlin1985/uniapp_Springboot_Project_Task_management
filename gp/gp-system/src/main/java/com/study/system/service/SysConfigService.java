package com.study.system.service;

import com.study.common.core.base.BaseService;
import com.study.system.entity.SysConfig;

public interface SysConfigService extends BaseService<SysConfig>{

    /**
     * 判断configKey是否唯一
     * @param configKey
     * @return
     */
    String checkConfigKeyUnique(SysConfig config);
    
    /**
     * 走缓存
     * 根据key获取value值
     * @param configKey
     * @return
     */
    String getConfigByKey(String configKey);
    
}
