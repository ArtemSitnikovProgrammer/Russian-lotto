package Lotto_Client;

import Lotto_Server.Table;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;



public class Client extends Thread {
    static int numberThisClient;
    static FrameWorking.Dialog_table dialog_table;
    static ObjectInputStream inStream;
    static ObjectOutputStream outStream;
    static String nameClient;
    static Socket fromserver = null;
    static Habitat habitat;
    static boolean going=true;
    static int numberThisTable;
    static String nameVictory;

    public Client()
    {
        System.out.println("Welcome to Client side");
        String S = "localhost";
        String boxitems=null;

        //запрашивать в строку количесвто клиентов
        System.out.println("Connecting to... " + S);

        try
        {
            fromserver = new Socket(S, 7777);


            inStream = new ObjectInputStream(fromserver.getInputStream());
            outStream = new ObjectOutputStream(fromserver.getOutputStream());
            outStream.flush();

/*
            boxitems = (String) inStream.readObject();
            System.out.println("списоок: " + boxitems);
*/
            numberThisClient = (int) inStream.readObject();
            System.out.println("номер: " + numberThisClient);


/*
            //при подключении должен отправить заявку на измение боксов
            outStream.writeObject("list");
            outStream.writeObject("add");
            outStream.writeObject(ThisClient);
*/
        } catch (IOException ex)
        {
            System.out.println("Соединение не установлено конструкток класса Client");
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    //-----------------------------------------------
    static void meGoClose()
    {
        try
        {
            going=false;
            outStream.writeObject("list");
            outStream.writeObject("remove");
            outStream.writeObject(numberThisClient);
/*
            inStream.close();
            outStream.close();
            fromserver.close();
            */
        } catch (IOException ex)
        {
            System.out.println("Проблемы в блоке meGoClose с отключением от сервера");
        }
    }
    static void meAuthorizationClose()
    {
        try
        {
            going=false;
            outStream.writeObject("disconnection");
            outStream.writeObject(numberThisClient);

        } catch (IOException ex)
        {
            System.out.println("Проблемы в блоке meAuthorizationClose с отключением от сервера");
        }
    }
    static void setAuthorization(String name_Client, String password_client){
        nameClient=name_Client;
        try {
            outStream.writeObject("authorization");//отправляем команду на авторизацию
            outStream.writeObject(numberThisClient);//отправляем номер подключаемого клиента, выданный серсером
            outStream.writeObject(name_Client);//отправляем имя клиента
            outStream.writeObject(password_client);//отправляем пороль
        } catch (IOException ex)
        {
            System.out.println("Проблемы в блоке setAuthorization с запросом авторизации на сервер");
        }

    }
    static void setReadyGo(){

        //Habitat.player.ready=true;
        try {
            outStream.writeObject("setReadyGo");
            //outStream.writeObject(numberThisClient);
            outStream.writeObject(Habitat.player);
            outStream.writeObject(FrameWorking.DialogFind_table.comboBoxListtable.getSelectedItem()); // отправляем номер стола к которому хотим подключиться

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void connectionTable(){

        try {
            outStream.writeObject("connection_a_table");
            outStream.writeObject(numberThisClient);
            outStream.writeObject(Habitat.player);
            outStream.writeObject(FrameWorking.DialogFind_table.comboBoxListtable.getSelectedItem()); // отправляем номер стола к которому хотим подключиться
            dialog_table = new FrameWorking.Dialog_table();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static void setTable(String table_name, String table_mode, int table_rate) {
        try {
            outStream.writeObject("creating_a_table");
            outStream.writeObject(numberThisClient);
            outStream.writeObject(Habitat.player);
            outStream.writeObject(table_name);
            outStream.writeObject(table_mode);
            outStream.writeObject(table_rate);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static void reportVictory(){
        try {
            outStream.writeObject("report_victory");
            //outStream.writeObject(numberThisClient);
            outStream.writeObject(Habitat.player);
            outStream.writeObject(numberThisTable);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run()
    {
        try
        {
            String flag;
            while (going) {
            flag=(String)inStream.readObject();
                if ("list".equals(flag)) {
                    Renewal_List_for_Client();
                }

                if ("confirmed".equals(flag)) {
                    Authorization_in_run();
                }

                if("anconfirmed".equals(flag)){
                    System.out.println("Авторизация не удалась");
                    Habitat.frameAuthorization = new FrameAuthorization();
                }

                if ("list_open_table".equals(flag)) {
                    Renewal_List_for_Table();
                }

                if ("table_created".equals(flag)) {
                    Table_Created();
                    System.out.println("стол создан, ответ от сервера получен");
                }
                if ("connection_to_your_table".equals(flag)) {
                    Player player_connected = (Player) inStream.readObject();
                    System.out.println("К нашему столу подключился клиент");
                }
                if ("start_game".equals(flag)) {
                    numberThisTable = (int)inStream.readObject();
                    FrameWorking.Dialog_table.dialog.setVisible(false);
                    Habitat.frameWorkinge.StartGame();
                    System.out.println("Игра начинается!!!");
                }
                if ("stop_game".equals(flag)) {
                    nameVictory = (String)inStream.readObject();
                    Habitat.frameWorkinge.StopGame(nameVictory);
                    System.out.println("Игра окончена!!! Победил игрок: " + nameVictory);
                }
                if ("cath_barrel".equals(flag)) {
                    FrameWorking.value_barrel = (int)inStream.readObject();
                    System.out.println("Получили боченок: " + FrameWorking.value_barrel);
                }
                if("table_closed".equals(flag)){

                    System.out.println("Стол закрыт");
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Клиент отключен от сервера");
        }finally {
           try {
               inStream.close();
               outStream.close();
               fromserver.close();
           }catch (IOException e) {
           }
        }
    }
    public void Table_Created(){
        FrameWorking.dialog_table = new FrameWorking.Dialog_table();
        /*
        try {
            FrameWorking.Dialog_table.table1.removeEditor();

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Client метод Table_Created");
        } catch (IOException e) {
            System.out.println("Ошибка в блоке Client метод Table_Created");
        }
        */
    }
    public void Renewal_List_for_Table(){
        try {
            ArrayList<Table> List_Table = new ArrayList();
            List_Table = (ArrayList<Table>)inStream.readObject();
            System.out.println(List_Table.get(0).table_name);
            FrameWorking.DialogFind_table.comboBoxListtable.removeAll();
            for( int i=0;i<List_Table.size();i++) {
                FrameWorking.DialogFind_table.tableTextArea.append(List_Table.get(i).numberThisTable + ") " + List_Table.get(i).table_name);
                FrameWorking.DialogFind_table.comboBoxListtable.addItem(List_Table.get(i).numberThisTable);
            }

        }catch (IOException ex)
        {
            System.out.println("Ошибка в блоке Client метод Renewal_List_for_Table");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Client метод Renewal_List_for_Table");
        }
    }

    public void Renewal_List_for_Client(){
        try {

            String list_Client_in_frame = (String) inStream.readObject();
            Habitat.frameWorkinge.areaListClient.setText(null);
            Habitat.frameWorkinge.areaListClient.append(list_Client_in_frame);
            System.out.println("Бокс: " + list_Client_in_frame);
        }catch (IOException ex)
        {
            System.out.println("Ошибка в блоке Client метод RenewalList_for_Client");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка в блоке Client метод RenewalList_for_Client");
        }
    }
    public void Authorization_in_run() {
        try {
            System.out.println("Авторизация успешна");

            //при подключении должен отправить заявку на измение списка подключенных игроков всем остальным
            outStream.writeObject("list");
            outStream.writeObject("add");
            outStream.writeObject(numberThisClient);

            Habitat.frameWorkinge = new FrameWorking();
            Habitat.frameWorkinge.setVisible(true);
            Habitat.listener = new Listener(habitat);
            Habitat.MeRepaint();
        }catch (IOException ex)
        {
            System.out.println("Ошибка в блоке Client метод Authorization_in_run");
        }
    }

}
