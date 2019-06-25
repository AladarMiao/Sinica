import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class SeamCarver {
    private int width;
    private int height;
    private Picture pic;
    private Color colorleft = new Color(256);
    private Color colorright = new Color(256);
    private Color colorup = new Color(256);
    private Color colordown = new Color(256);



    public SeamCarver(Picture picture) {
        if (picture.width() == 0 || picture.height() == 0) {
            throw new IllegalArgumentException("width and height cannot be 0!");
        }

        this.pic = new Picture(picture);
        this.height = this.pic.height();
        this.width = this.pic.width();
    }


    public Picture picture() {

        return this.pic;
    }                 // current picture

    public int width() {
        return this.width;
    }                        // width of current picture

    public int height() {
        return this.height;
    }                       // height of current picture

    private double gradient(Color x, Color y) {
        double reddiff = Math.pow((x.getRed() - y.getRed()), 2);
        double greendiff = Math.pow((x.getGreen() - y.getGreen()), 2);
        double bluediff = Math.pow((x.getBlue() - y.getBlue()), 2);
        return (reddiff + greendiff + bluediff);
    }

    private void normalenergy(Color a, Color b, Color c,
                              Color d, int x, int y) {
        if (x == width - 1 && y == height - 1) {
            colorleft = pic.get(x - 1, y);
            colorright = pic.get(0, y);
            colorup = pic.get(x, y - 1);
            colordown = pic.get(x, 0);
        } else if (x == 0 && y == 0) {
            colorleft = pic.get(width - 1, y);
            colorright = pic.get(x + 1, y);
            colorup = pic.get(x, height - 1);
            colordown = pic.get(x, y + 1);
        } else if (x == 0 && y == height - 1) {

            colorleft = pic.get(width - 1, y);
            colorright = pic.get(x + 1, y);
            colorup = pic.get(x, y - 1);
            colordown = pic.get(x, 0);

        } else if (x == width - 1 && y == 0) {

            colorleft = pic.get(x - 1, y);
            colorright = pic.get(0, y);
            colorup = pic.get(x, height - 1);
            colordown = pic.get(x, y + 1);

        } else if (x == width - 1) {
            colorleft = pic.get(x - 1, y);
            colorright = pic.get(0, y);
            colorup = pic.get(x, y - 1);
            colordown = pic.get(x, y + 1);
        } else if (x == 0) {
            colorleft = pic.get(width - 1, y);
            colorright = pic.get(x + 1, y);
            colorup = pic.get(x, y - 1);
            colordown = pic.get(x, y + 1);
        } else if (y == height - 1) {
            colorleft = pic.get(x - 1, y);
            colorright = pic.get(x + 1, y);
            colorup = pic.get(x, y - 1);
            colordown = pic.get(x, 0);
        } else if (y == 0) {
            colorleft = pic.get(x - 1, y);
            colorright = pic.get(x + 1, y);
            colorup = pic.get(x, height - 1);
            colordown = pic.get(x, y + 1);
        } else {
            colorleft = pic.get(x - 1, y);
            colorright = pic.get(x + 1, y);
            colorup = pic.get(x, y - 1);
            colordown = pic.get(x, y + 1);
        }
    }

    public double energy(int x, int y) {

        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("x or y is outside its prescribed range!");
        }
        if (height != 1 && width != 1) {
            normalenergy(colorleft, colorright,
                    colorup, colordown, x, y);
        } else if (height == 1 && width == 1) {
            colorleft = pic.get(x, y);
            colorright = pic.get(x, y);
            colorup = pic.get(x, y);
            colordown = pic.get(x, y);

        } else if (height == 1) {
            if (x == 0) {
                colorleft = pic.get(width - 1, y);
                colorright = pic.get(x + 1, y);
                colorup = pic.get(x, y);
                colordown = pic.get(x, y);
            } else if (x == width - 1) {
                colorleft = pic.get(x - 1, y);
                colorright = pic.get(0, y);
                colorup = pic.get(x, y);
                colordown = pic.get(x, y);
            } else {
                colorleft = pic.get(x - 1, y);
                colorright = pic.get(x + 1, y);
                colorup = pic.get(x, y);
                colordown = pic.get(x, y);
            }
        } else if (width == 1) {
            if (y == height - 1) {
                colorleft = pic.get(x, y);
                colorright = pic.get(x, y);
                colorup = pic.get(x, y - 1);
                colordown = pic.get(x, 0);
            } else if (y == 0) {
                colorleft = pic.get(x, y);
                colorright = pic.get(x, y);
                colorup = pic.get(x, height - 1);
                colordown = pic.get(x, y + 1);
            } else {
                colorleft = pic.get(x, y);
                colorright = pic.get(x, y);
                colorup = pic.get(x, y - 1);
                colordown = pic.get(x, y + 1);
            }
        }
        double gradx = gradient(colorleft, colorright);
        double grady = gradient(colorup, colordown);

        return gradx + grady;
    }

    public int[] findHorizontalSeam() {
        Picture hori = new Picture(height, width);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                hori.set(x, y, pic.get(y, x));
            }
        }

        SeamCarver temp = new SeamCarver(hori);
        return temp.findVerticalSeam();


    }            // sequence of indices for horizontal seam


    public int[] findVerticalSeam() {
        int[] seam = new int[height];
        if (width == 1) {
            return seam;
        }

        Map<Double, Integer> energytopos = new HashMap<>();

        double[][] arraysums = new double[height][width];
        for (int x = 0; x < width; x++) { //initialize first row

            arraysums[0][x] = energy(x, 0);
        }
        sumof(energytopos, arraysums);

        int index = 0;
        double smallest = Integer.MAX_VALUE;
        for (int i = 0; i < width; i++) {
            if (arraysums[height - 1][i] < smallest) {
                smallest = arraysums[height - 1][i];
                index = i;
            }
        }
        Map<Double, Integer> energytopo = new HashMap<>();
        seam[height - 1] = index;
        int colu = index;

        for (int x = height - 2; x >= 0; x--) {
            if (colu == 0) {
                double min1 = arraysums[x][colu];
                double min2 = arraysums[x][colu + 1];
                energytopo.put(min1, colu);
                energytopo.put(min2, colu + 1);
                double min = Math.min(min1, min2);
                for (double keys : energytopo.keySet()) {
                    if (keys == min) {
                        colu = energytopo.get(keys);
                    }
                }
                seam[x] = colu;
            } else if (colu == width - 1) {
                double min1 = arraysums[x][colu];
                double min2 = arraysums[x][colu - 1];
                energytopo.put(min1, colu);
                energytopo.put(min2, colu - 1);
                double min = Math.min(min1, min2);
                for (double keys : energytopo.keySet()) {
                    if (keys == min) {
                        colu = energytopo.get(keys);
                    }
                }
                seam[x] = colu;
            } else {
                double min1 = arraysums[x][colu];
                double min2 = arraysums[x][colu + 1];
                double min3 = arraysums[x][colu - 1];
                double min4 = Math.min(min1, min2);
                energytopo.put(min1, colu);
                energytopo.put(min2, colu + 1);
                energytopo.put(min3, colu - 1);
                double min = Math.min(min4, min3);
                for (double keys : energytopo.keySet()) {
                    if (keys == min) {
                        colu = energytopo.get(keys);
                    }
                }
                seam[x] = colu;
            }

        }

        return seam;
    }

    private void sumof(Map<Double, Integer> energytopos, double[][] arraysums) {
        for (int a = 1; a < height; a++) {
            for (int y = 0; y < width; y++) {
                double curr = energy(y, a);
                if (y == 0) {
                    double helper = curr + arraysums[a - 1][y];
                    double helper2 = curr + arraysums[a - 1][y + 1];
                    energytopos.put(helper, y);
                    energytopos.put(helper2, y + 1);
                    double helper3 = Math.min(helper, helper2);

                    arraysums[a][y] = helper3;
                } else if (y == width - 1) {
                    double helper = curr + arraysums[a - 1][y - 1];
                    double helper2 = curr + arraysums[a - 1][y];
                    energytopos.put(helper, y - 1);
                    energytopos.put(helper2, y);
                    double helper3 = Math.min(helper, helper2);


                    arraysums[a][y] = helper3;
                } else {
                    double helper = curr + arraysums[a - 1][y];
                    double helper2 = curr + arraysums[a - 1][y + 1];
                    double helper3 = curr + arraysums[a - 1][y - 1];
                    double helper4 = Math.min(helper, helper2);
                    double helper5 = Math.min(helper4, helper3);
                    energytopos.put(helper, y);
                    energytopos.put(helper2, y + 1);
                    energytopos.put(helper3, y - 1);

                    arraysums[a][y] = helper5;
                }

            }
        }
    }
    // sequence of indices for vertical seam

    public void removeHorizontalSeam(int[] seam) {
        pic = SeamRemover.removeHorizontalSeam(pic, seam);
        width = pic.width();
        height = pic.height();

    }  // remove horizontal seam from picture

    public void removeVerticalSeam(int[] seam) {
        pic = SeamRemover.removeVerticalSeam(pic, seam);
        width = pic.width();
        height = pic.height();
    }

}

