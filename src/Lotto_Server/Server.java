package Lotto_Server;


import Lotto_Client.Player;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server extends Thread {
    static int numberClient;
    public static ArrayList<ClientEdit> List_Client = new ArrayList<ClientEdit>();
    public static ArrayList<Player> List_Player = new ArrayList<Player>();
    public static ArrayList<Table> List_Table = new ArrayList<Table>();

    ObjectInputStream inStream = null;
    private boolean going=true;
    static ServerSocket servers = null;
   //static boolean boolAuthorization = false;
    static boolean boolCloseServer = false;
    MoveBochonok moveBochonok;

    class ClientEdit
    {
        Socket socket;
        String name_Client;
        boolean closed;
        boolean boolAuthorization = false;
        ObjectOutputStream outStream = null;


        public ClientEdit(Socket s)
        {
            socket = s;
            closed = false;
            name_Client = null;
            try
            {
                outStream = new ObjectOutputStream(s.getOutputStream());
                outStream.flush();


            } catch (IOException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public Server(Socket s)
    {

        ClientEdit c = new ClientEdit(s);
        List_Client.add(c);
        try
        {
            inStream = new ObjectInputStream(s.getInputStream());

            c.outStream.writeObject(numberClient);
            numberClient++;

        } catch (IOException e)
        {
            System.out.println("Соединение не установлено");
            System.out.println("ошибка: " + e);
        }

    }

    public static void main(String[] args)
    {

        System.out.println("Welcome to Server side");

        Socket fromclient = null;
        numberClient = 1;
        ReadFileSer();
        try
        {
            servers = new ServerSocket(7777);
            while (true)
            {
                fromclient = servers.accept();
                System.out.println("Соединение с клиентом ");
                Server nst = new Server(fromclient);
                nst.start();
            }
        } catch (Exception e)
        {
            System.out.println("Couldn't listen to port 7777");
            System.out.println("ошибка: " + e);
        }

    }

    public void run()
    {
        try
        {
            String flag, operationWithBox, name_Client, password_client;
            int numClient_recipient, numClient_sender, numberThisClient;
            System.out.println("Wait for messages");

            while (going)
            {
                flag = (String)inStream.readObject();
                //обновление списка у каждого клиента
                if ("list".equals(flag))
                {
                   Renewal_List_for_Client();
                }

                //авторизация
                if("authorization".equals(flag)){
                   Authorization();
                }

                //выход из окна авторизации и отключение от сервера
                if("disconnection".equals(flag)){
                    Disconnection();
                    break;
                }

                //получение запроса на создание стола
                if("creating_a_table".equals(flag)){
                    Creating_a_table();
                }
                //получение запроса на подключение к стола
                if("connection_a_table".equals(flag)){
                    Connection_a_table();
                }
                //получение подтверждения о готовности начать игру
                if("setReadyGo".equals(flag)){
                    ReadyGo();
                }
                //получение о победе игрока
                if("report_victory".equals(flag)){
                    ReportVictory();
                }

            }
        } catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  run");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  run");
        }
    }

    public void ReportVictory(){
        try {

            Player player = (Player) inStream.readObject(); //все данные этого клиента
            int numberTableConnection = (int) inStream.readObject();//номер стола на котором он играет
            b: for(int i =0; i<List_Table.size();i++){
                if(List_Table.get(i).numberThisTable==numberTableConnection){

                    System.out.println("Игра окончена! Победил игрок " + player.nameClient);
                    moveBochonok.gener=false;
                    List_Table.get(i).open = true;
                    for(int j=0;j<List_Table.get(i).List_Connected_Player.size();j++){
                        List_Table.get(i).List_Connected_Player.get(j).ready = false;
                    }

                    for(int j=0; j<List_Table.get(i).List_Connected_Client.size();j++){
                        List_Table.get(i).List_Connected_Client.get(j).outStream.writeObject("stop_game");
                        List_Table.get(i).List_Connected_Client.get(j).outStream.writeObject(player.nameClient);

                        //List_Table.get(i).List_Connected_Client.get(j).outStream.writeObject(List_Table.get(i).numberThisTable);
                    }
                    moveBochonok.gener=false;
                    List_Table.get(i).open = true;

                }
            }


        }catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  ReadyGo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  ReadyGo");
        }
    }
    public void ReadyGo(){
        try {
            //int numberThisClient = (int) inStream.readObject();//номер клиента подтверждающего готовность

            Player player = (Player) inStream.readObject(); //все данные этого клиента
            int numberTableConnection = (int) inStream.readObject();//номер стола на котором он играет
            b: for(int i =0; i<List_Table.size();i++){
                if(List_Table.get(i).numberThisTable==numberTableConnection){

                    for(int j=0;j<List_Table.get(i).List_Connected_Player.size();j++){
                        if(List_Table.get(i).List_Connected_Player.get(j).nameClient==player.nameClient){
                            List_Table.get(i).List_Connected_Player.get(j).ready = true;
                            System.out.println("Игрок " + player.nameClient + " Готов! " + List_Table.get(i).List_Connected_Player.get(j).ready);
                        }
                    }

                    boolean allReady=true;
                    for(int j=0;j<List_Table.get(i).List_Connected_Player.size();j++){
                        if(List_Table.get(i).List_Connected_Player.get(j).ready == false){
                            allReady=false;
                            break b;
                        }
                    }

                    if(allReady==true){
                        for(int j=0; j<List_Table.get(i).List_Connected_Client.size();j++){
                            List_Table.get(i).List_Connected_Client.get(j).outStream.writeObject("start_game");
                            List_Table.get(i).List_Connected_Client.get(j).outStream.writeObject(List_Table.get(i).numberThisTable);
                        }
                        List_Table.get(i).open=false;
                        moveBochonok = new MoveBochonok(List_Table.get(i));
                        moveBochonok.start();
                    }
                }
            }


        }catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  ReadyGo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  ReadyGo");
        }
    }
    //создание стола по запросу пользователя
    public void Creating_a_table(){
        try {
            int numberThisClient = (int) inStream.readObject();// получаем номер клиента создателя
            Player player_creator = (Player) inStream.readObject();
            String table_name = (String) inStream.readObject(),
                   table_mode = (String) inStream.readObject();
            int table_rate = (int) inStream.readObject();

            Table table = new Table(numberThisClient,table_name,table_mode,table_rate);//создается стол с указанными параметрами
            List_Table.add(table);                                                     //добавляется в список существующих столов
            table.List_Connected_Client.add(List_Client.get(numberThisClient-1));      // в список подключенных к столу клиентов добавляется клиент создатель
            table.List_Connected_Player.add(player_creator);

            // отправляем ответ клиенту создателю
            List_Client.get(numberThisClient-1).outStream.writeObject("table_created");

            //отправка всем клиентам нового списка столов
            Renewal_List_Table_for_Client();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  Creating_a_table");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  Creating_a_table");
        }
    }
    //подключение к существующему столу
    public void Connection_a_table(){
        try {
            int numberThisClient = (int) inStream.readObject();//номер подключающегося клиента
            Player player = (Player) inStream.readObject(); //все данные этого клиента
            int numberTableConnection = (int)inStream.readObject();//номер стола подключения

            for (int i = 0; i < List_Table.size(); i++) {
                if (List_Table.get(i).numberThisTable == numberTableConnection) {
                    if(List_Table.get(i).open==true) {
                        List_Table.get(i).List_Connected_Client.add(List_Client.get(numberThisClient - 1));
                        List_Table.get(i).List_Connected_Player.add(player);
                        List_Client.get(numberTableConnection - 1).outStream.writeObject("connection_to_your_table");
                        List_Client.get(numberTableConnection - 1).outStream.writeObject(player);
                        break;
                    }else {
                        List_Client.get(numberThisClient - 1).outStream.writeObject("table_closed");
                    }
                }
            }


        }catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  Connection_a_table");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  Connection_a_table");
        }

    }
    //оповещение всех пользователей об обновлении списка созданных столов
    public void Renewal_List_Table_for_Client(){
        try {
            System.out.println("Отправка списка созданных столов всем клиентам");
            for (int i = 0; i < List_Client.size(); i++) {
                if (List_Client.get(i).closed == false && List_Client.get(i).boolAuthorization == true) {
                    List_Client.get(i).outStream.writeObject("list_open_table");
                    List_Client.get(i).outStream.writeObject(List_Table);
                }
            }
        }catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  Renewal_List_Table_for_Client");
        }
    }
    //оповещение всех пользователей об обновлении списка всех подключенных клиентов
    public void Renewal_List_for_Client(){
        try {
            System.out.print("Обновление списка: ");
            String operationWithBox = (String) inStream.readObject();
            int numberThisClient;

            if ("add".equals(operationWithBox))//добавляем клиент в бокс
            {
                System.out.println("Добавление");
                numberThisClient = (int) inStream.readObject();
                for (int i = 0; i < List_Client.size(); i++) {
                    if (List_Client.get(i).closed == false) {
                        List_Client.get(i).outStream.writeObject("list");
                        List_Client.get(i).outStream.writeObject(RenewalList());
                    }
                }
            } else //удалить из бокса
            {
                going=false;
                System.out.println("Удаление");
                numberThisClient = (int) inStream.readObject();
                List_Client.get(numberThisClient - 1).closed = true;
                for (int i = 0; i < List_Client.size(); i++) {
                    if (List_Client.get(i).closed == false) {
                        List_Client.get(i).outStream.writeObject("list");
                        List_Client.get(i).outStream.writeObject(RenewalList());
                    }
                }

                inStream.close();
                List_Client.get(numberThisClient - 1).outStream.close();
                List_Client.get(numberThisClient - 1).socket.close();
            }
        }catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  Renewal_List_for_Client");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  Renewal_List_for_Client");
        }
    }
    //отключение от сервера не авторизовавшегося клиента
    public void Disconnection(){
        try {
            int numberThisClient = (int) inStream.readObject();
            List_Client.get(numberThisClient - 1).closed = true;
            inStream.close();
            List_Client.get(numberThisClient - 1).outStream.close();
            List_Client.get(numberThisClient - 1).socket.close();
        }catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  Disconnection");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  Disconnection");
        }
    }
    //авторизация клиента, после ввода логина и пароля
    public void Authorization() {
        try {
            int numberThisClient;
            String name_Client, password_client;
            numberThisClient = (int) inStream.readObject();
            name_Client = (String) inStream.readObject();
            password_client = (String) inStream.readObject();
            Player player = new Player(name_Client, password_client);
            List_Client.get(numberThisClient - 1).name_Client = name_Client;
        //    List_Player.add(player);
            System.out.println("Авторизация: даные получены " + name_Client + password_client);
      //      WriteFileSer();

            for (int i = 0; i < List_Player.size(); i++) {
                if (player.nameClient.equals(List_Player.get(i).nameClient) && player.passwordClient.equals(List_Player.get(i).passwordClient)) {

                    List_Client.get(numberThisClient - 1).outStream.writeObject("confirmed");
                    List_Client.get(numberThisClient - 1).outStream.writeObject(RenewalList());
                    List_Client.get(numberThisClient-1).boolAuthorization = true;
                    System.out.println("Авторизация успешна");
                    break;
                }
            }
            if (List_Client.get(numberThisClient-1).boolAuthorization == false) {

                List_Client.get(numberThisClient - 1).outStream.writeObject("anconfirmed");
                System.out.println("Авторизация не удалась");
            }

        }catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ошибка в блоке Server метод  Authorization");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Server метод  Authorization");
        }
    }
    //метод обновления списка всех подключенных клиентов, вызывается после новоавторизовавшегося клиента
    public static String RenewalList(){
        String list_Client_in_frame=""; String bb;
        for(int j = 0;j<List_Client.size();j++)
        {
            if(List_Client.get(j).closed==false)
            {
                bb = List_Client.get(j).name_Client;
                list_Client_in_frame+=bb;
                list_Client_in_frame+="\n";
            }
        }
        System.out.println("Список клиентов: " + list_Client_in_frame);
        return list_Client_in_frame;
    }
    //регистация новых клиентов и запись их в файл
    public static synchronized void WriteFileSer() {
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream("Authorization.ser"));
            oos.writeObject(List_Player);

            System.out.println("Cписок играков записан");
        } catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null,"Wrong output File");
            System.out.println("Ошибка в блоке WriteFileSer: Wrong output File");
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null,"Ошибка");
            System.out.println("Ошибка в блоке WriteFileSer: Wrong output File");
        }
        finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка в блоке WriteFileSer: Закрытие файла");
            }
        }
    }
    //чтение файла со списком всех зарегестрированных клиентов
    public static synchronized void ReadFileSer() {

        ObjectInputStream ois  = null;
        try
        {
            ois = new ObjectInputStream(new FileInputStream("Authorization.ser"));
            List_Player.clear();
            List_Player = (ArrayList<Player>) ois.readObject();

            System.out.println("Файл прочитан");

        } catch (FileNotFoundException ex)
        {
            System.out.println("Ошибка в блоке ReadFileSer: Wrong input File");
        } catch (IOException ex)
        {
            System.out.println("Ошибка в блоке ReadFileSer: Wrong input Object");
        } catch (ClassNotFoundException ex)
        {
            System.out.println("Ошибка в блоке ReadFileSer: Empty File");
        }
        finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка в блоке ReadFileSer: Закрытие файла");
            }
        }
    }

}
