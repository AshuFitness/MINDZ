package com.mindzglobal.www.mindz.FriendRequest.global;



public class QuestionAnswerModel {
    String question,answer;
    boolean open;

    public QuestionAnswerModel(String question, String answer, boolean open) {
        this.question = question;
        this.answer = answer;
        this.open=open;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
