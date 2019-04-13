package net.phatcode.rel.multimedia;

import java.net.URL;

/*********************************************************************************
 * Created by relminator
 * <p>
 * Richard Eric Lope BSN RN
 * http://rel.phatcode.net
 * Started: 4/22/2016
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

class ImageTextureDataGlowSprites implements ImageTextureData
{

    private String fileName = "assets/glowparts.png";

    public URL getUrl()
    {
        return this.getClass().getClassLoader().getResource( fileName );
    }

    @Override
    public String getPath()
    {
        return fileName;
    }

    public int[] getArray()
    {
        return textureCoords;
    }

    public int getNumImages()
    {
        return textureCoords.length / 4;
    }

    private int textureCoords[] =
            {
                    2,	2,	32,	32,	 // 0
                    38,	2,	32,	32,	 // 1
                    74,	2,	32,	32,	 // 2
                    2,	38,	32,	32,	 // 3
                    2,	74,	32,	32,	 // 4
                    38,	38,	32,	32,	 // 5
                    74,	38,	32,	32,	 // 6
                    38,	74,	32,	32,	 // 7
                    110, 2,	 2,	 2,	 // 8
            };

    @Override
    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

}