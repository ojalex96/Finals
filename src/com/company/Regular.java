package com.company;

import java.util.Vector;

public class Regular extends Savings implements Compound_Interest{
    public Regular(long CustNo,String CustName,double initialDep,int nyears,String savtype){
        super(CustNo,CustName,initialDep,nyears,savtype);
    }
    public void generateTable(){
        double starting,interest,ending;
        Vector v2=new Vector();
        for (int i =0;i<nyears;i++){
        }
    }
}
