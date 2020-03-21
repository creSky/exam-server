package com.mwt.oes.service.impl;

import com.mwt.oes.dao.TableExistsMapper;
import com.mwt.oes.service.TableExistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class TableExistsServiceImpl implements TableExistsService {
    @Autowired
    TableExistsMapper tableExistsMapper;

    @Override
    public void createTableIfNotExists(Map<String,String> tableName) {
        tableExistsMapper.createTableIfNotExists(tableName);
    }
}
