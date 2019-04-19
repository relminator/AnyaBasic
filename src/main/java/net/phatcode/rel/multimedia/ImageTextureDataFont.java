package net.phatcode.rel.multimedia;

import java.net.URL;

class ImageTextureDataFont implements ImageTextureData
{
	
	private String fileName = "font.png";
	
	public URL getUrl()
	{
		return this.getClass().getClassLoader().getResource(fileName);
	}

	@Override
	public String getPath()
	{
		return fileName;
	}

	public int[] getArray()
	{
		return null;
	}

	public int getNumImages()
	{
		return 0;
	}

	@Override
	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}


}
