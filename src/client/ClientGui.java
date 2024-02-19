package client;

import server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ClientGui extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();

    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField("ivan_igorevich");
    private final JPasswordField tfPassword = new JPasswordField("123456");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    ServerWindow serverWindow;

    private Boolean isConnected;

    public ClientGui(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;
        isConnected = false;

        this.serverWindow.newClient(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        setVisible(true);
    }

    private void sendMessage() {
        if (serverWindow.getServerWorking() && isConnected) {
            serverWindow.printMsg(tfLogin.getText() + ": " + tfMessage.getText() + "\n");
        }
        tfMessage.setText(null);
    }

    public void printMessage(String msg) {
        log.append(msg);
    }

    public void lostConnect() {
        log.append("Вы были отключены от сервера! \n");
        panelTop.setVisible(true);
    }

    private void connect() {
        if (serverWindow.getServerWorking()) {
            String line;

            panelTop.setVisible(false);
            log.append("Вы успешно подключились! \n");
            isConnected = true;

            try (BufferedReader reader = new BufferedReader(new FileReader(serverWindow.getFile()))) {
                while ((line = reader.readLine()) != null) {
                    log.append(line + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            log.append("Подключение не удалось! \n");
        }
    }
}
