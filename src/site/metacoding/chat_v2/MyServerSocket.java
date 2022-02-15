package site.metacoding.chat_v2;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServerSocket {

    // 리스너 (연결받기)- 메인스레드
    ServerSocket serverSocket;
    Socket socket;
    List<고객전담스레드> 고객리스트;

    // 서버는 메세지 받아서 보내기 (클라이언트 수마다)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            고객리스트 = new ArrayList<>();
            while (true) { // 한번받고 끝 x
                Socket socket = serverSocket.accept();// main스레드
                System.out.println("클라이언트 연결됨");
                고객전담스레드 t = new 고객전담스레드(socket);
                고객리스트.add(t);
                System.out.println("고객리스트 크기 :" + 고객리스트.size());
                new Thread(t).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 내부클래스
    class 고객전담스레드 implements Runnable {
        Socket socket;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;
        }
        // 버퍼

        @Override
        public void run() {

        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}
