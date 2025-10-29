/*
 * Copyright (c) 2025 xGuard / https://xguard.es/terms
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jn.willfrydev.autocorrector;
//         _____               _
//  __  _/__   \_ __ __ _  __| |
//  \ \/ / / /\/ '__/ _` |/ _` |
//   >  < / /  | | | (_| | (_| |
//  / _/\_\\/   |_|  \__,_|\__,_|
//
import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

public class Main {

    private boolean isUpdating = false;
    private JFrame frame;
    private static final Map<String, String> REGLAS_CORRECCION = new HashMap<>();

    static {
        REGLAS_CORRECCION.put("hola como estas", "Hola cómo estás");
        REGLAS_CORRECCION.put("que haces", "qué haces");
        REGLAS_CORRECCION.put("cuando vienes", "cuándo vienes");
        REGLAS_CORRECCION.put("donde estas", "dónde estás");
        REGLAS_CORRECCION.put("por que", "por qué");
        REGLAS_CORRECCION.put("mas tarde", "más tarde");
    }
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.crearYMostrarGUI();
        });
    }

    private void crearYMostrarGUI() {
        frame = new JFrame("Autocorrector v1.0.0 - xPlugins");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(textArea);
        JCheckBox activarToggle = new JCheckBox("Activar Autocorrector");
        activarToggle.setSelected(true);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(activarToggle);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton botonCopiar = new JButton("Copiar");
        JButton botonLimpiar = new JButton("Limpiar");
        Dimension buttonSize = new Dimension(80, 30);
        botonCopiar.setPreferredSize(buttonSize);
        botonLimpiar.setPreferredSize(buttonSize);
        panelBotones.add(botonCopiar);
        panelBotones.add(botonLimpiar);
        botonLimpiar.addActionListener(e -> textArea.setText(""));
        botonCopiar.addActionListener(e -> {
            String texto = textArea.getText();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(texto), null);
        });
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                corregirTexto(textArea);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}
            @Override
            public void changedUpdate(DocumentEvent e) {}
            private void corregirTexto(JTextArea currentTextArea) {
                if (!activarToggle.isSelected() || isUpdating) {
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                    try {
                        Document doc = currentTextArea.getDocument();
                        String textoCompleto = doc.getText(0, doc.getLength());
                        String textoLower = textoCompleto.toLowerCase();
                        for (Map.Entry<String, String> regla : REGLAS_CORRECCION.entrySet()) {
                            String originalLower = regla.getKey();
                            String correccion = regla.getValue();
                            int index = textoLower.indexOf(originalLower);
                            while (index != -1) {
                                isUpdating = true;
                                doc.remove(index, originalLower.length());
                                doc.insertString(index, correccion, null);
                                isUpdating = false;
                                textoCompleto = doc.getText(0, doc.getLength());
                                textoLower = textoCompleto.toLowerCase();
                                index = textoLower.indexOf(originalLower, index + correccion.length());
                            }
                        }
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);

        if (SystemTray.isSupported()) {
            final SystemTray tray = SystemTray.getSystemTray();
            URL imageUrl = getClass().getClassLoader().getResource("icon.png");
            Image image = null;
            if (imageUrl != null) {
                image = Toolkit.getDefaultToolkit().getImage(imageUrl);
            } else {
                System.err.println("Advertencia: No se encontró 'icon.png' en src/main/resources. Usando ícono genérico.");
                image = new ImageIcon(Main.class.getResource("/toolbarButtonGraphics/general/TipOfTheDay.gif")).getImage();
            }

            final PopupMenu popup = new PopupMenu();
            MenuItem restoreItem = new MenuItem("Restaurar");
            restoreItem.addActionListener(e -> frame.setVisible(true));
            MenuItem exitItem = new MenuItem("Salir");

            popup.add(restoreItem);
            popup.addSeparator();
            popup.add(exitItem);
            final TrayIcon trayIcon = new TrayIcon(image, "Autocorrector", popup);
            trayIcon.setImageAutoSize(true);
            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("No se pudo añadir el ícono a la bandeja: " + e);
            }

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    frame.setVisible(false);
                }
            });
        } else {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            System.err.println("La bandeja del sistema no es soportada.");
        }
        frame.setVisible(true);
    }
}