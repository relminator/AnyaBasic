package net.phatcode.rel.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Console extends JFrame
{
    /**
     * 
     */
    private static final long serialVersionUID = 8368625281028328760L;

    private JTextArea textOutput;
    private PrintStream printStream;
    
    public Console() throws IOException
    {
        
        try
        {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         
        } 
        catch (Exception ex)
        {
            System.err.println("Failed to initialize theme. Using fallback.");
        }
        
        ImageIcon abIcon = new ImageIcon(this.getClass().getClassLoader().getResource("assets/anyabasic_pink_32.png"));
        setIconImage(abIcon.getImage());
        
        setTitle("AnyaBasic 1.3.0");
        setPreferredSize(new Dimension(600, 600));
        textOutput = new JTextArea(5, 10);
        //textOutput.setEditable(false);
        textOutput.setFont(new Font("Monospaced", Font.BOLD, (int) 12));
        textOutput.setBackground(Color.BLACK);
        textOutput.setForeground(Color.GREEN);
        textOutput.setCaretColor(Color.GREEN);
        
        JScrollPane scrollPane = new JScrollPane(textOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        printStream = new PrintStream(new CustomOutputStream(textOutput));
        System.setOut(printStream);
        System.setErr(printStream);
        
        add(scrollPane);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        
    }
}