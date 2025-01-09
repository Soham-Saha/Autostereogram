import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/***
 * @Website https://blog.demofox.org/2023/10/22/how-to-make-your-own-spooky-magic-eye-pictures-autostereograms/
 */

public class Autostereogram {
	static double max_offset = 20;
	static BufferedImage tile;
	static BufferedImage dm;
	static BufferedImage img;
	static int tileside;

	public static void main(String[] args) throws IOException {
		// TODO: don't call img.getWidth() etc. multiple times, store in var.
		// Note that you needed MESSY/CROWDED wallpaper tiles
		tile = ImageIO.read(new File("imgfiles\\tile2.jpeg"));
		dm = ImageIO.read(new File("imgfiles\\depthmap.jpg"));
		if (tile.getWidth() != tile.getHeight()) {
			//TODO: most prob not needed anymore.
			System.err.println("Tile width and height different. Not supported. Get coding!");
			System.exit(0);
		}
		tileside = tile.getWidth();
		img = new BufferedImage(dm.getWidth() + tileside, dm.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);

		// Initialize left column
		for (int x = 0; x < tileside; x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				img.setRGB(x, y, tile.getRGB(x, y % tileside));
			}
		}

		// Now for the rest of the image...
		for (int y = 0; y < dm.getHeight(); y++) {
			for (int x = 0; x < dm.getWidth(); x++) {
				img.setRGB(x + tile.getWidth(), y, img.getRGB((x + offset(x, y)), y));
			}
		}
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(img.getWidth() / 2 - tileside / 2 - 5, 5, 10, 10);
		g.fillRect(img.getWidth() / 2 + tileside / 2 - 5, 5, 10, 10);

		ImageIO.write(img, "png", new File("imgfiles\\out.png"));
	}

	private static int offset(int x, int y) {
		double norm_depth = 1 - (double) new Color(dm.getRGB(x, y)).getRed() / 255;
		return (int) (norm_depth * max_offset);
	}
}
