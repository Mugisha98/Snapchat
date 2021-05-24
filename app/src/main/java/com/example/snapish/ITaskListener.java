package com.example.snapish;

public interface ITaskListener {
    //står for at vores bytes ryger det rigtige sted hen
    public void receive(byte[] bytes);
}
