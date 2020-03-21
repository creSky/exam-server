package com.mwt.oes.dao;

import com.mwt.oes.domain.BankAnswerQue;
import com.mwt.oes.domain.BankAnswerQueExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BankAnswerQueMapper {
    int countByExample(BankAnswerQueExample example);

    int deleteByExample(BankAnswerQueExample example);

    int deleteByPrimaryKey(Integer fillId);

    int insert(BankAnswerQue record);
    int insertSelective(BankAnswerQue record);

    List<BankAnswerQue> selectByExample(BankAnswerQueExample example);

    BankAnswerQue selectByPrimaryKey(Integer fillId);

    int updateByExampleSelective(@Param("record") BankAnswerQue record, @Param("example") BankAnswerQueExample example);

    int updateByExample(@Param("record") BankAnswerQue record, @Param("example") BankAnswerQueExample example);

    int updateByPrimaryKeySelective(BankAnswerQue record);

    int updateByPrimaryKey(BankAnswerQue record);

    List<BankAnswerQue> getAnswerQueListByPaperId(Integer paperId);
    List<Map<String,String>> getAnswerQueListByPaperIdAndSno(Map obj);

    List<BankAnswerQue> getRandomAnswerByCountAndLangId(@Param("langId") Integer langId, @Param("fillNum") Integer fillNum);
}