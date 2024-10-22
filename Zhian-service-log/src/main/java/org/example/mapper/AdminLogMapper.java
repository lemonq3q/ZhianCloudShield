package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.AdminLog;

@Mapper
public interface AdminLogMapper {
    public int insertAdminLog(AdminLog adminLog);
}
