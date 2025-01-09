import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/***
 * Idea from
 * https://blog.demofox.org/2023/10/22/how-to-make-your-own-spooky-magic-eye-pictures-autostereograms/
 */

public class Autostereogram {
	static double max_offset = 40;
	static BufferedImage tile;
	static BufferedImage dm;
	static BufferedImage output;

	private static void initTile() throws IOException {
		// Note that you needed MESSY/CROWDED wallpaper tiles

		tile = ImageIO.read(new File("imgfiles\\tile.jpeg"));

		/*-tile = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
		int black = Color.black.getRGB();
		for (int i = 0; i < tile.getWidth(); i++) {
			for (int j = 0; j < tile.getHeight(); j++) {
				if (Math.random() > 0.9) {
					tile.setRGB(j, i, black);
				}
			}
		}*/
	}

	private static void initDepthMap() throws IOException {
		dm = ImageIO.read(new File("imgfiles\\depthmap.jpg"));
	}

	public static void main(String[] args) throws IOException {
		// TODO: don't call img.getWidth() etc. multiple times, store in var.

		initTile();
		initDepthMap();
		output = new BufferedImage(dm.getWidth() + tile.getWidth(), dm.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);

		// Initialize left column
		for (int x = 0; x < tile.getWidth(); x++) {
			for (int y = 0; y < output.getHeight(); y++) {
				output.setRGB(x, y, tile.getRGB(x, y % tile.getHeight()));
			}
		}

		// Now for the rest of the image...
		for (int y = 0; y < dm.getHeight(); y++) {
			for (int x = 0; x < dm.getWidth(); x++) {
				output.setRGB(x + tile.getWidth(), y, output.getRGB((x + offset(x, y)), y));
			}
		}
		Graphics2D g = (Graphics2D) output.getGraphics();
		g.setColor(Color.white);
		g.fillRect(output.getWidth() / 2 - tile.getWidth() / 2 - 10, 10, 10, 10);
		g.fillRect(output.getWidth() / 2 + tile.getWidth() / 2 - 10, 10, 10, 10);

		ImageIO.write(output, "png", new File("imgfiles\\out.png"));
	}

	private static int offset(int x, int y) {
		double norm_depth = 1 - (double) (dm.getRGB(x, y) & 0xFF) / 255;
		return (int) (norm_depth * max_offset);
	}
}
