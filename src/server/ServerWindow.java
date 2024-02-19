package server;

import client.ClientGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ServerWindow extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();

    private final JPanel panelBottom = new JPanel(new GridLayout(1, 2));
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");

    private Boolean isServerWorking;

    private ArrayList<ClientGui> lClient = new ArrayList<ClientGui>();

    File file = new File("src/server/log.txt");

    public ServerWindow() {
        isServerWorking = false;
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                log.append("Сервер остановлен! \n");
                for (ClientGui client : lClient) {
                    client.lostConnect();
                }
                lClient.clear();
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String line;

                isServerWorking = true;
                log.setText(null);

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while ((line = reader.readLine()) != null) {
                        log.append(line + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                log.append("Сервер запущен! \n");
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);

        panelBottom.add(btnStart);
        panelBottom.add(btnStop);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        setVisible(true);
    }

    public void printMsg(String msg) {
        log.append(msg);
        logging(msg);
        for (ClientGui client : lClient) {
            client.printMessage(msg);
        }
    }

    public void newClient(ClientGui clientGui) {
        lClient.add(clientGui);
    }

    public Boolean getServerWorking() {
        return isServerWorking;
    }

    public void logging(String msg) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(msg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public File getFile() {
        return file;
    }
}
