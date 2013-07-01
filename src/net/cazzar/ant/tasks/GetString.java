package net.cazzar.ant.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class GetString extends Task {
	String	Url;
	
	public void setUrl(String url) {
		Url = url;
	}
	
	@Override
	public void execute() throws BuildException {
		log("Downloading from " + Url);
		
		try {
			URL url = new URL(Url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			
			String s = null;
			while ((s = reader.readLine()) != null)
				log(s);
		}
		catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
