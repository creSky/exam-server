package com.mwt.oes.domain;

public class StudentMultiEntity {
    private String multipleContent;

    private String choiceA;

    private String choiceB;

    private String choiceC;

    private String choiceD;

    private String choiceE;

    private String choiceF;

    private String choiceG;

    private String multipleAnswer;

    private String stuAnswer;

    private Integer score;

    private String pictureSrc;

    public String getPictureSrc() {
        return pictureSrc;
    }

    public String getMultipleContent() {
        return multipleContent== null ? null :multipleContent.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setMultipleContent(String multipleContent) {
        this.multipleContent = multipleContent;
    }

    public String getChoiceA() {
        return choiceA== null ? null :choiceA.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB== null ? null :choiceB.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC== null ? null :choiceC.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD== null ? null :choiceD.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getChoiceE() {
        return choiceE== null ? null :choiceE.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setChoiceE(String choiceE) {
        this.choiceE = choiceE;
    }

    public String getChoiceF() {
        return choiceF== null ? null :choiceF.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setChoiceF(String choiceF) {
        this.choiceF = choiceF;
    }

    public String getChoiceG() {
        return choiceG== null ? null :choiceG.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setChoiceG(String choiceG) {
        this.choiceG = choiceG;
    }

    public String getMultipleAnswer() {
        return multipleAnswer== null ? null :multipleAnswer.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setMultipleAnswer(String multipleAnswer) {
        this.multipleAnswer = multipleAnswer;
    }

    public String getStuAnswer() {
        return stuAnswer== null ? null :stuAnswer.replaceAll("<", " &lt;")
                .replaceAll(">", " &gt;").replaceAll("\n", "<w:p></w:p>");
    }

    public void setStuAnswer(String stuAnswer) {
        this.stuAnswer = stuAnswer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
