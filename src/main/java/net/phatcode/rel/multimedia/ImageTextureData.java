package net.phatcode.rel.multimedia;

import java.net.URL;

/**
 * 
 */

/**
 * @author Anya
 *
 */
interface ImageTextureData
{
	
	URL getUrl();
	String getPath();
	int[] getArray();
	int getNumImages();
	void setFileName( String fileName );
}
