package net.phatcode.rel.multimedia;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/*********************************************************************************
 * Created by relminator
 * <p>
 * Richard Eric Lope BSN RN
 * http://rel.phatcode.net
 * Started: 4/8/2016
 * Ended: Ongoing
 * <p>
 * License MIT:
 * Copyright (c) 2016 Richard Eric Lope
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. (As clarification, there is no
 * requirement that the copyright notice and permission be included in binary
 * distributions of the Software.)
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/

public class Graphics extends Renderer
{

    private int currentTexture;
    private SpriteFont spriteFont;
    private ImageAtlas glowImages;

    private List<SpriteGL> sprites = new ArrayList<>();   // images

    public Graphics()
    {
        super( 640, 480 );
    }

    public Graphics( int screenWidth, int screenHeight, int vsynch )
    {

        super( screenWidth, screenHeight );

        if( vsynch !=  0)
        {
            Display.setVSyncEnabled( true );
        }

        spriteFont = new SpriteFont( new ImageAtlas(
                                     new ImageTextureDataFont(),
                                     32, 32, GL11.GL_NEAREST ));

        glowImages = new ImageAtlas( new ImageTextureDataGlowSprites(), GL11.GL_LINEAR, 0 );

    }


    public void flip( int refreshRate )
    {
        Display.update();
        Display.sync(refreshRate);
    }

    public void flip()
    {
        Display.update();
        Display.sync(60);
    }

    public void destroy()
    {
        spriteFont.shutDown();
        glowImages.shutDown();
        //noinspection Convert2streamapi
        for(SpriteGL sprite: sprites)
        {
            sprite.shutDown();
        }
        Display.destroy();
    }

    public void cls()
    {
        GL11.glClear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
    }

    public void setColor( float r, float g, float b, float a )
    {
        GL11.glColor4f( r, g, b, a ) ;
    }

    public void drawLine( int x1, int y1, int x2, int y2,
                          float r, float g, float b, float a )
    {
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glColor4f( r, g, b, a ) ;

        GL11.glBegin( GL11.GL_LINES );
        GL11.glVertex2i( x1, y1 );
        GL11.glVertex2i( x2, y2 );
        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

        GL11.glColor4f( 1, 1, 1, 1 );

    }

    public void drawLine( int x1, int y1, int x2, int y2 )
    {
        GL11.glDisable( GL11.GL_TEXTURE_2D );

        GL11.glBegin( GL11.GL_LINES );
        GL11.glVertex2i( x1, y1 );
        GL11.glVertex2i( x2, y2 );
        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

    }

    public void drawBox( int x1, int y1, int x2, int y2,
                  float r, float g, float b, float a )
    {
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glColor4f( r, g, b, a ) ;

        GL11.glBegin( GL11.GL_LINE_STRIP );
        GL11.glVertex2i( x1, y1 );
        GL11.glVertex2i( x2, y1 );
        GL11.glVertex2i( x2, y2 );
        GL11.glVertex2i( x1, y2 );
        GL11.glVertex2i( x1, y1 );
        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

        GL11.glColor4f( 1, 1, 1, 1 );

    }

    public void drawBox( int x1, int y1, int x2, int y2 )
    {
        GL11.glDisable( GL11.GL_TEXTURE_2D );

        GL11.glBegin( GL11.GL_LINE_STRIP );
        GL11.glVertex2i( x1, y1 );
        GL11.glVertex2i( x2, y1 );
        GL11.glVertex2i( x2, y2 );
        GL11.glVertex2i( x1, y2 );
        GL11.glVertex2i( x1, y1 );
        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

    }

    public void drawBoxFilled( int x1, int y1, int x2, int y2,
                        float r, float g, float b, float a )
    {
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glColor4f( r, g, b, a ) ;

        x2++;
        y2++;

        GL11.glBegin( GL11.GL_QUADS );

            GL11.glVertex2i	( x1,y1 );
            GL11.glVertex2i	( x1,y2 );
            GL11.glVertex2i	( x2,y2 );
            GL11.glVertex2i	( x2,y1 );

        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

        GL11.glColor4f( 1, 1, 1, 1 );

    }

    public void drawBoxFilled( int x1, int y1, int x2, int y2 )
    {
        GL11.glDisable( GL11.GL_TEXTURE_2D );

        x2++;
        y2++;

        GL11.glBegin( GL11.GL_QUADS );

        GL11.glVertex2i	( x1,y1 );
        GL11.glVertex2i	( x1,y2 );
        GL11.glVertex2i	( x2,y2 );
        GL11.glVertex2i	( x2,y1 );

        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );


    }

    public void drawEllipse( int x, int y, int a, int b, int degrees,
                             float red, float green, float blue, float alpha )
    {

        // these constants decide the quality of the ellipse
        final float  pi = (float) Math.PI;
        final float  twopi  = 2 * pi;   //        two pi (radians in a circle)
        final int face_length  = 8;     //        approx. face length in pixels
        final int max_faces = 256;      //        maximum number of faces in ellipse
        final int min_faces = 16;       //        minimum number of faces in ellipse

        // approx. ellipse circumference (hudson's method)
        float h = ( a-b*a-b ) / (float)( a+b*a+b );
        float circumference = 0.25f * pi * (a+b) * (3* (1+h*0.25f)+1 / (1-h*0.25f));

        // number of faces in ellipse
        int num_faces = (int) (circumference/(float)face_length);

        // clamp number of faces
        if( num_faces > max_faces ) num_faces = max_faces;
        if( num_faces < min_faces ) num_faces = min_faces;

        // keep number of faces divisible by 4
        num_faces -= (num_faces & 3);

        // precalc cosine theta
        float angle = degrees * pi /180.0f;
        float s   = (float) Math.sin(twopi/(float)num_faces);
        float c   = (float) Math.cos(twopi/(float)num_faces);
        float xx  = 1;
        float yy  = 0;
        float xt;
        float ax  = (float) Math.cos(angle);
        float ay  = (float) Math.sin(angle);


        // draw ellipse
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glColor4f( red, green, blue, alpha ) ;

        int i;
        GL11.glBegin( GL11.GL_LINE_LOOP );

            for( i = 0; i < num_faces; i++ )
            {
                xt = xx;
                xx = c * xx - s * yy;
                yy = s * xt + c * yy;
                GL11.glVertex2f( x+a*xx*ax-b*yy*ay, y+a*xx*ay+b*yy*ax );
            }

            GL11.glVertex2f( x+a*ax, y+a*ay );

        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

        GL11.glColor4f( 1, 1, 1, 1 );

    }

    public void drawEllipse( int x, int y, int a, int b, int degrees )
    {

        // these constants decide the quality of the ellipse
        final float  pi = (float) Math.PI;
        final float  twopi  = 2 * pi;   //        two pi (radians in a circle)
        final int face_length  = 8;     //        approx. face length in pixels
        final int max_faces = 256;      //        maximum number of faces in ellipse
        final int min_faces = 16;       //        minimum number of faces in ellipse

        // approx. ellipse circumference (hudson's method)
        float h = ( a-b*a-b ) / (float)( a+b*a+b );
        float circumference = 0.25f * pi * (a+b) * (3* (1+h*0.25f)+1 / (1-h*0.25f));

        // number of faces in ellipse
        int num_faces = (int) (circumference/(float)face_length);

        // clamp number of faces
        if( num_faces > max_faces ) num_faces = max_faces;
        if( num_faces < min_faces ) num_faces = min_faces;

        // keep number of faces divisible by 4
        num_faces -= (num_faces & 3);

        // precalc cosine theta
        float angle = degrees * pi /180.0f;
        float s   = (float) Math.sin(twopi/(float)num_faces);
        float c   = (float) Math.cos(twopi/(float)num_faces);
        float xx  = 1;
        float yy  = 0;
        float xt;
        float ax  = (float) Math.cos(angle);
        float ay  = (float) Math.sin(angle);


        // draw ellipse
        GL11.glDisable( GL11.GL_TEXTURE_2D );

        int i;
        GL11.glBegin( GL11.GL_LINE_LOOP );

        for( i = 0; i < num_faces; i++ )
        {
            xt = xx;
            xx = c * xx - s * yy;
            yy = s * xt + c * yy;
            GL11.glVertex2f( x+a*xx*ax-b*yy*ay, y+a*xx*ay+b*yy*ax );
        }

        GL11.glVertex2f( x+a*ax, y+a*ay );

        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

    }

    public void drawEllipseFilled( int x, int y, int a, int b, int degrees,
                                   float red, float green, float blue, float alpha )
    {

        // these constants decide the quality of the ellipse
        final float  pi = (float) Math.PI;
        final float  twopi  = 2 * pi;   //        two pi (radians in a circle)
        final int face_length  = 8;     //        approx. face length in pixels
        final int max_faces = 256;      //        maximum number of faces in ellipse
        final int min_faces = 16;       //        minimum number of faces in ellipse

        // approx. ellipse circumference (hudson's method)
        float h = ( a-b*a-b ) / (float)( a+b*a+b );
        float circumference = 0.25f * pi * (a+b) * (3* (1+h*0.25f)+1 / (1-h*0.25f));

        // number of faces in ellipse
        int num_faces = (int) (circumference/(float)face_length);

        // clamp number of faces
        if( num_faces > max_faces ) num_faces = max_faces;
        if( num_faces < min_faces ) num_faces = min_faces;

        // keep number of faces divisible by 4
        num_faces -= (num_faces & 3);

        // precalc cosine theta
        float angle = degrees * pi /180.0f;
        float s   = (float) Math.sin(twopi/(float)num_faces);
        float c   = (float) Math.cos(twopi/(float)num_faces);
        float xx  = 1;
        float yy  = 0;
        float xt;
        float ax  = (float) Math.cos(angle);
        float ay  = (float) Math.sin(angle);


        // draw ellipse
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glColor4f( red, green, blue, alpha ) ;

        int i;
        GL11.glBegin( GL11.GL_TRIANGLE_FAN );

            for( i = 0; i < num_faces; i++ )
            {
                xt = xx;
                xx = c * xx - s * yy;
                yy = s * xt + c * yy;
                GL11.glVertex2f( x+a*xx*ax-b*yy*ay, y+a*xx*ay+b*yy*ax );
            }

            GL11.glVertex2f( x+a*ax, y+a*ay );

        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );

        GL11.glColor4f( 1, 1, 1, 1 );

    }

    public void drawEllipseFilled( int x, int y, int a, int b, int degrees )
    {

        // these constants decide the quality of the ellipse
        final float  pi = (float) Math.PI;
        final float  twopi  = 2 * pi;   //        two pi (radians in a circle)
        final int face_length  = 8;     //        approx. face length in pixels
        final int max_faces = 256;      //        maximum number of faces in ellipse
        final int min_faces = 16;       //        minimum number of faces in ellipse

        // approx. ellipse circumference (hudson's method)
        float h = ( a-b*a-b ) / (float)( a+b*a+b );
        float circumference = 0.25f * pi * (a+b) * (3* (1+h*0.25f)+1 / (1-h*0.25f));

        // number of faces in ellipse
        int num_faces = (int) (circumference/(float)face_length);

        // clamp number of faces
        if( num_faces > max_faces ) num_faces = max_faces;
        if( num_faces < min_faces ) num_faces = min_faces;

        // keep number of faces divisible by 4
        num_faces -= (num_faces & 3);

        // precalc cosine theta
        float angle = degrees * pi /180.0f;
        float s   = (float) Math.sin(twopi/(float)num_faces);
        float c   = (float) Math.cos(twopi/(float)num_faces);
        float xx  = 1;
        float yy  = 0;
        float xt;
        float ax  = (float) Math.cos(angle);
        float ay  = (float) Math.sin(angle);


        // draw ellipse
        GL11.glDisable( GL11.GL_TEXTURE_2D );

        int i;
        GL11.glBegin( GL11.GL_TRIANGLE_FAN );

        for( i = 0; i < num_faces; i++ )
        {
            xt = xx;
            xx = c * xx - s * yy;
            yy = s * xt + c * yy;
            GL11.glVertex2f( x+a*xx*ax-b*yy*ay, y+a*xx*ay+b*yy*ax );
        }

        GL11.glVertex2f( x+a*ax, y+a*ay );

        GL11.glEnd();

        GL11.glEnable( GL11.GL_TEXTURE_2D );


    }

    private int getflipMode( int value )
    {
        switch( value )
        {
            case 0:
                return SpriteGL.FLIP_NONE;
            case 1:
                return SpriteGL.FLIP_V;
            case 2:
                return SpriteGL.FLIP_H;
            case 3:
                return SpriteGL.FLIP_HV;
            default:
                return SpriteGL.FLIP_NONE;
        }
    }

    public void setBlendMode( int mode )
    {
        switch ( mode )
        {
            case 0:
                GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
                break;
            case 1:
                GL11.glBlendFunc( GL11.GL_ONE, GL11.GL_ONE );
                break;
            default:
                GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );

        }

    }

    void drawSprite( float x, float y,
                            int flipmode, SpriteGL sprite )
    {

        flipmode = getflipMode( flipmode );

        float x2 = x + sprite.width;
        float y2 = y + sprite.height;

        float u1 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u1 : sprite.u2;
        float v1 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v1 : sprite.v2;
        float u2 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u2 : sprite.u1;
        float v2 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v2 : sprite.v1;

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }


        GL11.glBegin( GL11.GL_QUADS );

            GL11.glTexCoord2f( u1, v1 ); GL11.glVertex2f( x, y );
            GL11.glTexCoord2f( u1, v2 ); GL11.glVertex2f( x, y2 );
            GL11.glTexCoord2f( u2, v2 ); GL11.glVertex2f( x2, y2 );
            GL11.glTexCoord2f( u2, v1 ); GL11.glVertex2f( x2, y );

        GL11.glEnd();


    }

    private void drawSprite( float x, float y,
                             float r, float g, float b, float a,
                             int flipmode, SpriteGL sprite )
    {

        flipmode = getflipMode( flipmode );

        float x2 = x + sprite.width;
        float y2 = y + sprite.height;

        float u1 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u1 : sprite.u2;
        float v1 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v1 : sprite.v2;
        float u2 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u2 : sprite.u1;
        float v2 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v2 : sprite.v1;

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }

        GL11.glColor4f( r, g, b, a ) ;

        GL11.glBegin( GL11.GL_QUADS );

        GL11.glTexCoord2f( u1, v1 ); GL11.glVertex2f( x, y );
        GL11.glTexCoord2f( u1, v2 ); GL11.glVertex2f( x, y2 );
        GL11.glTexCoord2f( u2, v2 ); GL11.glVertex2f( x2, y2 );
        GL11.glTexCoord2f( u2, v1 ); GL11.glVertex2f( x2, y );

        GL11.glEnd();


    }

    public void drawSprite( float x, float y,
                            int flipmode, int index )
    {
        drawSprite( x, y, flipmode, sprites.get( index ) );
    }

    public void drawSprite( float x, float y,
                            float r, float g, float b, float a,
                            int flipmode, int index )
    {
        drawSprite( x, y, r, g, b, a, flipmode, sprites.get( index ) );
    }

    private void drawTransformedSprite( float x, float y,
                                       float angle,
                                       float scaleX, float scaleY,
                                       int flipmode, SpriteGL sprite )
    {

        flipmode = getflipMode( flipmode );

        float s_half_x = sprite.width / 2.0f;
        float s_half_y = sprite.height / 2.0f;

        float x1 =  -s_half_x;
        float y1 =  -s_half_y;


        float u1 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u1 : sprite.u2;
        float v1 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v1 : sprite.v2;
        float u2 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u2 : sprite.u1;
        float v2 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v2 : sprite.v1;

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }

        GL11.glPushMatrix();

            GL11.glTranslatef( x, y, 0.0f );
            GL11.glScalef( scaleX, scaleY, 1.0f );
            GL11.glRotatef( angle, 0.0f, 0.0f, 1.0f );

            GL11.glBegin( GL11.GL_QUADS );

                GL11.glTexCoord2f( u1, v1 ); GL11.glVertex2f( x1, y1 );
                GL11.glTexCoord2f( u1, v2 ); GL11.glVertex2f( x1, s_half_y );
                GL11.glTexCoord2f( u2, v2 ); GL11.glVertex2f( s_half_x, s_half_y );
                GL11.glTexCoord2f( u2, v1 ); GL11.glVertex2f( s_half_x, y1 );

            GL11.glEnd();

        GL11.glPopMatrix();

    }

    private void drawTransformedSprite( float x, float y,
                                       float angle,
                                       float scaleX, float scaleY,
                                       float r, float g, float b, float a,
                                       int flipmode, SpriteGL sprite )
    {

        flipmode = getflipMode( flipmode );

        float s_half_x = sprite.width / 2.0f;
        float s_half_y = sprite.height / 2.0f;

        float x1 =  -s_half_x;
        float y1 =  -s_half_y;


        float u1 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u1 : sprite.u2;
        float v1 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v1 : sprite.v2;
        float u2 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u2 : sprite.u1;
        float v2 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v2 : sprite.v1;

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }

        GL11.glColor4f( r, g, b, a ) ;

        GL11.glPushMatrix();

        GL11.glTranslatef( x, y, 0.0f );
        GL11.glScalef( scaleX, scaleY, 1.0f );
        GL11.glRotatef( angle, 0.0f, 0.0f, 1.0f );

        GL11.glBegin( GL11.GL_QUADS );

        GL11.glTexCoord2f( u1, v1 ); GL11.glVertex2f( x1, y1 );
        GL11.glTexCoord2f( u1, v2 ); GL11.glVertex2f( x1, s_half_y );
        GL11.glTexCoord2f( u2, v2 ); GL11.glVertex2f( s_half_x, s_half_y );
        GL11.glTexCoord2f( u2, v1 ); GL11.glVertex2f( s_half_x, y1 );

        GL11.glEnd();

        GL11.glPopMatrix();

    }

    public void drawTransformedSprite( float x, float y,
                                       float angle,
                                       float scaleX, float scaleY,
                                       int flipmode, int index )
    {
        drawTransformedSprite( x, y,
                               angle,
                               scaleX, scaleY,
                               flipmode, sprites.get( index ) );
    }

    public void drawTransformedSprite( float x, float y,
                                       float angle,
                                       float scaleX, float scaleY,
                                       float r, float g, float b, float a,
                                       int flipmode, int index )
    {
        drawTransformedSprite( x, y,
                angle,
                scaleX, scaleY,
                r, g, b, a,
                flipmode, sprites.get( index ) );
    }

    public void drawString( int x, int y, String text )
    {
        spriteFont.print( x, y, text, this  );
    }

    public void drawString( int x, int y,
                            float r, float g, float b, float a,
                            String text )
    {
        spriteFont.print( x, y, r, g, b, a, text, this  );
    }

    public int loadImage( String filename )
    {
        //System.out.println(filename);
        ImageTextureData texture = new ImageTextureDataDefault();
        texture.setFileName( filename );
        ImageAtlas atlas = new ImageAtlas( texture, GL11.GL_LINEAR );
        sprites.add( atlas.getSprite( 0 ) );
        return (sprites.size()-1);   // return last index index
    }

    private void getSprite( float x1, float y1,
                            float x2, float y2,
                            SpriteGL source, SpriteGL dest )
    {

        float width = (x2 - x1 ) + 1;
        float height = (y2 - y1 ) + 1;
        float u1 = x1 / (float) source.width;
        float v1 = y1 / (float) source.height;
        float u2 = x2 / (float) source.width;
        float v2 = y2 / (float) source.height;

        dest.textureID = source.textureID;
        dest.width = (int) width;
        dest.height = (int) height;
        dest.u1 = u1;
        dest.v1 = v1;
        dest.u2 = u2;
        dest.v2 = v2;
    }

    public void getSprite( float x1, float y1,
                           float x2, float y2,
                           int source, int dest )
    {
        sprites.add( new SpriteGL() );
        getSprite( x1, y1, x2, y2,
                   sprites.get( source ),
                   sprites.get( dest ) );

    }

    public void drawSpriteOnLine( int x1, int y1,
                           int x2, int y2,
                           int width, int type )
    {


        SpriteGL sprite  = glowImages.getSprite(type % glowImages.getNumImages());

        // Only change active texture when there is a need
        // Speeds up the rendering by batching textures

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }

        float u1 = sprite.u1;
        float v1 = sprite.v1;
        float u2 = sprite.u2;
        float v2 = sprite.v2;
        float uh = (u1 + u2)/2.0f;

        float nx,ny;
        nx = -( y2-y1 );
        ny =  ( x2-x1 );

        float leng;
        leng = (float)Math.sqrt( nx * nx + ny * ny );
        nx = nx / leng;
        ny = ny / leng;

        nx *= width / 2.0;
        ny *= width / 2.0;

        float lx1, ly1, lx2, ly2, lx3, ly3, lx4, ly4;

        lx1 = x2 + nx;
        ly1 = y2 + ny;
        lx2 = x2 - nx;
        ly2 = y2 - ny;
        lx3 = x1 - nx;
        ly3 = y1 - ny;
        lx4 = x1 + nx;
        ly4 = y1 + ny;

        // MAIN
        GL11.glBegin( GL11.GL_QUADS );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx1, ly1, 0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx2, ly2, 0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx3, ly3, 0.0f );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx4, ly4, 0.0f );
        GL11.glEnd();

        //RIGHT
        float lx5, ly5, lx6, ly6, vx, vy;
        vx = ( x2-x1 );
        vy = ( y2-y1 );
        leng = (float)Math.sqrt( vx * vx + vy * vy );
        vx = vx / leng;
        vy = vy / leng;
        vx *= width / 2.0;
        vy *= width / 2.0;

        lx5 = lx1 + vx;
        ly5 = ly1 + vy;
        lx6 = lx2 + vx;
        ly6 = ly2 + vy;

        GL11.glBegin( GL11.GL_QUADS );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx1, ly1,0.0f );
        GL11.glTexCoord2f( u2, v1 ); GL11.glVertex3f( lx5, ly5,0.0f );
        GL11.glTexCoord2f( u2, v2 ); GL11.glVertex3f( lx6, ly6,0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx2, ly2,0.0f );
        GL11.glEnd();

        // LEFT
        lx5 = lx4 - vx;
        ly5 = ly4 - vy;
        lx6 = lx3 - vx;
        ly6 = ly3 - vy;

        GL11.glBegin( GL11.GL_QUADS );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx4, ly4, 0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx3, ly3 ,0.0f );
        GL11.glTexCoord2f( u2, v2 ); GL11.glVertex3f( lx6, ly6, 0.0f );
        GL11.glTexCoord2f( u2, v1 ); GL11.glVertex3f( lx5, ly5, 0.0f );
        GL11.glEnd();

    }

    public void drawSpriteOnLine( int x1, int y1,
                           int x2, int y2,
                           int width, int type,
                           float r, float g, float b, float a )
    {


        SpriteGL sprite  = glowImages.getSprite(type % glowImages.getNumImages());


        // Only change active texture when there is a need
        // Speeds up the rendering by batching textures

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }

        GL11.glColor4f( r, g, b, a ) ;

        float u1 = sprite.u1;
        float v1 = sprite.v1;
        float u2 = sprite.u2;
        float v2 = sprite.v2;
        float uh = (u1 + u2)/2.0f;

        float nx,ny;
        nx = -( y2-y1 );
        ny =  ( x2-x1 );

        float leng;
        leng = (float)Math.sqrt( nx * nx + ny * ny );
        nx = nx / leng;
        ny = ny / leng;

        nx *= width / 2.0;
        ny *= width / 2.0;

        float lx1, ly1, lx2, ly2, lx3, ly3, lx4, ly4;

        lx1 = x2 + nx;
        ly1 = y2 + ny;
        lx2 = x2 - nx;
        ly2 = y2 - ny;
        lx3 = x1 - nx;
        ly3 = y1 - ny;
        lx4 = x1 + nx;
        ly4 = y1 + ny;

        // MAIN
        GL11.glBegin( GL11.GL_QUADS );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx1, ly1, 0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx2, ly2, 0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx3, ly3, 0.0f );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx4, ly4, 0.0f );
        GL11.glEnd();

        //RIGHT
        float lx5, ly5, lx6, ly6, vx, vy;
        vx = ( x2-x1 );
        vy = ( y2-y1 );
        leng = (float)Math.sqrt( vx * vx + vy * vy );
        vx = vx / leng;
        vy = vy / leng;
        vx *= width / 2.0;
        vy *= width / 2.0;

        lx5 = lx1 + vx;
        ly5 = ly1 + vy;
        lx6 = lx2 + vx;
        ly6 = ly2 + vy;

        GL11.glBegin( GL11.GL_QUADS );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx1, ly1,0.0f );
        GL11.glTexCoord2f( u2, v1 ); GL11.glVertex3f( lx5, ly5,0.0f );
        GL11.glTexCoord2f( u2, v2 ); GL11.glVertex3f( lx6, ly6,0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx2, ly2,0.0f );
        GL11.glEnd();

        // LEFT
        lx5 = lx4 - vx;
        ly5 = ly4 - vy;
        lx6 = lx3 - vx;
        ly6 = ly3 - vy;

        GL11.glBegin( GL11.GL_QUADS );
        GL11.glTexCoord2f( uh, v1 ); GL11.glVertex3f( lx4, ly4, 0.0f );
        GL11.glTexCoord2f( uh, v2 ); GL11.glVertex3f( lx3, ly3 ,0.0f );
        GL11.glTexCoord2f( u2, v2 ); GL11.glVertex3f( lx6, ly6, 0.0f );
        GL11.glTexCoord2f( u2, v1 ); GL11.glVertex3f( lx5, ly5, 0.0f );
        GL11.glEnd();

    }

    private void drawScrolledSprite( float x, float y, float scaleX, float scaleY,
                                     float u, float v,
                                     int flipmode, SpriteGL sprite )
    {

        flipmode = getflipMode( flipmode );

        float x2 = x + sprite.width;
        float y2 = y + sprite.height;

        float u1 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u1 : sprite.u2;
        float v1 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v1 : sprite.v2;
        float u2 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u2 : sprite.u1;
        float v2 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v2 : sprite.v1;

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }

        GL11.glPushMatrix();

        GL11.glScalef( scaleX, scaleY, 1.0f );

        GL11.glBegin( GL11.GL_QUADS );

        GL11.glTexCoord2f( u1+u, v1+v ); GL11.glVertex2f( x, y );
        GL11.glTexCoord2f( u1+u, v2+v ); GL11.glVertex2f( x, y2 );
        GL11.glTexCoord2f( u2+u, v2+v ); GL11.glVertex2f( x2, y2 );
        GL11.glTexCoord2f( u2+u, v1+v ); GL11.glVertex2f( x2, y );

        GL11.glEnd();

        GL11.glPopMatrix();

    }

    private void drawScrolledSprite( float x, float y, float scaleX, float scaleY,
                                     float u, float v, float r, float g, float b, float a,
                                     int flipmode, SpriteGL sprite )
    {

        flipmode = getflipMode( flipmode );

        float x2 = x + sprite.width;
        float y2 = y + sprite.height;

        float u1 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u1 : sprite.u2;
        float v1 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v1 : sprite.v2;
        float u2 = ( (flipmode & SpriteGL.FLIP_H) == 0 ) ? sprite.u2 : sprite.u1;
        float v2 = ( (flipmode & SpriteGL.FLIP_V) == 0 ) ? sprite.v2 : sprite.v1;

        if ( sprite.textureID != currentTexture )
        {
            GL11.glBindTexture( GL11.GL_TEXTURE_2D, sprite.textureID );
            currentTexture = sprite.textureID;
        }

        GL11.glColor4f( r, g, b, a ) ;

        GL11.glPushMatrix();

        GL11.glScalef( scaleX, scaleY, 1.0f );

        GL11.glBegin( GL11.GL_QUADS );

        GL11.glTexCoord2f( u1+u, v1+v ); GL11.glVertex2f( x, y );
        GL11.glTexCoord2f( u1+u, v2+v ); GL11.glVertex2f( x, y2 );
        GL11.glTexCoord2f( u2+u, v2+v ); GL11.glVertex2f( x2, y2 );
        GL11.glTexCoord2f( u2+u, v1+v ); GL11.glVertex2f( x2, y );

        GL11.glEnd();

        GL11.glPopMatrix();

    }

    public void drawScrolledSprite( float x, float y, float scaleX, float scaleY,
                                    float u, float v,
                                    int flipmode, int index )
    {
        drawScrolledSprite( x, y, scaleX, scaleY, u, v, flipmode, sprites.get( index ) );
    }

    public void drawScrolledSprite( float x, float y, float scaleX, float scaleY,
                                    float u, float v, float r, float g, float b, float a,
                                    int flipmode, int index )
    {
        drawScrolledSprite( x, y, scaleX, scaleY, u, v, r, g, b, a, flipmode, sprites.get( index ) );
    }


    public List<SpriteGL> getSprites()
    {
        return sprites;
    }
}
