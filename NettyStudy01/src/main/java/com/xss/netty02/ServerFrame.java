package com.xss.netty02;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @ClassName ServerFarme
 * @Description 服务端窗口
 * @Author xushaoshuai
 * @Parameters
 * @Date 2020/10/29 2:50 下午
 * @Return
 */
public class ServerFrame extends Frame {

    Button btnStart = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();

    public ServerFrame () {
        this.setSize(1600, 600);
        this.setLocation(300, 30);
        this.add(btnStart, BorderLayout.NORTH);
        Panel p = new Panel(new GridLayout(1, 2));
        p.add(taLeft);
        p.add(taRight);
        this.add(p);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new ServerFrame();
    }
}
