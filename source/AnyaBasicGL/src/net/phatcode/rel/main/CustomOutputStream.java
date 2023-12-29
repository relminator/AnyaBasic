package net.phatcode.rel.main;

// A nice little class that redirects console output to a JTextArea
// Written by:  Nam Ha Minh
// https://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea
// Modified by Richard Eric M. Lope
// Can now handle a scanner and a buffered reader for text input like a proper Terminal.
// 12-29-23


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import javax.swing.JTextArea;

public class CustomOutputStream extends OutputStream
{
    private JTextArea textArea;
    private PrintWriter inWriter;

    public CustomOutputStream(JTextArea textArea) throws IOException
    {
        this.textArea = textArea;
        PipedInputStream pipeIn = new PipedInputStream();
        System.setIn(pipeIn);
        inWriter = new PrintWriter(new PipedOutputStream(pipeIn), true);

        textArea.addKeyListener(new KeyAdapter()
        {
            private StringBuffer line = new StringBuffer();

            @Override
            public void keyTyped(KeyEvent e)
            {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_ENTER)
                {
                    inWriter.println(line);
                    line.setLength(0);
                } 
                else if (c == KeyEvent.VK_BACK_SPACE)
                {
                    if (line.length() > 0)
                        line.setLength(line.length() - 1);
                } 
                else if (!Character.isISOControl(c))
                {
                    line.append(e.getKeyChar());
                }
            }
        });

    }

    @Override
    public void write(int b) throws IOException
    {
        // redirects data to the text area
        textArea.append(String.valueOf((char) b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
