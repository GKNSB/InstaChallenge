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
public class ImageSequence {
    
    public ImageSequence(BufferedImage[] imageChunks) throws IOException {
        int chunkPopulation = 0;
        int i = 0;
        boolean exists;
        for (int z = 0; z < 100; z++){
            File checker = new File("img" + z + ".png");
            exists = checker.exists();
            if (!exists){

            }
            else{
                chunkPopulation = chunkPopulation + 1;
            }
        }
        System.out.println("Number of chunks is " + chunkPopulation);
 
        File[] images = new File[chunkPopulation];
        BufferedImage[] bufimages = new BufferedImage[chunkPopulation]; 

        Raster[] rasters = new Raster[chunkPopulation];
        for (int j = 0; j < chunkPopulation; j++) {
            images[j] = new File("img" + j + ".png");
            bufimages[j] = ImageIO.read(images[j]);
            rasters[j] = bufimages[j].getData();
        }
        System.out.println("Chunk image files must have entered the buffer");

        int[][] orderOfChunks = new int[2][chunkPopulation];

        System.out.println("Ordering array initialized");
        int rgb = bufimages[5].getRGB(2, 20);
        System.out.println("I should see an int if buffered -> (" + rgb + ")");


        //ALL OF THE BELOW
        //start comparisons, fill arrays
        double similars = 0;
        double rows = rasters[1].getHeight();
        int numBands = rasters[1].getNumBands();
        int columns = rasters[1].getWidth();
        int similarsAll[][] = new int[chunkPopulation][chunkPopulation];
        for (i = 0; i < chunkPopulation; i++) {
            for(int j = 0; j < chunkPopulation; j++) {
                for (int l = 0; l < numBands; l++) {
                    for (int k = 0; k < rows; k++) {
                        //int rgb1 = bufimages[i].getRGB(0 , k);
                        //int rgb2 = bufimages[j].getRGB(columns -1 , k);
                        if (rasters[i].getSample(0, k, l) == rasters[j].getSample(columns-1, k ,l)) {similars = similars+1;}
                    }
                }
                similarsAll[i][j] = (int)similars;
                if(i == j){    
                    similarsAll[i][j] = 0;
                }
              // if (i == 8) {System.out.println("H "+ j +" prin thn " +i+ " similars " + similarsAll[i][j] );}
              /*if (similars > 55 && i!=j) {
                    System.out.println("MATCH!! image "+j+" should be placed before image "+i);
                }*/
                similars = 0;
            }

        }

        for(i = 0; i < chunkPopulation; i++ ){
            boolean match = false;
            int position = -1;
            int max = 0;
            int j;
            int times = 0;
            while (match == false) {
                for (j = 0; j < chunkPopulation; j++) {
                    if (similarsAll[i][j] > max) {
                        max = similarsAll[i][j];
                        position = j;
                    }
                }
                times = times +1;
                if (times == chunkPopulation){match = true;}
                //System.out.println(max);
                for (int n = 0; n < chunkPopulation; n++) {
                    if (similarsAll[n][position] > max) {
                        max=0;
                        similarsAll[i][position] = 0;
                    }
                }
                if(max != 0) {
                    match = true;
                }
                if (match == true) {
                    for(int eraser = 0; eraser < chunkPopulation; eraser++) {
                        if(similarsAll[i][eraser]!=max) { similarsAll[i][eraser] = 0;}
                    }
                }
            }
            if (times == chunkPopulation) {
                //System.out.println("Probable first piece is " + i);
                orderOfChunks[0][i] = -1;
                orderOfChunks[1][i] = i;
            }
            else{
                //System.out.println("MATCH!! image " + position + " should be placed before image " + i);
                orderOfChunks[0][i] = position;
                orderOfChunks[1][i] = i;
            }

        }

        int[] finalOrder = new int[chunkPopulation];
        for (int y = 0; y < chunkPopulation; y++) {
            if (orderOfChunks[0][y] == -1) {
                finalOrder[0] = orderOfChunks[1][y];
            }
            //System.out.println(orderOfChunks[0][y] + " " + orderOfChunks[1][y]);
        }

        int next = finalOrder[0];
        for (int y = 1; y < chunkPopulation; y++) {
            for (int z = 0; z < chunkPopulation; z++) {
                if (orderOfChunks[0][z] == next) {
                    finalOrder[y] = orderOfChunks[1][z];
                    next = finalOrder[y];
                    z = chunkPopulation;
                }
            }
        }

        //APO EDW KAI KATW
        /*System.out.println("Correct order: ");
        for(int w = 0; w < chunkPopulation; w++){
            System.out.print(" " +finalOrder[w]);
        }
        System.out.println("");
        System.out.println("Ended");*/
        ImageMerge last = new ImageMerge(finalOrder,bufimages);
    }

}
