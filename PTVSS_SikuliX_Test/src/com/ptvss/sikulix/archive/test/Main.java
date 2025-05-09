package com.ptvss.sikulix.archive.test;

import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;


public class Main
	{
		/*static Screen screen = new Screen();
		
		static Match match;
		
		static Region region1;
		
		static Region region2;
		
		static String[] path = {"C:\\System Administrator\\SikuliX_Resources\\Images\\"};
		*/
		public static void main(String[] args) throws FileNotFoundException, IOException
		{
			ArchiveCheck check = new ArchiveCheck();
			
			Properties prop = new Properties();
			
			String username;
			String password;
			
			try(FileInputStream input = new FileInputStream("C:\\System Administrator\\SikuliX_Resources\\Credentials\\credentials.properties"))
			{
				prop.load(input);
				
				username = prop.getProperty("bhills.username");
				password = prop.getProperty("bhills.password");
			}
			
			List<Deploy> deployments = DeploymentReader.loadDeployments("C:\\System Administrator\\SikuliX_Resources\\Credentials\\test.txt");
			
			for ( Deploy deploy : deployments)
				{
					ArchiveCheck.test(deploy.address, deploy.port, username, password, deploy.cameraList);
				}
			
			/*String[] bhillCameraList = { 
					"SideHouse-Stairwell-PTZ", "Side/Rear-Yard", 
					"Front-Door-Camera", "Above-Front-Door-Camera", 
					"Main-Entrance-Courtyard", "Side-Entrance-Courtyard",
					"Right-Rear-Side-PTZ", "Side-Courtyard-PTZ",
					"Rear-Yard-PTZ", "Rear-Yard-Patio", "Main-Carport-PTZ",
					"Side-Door-Camera", "Garden-Camera", "Pool-House-Chimney-PTZ",
					"Mud-Room-Camera", "Rear-Door", "Car-Port"};
			
			//System.out.println("OpenCV Core Version: " + Core.VERSION);
			
			Properties prop = new Properties();
			
			String username;
			String password;
			String address;
						
			try(FileInputStream input = new FileInputStream("C:\\System Administrator\\SikuliX_Resources\\Credentials\\credentials.properties"))
			{
				prop.load(input);
				
				username = prop.getProperty("bhills.username");
				password = prop.getProperty("bhills.password");
				address = prop.getProperty("bhills.address");
			}
			
			
			try
				{

					action("doubleClick", "PTVAS");
					action("click", "connectToOtherServer");
					action("type", "serverAddress", address);
					action("type", "serverPort", "7530");
					action("type", "username", username);
					action("type", "password", password);
					action("click", "connect");
					action("click", "archives");
										
					for (int i = 0; i < bhillCameraList.length; i++)
						{
							long breakCount = 0;
							action("type", "search", bhillCameraList[i]);
							
							if (screen.exists(path[0] + "archiveBubble") != null || 
									screen.exists(path[0] + "archiveFixed") != null || 
									screen.exists(path[0] + "archivePTZ") != null)
							{
								action("doubleClick", "archiveIcon");
								action("click", "calender");
								action("click", "calenderBackArrow");
								action("match", "timeLayout");
								region1 = new Region(match);
								for (int j = 0; j < 24; j++)
									{
										String pathTo = path[0] + "Time" + "\\"+j+".PNG";
										region1.click(pathTo);
										
										action("match", "archiveTimelineRef");
										region2 = new Region(match);
																			
										//pathTo = path[0] + "Captures";
										//String filename = bhillCameraList[i] + "_" + j;
										
										//screen.wait(1);
										
										//screen.capture(region2).save(pathTo, filename.replace("/", "-"));
										
										
										breakCount += archiveBreakCount(screen.capture(region2));
																				
									}
							}
							
							System.out.println(bhillCameraList[i] + " has " + breakCount + " breaks in archive" );
							
							action("rightClick", "archiveIcon");
							action("click", "stopPlaying");
							action("click", "clearSearch");
						}

				}
			
			catch (Exception e)
			{
				System.out.println(e);
			}*/
		}
		
		 // method to switch between actions
		/*public static void action(String action, String reference)
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
		public static long archiveBreakCount(ScreenImage timelineCapture)
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
		}*/
		
		
	}
