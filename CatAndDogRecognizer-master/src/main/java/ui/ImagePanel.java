package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public String defaultImg = "resources/No_image_available.gif";
	private BufferedImage bImg;
	private Image img;
	private InputStream iStream;

	public ImagePanel() throws IOException {
		iStream = new FileInputStream(defaultImg);
		setImg(iStream);
	}

	public void setImg(InputStream is) throws IOException {
		bImg = ImageIO.read(is);
		Image tempImage = bImg.getScaledInstance(400, 400, Image.SCALE_DEFAULT);
		setImage(tempImage);
		this.iStream = is;
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
    }

	public void setImage(Image i) {
		this.img = i;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
		repaint();
		updateUI();
	}

}