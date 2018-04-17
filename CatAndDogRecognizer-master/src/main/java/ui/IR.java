package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import vg16.VG16ForCat;
import vg16.PetType;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

public class IR {

	private JPanel mainPanel;
	private JFrame mainFrame;
	private ImagePanel IPanel;
	private JLabel response;
	private VG16ForCat vg16;
	private File file;
	private SpinnerNumberModel thresholdSize = new SpinnerNumberModel(0.50, 0.5, 1, 0.1);;
	private JSpinner threshold = new JSpinner(thresholdSize);

	public IR() throws Exception {

	}

	public void initIR() throws Exception {
		vg16 = new VG16ForCat();
		vg16.loadModel();

		mainFrame = initMainFrame();

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		JButton select = new JButton("Select Image");
		select.setBackground(new Color(59, 89, 182));
		select.setForeground(Color.WHITE);
		select.setFocusPainted(false);
		select.setFont(new Font("Tahoma", Font.BOLD, 18));
		JButton predict = new JButton("What type of animal?");
		predict.setBackground(new Color(59, 89, 182));
		predict.setForeground(Color.WHITE);
		predict.setFocusPainted(false);
		predict.setFont(new Font("Tahoma", Font.BOLD, 18));

		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory((new File("resources/animal_pictures").getAbsoluteFile()));
				int action = jfc.showOpenDialog(null);
				if (action == 0) {
					try {
						file = jfc.getSelectedFile();
						FileInputStream is = new FileInputStream(file);
						putImageOnScreen(is, IPanel);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				response.setText("");
			}
		});
		predict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					PetType type = vg16.detectCat(file, (Double) threshold.getValue());
					if (type == PetType.CAT) {
						response.setText("Cat");
						response.setForeground(Color.BLUE);
					} else if (type == PetType.DOG) {
						response.setText("Dog");
						response.setForeground(Color.BLUE);
					} else {
						response.setText("Neither a Cat or a Dog");
						response.setForeground(Color.BLACK);
					}
					mainPanel.updateUI();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});

		fillMainPanel(select, predict);
		mainPanel.setBackground(Color.WHITE);
		mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainFrame.setVisible(true);

	}

	private void fillMainPanel(JButton chooseButton, JButton predictButton) throws IOException {
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 0;
		c.weightx = 0;
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(chooseButton);
		buttonsPanel.add(predictButton);
		mainPanel.add(buttonsPanel, c);

		c.gridx = 1;
		c.gridy = 2;
		c.weighty = 1;
		c.weightx = 1;
		IPanel = new ImagePanel();
		mainPanel.add(IPanel, c);

		c.gridx = 1;
		c.gridy = 3;
		c.weighty = 0;
		c.weightx = 0;
		response = new JLabel();
		response.setFont(new Font("Tahoma", Font.BOLD, 70));
		mainPanel.add(response, c);
	}

	private JFrame initMainFrame() {
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Image Recognition");
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		mainFrame.setSize(800, 600);
		mainFrame.setLocationRelativeTo(null);
		ImageIcon imageIcon = new ImageIcon("resources/cat_image_icon.png");
		mainFrame.setIconImage(imageIcon.getImage());
		return mainFrame;
	}

	private void putImageOnScreen(InputStream is, ImagePanel ip) throws IOException {
		BufferedImage bi = ImageIO.read(is);
		IPanel.setImage(bi);
	}

}