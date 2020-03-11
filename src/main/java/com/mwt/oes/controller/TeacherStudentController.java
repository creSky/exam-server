package com.mwt.oes.controller;

import com.mwt.oes.domain.Paper;
import com.mwt.oes.domain.Student;
import com.mwt.oes.service.StudentHomeService;
import com.mwt.oes.service.StudentSystemService;
import com.mwt.oes.service.TeacherStudentService;
import com.mwt.oes.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //    获取学生列表信息
    @RequestMapping("/getStudentsList")
    public ServerResponse getStudentsList(){
        List<Student> resultList = teacherStudentService.getStudentsList();
        return ServerResponse.createBySuccess("获取全部学生信息成功",resultList);
    }
    //    更新学生信息
    @RequestMapping(value = "/updateStudentInfo",method = RequestMethod.POST)
    public ServerResponse updateStudentInfo(@RequestBody(required = false)Student student){
        int result = teacherStudentService.updateStudentInfo(student);
        if (result > 0){
            return ServerResponse.createBySuccess("更新学生信息成功",null);
        }
        else {
            return ServerResponse.createByError("数据库错误，更新学生信息失败");
        }
    }

    //    获取搜索学生列表信息
    @RequestMapping("/searchStudentInfo")
    public ServerResponse searchStudentInfo(@RequestParam("sno")String sno,
                                            @RequestParam("stuName")String stuName,
                                            @RequestParam("stuSex")String stuSex){
        List<Student> resultList = teacherStudentService.searchStudentInfo(sno, stuName, stuSex);
        return ServerResponse.createBySuccess("获取搜索学生信息成功",resultList);
    }

    //    添加学生信息
    @RequestMapping(value = "/getSno",method = RequestMethod.GET)
    public ServerResponse getSno(){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String,String> map=new HashMap<>();
        map.put("sno",fmt.format(new Date()));
        return ServerResponse.createBySuccess("自动生成学号成功",map);
    }


    //    添加学生信息
    @RequestMapping(value = "/insertStudentInfo",method = RequestMethod.POST)
    public ServerResponse insertStudentInfo(@RequestBody(required = false)Student student){
        boolean isRegistered = studentSystemService.snoIsExist(student.getSno());
        if(isRegistered){
            return ServerResponse.createByError("此学号已被注册");
        }

        student.setStuCreateTime(new Date());
        int result = teacherStudentService.insertStudentInfo(student);
        if (result > 0){
            return ServerResponse.createBySuccess("插入学生信息成功",null);
        }
        else {
            return ServerResponse.createByError("数据库错误，插入学生信息失败");
        }
    }

    //    获取成绩列表信息
    @RequestMapping("/getScoresList")
    public ServerResponse getScoresList(){
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> scoresList = teacherStudentService.getScoresList();
        map.put("scoresList",scoresList);
        List<Map<String, Object>> papersList = teacherStudentService.getPapersList();
        map.put("papersList",papersList);
        return ServerResponse.createBySuccess("获取全部成绩信息成功",map);
    }

    //  删除学生成绩
    @RequestMapping(value = "/deleteScore",method = RequestMethod.POST)
    public ServerResponse deleteScore(@RequestBody Map<String, Object> obj){
        String sno = (String) obj.get("sno");
        Integer paperId = (Integer) obj.get("paperId");
        int result = teacherStudentService.deleteScore(sno, paperId);
        if(result > 0){
            return ServerResponse.createBySuccess("删除成功",null);
        } else {
            return ServerResponse.createByError("数据库错误，删除失败");
        }
    }

    //    获取搜索成绩列表信息
    @RequestMapping("/searchScoresList")
    public ServerResponse searchScoresList(@RequestParam("sno")String sno,
                                            @RequestParam("paperId")Integer paperId){
        List<Map<String, Object>> resultList = teacherStudentService.searchScoresList(sno, paperId);
        return ServerResponse.createBySuccess("获取搜索成绩信息成功",resultList);
    }

    //    获取全部已发布试卷信息
    @RequestMapping("/getPapersList")
    public ServerResponse getPapersList(){
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> papersList = teacherStudentService.getPapersList();
        map.put("papersList",papersList);
        return ServerResponse.createBySuccess("获取全部成绩信息成功",map);
    }

    // 获取成绩图标数据
    @RequestMapping("/getChartData")
    public ServerResponse getChartData(@RequestParam("paperId")Integer paperId){
        Map<String, Object> resultMap = teacherStudentService.getChartData(paperId);
        return ServerResponse.createBySuccess("获取成绩图标数据成功",resultMap);
    }

    // 添加导入学生信息Excel文件
    @RequestMapping(value = "/insertStudentInfoList",method = RequestMethod.POST)
    public ServerResponse insertStudentInfoList(@RequestBody Map<String, Object> obj){
        List<Map<String, Object>> studentList = (List<Map<String, Object>>) obj.get("studentList");
        Map<String, Object> result = teacherStudentService.insertStudentInfoList(studentList);
        return ServerResponse.createBySuccess("文件上传成功",result);
    }

    /*
      通过paperId获取试卷及单选题、多选题、判断题和填空题信息
   */
    @RequestMapping("/getPapersInfoByPaperIdAndStu")
    public ServerResponse getPapersInfoByPaperIdAndStu(@RequestParam("paperId")Integer paperId,@RequestParam("stuId")String sno ){
        Paper paper = studentHomeService.getPapersInfoByPaperId(paperId);
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("paperId",paperId);
        paramMap.put("sno",sno);
        Map<String, Integer> numObj = studentHomeService.getPaperQueNumByPaperId(paperId);
        List<Map<String, Object>> fillQueList =
                studentHomeService.getFillQueListByPaperIdAndSno(paramMap);
        List<Map<String, Object>> answerQueList =
                studentHomeService.getAnswerQueListByPaperIdAndSno(paramMap);
        if(paper != null && numObj != null){
/*            List listResult = new ArrayList();
            listResult.add(paper);
            listResult.add(numObj);*/
            Map<String, Object> map = new HashMap<>();
            map.put("paperInfo",paper);
            map.put("queNumInfo",numObj);
            map.put("fillQueList",fillQueList);
            map.put("answerQueList",answerQueList);
            return ServerResponse.createBySuccess("试卷id为" + paperId + "的试卷信息获取成功",map);
        }
        else {
            return ServerResponse.createByError("试卷id为" + paperId + "的试卷信息获取失败");
        }
    }


    /*
      插入学生成绩表成绩信息和学生试卷答题记录信息
   */
    @RequestMapping(value = "/submitPaper", method = RequestMethod.POST)
    public ServerResponse submitPaper(@RequestBody Map<String, Object> obj){

        //学号
        String sno = (String) obj.get("sno");
        String tno = (String) obj.get("tno");
        //试卷id
        Integer paperId = Integer.parseInt((String) obj.get("paperId"));
        //填空题答案数组
        List<String> fillScore=(List) obj.get("fillScores");
        List<String> answerScore = (List) obj.get("answerScores");

        int result = studentHomeService.updatePaperAnswerAndPaperScore(sno,tno,
                paperId,fillScore,answerScore);

        if(result == 0){
            return ServerResponse.createByError("数据库错误，插入学生试卷答案记录表或学生成绩表失败");
        }
        else {
            return ServerResponse.createBySuccess("学号" + sno + "试卷id" + paperId + "交卷成功，数据已插入数据库并已自动计算出成绩",null);
        }
    }
}
