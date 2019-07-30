package com.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CaseLoader {
    public CaseRunner load() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String pkg="com\\test\\Cases";
        String pkgDot="com.test.Cases";
        //通过反射和类加载器拿到文件的绝对路径
        Enumeration<URL> url = this.getClass().getClassLoader().getResources(pkg);
        List<String>  caseName=new ArrayList<String>();
        while(url.hasMoreElements()){
            URL url1=url.nextElement();
            //获取路径类型
            String protocol = url1.getProtocol();
            if(!"file".equals(protocol)){
               continue;
            }
            //将路径进行解码操作
            String name1=URLDecoder.decode(url1.getPath(),"UTF-8");
            //获取文件操作类
            File file=new File(name1);
            //获取文件下的列表
            File[] files=file.listFiles();
            for (File i:files){
                //如果这个文件是目录直接跳出
                if(i.isDirectory()){
                    continue;
                }
                //获取文件名
                String name=i.getName();
                if(i.isFile()){
                    //将文件名存入caseName中
                    caseName.add(name.substring(0,name.length()-6));
                }
            }
        }
        //获取实例对象
        List<Case> listCase=new ArrayList<Case>();
        for(String i:caseName){
            if(hasIntfCase(i,Case.class)){
                Class<?> cls=Class.forName(pkgDot+"."+i);
                listCase.add((Case) cls.newInstance());
            }
        }
        return new CaseRunner(listCase);

    }

    private boolean hasIntfCase(String i, Class<Case> caseClass) throws ClassNotFoundException {
        Class<?> cls=Class.forName("com.test.Cases."+i);
        Class<?>[] interfaces = cls.getInterfaces();
        for(Class<?> j:interfaces)
        if(j==caseClass){
            return true;
        }
        return false;
    }
}
