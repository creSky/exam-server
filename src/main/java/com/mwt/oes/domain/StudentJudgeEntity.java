package com.mwt.oes.domain;

public class StudentJudgeEntity {

    private String judgeContent;

    private String stuAnswer;

    private String score;

    private String multipleAnswer;

    private String pictureSrc;

    public String getPictureSrc() {
        return pictureSrc;
    }

    public String getJudgeContent() {
        return judgeContent== null ? null :judgeContent.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setJudgeContent(String judgeContent) {
        this.judgeContent = judgeContent;
    }

    public String getStuAnswer() {
        return stuAnswer== null ? null :stuAnswer.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setStuAnswer(String stuAnswer) {
        this.stuAnswer = stuAnswer;
    }

    public String getScore() {
        return score== null ? null :score.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMultipleAnswer() {
        return multipleAnswer== null ? null :multipleAnswer.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setMultipleAnswer(String multipleAnswer) {
        this.multipleAnswer = multipleAnswer;
    }
}
