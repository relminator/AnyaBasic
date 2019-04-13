package net.phatcode.rel.multimedia;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*********************************************************************************
 * Created by relminator
 * <p>
 * Richard Eric Lope BSN RN
 * http://rel.phatcode.net
 * Started: 4/28/2016
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

public class Sonics
{
    private Map<String, SoundEntity> samples = new HashMap<>();
    private List<String> names = new ArrayList<>();

    public Sonics()
    {
        // Initialize OpenAL and clear the error bit.
        try
        {
            AL.create();
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
            return;
        }
        AL10.alGetError();

    }

    public int addSample( String filename, boolean isLooping )
    {
        SoundEntity sound = new SoundEntity();
        int res = sound.load( filename, isLooping );
        samples.put( filename, sound );
        names.add( filename );
        return res;
    }

    public void play( String filename )
    {
        samples.get(filename).play();
    }

    public void pause( String filename )
    {
        samples.get(filename).pause();
    }

    public void stop( String filename )
    {
        samples.get(filename).stop();
    }

    public void shutDown()
    {
        for( int i = 0; i < names.size(); i++ )
        {
            samples.get( names.get(i) ).destroy();
        }
        AL.destroy();
    }
}


class SoundEntity
{
    private IntBuffer buffer;

    private IntBuffer source;

    private FloatBuffer sourcePos;

    private FloatBuffer sourceVel;

    private FloatBuffer listenerPos;

    private FloatBuffer listenerVel;

    private FloatBuffer listenerOri;

    public SoundEntity()
    {

        buffer = BufferUtils.createIntBuffer(1);

        source = BufferUtils.createIntBuffer(1);

        sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

        sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

        listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

        listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

        listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();

    }

    public int load( String filename, boolean isLooping )
    {
        // Load wav data into a buffer.
        AL10.alGenBuffers(buffer);

        if(AL10.alGetError() != AL10.AL_NO_ERROR)
            return AL10.AL_FALSE;

        URL url = this.getClass().getClassLoader().getResource(filename);

        InputStream in ;
        WaveData waveFile = null;
        try
        {
            in = url.openStream();
            //Loads the wave file from this class's package in your classpath
            waveFile = WaveData.create(in);
            in.close();
            if(waveFile == null )
            {
                System.out.println("Error Loading sound data!");
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }


        AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();

        // Bind the buffer with the source.
        AL10.alGenSources(source);

        if (AL10.alGetError() != AL10.AL_NO_ERROR)
            return AL10.AL_FALSE;

        AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
        AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
        AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
        AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
        AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );
        if( isLooping ) AL10.alSourcei(source.get(0), AL10.AL_LOOPING,  AL10.AL_TRUE  );

        setListenerValues();

        // Do another error check and return.
        if (AL10.alGetError() == AL10.AL_NO_ERROR)
            return AL10.AL_TRUE;

        return AL10.AL_FALSE;
    }

    private void setListenerValues()
    {
        AL10.alListener(AL10.AL_POSITION,    listenerPos);
        AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
        AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
    }

    public void destroy()
    {
        AL10.alDeleteSources(source);
        AL10.alDeleteBuffers(buffer);
    }

    public void play()
    {
        AL10.alSourcePlay(source.get(0));
    }

    public void pause()
    {
        AL10.alSourcePause(source.get(0));
    }

    public void stop()
    {
        AL10.alSourceStop(source.get(0));
    }

}
