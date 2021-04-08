package Lotto_Server;

import Lotto_Client.FrameWorking;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

import static java.lang.Thread.sleep;

public class MoveBochonok extends Thread {

    boolean gener = true, sleep = false;
    static MoveBochonok.Bochonok B;
    FrameWorking F = new FrameWorking();
    //PipedOutputStream pipeOut;
    int k_rand = 0;
    static Iterator it;
    public static TreeSet<Integer> Tree_Indif = new TreeSet<Integer>();
    public Table table;

    public MoveBochonok(Table table)
    {
        this.table = table;
        /*
        try
        {
            pipeOut = new PipedOutputStream(F.pipeIn = new PipedInputStream());
        } catch (IOException ex)
        {
        }
        */
    }

    void StopIt()
    {
        gener = false;
    }

    public void goSleep()
    {
        sleep = true;
    }

    public synchronized void goWakeUp()
    {
        sleep = false;
        notify();
    }

    class Bochonok
    {

        Random r = new Random();
        int value;

        public Bochonok()
        {
            while (k_rand == 0)
            {
                value = r.nextInt(89) + 1;
                k_rand = 1;
                it = Tree_Indif.iterator();
                while (it.hasNext())
                {
                    if (value == (int) it.next())
                    {
                        k_rand = 0;
                    }
                }
            }
            k_rand = 0;
            Tree_Indif.add(value);
        }
    }

    public void run()
    {
        while (gener)
        {
            try
            {
                synchronized (this)
                {
                    while (sleep)
                    {
                        wait();
                    }
                }
            } catch (InterruptedException ex)
            {
            }
            B = new MoveBochonok.Bochonok();
            try
            {
                //pipeOut.write(B.value);

                for(int j=0; j<table.List_Connected_Client.size();j++){
                    table.List_Connected_Client.get(j).outStream.writeObject("cath_barrel");
                    table.List_Connected_Client.get(j).outStream.writeObject(B.value);
                }

            } catch (IOException ex)
            {
            }
            try
            {
                sleep(3000);
            } catch (InterruptedException ex)
            {

            }
        }
    }
}
