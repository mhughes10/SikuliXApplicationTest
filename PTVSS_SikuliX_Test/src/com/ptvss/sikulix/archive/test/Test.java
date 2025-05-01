package com.ptvss.sikulix.archive.test;

import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;
import org.sikuli.script.Match;

import org.sikuli.script.Region;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bytedeco.javacv.Java2DFrameUtils;
//import org.bytedeco.opencv.opencv_core.*;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.imgproc.Imgproc;
//import org.bytedeco.opencv.global.opencv_imgproc;
//import org.bytedeco.opencv.global.opencv_core;
//import org.bytedeco.opencv.global.opencv_imgcodecs;
//import org.bytedeco.opencv.opencv_core.Mat;
//import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_imgproc.*;
import org.opencv.core.MatOfPoint;
//import org.bytedeco.opencv.opencv_javafx.CanvasFrame;
import org.bytedeco.opencv.global.opencv_imgcodecs;

public class Test
	{

		public static void main(String[] args)
			{
				// TODO Auto-generated method stub
				
				Mat image = opencv_imgcodecs.imread("C:\\System Administrator\\SikuliX_Resources\\Images\\Test_Bar\\biggerbreaks.png");
				
				opencv_imgcodecs.imwrite("image_preview.png", image);
				
				System.out.println("break count: " + archiveBreakCount(image));

			}
		
		public static long archiveBreakCount(Mat image)
			{
				
				Mat greyScale = new Mat();
				opencv_imgproc.cvtColor(image, greyScale, opencv_imgproc.COLOR_BGR2GRAY);

				// Adaptive thresholding to better capture smaller breaks
				Mat binaryImage = new Mat();
				opencv_imgproc.adaptiveThreshold(greyScale, binaryImage, 255, opencv_imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, 
				                                 opencv_imgproc.THRESH_BINARY, 55, 2);

				// Morphological operations to enhance breaks detection
				Mat morphImage = new Mat();
				Mat kernel = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT, new Size(3,3));
				opencv_imgproc.morphologyEx(binaryImage, morphImage, opencv_imgproc.MORPH_OPEN, kernel);

				opencv_imgcodecs.imwrite("ProcessedImage_preview.png", morphImage);

				MatVector contours = new MatVector();
				Mat hierarchy = new Mat();

				// Use RETR_TREE to detect both outer and inner contours
				opencv_imgproc.findContours(morphImage, contours, hierarchy, opencv_imgproc.RETR_TREE, opencv_imgproc.CHAIN_APPROX_NONE);

				//System.out.println("Number of breaks detected: " + contours.size());
				return contours.size() -1;
			}

	}
