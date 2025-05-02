package com.ptvss.sikulix.archive.test;

import org.sikuli.script.*;

import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bytedeco.javacv.CameraDevice.Settings;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;

public class ArchiveCheck
	{
		
		private static final Logger LOGGER = Logger.getLogger(ArchiveCheck.class.getName());
		
		static Screen screen = new Screen();
		
		static Match match;
		
		static Region region1;
		
		static Region region2;
		
		static String[] path = {"C:\\System Administrator\\SikuliX_Resources\\Images\\"};
		
		public static void test(String address, String port, String username, String password, List<String> cameraList)
		{
			FileHandler handler;
			
			try
				{
					handler = new FileHandler("C:\\System Administrator\\SikuliX_Resources\\Logs\\" + address.replace(".", "_")+ ".log", true);
					LOGGER.addHandler(handler);
					LOGGER.setLevel(Level.INFO);
				}
			
			catch (Exception e)
				{
					System.out.println(e);
				}
					
					//action("doubleClick", "PTVAS");
					action("click", "connectToOtherServer");
					action("type", "serverAddress", address);
					action("type", "serverPort", "7530");
					action("type", "username", username);
					action("type", "password", password);
					action("click", "connect");
					//action("click", "archives");
				
										
					for (int i = 0; i < cameraList.size(); i++)
						{
							long breakCount = 0;
							action("type", "search", cameraList.get(i));
							
							if (screen.exists(path[0] + "liveFeed") != null || 
									screen.exists(path[0] + "cautionFeed") != null)
							{
								action("click", "archives");
								action("type", "search", cameraList.get(i));
								action("doubleClick", "archiveIcon");
								action("click", "calender");
								action("click", "calenderBackArrow");
								action("match", "timeLayout");
								region1 = new Region(match);
								for (int j = 0; j < 24; j++)
									{
										String pathTo = path[0] + "Time" + "\\"+j+".PNG";
										
										try
											{
												region1.click(pathTo);
												action("match", "archiveTimelineRef");
												region2 = new Region(match);
												breakCount += archiveBreakCount(screen.capture(region2));
											}
										
										catch (Exception e)
										{
											System.out.println(e);
											continue;
										}
																			
										//pathTo = path[0] + "Captures";
										//String filename = bhillCameraList[i] + "_" + j;
										
										//screen.wait(1);
										
										//screen.capture(region2).save(pathTo, filename.replace("/", "-"));
										
									}
								
								System.out.println(cameraList.get(i) + " has " + breakCount + " breaks in archive" );
								
								if(breakCount > 0)
									{
										LOGGER.info(cameraList.get(i) + " has " + breakCount + " breaks in archive" );
									}
								
								action("click", "calenderForwardArrow");
								action("rightClick", "archiveIcon");
								action("click", "stopPlaying");
								action("click", "clearSearch");
								action("click", "liveView");
								action("click", "clearSearch");
							}
							
							else
								{
									action("click", "clearSearch");
								}
							
							
						}
					
					action("click", "admin");
					action("click", "disconnect");
					action("click", "cancel");
		}
		
		// method to switch between actions
		public static void action(String action, String reference)
		{
			String pathTo = path[0] + reference + ".png";
			
			try
				{
					switch (action) 
					{
						case "click":
							screen.wait(pathTo, 60);
							screen.click(pathTo);
							break;
							
						case "doubleClick":
							screen.wait(pathTo, 60);
							screen.doubleClick(pathTo);
							break;
							
						case "rightClick":
							screen.wait(pathTo, 60);
							screen.rightClick(pathTo);
							break;
							
						case "match":
							screen.wait(pathTo, 60);
							match = screen.find(pathTo);
							System.out.println(reference + " dimensions are " + match);
							break;
							
						case "region":
							region1 = new Region(match);
							
						default:
							System.out.println(reference + ": missing or unmatched action type");
					}
				}
			catch (Exception e)
				{
					System.out.println(e);
				}
		}
		
		public static void action(String action, String reference, String input)
			{
				String pathTo = path[0] + reference + ".png";
				
				try
					{
						switch (action) 
						{
							case "type":
								screen.wait(pathTo, 60);
								screen.type(pathTo, input);
								break;
								
							default:
								System.out.println(reference + ": missing or unmatched action type");
						}
					}
				catch (Exception e)
					{
						System.out.println(e);
					}
			}
		
		// Returns the total amount of breaks in archive footage
		private static long archiveBreakCount(ScreenImage timelineCapture)
		{
			
			Mat image = Java2DFrameUtils.toMat(timelineCapture.getImage());
			
			Mat greyScale = new Mat();
			opencv_imgproc.cvtColor(image, greyScale, opencv_imgproc.COLOR_BGR2GRAY);

			// Adaptive thresholding to better capture smaller breaks
			Mat binaryImage = new Mat();
			opencv_imgproc.adaptiveThreshold(greyScale, binaryImage, 255, opencv_imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, 
			                                 opencv_imgproc.THRESH_BINARY, 11, 2);

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
