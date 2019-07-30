package com.test;

import com.test.Cases.Sort;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
        CaseLoader caseLoader=new CaseLoader();
        CaseRunner caseRunner=caseLoader.load();
            caseRunner.run();
    }
}
