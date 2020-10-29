package com.xss.netty02;

/**
 * @ClassName ClientFrame
 * @Description 客户端窗口
 * @Author xushaoshuai
 * @Parameters
 * @Date 2020/10/29 1:48 下午
 * @Return
 */
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame extends Frame {

    public static final ClientFrame INSTANCE = new ClientFrame();


    TextArea ta = new TextArea();
    TextField tf = new TextField();
    Client c = null;
    private ClientFrame() {
        this.setSize(600, 400);
        this.setLocation(100, 20);
        this.add(ta, BorderLayout.CENTER);
        this.add(tf, BorderLayout.SOUTH);
        tf.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                c.send(tf.getText());
                String text = tf.getText();
                if(text.equals("bye")){
                    System.exit(0);
                }
                //ta.setText(ta.getText() + tf.getText());
                tf.setText("");
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                c.closeClient();
                System.exit(0);
            }
        });

    }

    private void connectToServer() {
        c = new Client();
        c.connect();
    }


    public static void main(String[] args) {
        ClientFrame clientFrame = ClientFrame.INSTANCE;
        clientFrame.setVisible(true);
        clientFrame.connectToServer();
    }
    public void updateText(String msgAccepted) {
        this.ta.setText(ta.getText() + System.getProperty("line.separator") + msgAccepted);
    }
}