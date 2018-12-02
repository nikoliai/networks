package com.hm.tfour.server.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.hm.tfour.client.ui.ClientPanel;
import com.hm.tfour.server.ServerController;
import com.hm.tfour.server.model.Car;
/**
 * 
 * @author Thilo Barth
 *
 */
public class ServerCenter {

	private JFrame frame;
	private JPanel contentPane;
	private ArrayList<Car> carList;
	private ServerController controller;

	/**
	 * Create the application.
	 */
	public ServerCenter(ArrayList<Car> driverList) {
		this.carList = driverList;
		controller = new ServerController(driverList);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1000, 400);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		for (Car car : carList) {
			tabbedPane.addTab("Driver: " + car.getID(), new ClientPanel(car));
		}
		
		JPanel panelButtons = new JPanel();
		contentPane.add(panelButtons, BorderLayout.WEST);
		panelButtons.setLayout(new GridLayout(2, 1, 0, 0));
		
		JButton btnDriverAdd = new JButton("Add");
		panelButtons.add(btnDriverAdd);
		btnDriverAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] startAddress = new String[4];
				startAddress[0] = JOptionPane.showInputDialog("Please insert Start House nr:");
				startAddress[1] = JOptionPane.showInputDialog("Please insert Start Street:");
				startAddress[2] = JOptionPane.showInputDialog("Please insert Start City:");
				startAddress[3] = JOptionPane.showInputDialog("Please insert Start Country:");
				
				String[] destinationAddress = new String[4];
				destinationAddress[0] = JOptionPane.showInputDialog("Please insert Destination House nr:");
				destinationAddress[1] = JOptionPane.showInputDialog("Please insert Destination Street:");
				destinationAddress[2] = JOptionPane.showInputDialog("Please insert Destination City:");
				destinationAddress[3] = JOptionPane.showInputDialog("Please insert Destination Country:");
				
				String destinationTime = JOptionPane.showInputDialog("Please insert a destination time (yyyy-MM-dd HH:mm:ss):");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		        Date date = null;
				try {
					date = sdf.parse(destinationTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				Car newDriver = controller.addCar(startAddress, destinationAddress, date.getTime());
				tabbedPane.addTab("Driver: " + newDriver.getID(), new ClientPanel(newDriver));
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
				for (int i = 0; i < carList.size(); i++) {
					if (driverID == carList.get(i).getID()) {
						driverIndex = i;
						break;
					}
				}
				carList.remove(driverIndex);
				tabbedPane.remove(selectedTab);
			}
		});
		
		frame.setVisible(true);
	}
	/**
	 * Initializes the cars with default data and starts ServerSenter
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Car> carList = new ArrayList<>();
		carList.add(new Car(1, new String[] {"9", "Hauptstraße", "Hamburg", "Germany"}, new String[] {"1", "Kirchenweg", "Gilching", "Germany"}, System.currentTimeMillis() + 3600000));
		carList.add(new Car(2, new String[] {"9", "Hauptstraße", "Hamburg", "Germany"}, new String[] {"64", "Lothstraße", "München", "Germany"}, System.currentTimeMillis() + 3600000));
		carList.add(new Car(3, new String[] {"9", "Hauptstraße", "Hamburg", "Germany"}, new String[] {"1", "Am bahnhof", "Gilching", "Germany"}, System.currentTimeMillis() + 3600000));
		new ServerCenter(carList);
	}

}
