import jdk.nashorn.internal.runtime.regexp.joni.Matcher;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.*;




/**
 * @Author: yd
 * @Date: 2019/5/13 17:01
 * @Version 1.0
 */
//存储用户信息的Hash表
//创建套接字
//创建内部实现分发的功能
public class Services {
    private static Map<String,Socket> map=new ConcurrentHashMap<String, Socket>();
    static private class RealServices implements Runnable{
        private Socket socket;

        public RealServices(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                Scanner in=new Scanner(socket.getInputStream());
                String msg=null;
                while(true){
                    if(in.hasNext()) {
                        msg = in.next();
                    }
                    Pattern pattern = Pattern.compile("\r");
                  java.util.regex.Matcher matcher = pattern.matcher(msg);
                  if(msg.startsWith("userName")){
                      String username=msg.split(":")[1];
                      register(username);
                        continue;
                  }
                  if(msg.startsWith("G")){
                      String send=msg.split(":")[1];
                      group(send);
                  }
                  if (msg.startsWith("P")){
                      String username=msg.split(":")[1].split("\\-")[0];
                      String send=msg.split("\\-")[1];
                      privateSend(username,send);
                  }
                  if(msg.contains("Bye")){
                      Set<Map.Entry<String, Socket>> entries = map.entrySet();
                      Iterator<Map.Entry<String,Socket>> iterator=entries.iterator();
                      String username=null;
                      Socket socket=null;
                      while(iterator.hasNext()){
                          Map.Entry<String,Socket> i=iterator.next();
                          username=i.getKey();
                           socket=i.getValue();
                          if(socket==this.socket){
                              iterator.remove();
                          }
                      }
                      group(username+"退出群聊,当前聊天室还剩"+map.size());
                      System.out.println(username+"退出群聊");
                  }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        private void register(String username) throws IOException {
            if(map.containsKey(username)){
                System.out.println("当前用户已存在");
            }else {
                map.put(username,socket);
                group("欢迎"+username+"加入聊天室，当前聊天室人数:"+map.size());
                System.out.println("欢迎"+username+"加入聊天室，当前聊天室人数:"+map.size());

            }
        }

        private  void group(String send) throws IOException {
            Set<Map.Entry<String, Socket>> entries = map.entrySet();
            Iterator<Map.Entry<String,Socket>> iterator=entries.iterator();
            while (iterator.hasNext()){
                Map.Entry<String,Socket> i=iterator.next();
                Socket socket=i.getValue();
                if(socket==this.socket){
                    continue;
                }
                PrintStream printStream=new PrintStream(socket.getOutputStream());
                printStream.println(send);
            }

        }
        private  void privateSend(String username, String send) throws IOException {
            Socket to=map.get(username);
            if(to==null){
                PrintStream out=new PrintStream(socket.getOutputStream());
                out.println("当前用户不存在");
            }else {
                PrintStream out=new PrintStream(to.getOutputStream());
                out.println("发送信息： "+send);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        ServerSocket serverSocket = new ServerSocket(6666);
        for (int i = 0 ; i < 20 ; i++) {
            System.out.println("等待客户端连接...");
            Socket client = serverSocket.accept();
            System.out.println("有新的客户端连接，端口号为: "+client.getPort());
            executorService.submit(new RealServices(client));
        }
            executorService.shutdown();
        serverSocket.close();
    }

}
