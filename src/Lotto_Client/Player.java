package Lotto_Client;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class Player implements Serializable {
    public String nameClient;
    public String passwordClient;
    public int cash;
    public boolean ready=false;

    static int[][] Kartochka = new int[3][9];
    static int k_rand = 0;
    static Iterator it;
    public static TreeSet<Integer> Tree_Indif = new TreeSet<Integer>();

    public Player(String name_client, String password_client){
        nameClient = name_client;
        passwordClient = password_client;
        cash = 1500;

    }
    static void setCard()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                Kartochka[i][j] = -1;
            }
        }
        Random r = new Random();
        int numNull, numNotNull; //флажок проверки пустых клеток в строке
        for (int j = 0; j < 9; j++)
        {
            for (int i = 0; i < 3; i++)
            {
                numNull = 0;
                numNotNull = 0;
                for (int s = 0; s < 9; s++)
                {
                    if (Kartochka[i][s] == 0)
                    {
                        numNull++;
                    }
                }
                numNotNull = j - numNull;
                if (numNull == 4)//если пустых уже 4 то заполнять числом
                {
                    while (k_rand == 0)
                    {
                        Kartochka[i][j] = 10 * j + r.nextInt(9) + 1;
                        k_rand = 1;
                        it = Tree_Indif.iterator();
                        while (it.hasNext())
                        {
                            if (Kartochka[i][j] == (int) it.next())
                            {
                                k_rand = 0;
                            }
                        }
                    }
                    k_rand = 0;
                    Tree_Indif.add(Kartochka[i][j]);

                } else
                {
                    if (numNotNull == 5) //если не пустых уже 5 то пустота
                    {
                        Kartochka[i][j] = 0;

                    } else      //рандом
                    {
                        if (r.nextFloat() < 0.5)
                        {
                            while (k_rand == 0)
                            {
                                Kartochka[i][j] = 10 * j + r.nextInt(9) + 1;
                                k_rand = 1;
                                it = Tree_Indif.iterator();
                                while (it.hasNext())
                                {
                                    if (Kartochka[i][j] == (int) it.next())
                                    {
                                        k_rand = 0;
                                    }
                                }
                            }
                            k_rand = 0;
                            Tree_Indif.add(Kartochka[i][j]);
                        } else
                        {
                            Kartochka[i][j] = 0;
                        }
                    }
                }
            }
        }
    }

    static boolean allNull()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (Kartochka[i][j] != 0)
                {
                    return false;
                }
            }
        }
        return true;
    }

}
