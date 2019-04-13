package net.phatcode.rel.multimedia;

class SpriteFont
{
	private ImageAtlas fontAtlas;
	private int charWidth = 32;
	private int charHeight = 32;

	SpriteFont( ImageAtlas imageAtlas )
	{
		fontAtlas = imageAtlas;
		charWidth = fontAtlas.getSprite(0).width;
		charHeight = fontAtlas.getSprite(0).height;

	}

    void shutDown()
    {
        fontAtlas.shutDown();
    }

	public void print( int x, int y, String text, Graphics graphics )
	{
		int len = text.length(); 
		for(int i = 0; i< len; i++ )
		{
			int index = text.charAt(i) - 32;
			graphics.drawSprite( x, y,
					0, fontAtlas.getSprite(index) );
			x += charWidth; 
		}
		
	}

    public void print( int x, int y,
                       float r, float g, float b, float a,
                       String text, Graphics graphics )
    {

        graphics.setColor( r, g, b, a );
        int len = text.length();
        for(int i = 0; i< len; i++ )
        {
            int index = text.charAt(i) - 32;
            graphics.drawSprite( x, y,
                    0, fontAtlas.getSprite(index) );
            x += charWidth;
        }

    }


    public void setCharWidth( int charWidth )
	{
		this.charWidth = charWidth;
	}
	
	public void setCharHeight( int charHeight )
	{
		this.charHeight = charHeight;
	}
	
	public int getCharWidth()
	{
		return charWidth;
	}

	public int getCharHeight()
	{
		return charHeight;
	}

}
