package net.phatcode.rel.multimedia; /**
 *
 * @author Richard Eric M. Lope (Relminator)
 * @version 1.00 2013/3/04
 */

// ************************************************************************
//
// Sprite Atlas Class
// Can use fixed tilesizes or variable sized images
// Richard Eric M. Lope (relminator)
// http://rel.phatcode.net
//
// ************************************************************************

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;



class ImageAtlas
{

	private int textureID = 0;
	private int width = 0;
	private int height = 0;
	
	private SpriteGL[] sprites;
	
	ImageAtlas( ImageTextureData textureData, int filtermode )
	{

        InputStream in;
        ByteBuffer buf = null;
        PNGDecoder decoder;
        try
        {
            //in = textureData.getUrl().openStream();
            in = new FileInputStream(textureData.getPath());
            decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
            decoder.decode( buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA );
            buf.flip();
            in.close();

        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        int tileWidth = width;
        int tileHeight = height;
        int numImages = (width/tileWidth) * (height/tileHeight);


        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filtermode);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filtermode);
        GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf );

        sprites = new SpriteGL[numImages];

        int i = 0;

        for( int y = 0; y < (height/tileHeight); y++)
        {
            for( int x = 0; x < (width/tileWidth); x++)
            {

                int ix = x * tileWidth;
                int iy = y * tileHeight;

				sprites[i] = new SpriteGL();
                sprites[i].width = tileWidth;
                sprites[i].height = tileHeight;
                sprites[i].u1 = ix / (float)width;
                sprites[i].v1 = iy / (float)height;
                sprites[i].u2 = (ix + tileWidth ) / (float)width;
                sprites[i].v2 = (iy + tileHeight ) / (float)height;
                sprites[i].textureID = textureID;
                i++;

            }
        }

    }

	ImageAtlas( ImageTextureData textureData, int tileWidth, int tileHeight, int filtermode )
	{
		InputStream in;
		ByteBuffer buf = null;
		PNGDecoder decoder;
		
		try 
		{
			in = textureData.getUrl().openStream();
			decoder = new PNGDecoder(in);
			width = decoder.getWidth();
	        height = decoder.getHeight();
			buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode( buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA );
			buf.flip();
			in.close();
			
		}
		catch( IOException e ) 
		{
			e.printStackTrace();
		}
		
		int numImages = (width/tileWidth) * (height/tileHeight);


		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filtermode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filtermode);
		GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
				0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf );

		sprites = new SpriteGL[numImages];
        
        int i = 0;
    	
        for( int y = 0; y < (height/tileHeight); y++)
    	{
    		for( int x = 0; x < (width/tileWidth); x++) 
    		{
    			
    			int ix = x * tileWidth;
                int iy = y * tileHeight;

				sprites[i] = new SpriteGL();
                sprites[i].width = tileWidth;
                sprites[i].height = tileHeight;
                sprites[i].u1 = ix / (float)width;
                sprites[i].v1 = iy / (float)height;
                sprites[i].u2 = (ix + tileWidth ) / (float)width;
                sprites[i].v2 = (iy + tileHeight ) / (float)height;
				sprites[i].textureID = textureID;
                i++;
                
    		}
    	}



	}

	ImageAtlas( ImageTextureData textureData, int filtermode, int foo )
	{

		InputStream in;
		ByteBuffer buf = null;
		PNGDecoder decoder;

		try
		{
			in = textureData.getUrl().openStream();
			decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode( buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA );
			buf.flip();
			in.close();

		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		int numImages = textureData.getNumImages();
		int texcoords[] = textureData.getArray();

		sprites = new SpriteGL[numImages];
        textureID = GL11.glGenTextures();

        for( int i = 0; i < numImages; i++ )
		{

			int j = i * 4;
			int x = texcoords[j];
			int y = texcoords[j+1];
			int w = texcoords[j+2];
			int h = texcoords[j+3];

			sprites[i] = new SpriteGL();
			sprites[i].width = w;
			sprites[i].height = h;
			sprites[i].u1 = x / (float)width;
			sprites[i].v1 = y / (float)height;
			sprites[i].u2 = (x + w) / (float)width;
			sprites[i].v2 = (y + h) / (float)height;
            sprites[i].textureID = textureID;

        }

		int repeatMode = GL11.GL_REPEAT;
		if( foo != 0 )
		{
			repeatMode = GL11.GL_CLAMP;
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, repeatMode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, repeatMode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filtermode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filtermode);
		GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
				0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf );


	}


	public void shutDown()
	{
		GL11.glDeleteTextures( textureID );
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
		
	public int getNumImages()
	{
		return sprites.length;
	}
	
	public SpriteGL getSprite( int index )
	{
		return sprites[index];
	}

	public int getTextureID()
	{
		return textureID;
	}

	public void setTextureID( int textureID )
	{
		this.textureID = textureID;
	}

	public void setWidth( int width )
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}
	
	
}
