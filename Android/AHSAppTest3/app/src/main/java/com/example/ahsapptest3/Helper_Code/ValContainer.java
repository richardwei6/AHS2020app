package com.example.ahsapptest3.Helper_Code;

// https://stackoverflow.com/questions/5977735/setting-outer-variable-from-anonymous-inner-class
public class ValContainer<T> {
    private T val;

    public ValContainer() {
    }

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