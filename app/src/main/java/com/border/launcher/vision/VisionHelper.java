package com.border.launcher.vision;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.border.launcher.R;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


public class VisionHelper {

    public static Bitmap colorLogo(Bitmap bmp, Context context) {

        String a = "a";
        Integer.parseInt(a, 16);
        return null;
    }

    public static void getNewImage(Bitmap bmp, String packageName, Context context) {


        System.loadLibrary("opencv_java3");

        Mat input = new Mat();
        // Convert image to bmp
        Bitmap image = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(image, input);
        Mat output = new Mat();

        // Aperture size is problematic: Causes textured outputs
        Imgproc.Canny(input, output, 100, 200, 3, false);
        // Imgproc.threshold(output, output, 100, 255, Imgproc.THRESH_BINARY);

        Log.e("VisionHelper", "TEST");
        Log.e("VisionHelper", packageName);

        Mat alpha = new Mat();
        Imgproc.threshold(output, alpha, 0, 255, Imgproc.THRESH_BINARY);

        // TODO: Use Dilate to make icons better
        Mat m = new Mat(new Size(4, 4), CvType.CV_8U, new Scalar(1));


        Imgproc.cvtColor(output, output, Imgproc.COLOR_GRAY2BGRA);
        float sum_0 = 0;
        float sum_1 = 0;
        float sum_2 = 0;

        for (int i = 0; i < output.rows(); i++) {
            for (int j = 0; j < output.cols(); j++) {
                sum_0 += input.get(i, j)[0];
                sum_1 += input.get(i, j)[1];
                sum_2 += input.get(i, j)[2];
            }
        }

        int mean_0 = (int) sum_0 / (input.rows() * input.cols());
        int mean_1 = (int) sum_1 / (input.rows() * input.cols());
        int mean_2 = (int) sum_2 / (input.rows() * input.cols());

        int max_mean = Math.max(Math.max(mean_0, mean_1), mean_2);

        double ratio_0 = 0.6;
        double ratio_1 = 0.6;
        double ratio_2 = 0.6;


        if (max_mean == mean_0) {
            ratio_0 = 1.5;
        } else if (max_mean == mean_1) {
            ratio_1 = 1.5;
        } else {
            ratio_2 = 1.5;
        }
        SharedPreferences preferences = context.getSharedPreferences
                (context.getString(R.string.preference_key), Context.MODE_PRIVATE);

        // Convert HEX to RGB?
        // Check if there is a
        // String hex = String.format("#%02x%02x%02x", r, g,b);

        mean_0 = (int) Math.min(mean_0 + ratio_0 * mean_0, 255);
        mean_1 = (int) Math.min(mean_1 + ratio_1 * mean_1, 255);
        mean_2 = (int) Math.min(mean_2 + ratio_2 * mean_2, 255);


        int intColor = preferences.getInt(packageName, -1);
        Log.e("VisionHelper", Integer.toString(intColor));
        if (intColor != -1) {
            mean_0 = (intColor >> 16) & 0xFF;
            mean_1 = (intColor >> 8) & 0xFF;
            mean_2 = (intColor) & 0xFF;
            String hex = String.format("#%02x%02x%02x", mean_0, mean_1, mean_2);
            Log.e("VisionHElper", hex);
        } else {
            Log.e("VisionHelper", "INTCOLOR IS ZERO");
        }

        if (mean_0 > 100 || mean_1 > 100 || mean_2 > 100) {
            Log.e("VisionHelper", "Dilate");
            Imgproc.dilate(output, output, m);
        }


        // TODO: Add code for output refinement
        for (int i = 0; i < output.rows(); i++) {
            for (int j = 0; j < output.cols(); j++) {
                double[] value = output.get(i, j);
                if (value[0] + value[1] + value[2] == 0) {
                    value[3] = 0;
                } else {
                    value[2] = mean_2;
                    value[1] = mean_1;
                    value[0] = mean_0;
                }
                output.put(i, j, value);
            }
        }
        Size size = new Size(3, 3);
        Imgproc.blur(output, output, size);
        Utils.matToBitmap(output, bmp);
    }

    public static void getNewImageSharedPref(Bitmap bmp, Context context) {

        System.loadLibrary("opencv_java3");

        Mat input = new Mat();
        // Convert image to bmp
        Bitmap image = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(image, input);
        Mat output = new Mat();

        // Aperture size is problematic: Causes textured outputs
        Imgproc.Canny(input, output, 100, 200, 3, false);
        Imgproc.threshold(output, output, 100, 255, Imgproc.THRESH_BINARY);

        Log.e("VisionHelper", Integer.toString(output.dims()));

        Mat alpha = new Mat();
        // Imgproc.threshold(output, alpha, 0, 255, Imgproc.THRESH_BINARY);

        // TODO: Use Dilate to make icons better
        Mat m = new Mat(new Size(4, 4), CvType.CV_8U, new Scalar(1));
        Imgproc.dilate(output, output, m);

        Imgproc.cvtColor(output, output, Imgproc.COLOR_GRAY2BGRA);
        float sum_0 = 0;
        float sum_1 = 0;
        float sum_2 = 0;

        for (int i = 0; i < output.rows(); i++) {
            for (int j = 0; j < output.cols(); j++) {
                sum_0 += input.get(i, j)[0];
                sum_1 += input.get(i, j)[1];
                sum_2 += input.get(i, j)[2];
            }
        }

        int mean_0 = (int) sum_0 / (input.rows() * input.cols());
        int mean_1 = (int) sum_1 / (input.rows() * input.cols());
        int mean_2 = (int) sum_2 / (input.rows() * input.cols());

        int max_mean = Math.max(Math.max(mean_0, mean_1), mean_2);

        double ratio_0 = 0.6;
        double ratio_1 = 0.6;
        double ratio_2 = 0.6;

        if (max_mean == mean_0) {
            ratio_0 = 1.5;
        } else if (max_mean == mean_1) {
            ratio_1 = 1.5;
        } else {
            ratio_2 = 1.5;
        }

        mean_0 = (int) Math.min(mean_0 + ratio_0 * mean_0, 255);
        mean_1 = (int) Math.min(mean_1 + ratio_1 * mean_1, 255);
        mean_2 = (int) Math.min(mean_2 + ratio_2 * mean_2, 255);

        for (int i = 0; i < output.rows(); i++) {
            for (int j = 0; j < output.cols(); j++) {
                double[] value = output.get(i, j);
                if (value[0] + value[1] + value[2] == 0) {
                    value[3] = 0;
                } else {
                    value[2] = mean_0;
                    value[1] = mean_1;
                    value[0] = mean_2;
                }
                output.put(i, j, value);
            }
        }
        // Size size = new Size(3,3);
        // Imgproc.blur(output, output, size);
        Utils.matToBitmap(output, bmp);
    }

}
