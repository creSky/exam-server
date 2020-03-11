package com.mwt.oes.domain;

import java.util.Date;

public class Paper {
    private Integer paperId;

    private String paperName;

    private Date paperCreateTime;

    private Integer paperDuration;

    private Integer paperDifficulty;

    private String paperAttention;

    private Integer paperType;

    private Integer singleScore;

    private Integer singleNum;

    private Integer multipleScore;

    private Integer multipleNum;

    private Integer judgeScore;

    private Integer judgeNum;

    private Integer fillScore;

    private Integer fillNum;

    private Integer answerScore;

    private Integer answerNum;

    private Integer langId;

    private Integer participateNum;

    private Integer visible;

    private Integer pid;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(Integer singleNum) {
        this.singleNum = singleNum;
    }

    public Integer getMultipleNum() {
        return multipleNum;
    }

    public void setMultipleNum(Integer multipleNum) {
        this.multipleNum = multipleNum;
    }

    public Integer getJudgeNum() {
        return judgeNum;
    }

    public void setJudgeNum(Integer judgeNum) {
        this.judgeNum = judgeNum;
    }

    public Integer getFillNum() {
        return fillNum;
    }

    public void setFillNum(Integer fillNum) {
        this.fillNum = fillNum;
    }

    public Integer getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(Integer answerNum) {
        this.answerNum = answerNum;
    }

    public Integer getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(Integer answerScore) {
        this.answerScore = answerScore;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName == null ? null : paperName.trim();
    }

    public Date getPaperCreateTime() {
        return paperCreateTime;
    }

    public void setPaperCreateTime(Date paperCreateTime) {
        this.paperCreateTime = paperCreateTime;
    }

    public Integer getPaperDuration() {
        return paperDuration;
    }

    public void setPaperDuration(Integer paperDuration) {
        this.paperDuration = paperDuration;
    }

    public Integer getPaperDifficulty() {
        return paperDifficulty;
    }

    public void setPaperDifficulty(Integer paperDifficulty) {
        this.paperDifficulty = paperDifficulty;
    }

    public String getPaperAttention() {
        return paperAttention;
    }

    public void setPaperAttention(String paperAttention) {
        this.paperAttention = paperAttention == null ? null : paperAttention.trim();
    }

    public Integer getPaperType() {
        return paperType;
    }

    public void setPaperType(Integer paperType) {
        this.paperType = paperType;
    }

    public Integer getSingleScore() {
        return singleScore;
    }

    public void setSingleScore(Integer singleScore) {
        this.singleScore = singleScore;
    }

    public Integer getMultipleScore() {
        return multipleScore;
    }

    public void setMultipleScore(Integer multipleScore) {
        this.multipleScore = multipleScore;
    }

    public Integer getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(Integer judgeScore) {
        this.judgeScore = judgeScore;
    }

    public Integer getFillScore() {
        return fillScore;
    }

    public void setFillScore(Integer fillScore) {
        this.fillScore = fillScore;
    }

    public Integer getLangId() {
        return langId;
    }

    public void setLangId(Integer langId) {
        this.langId = langId;
    }

    public Integer getParticipateNum() {
        return participateNum;
    }

    public void setParticipateNum(Integer participateNum) {
        this.participateNum = participateNum;
    }

}