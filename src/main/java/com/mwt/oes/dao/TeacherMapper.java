package com.mwt.oes.dao;

import com.mwt.oes.domain.*;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TeacherMapper {
    int countByExample(TeacherExample example);

    int deleteByExample(TeacherExample example);

    int deleteByPrimaryKey(String tno);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    List<Teacher> selectByExample(TeacherExample example);

    Teacher selectByPrimaryKey(String tno);

    int updateByExampleSelective(@Param("record") Teacher record, @Param("example") TeacherExample example);

    int updateByExample(@Param("record") Teacher record, @Param("example") TeacherExample example);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    List<StudentSingleEntity> singleExportDoc(Map record);

    List<StudentMultiEntity> multiExportDoc(Map record);

    List<StudentJudgeEntity> judgeExportDoc(Map record);

    List<StudentFillEntity> fillExportDoc(Map record);

    List<StudentAnswerEntity> answerExportDoc(Map record);


}