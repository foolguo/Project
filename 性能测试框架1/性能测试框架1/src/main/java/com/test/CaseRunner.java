package com.test;

import com.test.Annotations.BenchMark;
import com.test.Annotations.Measurement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class CaseRunner {
    private List<Case> list;

    public CaseRunner(List<Case> list) {
        this.list = list;
    }
    public void run() throws InvocationTargetException, IllegalAccessException {
        int iteration=10,group=5;
        for(Case cases:list){
            Class<Case> cls= (Class<Case>) cases.getClass();
            Measurement measurement = cls.getAnnotation(Measurement.class);
            int it=iteration,g=group;
            if(measurement!=null) {
                it = measurement.iteration();
                g = measurement.group();
            }
            Method[] methods=cls.getMethods();
            run(methods,cases,it,g);
        }
    }
    private void run(Method[] methods,Case cases,int it,int g ) throws InvocationTargetException, IllegalAccessException {

        for(Method method:methods) {
            BenchMark benchMark1 = method.getAnnotation(BenchMark.class);
            if (benchMark1 == null) {
                continue;
            }
            Measurement measurement1 = method.getAnnotation(Measurement.class);
            if (measurement1 != null) {
                it = measurement1.iteration();
                g = measurement1.group();
            }
            for (int i = 0; i < g; i++) {
                System.out.println("第" + i + "组");
                long start = System.currentTimeMillis();
                for (int j = 0; j < it; j++) {
                    method.invoke(cases);
                }
                long end = System.currentTimeMillis();
                System.out.println("花费" + (end - start + 1)+"ms");
            }
        }

    }
}
