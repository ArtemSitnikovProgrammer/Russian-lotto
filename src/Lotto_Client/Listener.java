package Lotto_Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Listener {
    Listener(Habitat habitat){

        ActionListener actionListenerButtonFind_table = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameWorking.DialogFind_table dialogFind_table = new FrameWorking.DialogFind_table(habitat);

            }
        };

        ActionListener actionListenerButtonCreate_table = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameWorking.DialogCreate_table dialogCreate_table = new FrameWorking.DialogCreate_table(habitat);

            }
        };
        ActionListener actionListenerButtonStart = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Habitat.frameWorkinge.StartGame();

            }
        };

        Habitat.frameWorkinge.buttonStart.addActionListener(actionListenerButtonStart);
        Habitat.frameWorkinge.buttonCreate_table.addActionListener(actionListenerButtonCreate_table);
        Habitat.frameWorkinge.buttonFind_table.addActionListener(actionListenerButtonFind_table);
    }
}
