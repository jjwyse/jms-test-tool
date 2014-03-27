package com.jjw.jmstesttool.gui;

import com.jjw.jmstesttool.gui.tabbedpanel.JmsPanel;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TestTool GUI
 *
 * @author jjwyse
 */
public class MainFrame extends JFrame {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger instance.
     */
    Logger LOG = Logger.getLogger(MainFrame.class);

    public ProducerTemplate myProducer;

    /**
     *
     */
    public MainFrame(ProducerTemplate producerTemplate) {
        super();

        this.myProducer = producerTemplate;

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ExitListener());
        menu.add(menuItem);
        menuBar.add(menu);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("JMS", new JmsPanel(myProducer));

        this.setJMenuBar(menuBar);
        this.add(tabs);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Listens for an Exit click
     *
     * @author jjwyse
     */
    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            LOG.info("Exiting the application");
            System.exit(0);
        }
    }
}
