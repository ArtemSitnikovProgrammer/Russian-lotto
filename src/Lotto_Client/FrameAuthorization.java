package Lotto_Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FrameAuthorization extends JFrame {
    JPanel panelBack,panelauthorization;
    JLabel labelName,labelPassword;
    JTextField textFieldName;
    JPasswordField passwordFieldPassword;
    JButton enter,close;

    FrameAuthorization(){
        super();
        setSize(600, 300);
        setLocation(500,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelBack = new JPanel(new GridBagLayout()) {
            public void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("images/Заставка2.jpg")), 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error image Entrance");
                }
            }
        };

        panelauthorization = new JPanel(new GridLayout(3, 2, 5, 0));
        panelauthorization.setBorder(new TitledBorder("Авторизация"));
        labelName = new JLabel("Имя: ");
        labelPassword = new JLabel("Пароль: ");
        textFieldName = new JTextField();
        passwordFieldPassword = new JPasswordField();
        enter = new JButton("Войти");
        close = new JButton("Закрыть");

        panelauthorization.add(labelName);
        panelauthorization.add(textFieldName);
        panelauthorization.add(labelPassword);
        panelauthorization.add(passwordFieldPassword);
        panelauthorization.add(enter);
        panelauthorization.add(close);
        panelauthorization.setOpaque(false);

        panelBack.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady     = 20;    // установить первоначальный размер кнопки
        constraints.insets    = new Insets(100, -350, 0, -50);  // граница ячейки по Y
        panelBack.add(panelauthorization,constraints);

        add(panelBack, BorderLayout.CENTER);



        // Обработчики кнопок ОК и Отмена.
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Habitat.Authorization();
                setVisible(false);
            }
        });

        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Client.meAuthorizationClose();
                setVisible(false);
            }
        });
        setVisible(true);
    }
}
