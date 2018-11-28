package com.hm.simpleservice.client.ui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import org.json.simple.JSONObject;

import com.hm.simpleservice.client.RestPostUpdate;
import com.hm.simpleservice.server.model.Car;
import javax.swing.JRadioButton;

public class ClientPanel extends JPanel {

	private Car driver;
	private JTextField textField_Start;
	private JTextField textField_destination;
	private JTextField textField_desTime;
	private JLabel lblCurrentState;
	private RestPostUpdate updater;
	
	public ClientPanel(final Car driver, RestPostUpdate updater) {
		this.driver = driver;
		this.updater = updater;
		//TODO uebergabeparameter driver
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelScroll = new JPanel();
		panelScroll.setLayout(new BorderLayout(0, 0));
		
		JPanel panelStatus = new JPanel();
		panelScroll.add(panelStatus, BorderLayout.NORTH);
		panelStatus.setLayout(new BorderLayout(0, 0));
		
		JPanel panelShowState = new JPanel();
		panelStatus.add(panelShowState, BorderLayout.NORTH);
		panelShowState.setLayout(new BorderLayout(0, 0));
		
		JLabel lblStatus = new JLabel("Status:");
		panelShowState.add(lblStatus, BorderLayout.WEST);
		
		lblCurrentState = new JLabel(driver.getState().name());
		panelShowState.add(lblCurrentState, BorderLayout.CENTER);
		
		JPanel panelEditState = new JPanel();
		panelStatus.add(panelEditState, BorderLayout.SOUTH);
		panelEditState.setLayout(new GridLayout(1, 3, 0, 0));
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Inactive");
		panelEditState.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setSelected(false);
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Finished");
		panelEditState.add(rdbtnNewRadioButton_2);
		rdbtnNewRadioButton_2.setSelected(false);
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("On Road (Start)");
		panelEditState.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setSelected(true);
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		final ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnNewRadioButton);
		bg.add(rdbtnNewRadioButton_1);
		bg.add(rdbtnNewRadioButton_2);
		
		JScrollPane scrollPane = new JScrollPane(panelScroll);
		
		JPanel panelOverview = new JPanel();
		panelScroll.add(panelOverview, BorderLayout.CENTER);
		panelOverview.setLayout(new BorderLayout(0, 0));
		
		JPanel panelAddresses = new JPanel();
		panelOverview.add(panelAddresses, BorderLayout.CENTER);
		panelAddresses.setLayout(new GridLayout(3, 4, 0, 0));
		
		JLabel lblCurrentAddress = new JLabel("Current Address:");
		panelAddresses.add(lblCurrentAddress);
		
		textField_Start = new JTextField();
		panelAddresses.add(textField_Start);
		
		JButton button_start = new JButton("Update");
		panelAddresses.add(button_start);
		
		button_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				driver.setStartAddress(textField_Start.getText());
			}
		});
		
		JLabel label_3 = new JLabel("Destination Address:");
		panelAddresses.add(label_3);
		
		textField_destination = new JTextField();
		panelAddresses.add(textField_destination);
		
		JButton button_destination = new JButton("Update");
		panelAddresses.add(button_destination);
		
		button_destination.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				driver.setDestinationAddress(textField_destination.getText());
			}
		});
		
		JLabel label_desTime = new JLabel("Destination Time:");
		panelAddresses.add(label_desTime);
		
		textField_desTime = new JTextField();
		panelAddresses.add(textField_desTime);
		
		JButton button_desTime = new JButton("Update");
		panelAddresses.add(button_desTime);
		
		button_desTime.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				driver.setDestinationTime(Integer.parseInt(textField_desTime.getText()));
			}
		});
		
		JPanel panelID = new JPanel();
		panelOverview.add(panelID, BorderLayout.NORTH);
		panelID.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("Driver ID:");
		panelID.add(label, BorderLayout.WEST);
		
		JLabel label_1 = new JLabel(String.valueOf(driver.getID()));
		panelID.add(label_1, BorderLayout.CENTER);
		
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public int getDriverID() {
		return this.driver.getID();
	}
	
	public void updateState() {
		lblCurrentState.setText(driver.getState().name());
	}
	
	private void sendUpdate(JSONObject message) {
		updater.send(message);
	}
}
