package com.mwt.oes.domain;

public class StudentFillEntity {

    private String fillContent;

    private String stuAnswer;

    private String score;

    private String pictureSrc;

    public String getPictureSrc() {
        return pictureSrc;
    }

    public String getFillContent() {
        return fillContent== null ? null :fillContent.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setFillContent(String fillContent) {
        this.fillContent = fillContent;
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
}
