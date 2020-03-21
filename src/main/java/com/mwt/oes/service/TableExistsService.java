package com.mwt.oes.service;

import java.util.Map;

public interface TableExistsService {
    public void createTableIfNotExists(Map<String,String> tableName);
}
