package net.phatcode.rel.multimedia;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

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

abstract class Renderer {
    private int screenWidth;
    private int screenHeight;

    Renderer( int screenWidth, int screenHeight ) {
        try {
            Display.setDisplayMode( new DisplayMode( screenWidth, screenHeight ) );
            Display.create();
            Display.setTitle( "AnyaBasic 0.4.0 beta" );
        } catch ( LWJGLException e ) {
            e.printStackTrace();
        }

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        GL11.glViewport( 0, 0,
                screenWidth, screenHeight );

        GL11.glMatrixMode( GL11.GL_PROJECTION );
        GL11.glLoadIdentity();

        GL11.glOrtho( 0, screenWidth, screenHeight, 0, 1, -1 );
        GL11.glMatrixMode( GL11.GL_MODELVIEW );

        GL11.glLoadIdentity();

        GL11.glShadeModel( GL11.GL_SMOOTH );             //set shading to smooth(try GL_FLAT)
        GL11.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );     //set Clear color to BLACK
        GL11.glClearDepth( 1.0f );                       //Set Depth buffer to 1(z-Buffer)
        GL11.glDisable( GL11.GL_DEPTH_TEST );            //Disable Depth Testing so that our z-buffer works

        GL11.glDepthFunc( GL11.GL_LEQUAL );

        GL11.glEnable( GL11.GL_COLOR_MATERIAL );


        GL11.glEnable( GL11.GL_TEXTURE_2D );

        GL11.glEnable( GL11.GL_ALPHA_TEST );
        GL11.glAlphaFunc( GL11.GL_GREATER, 0 );

        GL11.glEnable( GL11.GL_BLEND );
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );

        GL11.glHint( GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST );

        GL11.glDisable( GL11.GL_CULL_FACE );

        GL11.glPolygonMode( GL11.GL_FRONT, GL11.GL_FILL );

        GL11.glMatrixMode( GL11.GL_MODELVIEW );
        GL11.glLoadIdentity();
        GL11.glTranslatef( 0.375f, 0.375f, 0 );    // magic trick

    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
