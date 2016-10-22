package com.akash.sudokusolver;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.opencv.imgproc.Imgproc.line;

public class SudokuSolve {

    public int[][] matrix=new int[9][9];

    public int[][] solve(Context context, String filepath, Integer type) throws IOException {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
            matrix[i][j]=0;
        }
        Mat img;
        if(type == 1) {
            img = Imgcodecs.imread(filepath);
//            TessBaseAPI api=new TessBaseAPI();
//            api.init(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SudokuSolver/", "eng");
//            api.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "123456789");
//            api.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "qwertyuioplkjhgfdsazxcvbnm|-/*-+!@#$%^&*()\'\":;><?");
//            Bitmap bitmap = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
//            api.setImage(bitmap);
//            String recognizedText = api.getUTF8Text();
//            Log.d("recogtext", recognizedText);
        }
        else {
            img = Utils.loadResource(context,R.drawable.index12);
        }
        Mat gray = new Mat();
        Log.d("ERRORCVT", img.channels() + " " + img.width() + " " + img.height());
        if(img.channels() > 1) {
            Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        }
        else{
            gray = img.clone();
        }
        Imgproc.GaussianBlur(gray, gray, new Size(11, 11), 0);
        Imgproc.adaptiveThreshold(gray, gray, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 2);
        //Imgproc.adaptiveThreshold(gray, gray, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 2);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat heirarchy = new Mat();
        Imgproc.findContours(gray, contours, heirarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        int h = img.height();
        int w = img.width();
        Double max = (double) 0;
        MatOfPoint2f biggest = new MatOfPoint2f();
        for (MatOfPoint m : contours) {
            MatOfPoint2f a = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(m.toArray()), a, 4, true);
            if (a.total() != 4) {
                continue;
            }
            MatOfPoint am = new MatOfPoint();
            a.convertTo(am, CvType.CV_32S);
            if (!Imgproc.isContourConvex(am)) {
                continue;
            }
            Double sizeApprox = Imgproc.contourArea(a);
            if (sizeApprox > max) {
                max = sizeApprox;
                biggest = a;
            }
        }
        Point[] points = biggest.toArray();
        Mat copyImage = img.clone();
        line(copyImage, new Point(points[0].x, points[0].y), new Point(points[1].x, points[1].y), new Scalar(255, 0, 0), 2);
        line(copyImage, new Point(points[1].x, points[1].y), new Point(points[2].x, points[2].y), new Scalar(255, 0, 0), 2);
        line(copyImage, new Point(points[2].x, points[2].y), new Point(points[3].x, points[3].y), new Scalar(255, 0, 0), 2);
        line(copyImage, new Point(points[3].x, points[3].y), new Point(points[0].x, points[0].y), new Scalar(255, 0, 0), 2);
        List<Point> pointList = biggest.toList();
        int y = 0, s2 = 0;
        double min = points[0].y;
        for (int i = 1; i < 4; i++) {
            if (min > points[i].y) {
                y = i;
                min = points[i].y;
            }
        }
        boolean flag = false;
        for (int i = 0; i < 4; i++) {
            if (i != y) {
                if (!flag) {
                    min = Math.abs(points[y].y - points[i].y);
                    flag = true;
                    s2 = i;
                } else if (min > Math.abs(points[y].y - points[i].y)) {
                    s2 = i;
                    min = Math.abs(points[y].y - points[i].y);
                }
            }
        }
        List<Point> pointListOri = new ArrayList<>();
        if (points[y].x > points[s2].x) {
            pointListOri.add(points[s2]);
            pointListOri.add(points[y]);
        } else {
            pointListOri.add(points[y]);
            pointListOri.add(points[s2]);
        }
        Integer[] a = {0, 0, 0, 0};
        a[y] = 1;
        a[s2] = 1;
        int j = 0;
        Integer[] s3 = new Integer[2];
        for (int i = 0; i < 4; i++) {
            if (a[i] == 0)
                s3[j++] = i;
        }
        if (points[s3[0]].x > points[s3[1]].x) {
            pointListOri.add(points[s3[0]]);
            pointListOri.add(points[s3[1]]);
        } else {
            pointListOri.add(points[s3[1]]);
            pointListOri.add(points[s3[0]]);
        }
        MatOfPoint2f s = new MatOfPoint2f();
        s.fromList(pointListOri);
        Size reshape = new Size(180, 180);
        Mat undistorted = new Mat(reshape, CvType.CV_8UC1);
        MatOfPoint2f d = new MatOfPoint2f();
        d.fromArray(new Point(0, 0), new Point(reshape.height, 0),
                new Point(reshape.width, reshape.height), new Point(0, reshape.width));
        Mat tranformMatrix = Imgproc.getPerspectiveTransform(s, d);
        Imgproc.warpPerspective(img, undistorted, tranformMatrix, reshape);
        //Highgui.imwrite("output.png", undistorted);

        Size cell = new Size(20, 20);
        Mat cellmat = new Mat(reshape, CvType.CV_8UC1);
        int dw = 0, dh = 0;
        TessBaseAPI api=new TessBaseAPI();
        api.init(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SudokuSolver/", "eng");
        api.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "123456789");
        for (int i = 0; i < 9; i++) {
            dw = 0;
            for (int k = 0; k < 9; k++) {
                MatOfPoint2f cellsrc = new MatOfPoint2f();
                MatOfPoint2f celldest = new MatOfPoint2f();
                cellsrc.fromArray(new Point(5 + dw, 2 + dh), new Point(cell.height - 5 + dw, 2 + dh),
                        new Point(cell.width - 5 + dw, cell.height - 2 + dh), new Point(5 + dw, cell.width - 2 + dh));
                celldest.fromArray(new Point(0, 0), new Point(cell.height, 0),
                        new Point(cell.width, cell.height), new Point(0, cell.width));
                Mat tranformCell = Imgproc.getPerspectiveTransform(cellsrc, celldest);
                Imgproc.warpPerspective(undistorted, cellmat, tranformCell, cell);
                if(cellmat.channels() > 1) {
                    Imgproc.cvtColor(cellmat, cellmat, Imgproc.COLOR_BGR2GRAY);
                }
                //imwrite("cell.png", cellmat);
                //Bitmap bitmap = BitmapFactory.decodeResource(cellmat);
                Bitmap bitmap = Bitmap.createBitmap(cellmat.cols(), cellmat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(cellmat, bitmap);
                api.setImage(bitmap);
                String recognizedText = api.getUTF8Text();
                Log.d(i+"recogtext"+k, recognizedText);
                if(!recognizedText.isEmpty()) {
                    matrix[i][k] = Integer.valueOf(recognizedText);
                }else{
                    matrix[i][k] = 0;
                }
                dw += 20;
            }
            dh += 20;
        }
        api.end();
        Log.d("matrix", Arrays.deepToString(matrix));
        SolverMatrix m=new SolverMatrix(matrix);
        int[][] solved = m.getSolution(0,0);

        Log.d("matrixAns", Arrays.deepToString(solved));
        return solved;
    }
}
