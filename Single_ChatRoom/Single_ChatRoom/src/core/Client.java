import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author: yd
 * @Date: 2019/5/13 16:45
 * @Version 1.0
 */
/*客户端*/
//    1.客户端的创建规则：
//        1.首先要有一个读线程（用来读取服务器发送的数据）；
//        2.要有一个写线程，负责向服务器发送信息已经关闭客户端


//1.读线程：
//2.写线程：
class Read implements Runnable{
    private Socket socket;

    public Read(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Scanner scanner= null;
        try {
            scanner = new Scanner(socket.getInputStream());
            while(true){
                System.out.println("等待客户端发来的消息");
                String str=null;
                if(scanner.hasNext()){
                    System.out.println(scanner.next());
                }
                if(socket.isClosed()){
                    break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
 class Write implements Runnable{
    private Socket socket;

    public Write(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            PrintStream out=new PrintStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            String msg=null;
            while(true) {
                if(scanner.hasNext()) {
                    msg = scanner.next();
                }
                out.println(msg);
                if(msg.contains("Bye")){
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
public class Client{
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("127.0.0.1",6666);
        new Thread(new Write(socket)).start();
        new Thread(new Read(socket)).start();

    }
}