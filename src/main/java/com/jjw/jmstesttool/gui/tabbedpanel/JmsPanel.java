package com.jjw.jmstesttool.gui.tabbedpanel;

import com.jjw.jmstesttool.jaxb.Note;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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

    public ProducerTemplate myProducer;

    public JmsPanel(ProducerTemplate producer) {
        super();
        myProducer = producer;
        setupLayout();
        setupSouthPanel();
        setupWestPanel();
    }

    private void setupLayout() {
        this.setLayout(new BorderLayout(10, 10));
    }

    private void setupSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

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
                    LOG.info("Sending to jms:queue:" + queueTextField.getText());
                    myProducer.requestBody("jms:queue:" + queueTextField.getText(), "Queue");
                }
                else if (topicButton.isSelected()) {
                    LOG.info("Sending to jms:topic:" + topicTextField.getText());
                    myProducer.sendBody("jms:topic:" + topicTextField.getText(), "Topic");
                }
                else {
                    String endpoint = directTextField.getText();
                    LOG.info("Sending to direct:" + endpoint);
                    myProducer.sendBody("direct:" + endpoint, createDirectBody(endpoint));
                }
            }

        });

        buttonGroup.add(queueButton);
        buttonGroup.add(topicButton);
        buttonGroup.add(directButton);

        southPanel.add(queueButton);
        southPanel.add(queueTextField);
        southPanel.add(topicButton);
        southPanel.add(topicTextField);
        southPanel.add(directButton);
        southPanel.add(directTextField);
        southPanel.add(sendButton);

        this.add(southPanel, BorderLayout.SOUTH);
    }

    private Object createDirectBody(String directEndpoint) {
        if (StringUtils.equals(directEndpoint, NOTE)) {
            Note note = new Note();
            note.setTo("note_to");
            note.setFrom("note_from");
            note.setHeading("note_heading");
            note.setBody("note_body");
            return note;
        }

        return "Direct";
    }

    private void setupWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        this.add(westPanel, BorderLayout.WEST);
    }

}
