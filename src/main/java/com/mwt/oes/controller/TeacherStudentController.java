package com.mwt.oes.controller;

import com.alibaba.fastjson.JSONObject;
import com.mwt.oes.dao.PaperMapper;
import com.mwt.oes.dao.StudentMapper;
import com.mwt.oes.dao.StudentPaperScoreMapper;
import com.mwt.oes.domain.*;
import com.mwt.oes.properties.Global;
import com.mwt.oes.service.StudentHomeService;
import com.mwt.oes.service.StudentSystemService;
import com.mwt.oes.service.TeacherStudentService;
import com.mwt.oes.util.FileUtil;
import com.mwt.oes.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/teacher")
public class TeacherStudentController {
    @Autowired
    private TeacherStudentService teacherStudentService;
    @Autowired
    private StudentSystemService studentSystemService;

    @Autowired
    private StudentHomeService studentHomeService;

    @Autowired
    private StudentPaperScoreMapper studentPaperScoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PaperMapper paperMapper;


    //    获取学生列表信息
    @RequestMapping("/getStudentsList")
    public ServerResponse getStudentsList() {
        List<Student> resultList = teacherStudentService.getStudentsList();
        return ServerResponse.createBySuccess("获取全部学生信息成功", resultList);
    }

    //    更新学生信息
    @RequestMapping(value = "/updateStudentInfo", method = RequestMethod.POST)
    public ServerResponse updateStudentInfo(@RequestBody(required = false) Student student) {
        int result = teacherStudentService.updateStudentInfo(student);
        if (result > 0) {
            return ServerResponse.createBySuccess("更新学生信息成功", null);
        } else {
            return ServerResponse.createByError("数据库错误，更新学生信息失败");
        }
    }

    //    获取搜索学生列表信息
    @RequestMapping("/searchStudentInfo")
    public ServerResponse searchStudentInfo(@RequestParam("sno") String sno,
                                            @RequestParam("stuName") String stuName,
                                            @RequestParam("stuSex") String stuSex) {
        List<Student> resultList = teacherStudentService.searchStudentInfo(sno, stuName, stuSex);
        return ServerResponse.createBySuccess("获取搜索学生信息成功", resultList);
    }

