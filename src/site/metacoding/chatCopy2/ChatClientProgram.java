package site.metacoding.chatCopy2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatServerProgram extends JFrame {
    // gui
    JPanel northPanel, southPanel;
    TextArea chatList;
    ScrollPane scroll;
    JTextField txtHost, txtPort, txtMsg;
    JButton btnConnect, btnSend;

    // 통신
    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;

    public ChatClientProgram(){
        initUI();
        initListener();

        setVisible(true);
    }

    private void initUI(){
        //1.상단디자인
        northPanel = new JPanel();
        txtHost = new JTextField(20);
        txtHost.setText("127.0.0.1");
        txtPort = new JTextField(5);
        txtPort.setText("2000");
        btnConnect = new JButton("Connect");

        northPanel.add(txtHost);
        northPanel.add(txtPort);
        northPanel.add(btnConnect);
        
        //2.센터디자인
        chatList = new TextArea(10, 30);
        chatList.setBackground(Color.ORANGE);
        chatList.setForeGround(Color.BLUE);
        scroll = new ScrollPane();
        scroll.add(chatList);

        //3.하단디자인
        southPanel = new JPanel();
        txtMsg = new JTextField(25);
        btnSend = new JButton("send");
        southPanel.add(txtMsg);
        southPanel.add(btnSend);

        //4.프레임 세팅
        setTitle("MyChat1.0");
        setSize(400,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(northPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }
    private void connect(){
        try {
            //1.host 주소와 port가져오기
            String host = txtHost.getText();
            int port = Integer.parseInt(txtPort.getText());

            //2.서버 연결하고 버퍼달기
            socket = new Socket(host, port);
            writer = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader ( new InputStreamReader(socket.getInputStream()));

            //3.읽기 전용 스레드 만들기
            new Thread(()->{
                read();
            }).start();

            System.out.println("서버 연결이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //메세지 전송 : 메인스레드
    private void send(){
        try{
            String msg = txtMsg.getText();
            chatList.append(msg + "\n");
            txtMsg.setText(""); // 비우기
            txtMsg.requestFocus(); //커서두기

            writer.write(msg + "\n");
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

        }
        //메세지 읽기 : 새로운 스레드
        private void read(){
            try {
                while(true){
                    String msg = reader.readLine();
                    chatList.append("[받음]"+msg+"\n");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void main(String[] args) {
            new ChatClientProgram();
        }
    }
}
