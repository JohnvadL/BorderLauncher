package com.border.launcherfree.vision;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.border.launcherfree.R;
import com.border.launcherfree.initialization.Initialization;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


public class VisionHelper {


    public static void getNewImage(Bitmap bmp, String packageName, Context context) {

        if (Initialization.initializing) {
            Initialization.updateProgress();
        }

        System.loadLibrary("opencv_java3");
        Mat input = new Mat();
        // Convert image to bmp
        Bitmap image = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(image, input);
        Mat output = new Mat();

        // Aperture size is problematic: Causes textured outputs
        Imgproc.Canny(input, output, 100, 200, 3, false);

        Log.e("VisionHelper", "TEST");
        Log.e("VisionHelper", packageName);

        Mat alpha = new Mat();
        Imgproc.threshold(output, alpha, 0, 255, Imgproc.THRESH_BINARY);

        // TODO: Use Dilate to make icons better
        Mat m = new Mat(new Size(4, 4), CvType.CV_8U, new Scalar(1));

        Imgproc.cvtColor(output, output, Imgproc.COLOR_GRAY2BGRA);
        SharedPreferences preferences = context.getSharedPreferences
                (context.getString(R.string.preference_key), Context.MODE_PRIVATE);

        int mean_0;
        int mean_1;
        int mean_2;
        int intColor = preferences.getInt(packageName, 2147483647);
        Log.e("VisionHelper", Integer.toString(intColor));
        if (intColor != 2147483647) {
            mean_0 = (intColor >> 16) & 0xFF;
            mean_1 = (intColor >> 8) & 0xFF;
            mean_2 = (intColor) & 0xFF;
            String hex = String.format("#%02x%02x%02x", mean_0, mean_1, mean_2);
            Log.e("VisionHElper", hex);
        } else {
            mean_0 = 255;
            mean_1 = 255;
            mean_2 = 255;
            Log.e("VisionHelper", "INTCOLOR IS ZERO");
        }

        if (mean_0 > 100 || mean_1 > 100 || mean_2 > 100) {
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
}
