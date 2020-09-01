package com.hsappdev.ahs.misc;

// https://stackoverflow.com/questions/5977735/setting-outer-variable-from-anonymous-inner-class
public class ValContainer<T> {
    private T val;

// --Commented out by Inspection START (8/31/2020 11:34 AM):
//    public ValContainer() {
//    }
// --Commented out by Inspection STOP (8/31/2020 11:34 AM)

    public ValContainer(T v) {
        this.val = v;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }
}