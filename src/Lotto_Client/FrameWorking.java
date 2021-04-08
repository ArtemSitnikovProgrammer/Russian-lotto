package Lotto_Client;

import Lotto_Server.MoveBochonok;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;


public class FrameWorking extends JFrame {

    Thread drawBochonok;
    boolean booldrawing = true;
    //Панель симуляции

    Font font_bochonok = new Font("Courier", Font.BOLD, 25),
         font1 = new Font("Monaco", Font.ITALIC, 50),
         font2 = new Font("Arial", Font.ITALIC, 50);
    Font fontArea1 = new Font("Courier", Font.ITALIC, 40);
    Font fontLabl = new Font("Courier", Font.ITALIC, 10);


    //Панель Меню
    JMenuBar panelMenu=new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem startItem = new JMenuItem("Start");
    JMenuItem stopItem = new JMenuItem("Stop");
    JMenuItem saveAllItem = new JMenuItem("Save All");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem exitItem = new JMenuItem("Exit");

    //все панели
    JPanel panelParametr=new JPanel(){
        public void paintComponent(Graphics g) {
            try {
                g.drawImage(ImageIO.read(new File("images/Фон стола1.jpg")), -10, -10, getWidth()+20, getHeight()+20, null);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error image Entrance");
            }

        }
    };

    JPanel panelInf = new JPanel(new GridBagLayout()){
        public void paintComponent(Graphics g) {
            try {
                g.drawImage(ImageIO.read(new File("images/Фон стола1.jpg")), -50, -10, getWidth()+100, getHeight()+20, null);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error image Entrance");
            }
        }
    };
    MyPanel panelWorking = new MyPanel();
    MyPanel panelPlayer = new MyPanel();

    public static Dialog_table dialog_table;
    JButton buttonStart, buttonRegulations, buttonCreate_table, buttonFind_table;
    JTextArea areaHelp = new JTextArea("Подсказки");
    JTextArea areaListClient = new JTextArea();
    JScrollPane jspList = new JScrollPane(areaListClient, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    JScrollPane jspHelp = new JScrollPane(areaHelp, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    public static String table_name, table_mode;
    public static int table_rate;
    boolean FLAG = true;
    public static int value_barrel;


    public FrameWorking() {
        super("Русское лото");

        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //размеры панелей
        panelParametr.setPreferredSize(new Dimension(150, getHeight()));
        panelParametr.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GREEN, 4, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        panelInf.setPreferredSize(new Dimension(getWidth(), 120));
        panelInf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GREEN, 4, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        panelWorking = new MyPanel(){
            public void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("images/Фон Стола2.jpg")), 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error image Entrance");
                }

            }
        };

        panelPlayer = new MyPanel(){
            @Override
            public void paintComponent(Graphics g) {

                g.setColor(Color.BLACK);
                g.setFont(new Font("Courier", Font.TYPE1_FONT, 30));
                g.drawString("Игрок ", 5, 80);
            }
        };

        panelPlayer.setPreferredSize(new Dimension(100, 150));
        panelPlayer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        //инициализация всех объектов фрейма
        buttonStart = new JButton("Играть");
        buttonRegulations = new JButton("Правила");
        buttonCreate_table = new JButton("Создать стол");
        buttonFind_table = new JButton("Найти стол");

