package net.phatcode.rel.multimedia;

import org.lwjgl.opengl.GL11;

class SpriteGL
{
	
	static final int FLIP_NONE 		= 1;
	static final int FLIP_V 	  	= 1 << 1;
	static final int FLIP_H	  		= 1 << 2;
    static final int FLIP_HV	  	= FLIP_H | FLIP_V;

	int width;
	int height;
	public float u1;
	public float v1;
	float u2;
	float v2;

	int textureID;

	SpriteGL()
	{
		
	}

	void shutDown()
	{
		GL11.glDeleteTextures( textureID );
	}

}
