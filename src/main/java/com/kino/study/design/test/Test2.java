package com.kino.study.design.test;

/**
 * @author kino
 * @date 2024/6/14 13:43
 */
public class Test2 {

}

interface Flyable {
    void fly();
}

class FlyAbility implements Flyable {
    @Override public void fly() {
        //...
    }
}

interface Tweetable {
    void tweet();
}

class TweetAbility implements Tweetable {
    @Override
    public void tweet() {

    }
}
interface EggLayable {
    default void layEgg(){

    }
}

class EggLayAbility implements EggLayable {
    @Override
    public void layEgg() {

    }
}

class Ostrich implements Tweetable, EggLayable { //鸵鸟
    private TweetAbility tweetAbility = new TweetAbility(); //组合
    private EggLayAbility eggLayAbility = new EggLayAbility(); //组合
    @Override
    public void tweet() {
        tweetAbility.tweet(); // 委托
    }
    @Override
    public void layEgg() {
        eggLayAbility.layEgg(); // 委托
    }
}