        //Добавление элементов в панель параметров
        panelParametr.add(panelPlayer);//панель игрока
        panelParametr.add(buttonStart);//Кнопка играть
        panelParametr.add(buttonRegulations);
        panelParametr.add(buttonCreate_table);
        panelParametr.add(buttonFind_table);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady     = 50;    // установить первоначальный размер кнопки
        constraints.insets    = new Insets(0, -450, 0, 200);  // граница ячейки по Y
        panelInf.add(jspHelp,constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady     = 50;    // установить первоначальный размер кнопки
        constraints.insets    = new Insets(0, 200, 0, -450);  // граница ячейки по Y
        panelInf.add(jspList,constraints);


        //добавление элементов в меню
        panelMenu.add(fileMenu);
        fileMenu.add(startItem);
        fileMenu.addSeparator();
        fileMenu.add(stopItem);
        fileMenu.addSeparator();
        fileMenu.add(saveAllItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        //Добавление панелей в Фрейм
        setJMenuBar(panelMenu);
        add(panelWorking, BorderLayout.CENTER);
        add(panelInf, BorderLayout.SOUTH);
        add(panelParametr, BorderLayout.EAST);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Client.meGoClose();

            }
        });

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent m)
            {

                if (m.getX() > 180 && m.getX() < 720 && m.getY() > 240 && m.getY() < 420)
                {
                    int j = m.getX() / 60 - 3;
                    int i = m.getY() / 60 - 4;
                    System.out.println(Player.Kartochka[i][j]);
                    if (Player.Kartochka[i][j] == value_barrel)
                    {
                        Graphics g = getGraphics();
                        g.setColor(Color.ORANGE);
                        g.fillOval(m.getX() / 60 * 60 + 10, m.getY() / 60 * 60 + 10, 40, 40);
                        Player.Kartochka[i][j] = 0;
                        if (Player.allNull()){
                            Client.reportVictory();
                        }
                    }
                }
            }
        });


    }

    public void StopGame(String nameVictory){
        booldrawing = false;
        JOptionPane.showMessageDialog(null, "Победил игрок " + nameVictory);
        panelWorking.repaint();
    }
    public void StartGame(){
        getCard();
        drawBochonok =new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (booldrawing)
                {
                    try
                    {
                        sleep(1000);
                    } catch (InterruptedException ex)
                    {

                    }
                    drawBochonok();
                }
            }
        });
        drawBochonok.start();
    }

    public void getCard()    //рисует карточку и забивает массив плеера
    {
        Graphics g = this.getGraphics();
        Player.setCard();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(180, 240, 540, 180);
        g.setColor(Color.black);
        for (int i = 0, xKordLine = 180; i < 9; i++)
        { //сверху вниз
            g.drawLine(xKordLine, 240, xKordLine, 420);
            xKordLine += 60;
        }
        for (int i = 0, yKordLine = 240; i < 3; i++)
        { // слева на право
            g.drawLine(180, yKordLine, 720, yKordLine);
            yKordLine += 60;
        }
        int xString = 200, yString = 270;
        String str;
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (Player.Kartochka[i][j] != 0)
                {
                    str = Integer.toString(Player.Kartochka[i][j]);
                    g.drawString(str, xString, yString);
                }
                xString += 60;
            }
            yString += 60;
            xString = 200;
        }
    }

    void drawBochonok()
    {
        Graphics g = this.getGraphics();
        g.setColor(Color.RED);
        g.fillOval(420, 80, 60, 60);
        g.setColor(Color.WHITE);
        g.fillOval(425, 85, 50, 50);
        g.setColor(Color.RED);
        g.setFont(font_bochonok);
        g.drawString(Integer.toString(value_barrel), 435, 115);
    }

    public static class DialogFind_table {

        public static JDialog dialog;
        public static JTextArea tableTextArea = new JTextArea();
        public static String texttables = new String("Номер стола                        Ставки                       Игроки");
        JLabel label = new JLabel("Выберете стол");
        JButton buttonСhoose = new JButton("Выбрать");
        public static JComboBox comboBoxListtable = new JComboBox();
        JPanel panelButton = new JPanel();

        DialogFind_table(Habitat habitat){
            tableTextArea.setEnabled(false);
            dialog = new JDialog(habitat.frameWorkinge, "Выбор стола", true);
            tableTextArea.setText(texttables);
            JScrollPane jsp = new JScrollPane(tableTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dialog.setSize(500, 300);
            dialog.setLocation(500,200);
            dialog.add(jsp, BorderLayout.CENTER);

            panelButton.add(label);
            panelButton.add(comboBoxListtable);
            panelButton.add(buttonСhoose);
            dialog.add(panelButton,BorderLayout.SOUTH);

            // Обработчики кнопок
            buttonСhoose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    Client.connectionTable();
                }
            });
            jsp.setVisible(true);
            dialog.setVisible(true);


        }

    } // выбор стола

    public static class DialogCreate_table {

        JLabel labelTable = new JLabel("Название стола:");
        JLabel labelStavka = new JLabel("Ставка:");
        JLabel labelRezhim = new JLabel("Режим игры:");
        JTextField textFieldTable = new JTextField();
        String[] modbox = {"Простой", "Короткий", "Трина на три"};
        JComboBox comboBoxRezhim = new JComboBox(modbox);
        JSlider sliderStavka = new JSlider(100, 500, 100);
        JButton buttonСreate = new JButton("Создать");
        JPanel panelButton = new JPanel(new GridLayout(4, 2, 5, 0));
        JPanel panelBack = new JPanel() {
            public void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("images/Заставка4.jpeg")), 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error image Entrance");
                }
            }
        };

        DialogCreate_table(Habitat habitat){

            JDialog dialog = new JDialog(habitat.frameWorkinge, "Созданиe стола", true);
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dialog.setSize(500, 300);
            dialog.setLocation(500,200);

            sliderStavka.setMajorTickSpacing(100);
            sliderStavka.setSnapToTicks(true);
            sliderStavka.setPaintLabels(true);

            panelButton.add(labelTable);
            panelButton.add(textFieldTable);
            panelButton.add(labelRezhim);
            panelButton.add(comboBoxRezhim);
            panelButton.add(labelStavka);
            panelButton.add(sliderStavka);
            panelButton.add(buttonСreate);
            panelButton.setOpaque(false);

            panelBack.add(panelButton, BorderLayout.NORTH);

            dialog.add(panelBack,BorderLayout.CENTER);

            // Обработчики кнопок
            buttonСreate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    table_name = textFieldTable.getText();
                    table_mode = (String) comboBoxRezhim.getSelectedItem();
                    table_rate = sliderStavka.getValue();
                    dialog.setVisible(false);
                    Client.setTable(table_name,table_mode,table_rate);

                    System.out.println(table_name + table_mode + table_rate);
                }
            });

            dialog.setVisible(true);
        }

    } // диалоговое окно, где создается стола

    public static class Dialog_table {

        public static JDialog dialog;
        // Данные для таблиц
        public static Object[][] array_connection_player = new String[][] {
                { Habitat.player.nameClient , String.valueOf(Habitat.player.cash), String.valueOf(Habitat.player.ready)}
        };
        // Заголовки столбцов
        public static Object[] columnsHeader = new String[] {"Имя игрока", "Фишки", "Готовность"};

        public static JTable table1 = new JTable(array_connection_player, columnsHeader);
        Box contents = new Box(BoxLayout.Y_AXIS);


        JLabel labelStavka = new JLabel("Ставка: " + table_rate);
        JLabel labelRezhim = new JLabel("Режим: " + table_mode);
        JButton buttonGO = new JButton("Готов");
        JPanel panelLable = new JPanel();
        JPanel panelTime = new JPanel(){
            public void paintComponent(Graphics g) {

            }
        };

        Dialog_table(){

            table1.setEnabled(false);
            contents.add(new JScrollPane(table1));

            dialog = new JDialog(Habitat.frameWorkinge, "Стол: "+ Client.numberThisClient + ") " + table_name, false);
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dialog.setSize(500, 300);
            dialog.setLocation(500,200);

            panelLable.add(labelStavka);
            panelLable.add(labelRezhim);
            panelLable.setOpaque(false);

            panelTime.add(buttonGO);

            dialog.add(panelLable, BorderLayout.NORTH);
            dialog.add(contents,  BorderLayout.CENTER);
            dialog.add(panelTime, BorderLayout.SOUTH);

            // Обработчики кнопок
            buttonGO.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Client.setReadyGo();
                }
            });

            dialog.setVisible(true);
        }

    } // окно подключенных к столу клиентов

    // отрисовка панели
    public class MyPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {

        }

    }
}
