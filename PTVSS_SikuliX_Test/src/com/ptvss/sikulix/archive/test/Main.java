package com.ptvss.sikulix.archive.test;

import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;
import org.sikuli.script.Match;

import org.sikuli.script.Region;

import org.opencv.core.*;

public class Main
	{
		static Screen screen = new Screen();
		
		static Match match;
		
		static Region region1;
		
		static Region region2;
		
		static String[] path = {"C:\\System Administrator\\SikuliX_Resources\\Images\\"};
		
		public static void main(String[] args)
		{
			
			String[] bhillCameraList = {"Bush-Maze-Side-Courtyard", 
					"SideHouse-Stairwell-PTZ", "Side/Rear-Yard", 
					"Front-Door-Camera", "Above-Front-Door-Camera", 
					"Main-Entrance-Courtyard", "Side-Entrance-Courtyard",
					"Right-Rear-Side-PTZ", "Side-Courtyard-PTZ",
					"Rear-Yard-PTZ", "Rear-Yard-Patio", "Main-Carport-PTZ",
					"Side-Door-Camera", "Garden-Camera", "Pool-House-Chimney-PTZ",
					"Mud-Room-Camera", "Rear-Door", "Car-Port"};
			
			String[] time = {"00:00", "01:00", "02:00", "03:00", 
					"04:00", "05:00", "06:00", "07:00", "08:00", 
					"09:00", "10:00", "11:00", "12:00", "13:00", 
					"14:00", "15:00", "16:00", "17:00", "18:00", 
					"19:00", "20:00", "21:00", "22:00", "23:00"};
			
			System.out.println("OpenCV Core Version: " + Core.VERSION);
			
			
			try
				{

					action("doubleClick", "PTVAS");
					action("click", "connectToOtherServer");
					action("type", "serverAddress", "fsdhgkskjfhgs");
					action("type", "serverPort", "7530");
					action("type", "username", "admin");
					action("type", "password", "jkufdgsjkh");
					action("click", "connect");
					action("click", "archives");
										
					for (int i = 0; i < bhillCameraList.length; i++)
						{
							action("type", "search", bhillCameraList[i]);
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
																		
									pathTo = path[0] + "Captures";
									String filename = bhillCameraList[i] + "_" + j;
									screen.capture(region2).save(pathTo, filename);
									
									
								}
							action("rightClick", "archiveIcon");
							action("click", "stopPlaying");
							action("click", "clearSearch");
						}

				}
			
			catch (Exception e)
			{
				System.out.println(e);
			}
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
		
		public static int archiveBreakCount(Mat timelineCapture)
		{
			int result = 0;
			
			
			
			return result;
		}
		
		
	}