    //    添加学生信息
    @RequestMapping(value = "/getSno", method = RequestMethod.GET)
    public ServerResponse getSno() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> map = new HashMap<>();
        map.put("sno", fmt.format(new Date()));
        return ServerResponse.createBySuccess("自动生成学号成功", map);
    }


    //    添加学生信息
    @RequestMapping(value = "/insertStudentInfo", method = RequestMethod.POST)
    public ServerResponse insertStudentInfo(@RequestBody(required = false) Student student) {
        boolean isRegistered = studentSystemService.snoIsExist(student.getSno());
        if (isRegistered) {
            return ServerResponse.createByError("此学号已被注册");
        }

        student.setStuCreateTime(new Date());
        int result = teacherStudentService.insertStudentInfo(student);
        if (result > 0) {
            return ServerResponse.createBySuccess("插入学生信息成功", null);
        } else {
            return ServerResponse.createByError("数据库错误，插入学生信息失败");
        }
    }

    //    获取成绩列表信息
    @RequestMapping("/getScoresList")
    public ServerResponse getScoresList() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> scoresList = teacherStudentService.getScoresList();
        map.put("scoresList", scoresList);
        List<Map<String, Object>> papersList = teacherStudentService.getPapersList();
        map.put("papersList", papersList);
        return ServerResponse.createBySuccess("获取全部成绩信息成功", map);
    }

    //    获取成绩列表信息
    @RequestMapping("/exportScoresList")
    public ServerResponse exportScoresList(@RequestBody String json) {
        JSONObject jsonObject=JSONObject.parseObject(json);
        String sno = jsonObject.getString("sno");
        Integer paperId = jsonObject.getInteger("paperId");;
        //判断系统
        String os = System.getProperty("os.name");
        String sysFile = Global.getConfig("linux.project.path");
        /*if (os.toLowerCase().startsWith("win")) {
            sysFile = Global.getConfig("win.project.path");
        }*/
        String relativePath ="";
        String filepath = "bishi.xml";

        List<String> items = new ArrayList<>();

        List<String> data = new ArrayList<>();

        StudentPaperScoreExample studentPaperScoreExample = new StudentPaperScoreExample();
        StudentPaperScoreExample.Criteria criteria = studentPaperScoreExample.createCriteria();
        criteria.andSnoEqualTo(sno);
        criteria.andPaperIdEqualTo(paperId);
        List<StudentPaperScore> studentPaperScoreList = studentPaperScoreMapper.selectByExample(studentPaperScoreExample);

        if (studentPaperScoreList.get(0).getDocPath() != null && !"".equals(studentPaperScoreList.get(0).getDocPath())) {
            return ServerResponse.createBySuccess("导出成功", studentPaperScoreList.get(0).getDocPath());

        }


        Student student = studentMapper.selectByPrimaryKey(sno);

        Paper paper = paperMapper.selectByPrimaryKey(paperId);
        items.add("studenttitle");
        items.add("student");
        items.add("totalscore");
        data.add(paper.getPaperName());
        data.add(student.getStuName());
        data.add(studentPaperScoreList.get(0).getScore().toString());
        /*items.add("fill");
        items.add("judge");
        items.add("anwer");*/
        List<String> single = new ArrayList<>();
        Map<String, String> singleParam = new HashMap<>();
        singleParam.put("sno", sno);
        singleParam.put("paperId", "14");
        //单选题
        List<StudentSingleEntity> studentSingleEntities =
                teacherStudentService.singleExportDoc(singleParam);
        StringBuilder singStringBuilder = new StringBuilder();
        Integer rownum = 1;
        items.add("singleoes");
        try {
            if (studentSingleEntities != null && studentSingleEntities.size() > 0) {
                singStringBuilder.append("单选题:<w:p></w:p>");
                for (StudentSingleEntity studentSingleEntity : studentSingleEntities) {
                    singStringBuilder.append(rownum).append(".").append(studentSingleEntity.getSingleContent()).append("<w:p></w:p>");
                    if (studentSingleEntity.getPictureSrc() != null && !"".equals(studentSingleEntity.getPictureSrc())) {
                        singStringBuilder.append(FileUtil.addImgToDoc(FileUtil.getImageString(studentSingleEntity.getPictureSrc()),rownum)).append("<w:p></w:p>");
                    }
                    singStringBuilder.append(studentSingleEntity.getChoiceA()).append("<w:p></w:p>")
                            .append(studentSingleEntity.getChoiceB()).append("<w:p></w:p>")
                            .append(studentSingleEntity.getChoiceC()).append("<w:p></w:p>")
                            .append(studentSingleEntity.getChoiceD()).append("<w:p></w:p>");
                    if (studentSingleEntity.getChoiceE() != null && !"".equals(studentSingleEntity.getChoiceE())) {
                        singStringBuilder.append(studentSingleEntity.getChoiceE()).append("<w:p></w:p>");
                    }
                    if (studentSingleEntity.getChoiceF() != null && !"".equals(studentSingleEntity.getChoiceF())) {
                        singStringBuilder.append(studentSingleEntity.getChoiceF()).append("<w:p></w:p>");
                    }
                    if (studentSingleEntity.getChoiceG() != null && !"".equals(studentSingleEntity.getChoiceG())) {
                        singStringBuilder.append(studentSingleEntity.getChoiceG()).append("<w:p></w:p>");
                    }
                    singStringBuilder.append("正确答案是: ").append(studentSingleEntity.getSingleAnswer()).append("  考生答案是：").append(studentSingleEntity.getStuAnswer()).append("<w:p></w:p>")
                            .append("  考生得分：").append(studentSingleEntity.getScore()).append("<w:p></w:p>");
                    rownum = rownum + 1;
                }
                //替换换行和null
                data.add(singStringBuilder.toString()
                        .replaceAll("null", ""));
            } else {
                data.add("");
            }
            //多选题
            StringBuilder multiStringBuilder = new StringBuilder();
            List<StudentMultiEntity> studentMultiEntities =
                    teacherStudentService.multiExportDoc(singleParam);
            items.add("multioes");
            if (studentMultiEntities != null && studentMultiEntities.size() > 0) {
                multiStringBuilder.append("多选题:<w:p></w:p>");
                for (StudentMultiEntity studentMultiEntity : studentMultiEntities) {
                    multiStringBuilder.append(rownum).append(".").append(studentMultiEntity.getMultipleContent()).append("<w:p></w:p>");
                    if (studentMultiEntity.getPictureSrc() != null && !"".equals(studentMultiEntity.getPictureSrc())) {
                        multiStringBuilder.append(FileUtil.addImgToDoc(FileUtil.getImageString(studentMultiEntity.getPictureSrc()),rownum)).append("<w:p></w:p>");
                    }
                    multiStringBuilder.append(studentMultiEntity.getChoiceA()).append("<w:p></w:p>")
                            .append(studentMultiEntity.getChoiceB()).append("<w:p></w:p>")
                            .append(studentMultiEntity.getChoiceC()).append("<w:p></w:p>")
                            .append(studentMultiEntity.getChoiceD()).append("<w:p></w:p>");
                    if (studentMultiEntity.getChoiceE() != null && !"".equals(studentMultiEntity.getChoiceE())) {
                        multiStringBuilder.append(studentMultiEntity.getChoiceE()).append("<w:p></w:p>");
                    }
                    if (studentMultiEntity.getChoiceF() != null && !"".equals(studentMultiEntity.getChoiceF())) {
                        multiStringBuilder.append(studentMultiEntity.getChoiceF()).append("<w:p></w:p>");
                    }
                    if (studentMultiEntity.getChoiceG() != null && !"".equals(studentMultiEntity.getChoiceG())) {
                        multiStringBuilder.append(studentMultiEntity.getChoiceG()).append("<w:p></w:p>");
                    }
                    multiStringBuilder.append("正确答案是: ").append(studentMultiEntity.getMultipleAnswer()).append("  考生答案是：").append(studentMultiEntity.getStuAnswer())
                            .append("  考生得分：").append(studentMultiEntity.getScore()).append("<w:p></w:p>").append("<w:p></w:p>");
                    rownum = rownum + 1;
                }
                //替换换行和null
                data.add(multiStringBuilder.toString()
                        .replaceAll("null", "")
                );
            } else {
                data.add("");
            }

            //判断题
            //多选题
            StringBuilder judgeStringBuilder = new StringBuilder();
            List<StudentJudgeEntity> studentJudgeEntities =
                    teacherStudentService.judgeExportDoc(singleParam);
            items.add("judgeoes");
            if (studentJudgeEntities != null && studentJudgeEntities.size() > 0) {
                judgeStringBuilder.append("判断题:<w:p></w:p>");
                for (StudentJudgeEntity studentJudgeEntity : studentJudgeEntities) {
                    judgeStringBuilder.append(rownum).append(".").append(studentJudgeEntity.getJudgeContent()).append("<w:p></w:p>");
                    if (studentJudgeEntity.getPictureSrc() != null && !"".equals(studentJudgeEntity.getPictureSrc())) {
                        judgeStringBuilder.append(FileUtil.addImgToDoc(FileUtil.getImageString(studentJudgeEntity.getPictureSrc()),rownum)).append("<w:p></w:p>");
                    }
                    judgeStringBuilder.append("正确答案是: ").append(studentJudgeEntity.getStuAnswer()).append("<w:p></w:p>");
                    judgeStringBuilder.append("考生答案是：").append(studentJudgeEntity.getMultipleAnswer()).append("<w:p></w:p>");
                    judgeStringBuilder.append("考生得分：").append(studentJudgeEntity.getScore()).append("<w:p></w:p>").append("<w:p></w:p>");
                    rownum = rownum + 1;
                }
                //替换换行和null
                data.add(judgeStringBuilder.toString()
                        .replaceAll("null", "")
                );
            } else {
                data.add("");
            }

            //填空题
            StringBuilder fillStringBuilder = new StringBuilder();
            List<StudentFillEntity> studentFillEntities =
                    teacherStudentService.fillExportDoc(singleParam);

            items.add("filloes");
            if (studentFillEntities != null && studentFillEntities.size() > 0) {
                fillStringBuilder.append("填空题:<w:p></w:p>");
                for (StudentFillEntity studentFillEntity : studentFillEntities) {

                    fillStringBuilder.append(rownum).append(".").append(studentFillEntity.getFillContent()).append("<w:p></w:p>");
                    if (studentFillEntity.getPictureSrc() != null && !"".equals(studentFillEntity.getPictureSrc())) {
                        fillStringBuilder.append(FileUtil.addImgToDoc(FileUtil.getImageString(studentFillEntity.getPictureSrc()),rownum)).append("<w:p></w:p>");
                    }
                    fillStringBuilder.append("考生答案是：").append(studentFillEntity.getStuAnswer()).append("<w:p></w:p>");
                    fillStringBuilder.append("考生得分：").append(studentFillEntity.getScore()).append("<w:p></w:p>").append("<w:p></w:p>");
                    rownum = rownum + 1;
                }
                //替换换行和null
                data.add(fillStringBuilder.toString()
                        .replaceAll("null", ""));
            } else {
                data.add("");
            }


            //简答题
            StringBuilder answerStringBuilder = new StringBuilder();
            List<StudentAnswerEntity> studentAnswerEntities =
                    teacherStudentService.answerExportDoc(singleParam);
            items.add("answeroes");
            if (studentAnswerEntities != null && studentAnswerEntities.size() > 0) {
                answerStringBuilder.append("简答题:<w:p></w:p>");
                for (StudentAnswerEntity studentAnswerEntity : studentAnswerEntities) {
                    answerStringBuilder.append(rownum).append(".").append(studentAnswerEntity.getFillContent()).append("<w:p></w:p>");
                    if (studentAnswerEntity.getPictureSrc() != null && !"".equals(studentAnswerEntity.getPictureSrc())) {
                        answerStringBuilder.append(FileUtil.addImgToDoc(FileUtil.getImageString(studentAnswerEntity.getPictureSrc()),rownum)).append("<w:p></w:p>");
                    }
                    answerStringBuilder.append("考生答案是：").append(studentAnswerEntity.getStuAnswer()).append("<w:p></w:p>");
                    answerStringBuilder.append("考生得分：").append(studentAnswerEntity.getScore()).append("<w:p></w:p>").append("<w:p></w:p>");
                    rownum = rownum + 1;
                }
                //替换换行和null
                data.add(answerStringBuilder.toString()
                        .replaceAll("null", ""));
            } else {
                data.add("");
            }
            String fileName = student.getSno() + student.getStuName() + "面试题.doc";
            Map<String, String> tofilepath =
                    FileUtil.getFilePath(fileName);
            relativePath =
                    Global.getConfig("ip") + tofilepath.get("relativePath") + File.separator + fileName;
            FileUtil.changeFileText(filepath, tofilepath.get("absolutePath"), items, data);
            StudentPaperScore studentPaperScore = studentPaperScoreList.get(0);
            studentPaperScore.setDocPath(relativePath);
            studentPaperScoreMapper.updateByPrimaryKey(studentPaperScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.createBySuccess("导出成功", relativePath);
    }

    //  删除学生成绩
    @RequestMapping(value = "/deleteScore", method = RequestMethod.POST)
    public ServerResponse deleteScore(@RequestBody Map<String, Object> obj) {
        String sno = (String) obj.get("sno");
        Integer paperId = (Integer) obj.get("paperId");
        int result = teacherStudentService.deleteScore(sno, paperId);
        if (result > 0) {
            return ServerResponse.createBySuccess("删除成功", null);
        } else {
            return ServerResponse.createByError("数据库错误，删除失败");
        }
    }

    //    获取搜索成绩列表信息
    @RequestMapping("/searchScoresList")
    public ServerResponse searchScoresList(@RequestParam("sno") String sno,
                                           @RequestParam("paperId") Integer paperId) {
        List<Map<String, Object>> resultList = teacherStudentService.searchScoresList(sno, paperId);
        return ServerResponse.createBySuccess("获取搜索成绩信息成功", resultList);
    }

    //    获取全部已发布试卷信息
    @RequestMapping("/getPapersList")
    public ServerResponse getPapersList() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> papersList = teacherStudentService.getPapersList();
        map.put("papersList", papersList);
        return ServerResponse.createBySuccess("获取全部成绩信息成功", map);
    }

    // 获取成绩图标数据
    @RequestMapping("/getChartData")
    public ServerResponse getChartData(@RequestParam("paperId") Integer paperId) {
        Map<String, Object> resultMap = teacherStudentService.getChartData(paperId);
        return ServerResponse.createBySuccess("获取成绩图标数据成功", resultMap);
    }

    // 添加导入学生信息Excel文件
    @RequestMapping(value = "/insertStudentInfoList", method = RequestMethod.POST)
    public ServerResponse insertStudentInfoList(@RequestBody Map<String, Object> obj) {
        List<Map<String, Object>> studentList = (List<Map<String, Object>>) obj.get("studentList");
        Map<String, Object> result = teacherStudentService.insertStudentInfoList(studentList);
        return ServerResponse.createBySuccess("文件上传成功", result);
    }

    /*
      通过paperId获取试卷及单选题、多选题、判断题和填空题信息
   */
    @RequestMapping("/getPapersInfoByPaperIdAndStu")
    public ServerResponse getPapersInfoByPaperIdAndStu(@RequestParam("paperId") Integer paperId, @RequestParam("stuId") String sno) {
        Paper paper = studentHomeService.getPapersInfoByPaperId(paperId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("paperId", paperId);
        paramMap.put("sno", sno);
        Map<String, Integer> numObj = studentHomeService.getPaperQueNumByPaperId(paperId);
        List<Map<String, Object>> fillQueList =
                studentHomeService.getFillQueListByPaperIdAndSno(paramMap);
        List<Map<String, Object>> answerQueList =
                studentHomeService.getAnswerQueListByPaperIdAndSno(paramMap);
        if (paper != null && numObj != null) {
/*            List listResult = new ArrayList();
            listResult.add(paper);
            listResult.add(numObj);*/
            Map<String, Object> map = new HashMap<>();
            map.put("paperInfo", paper);
            map.put("queNumInfo", numObj);
            map.put("fillQueList", fillQueList);
            map.put("answerQueList", answerQueList);
            return ServerResponse.createBySuccess("试卷id为" + paperId + "的试卷信息获取成功", map);
        } else {
            return ServerResponse.createByError("试卷id为" + paperId + "的试卷信息获取失败");
        }
    }


    /*
      插入学生成绩表成绩信息和学生试卷答题记录信息
   */
    @RequestMapping(value = "/submitPaper", method = RequestMethod.POST)
    public ServerResponse submitPaper(@RequestBody Map<String, Object> obj) {

        //学号
        String sno = (String) obj.get("sno");
        String tno = (String) obj.get("tno");
        //试卷id
        Integer paperId = Integer.parseInt((String) obj.get("paperId"));
        //填空题答案数组
        List<String> fillScore = (List) obj.get("fillScores");
        List<String> answerScore = (List) obj.get("answerScores");

        int result = studentHomeService.updatePaperAnswerAndPaperScore(sno, tno,
                paperId, fillScore, answerScore);

        if (result == 0) {
            return ServerResponse.createByError("数据库错误，插入学生试卷答案记录表或学生成绩表失败");
        } else {
            return ServerResponse.createBySuccess("学号" + sno + "试卷id" + paperId + "交卷成功，数据已插入数据库并已自动计算出成绩", null);
        }
    }
}
