package Lotto_Server;


import Lotto_Client.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Table implements Serializable{
    public int numberThisTable;
    public String table_name, table_mode;
    public int table_rate;
    public boolean open = true;
    transient public ArrayList<Server.ClientEdit> List_Connected_Client = new ArrayList<Server.ClientEdit>();
    transient public ArrayList<Player> List_Connected_Player = new ArrayList<Player>();
    Table(int numberThisTable, String table_name, String table_mode, int table_rate){
        this.numberThisTable = numberThisTable;
        this.table_mode = table_mode;
        this.table_name = table_name;
        this.table_rate = table_rate;
    }
}
