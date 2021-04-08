package Lotto_Client;


import java.util.Timer;
import java.util.TimerTask;

public class Habitat {
    static FrameAuthorization frameAuthorization;
    static FrameWorking frameWorkinge;
    static Listener listener;
    static Client client;
    static Player player;

    public Habitat(){

        /*
        frameWorkinge = new FrameWorking();
        frameWorkinge.setVisible(true);
        listener = new Listener(this);
*/

        frameAuthorization = new FrameAuthorization();
        client = new Client();
        client.start();

    }
    static void Authorization(){
        String name_client = new String(frameAuthorization.textFieldName.getText());
        String password_client = new String(frameAuthorization.passwordFieldPassword.getPassword());
        player = new Player(name_client, password_client);
        Client.setAuthorization(name_client,password_client);
    }
    static void MeRepaint()     {
        //создаём новые объекты таймер и потоки
        Timer timer1 = new Timer();
/*
        TimerTask task1 = new TimerTask() {
            public void run()
            {
                TimeTask();
                synchronized (Arr) {
                    if (Math.random() <= P1) {
                        RandomID();
                        Car Cr = new Car(myFrame.panel.getWidth(), myFrame.panel.getHeight(), ObjectID, life_Time_Car);
                        ID.add(Cr.ObjectID);
                        Arr.add(Cr);
                        Time_Life.put(Cr.ObjectID, (int) m_lastTime / 1000);
                        Car++;
                        ALL++;
                    }
                }

            }
        };

        TimerTask deleteObject = new TimerTask() {
            public void run()
            {
                TimeTask();
                synchronized (Arr) {
                    for (int i = 0; i < Arr.size(); i++) {
                        if (m_lastTime / 1000 - Time_Life.get(Arr.get(i).ObjectID) >= Arr.get(i).life_Time) {
                            ID.remove(Arr.get(i).ObjectID);
                            Time_Life.remove(Arr.get(i).ObjectID);
                            Arr.remove(i);
                            i--;
                        }
                    }
                }
            }
        };
*/
        TimerTask otrisovka = new TimerTask() {
            public synchronized void run()
            {
                frameWorkinge.panelWorking.repaint();
            }
        };

        //запускаем таймер
       //timer1.schedule(otrisovka, 50,50);
    }
}
