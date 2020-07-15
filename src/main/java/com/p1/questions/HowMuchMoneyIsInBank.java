package com.p1.questions;

public class HowMuchMoneyIsInBank {
    public float answer;

    public HowMuchMoneyIsInBank() {
    }

    public HowMuchMoneyIsInBank(float answer) {
        this.answer = answer;
    }

    public HowMuchMoneyIsInBank answered(float answer){
        return new HowMuchMoneyIsInBank(answer);
    }
}
