package com.hm.simpleservice.server.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.hm.simpleservice.client.RestPostUpdate;
import com.hm.simpleservice.client.ui.ClientPanel;
import com.hm.simpleservice.server.ServerController;
import com.hm.simpleservice.server.model.Car;

public class ServerCenter {

	private JFrame frame;
	private JPanel contentPane;
	private ArrayList<Car> driverList;
	private ServerController controller;
	private RestPostUpdate driverUpdater;

	/**
	 * Create the application.
	 */
	public ServerCenter(ArrayList<Car> driverList) {
		this.driverList = driverList;
		controller = new ServerController(driverList);
		driverUpdater = new RestPostUpdate("127.0.0.1", "80");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1000, 1000);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelButtons = new JPanel();
		contentPane.add(panelButtons, BorderLayout.WEST);
		panelButtons.setLayout(new GridLayout(2, 1, 0, 0));
		
		JButton btnDriverAdd = new JButton("Add");
		panelButtons.add(btnDriverAdd);
		btnDriverAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Car newDriver = controller.addCar("", "");
				tabbedPane.addTab("Driver: " + newDriver.getID(), new ClientPanel(newDriver, driverUpdater));
			}
		});
		
		JButton btnDriverRem = new JButton("Delete");
		panelButtons.add(btnDriverRem);
		btnDriverRem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Component selectedTab = tabbedPane.getSelectedComponent();
				int driverID = -1;
				if (selectedTab instanceof ClientPanel) {
					driverID = ((ClientPanel) selectedTab).getDriverID();
				}
				int driverIndex = 0;
				for (int i = 0; i < driverList.size(); i++) {
					if (driverID == driverList.get(i).getID()) {
						driverIndex = i;
						break;
					}
				}
				driverList.remove(driverIndex);
				tabbedPane.remove(selectedTab);
			}
		});
		
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new ServerCenter(new ArrayList<Car>());
	}

}
