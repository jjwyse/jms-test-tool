package com.jjw.jmstesttool.gui.tabbedpanel;

import com.jjw.jmstesttool.jaxb.Note;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JmsPanel extends JPanel {
    /**
     * Logger instance.
     */
    Logger LOG = Logger.getLogger(JmsPanel.class);

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    private static final String NOTE = "note";

    final JSpinner spinner = new JSpinner();

    public ProducerTemplate myProducer;

    public JmsPanel(ProducerTemplate producer) {
        super();
        myProducer = producer;
        setupLayout();
        setupNorthPanel();
        setupSouthPanel();
        setupWestPanel();
    }

    private void setupLayout() {
        this.setLayout(new BorderLayout(10, 10));
    }

    private void setupNorthPanel() {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        ButtonGroup buttonGroup = new ButtonGroup();
        final JRadioButton queueButton = new JRadioButton("Queue");
        final JRadioButton topicButton = new JRadioButton("Topic");
        final JRadioButton directButton = new JRadioButton("Direct");

        final JTextField queueTextField = new JTextField(10);
        final JTextField topicTextField = new JTextField(10);
        final JTextField directTextField = new JTextField(10);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (queueButton.isSelected()) {
                    sendMessages("jms:queue:" + queueTextField.getText());
                }
                else if (topicButton.isSelected()) {
                    sendMessages("jms:topic:" + topicTextField.getText());
                }
                else {
                    sendMessages("direct:" + directTextField.getText());
                }
            }

            private void sendMessages(String endpoint) {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();

                int numMessagesToSend = (Integer) spinner.getValue();
                for (int i = 0; i < numMessagesToSend; i++) {
                    LOG.info(i + " - Sending to " + endpoint);
                    if (endpoint.startsWith("jms:queue:")) {
                        myProducer.requestBody(endpoint, createBody(endpoint));
                    }
                    else {
                        myProducer.sendBody(endpoint, createBody(endpoint));
                    }
                }
                stopWatch.stop();
                LOG.info("Elapsed time for sending " + numMessagesToSend + " message was: " + stopWatch.getTotalTimeSeconds() + " seconds");
                double messagesPerSecond = stopWatch.getTotalTimeSeconds() / numMessagesToSend;
                LOG.info("messages/s: " + messagesPerSecond);
            }

        });

        buttonGroup.add(queueButton);
        buttonGroup.add(topicButton);
        buttonGroup.add(directButton);

        northPanel.add(queueButton);
        northPanel.add(queueTextField);
        northPanel.add(topicButton);
        northPanel.add(topicTextField);
        northPanel.add(directButton);
        northPanel.add(directTextField);
        northPanel.add(sendButton);

        this.add(northPanel, BorderLayout.NORTH);
    }

    private void setupSouthPanel() {
        JPanel southPanel = new JPanel();

        JLabel label = new JLabel("Number of messages to send:");

        spinner.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        spinner.setName("Number of messages to send");

        southPanel.add(label);
        southPanel.add(spinner);

        this.add(southPanel, BorderLayout.SOUTH);
    }

    private Object createBody(String endpoint) {
        if (StringUtils.endsWith(endpoint, NOTE)) {
            Note note = new Note();
            note.setTo("note_to");
            note.setFrom("note_from");
            note.setHeading("note_heading");
            note.setBody("note_body");
            return note;
        }

        return "Message";
    }

    private void setupWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        this.add(westPanel, BorderLayout.WEST);
    }

}
