package com.mwt.oes.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

public interface TableExistsMapper {
    void createTableIfNotExists(Map<String,String> tableName);
}
