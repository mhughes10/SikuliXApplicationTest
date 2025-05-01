package com.ptvss.sikulix.archive.test;

import java.io.*;
import java.util.*;

public class DeploymentReader
	{
		public static List<Deploy> loadDeployments(String fileName) throws IOException
		{
			
			List<Deploy> deployments = new ArrayList<Deploy>();
			
			try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
			{
				String line;
				while ((line = br.readLine()) != null)
					{
						String[] parts = line.split(";");
						String address = parts[0];
						String port = parts[1];
						List<String> cameraList = Arrays.asList(parts[2].split(","));
						
						deployments.add(new Deploy(address,port,cameraList));
					}
			}
			
			return deployments;
		}
	}
