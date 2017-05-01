package io.jsql.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by 长宏 on 2017/4/30 0030.
 */
public class SocketTest {
    public static void main(String[] args) throws IOException {
        //为了简单起见，所有的异常都直接往外抛
        String host = "127.0.0.1";  //要连接的服务端IP地址
        int port = 8007;   //要连接的服务端对应的监听端口
        //与服务端建立连接
        Socket client = new Socket(host, port);
        //建立连接后就可以往服务端写数据了
        PrintWriter writer = new PrintWriter(client.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        writer.write("hello" + "\n");
        writer.flush();//写完后要记得flush

        System.out.println(reader.readLine());
        try {
            for (; ; ) {

                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                writer.write(s + "\n");
                writer.flush();
                System.out.println(reader.readLine());
            }
        } finally {
            writer.close();
            client.close();
        }


    }
}
