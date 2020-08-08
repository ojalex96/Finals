package com.company;

public class Savings {
    long CustNo;
    String CustName;
    double initialDep;
    int nyears;
    String savtype;
    public Savings(long CustNo,String CustName,double initialDep,int nyears,String savtype){
        this.CustNo=CustNo;
        this.CustName=CustName;
        this.initialDep=initialDep;
        this.nyears=nyears;
        this.savtype=savtype;
    }
}
