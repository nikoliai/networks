package com.hm.tfour.client.ui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.hm.tfour.server.ServerController;
import com.hm.tfour.server.model.Car;
import com.hm.tfour.server.model.CarUtil;
import com.hm.tfour.server.ressource.RessourceDriverState;

import javax.swing.JRadioButton;

public class ClientPanel extends JPanel {

	private Car driver;
	private JTextField textField_OverviewDesStreet;
	private JTextField textField_OverviewDesHnr;
	private JTextField textField_OverviewDesCity;
	private JTextField textField_OverviewDesCountry;
	private JTextField textField_OverviewCurStreet;
	private JTextField textField_OverviewCurHnr;
	private JTextField textField_OverviewCurCity;
	private JTextField textField_OverviewCurCountry;
	private JLabel lblCurrentState;
	private JTextField textField_OverviewTime;
	
	public ClientPanel(final Car driver) {
		this.driver = driver;
		
		String[] startAddress = driver.getStartAddress();
		String[] destinationAddress = driver.getDestinationAddress();
		
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
		
		Car.State state = driver.getState();
		String stateName = state.name();
		
		lblCurrentState = new JLabel(stateName);
		panelShowState.add(lblCurrentState, BorderLayout.CENTER);
		
		JPanel panelEditState = new JPanel();
		panelStatus.add(panelEditState, BorderLayout.SOUTH);
		panelEditState.setLayout(new GridLayout(1, 3, 0, 0));
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Inactive");
		panelEditState.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendUpdate(CarUtil.createDriverStatePostRequestJsonString(driver.getID(), Car.State.INACTIVE, new String[] {"", "", "", ""}));
				}
		});
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Finished");
		panelEditState.add(rdbtnNewRadioButton_2);
		rdbtnNewRadioButton_2.setSelected(false);
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendUpdate(CarUtil.createDriverStatePostRequestJsonString(driver.getID(), Car.State.FREE, new String[] {"", "", "", ""}));
				}
		});
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("On Road (Start)");
		panelEditState.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setSelected(false);
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendUpdate(CarUtil.createDriverStatePostRequestJsonString(driver.getID(), Car.State.IN_TIME, driver.getStartAddress()));
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
		
		JPanel panelID = new JPanel();
		panelOverview.add(panelID, BorderLayout.NORTH);
		panelID.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("Driver ID:");
		panelID.add(label, BorderLayout.WEST);
		
		JLabel label_1 = new JLabel(String.valueOf(driver.getID()));
		panelID.add(label_1, BorderLayout.CENTER);
		
		JPanel panelAddresses = new JPanel();
		panelOverview.add(panelAddresses);
		panelAddresses.setLayout(new GridLayout(6, 1, 0, 0));
		
		JLabel lblOverviewCurrent = new JLabel("Current address:");
		panelAddresses.add(lblOverviewCurrent);
		
		
		
		
		
		JPanel panelCur = new JPanel();
		panelAddresses.add(panelCur);
		panelCur.setLayout(new GridLayout(1, 5, 0, 0));
		
		JPanel panel_OverviewCurStreet = new JPanel();
		panelCur.add(panel_OverviewCurStreet);
		panel_OverviewCurStreet.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewCurStreet = new JLabel("Street:");
		panel_OverviewCurStreet.add(lblOverviewCurStreet, BorderLayout.WEST);
		
		textField_OverviewCurStreet = new JTextField(startAddress[1]);
		panel_OverviewCurStreet.add(textField_OverviewCurStreet);
		
		JPanel panel_OverviewCurHnr = new JPanel();
		panelCur.add(panel_OverviewCurHnr);
		panel_OverviewCurHnr.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewCurHnr = new JLabel("HouseNr:");
		panel_OverviewCurHnr.add(lblOverviewCurHnr, BorderLayout.WEST);
		
		textField_OverviewCurHnr = new JTextField(startAddress[0]);
		panel_OverviewCurHnr.add(textField_OverviewCurHnr);
		
		JPanel panel_OverviewCurCity = new JPanel();
		panelCur.add(panel_OverviewCurCity);
		panel_OverviewCurCity.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewCurCity = new JLabel("City:");
		panel_OverviewCurCity.add(lblOverviewCurCity, BorderLayout.WEST);
		
		textField_OverviewCurCity = new JTextField(startAddress[2]);
		panel_OverviewCurCity.add(textField_OverviewCurCity);
		
		JPanel panel_OverviewCurCountry = new JPanel();
		panelCur.add(panel_OverviewCurCountry);
		panel_OverviewCurCountry.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewCurCountry = new JLabel("Country:");
		panel_OverviewCurCountry.add(lblOverviewCurCountry, BorderLayout.WEST);
		
		textField_OverviewCurCountry = new JTextField(startAddress[3]);
		panel_OverviewCurCountry.add(textField_OverviewCurCountry);
		
		JButton button_OverviewCurUpdate = new JButton("Update");
		panelCur.add(button_OverviewCurUpdate);
		button_OverviewCurUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				driver.setStartAddress(new String[] {textField_OverviewCurHnr.getText(), textField_OverviewCurStreet.getText(), textField_OverviewCurCity.getText(), textField_OverviewCurCountry.getText()});
				if (driver.getState().equals(Car.State.IN_TIME) || driver.getState().equals(Car.State.LATE)) {
					sendUpdate(CarUtil.createDriverStatePostRequestJsonString(driver.getID(), Car.State.IN_TIME, driver.getStartAddress()));
				}
			}
		});
		
		JLabel lblOverviewDes = new JLabel("Current destination:");
		panelAddresses.add(lblOverviewDes);
		
		JPanel panelDes = new JPanel();
		panelAddresses.add(panelDes);
		panelDes.setLayout(new GridLayout(1, 5, 0, 0));
		
		JPanel panel_OverviewDesStreet = new JPanel();
		panelDes.add(panel_OverviewDesStreet);
		panel_OverviewDesStreet.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewDesStreet = new JLabel("Street:");
		panel_OverviewDesStreet.add(lblOverviewDesStreet, BorderLayout.WEST);
		
		textField_OverviewDesStreet = new JTextField(destinationAddress[1]);
		panel_OverviewDesStreet.add(textField_OverviewDesStreet);
		
		JPanel panel_OverviewDesHnr = new JPanel();
		panelDes.add(panel_OverviewDesHnr);
		panel_OverviewDesHnr.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewDesHnr = new JLabel("HouseNr:");
		panel_OverviewDesHnr.add(lblOverviewDesHnr, BorderLayout.WEST);
		
		textField_OverviewDesHnr = new JTextField(destinationAddress[0]);
		panel_OverviewDesHnr.add(textField_OverviewDesHnr);
		
		JPanel panel_OverviewDesCity = new JPanel();
		panelDes.add(panel_OverviewDesCity);
		panel_OverviewDesCity.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewDesCity = new JLabel("City:");
		panel_OverviewDesCity.add(lblOverviewDesCity, BorderLayout.WEST);
		
		textField_OverviewDesCity = new JTextField(destinationAddress[2]);
		panel_OverviewDesCity.add(textField_OverviewDesCity);
		
		JPanel panel_OverviewDesCountry = new JPanel();
		panelDes.add(panel_OverviewDesCountry);
		panel_OverviewDesCountry.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOverviewDesCountry = new JLabel("Country:");
		panel_OverviewDesCountry.add(lblOverviewDesCountry, BorderLayout.WEST);
		
		textField_OverviewDesCountry = new JTextField(destinationAddress[3]);
		panel_OverviewDesCountry.add(textField_OverviewDesCountry);
		
		JButton button_OverviewDesUpdate = new JButton("Update");
		panelDes.add(button_OverviewDesUpdate);
		
		button_OverviewDesUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (driver.getState() == Car.State.FREE || driver.getState() == Car.State.INACTIVE) {
					driver.setDestinationAddress(new String[] {textField_OverviewDesHnr.getText(), textField_OverviewDesStreet.getText(), textField_OverviewDesCity.getText(), textField_OverviewDesCountry.getText()});
				} else {
					String[] destination = driver.getDestinationAddress();
					textField_OverviewCurHnr.setText(destination[0]);
					textField_OverviewCurStreet.setText(destination[1]);
					textField_OverviewCurCity.setText(destination[2]);
					textField_OverviewCurCountry.setText(destination[3]);
				}
			}
		});

		
		
		
		
		JLabel lblOverviewTime = new JLabel("Current destination time (yyyy-MM-dd HH:mm:ss):");
		panelAddresses.add(lblOverviewTime);
		
		JPanel panel = new JPanel();
		panelAddresses.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton button_OverviewTimeUpdate = new JButton("Update");
		panel.add(button_OverviewTimeUpdate, BorderLayout.EAST);
		
		button_OverviewTimeUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (driver.getState() == Car.State.FREE || driver.getState() == Car.State.INACTIVE ) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        TimeZone tz = TimeZone.getTimeZone("GMT+1");
					sdf.setTimeZone(tz);

			        Date date = null;
					try {
						date = sdf.parse(textField_OverviewTime.getText());
						driver.setDestinationTime(date.getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					textField_OverviewTime.setText(new Date(driver.getDestinationTime()).toString());
				}
			}
		});
		
		textField_OverviewTime = new JTextField(new Date(driver.getDestinationTime()).toString());
		panel.add(textField_OverviewTime, BorderLayout.CENTER);
		textField_OverviewTime.setColumns(10);
		
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public int getDriverID() {
		return this.driver.getID();
	}
	
	public void updateState() {
		lblCurrentState.setText(driver.getState().name());
	}
	
	private String sendUpdate(String message) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(ServerController.BASE_URI + RessourceDriverState.PATH + "/");
		String response = target.request().accept(MediaType.TEXT_PLAIN_TYPE)
				.post(Entity.entity(message, MediaType.APPLICATION_JSON), String.class);
		client.close();
		return response;
	}
}
