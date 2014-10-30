/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.awt.image.Raster;
/**
 *
 * @author Manos
 */
public class ImageMerge {
    private int[] order;
    private BufferedImage[] chunks;
    private int chunkWidth, chunkHeight, type;
    File[] toDelete;
    public ImageMerge(int[] correctOrder, BufferedImage[] bufimages) throws IOException{

        order = correctOrder;
        chunks = bufimages;

        chunkWidth=chunks[0].getWidth();
        chunkHeight=chunks[0].getHeight();
        type=chunks[0].getType();

        int rows = 1;
        int cols = order.length;

        BufferedImage finalImage = new BufferedImage(chunkWidth*cols, chunkHeight*rows, type);

        int num = 0;
        for(int x = 0; x < rows; x++){
            for (int y = 0; y < cols; y++){
                finalImage.createGraphics().drawImage(chunks[order[num]], chunkWidth*y, chunkHeight*x, null);
                num++;
            }
        }

        ImageIO.write(finalImage, "jpg", new File("Output.png"));
        toDelete = new File[order.length];
        for(int i = 0; i < order.length; i++){
            toDelete[i] = new File("img" + i + ".png");
            if (!toDelete[i].exists()){
                System.out.println("Not a file");
            }
            else{
                toDelete[i].delete();
            }
        }

    }

}
