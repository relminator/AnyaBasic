# AnyaBasic
[A]bsolutely [N]ot [Y]our [A]verage BASIC

"The little toy language that could."

![Alt text](http://www.rel.phatcode.net/Mypics/abscreenshot01.png "SHMUP")

AnyaBASIC is a portable Interpreted Programming Language made in Java. 
It has a syntax similar to BASIC with a bit of C, Javascript and PASCAL thrown in.

This is a "Toy Language", so don't expect much. However, while this started as a "joke",
this language evolved into something which is capable of doing a
lot more(games, graphics, etc). It's also a language whose keywords are in English 
so it should be a good language to use in teaching kids how to program.

Release 0.5.1

Download: http://rel.phatcode.net/junk.php?id=157

Author: Relminator (Richard Eric M. Lope)
            http://rel.phatcode.net
            email: vic_viperph@yahoo.com


![Alt text](http://www.rel.phatcode.net/Mypics/abscreenshot02.png "Flappy sa AB")

AnyaBASIC uses the MIT license. That means it's free to use for either non-commercial
or commercial purposes.
See LICENSE.txt for full license.


Features
 
 - Very easy to use
 - Case insensitive
 - As loosely-typed as any language can get
 - Easy to figure out with clean syntax
 - Uses very few global keywords 
 - Fast(for an interpreter made in an interpreted language)
 - Portable
 - Graphics capable
 - Full hardware acceleration


Installing:

* Unzip the archive to any directory of your choice.
* C:/Programming/ is recommended for the ConTEXT IDE to work.
* There's a video of how to set up an offline version
  of Context on my facebook page(read the docs for the link).

How to run the sample codes:

Usage: java -cp AnyaBasic.jar net.phatcode.rel.main.AnyaBasicMain <source.abs> 

* Just type the above line on the terminal/console or
* Double click any of the "runxxxxxx.bat" for Windows users.  
* For graphics and sound apps, you need to include the lib path:
> Java -Djava.library.path=. -cp AnyaBasic.jar net.phatcode.rel.main.AnyaBasicMain <source.abs>
* See the batch and SH files in the samples section.
* You can also use the accompanying ConTEXT editor as an IDE to make coding a bit easier.


Fun fact:

* The language supports "jejemon" and "tagalog" keywords. 
* Please read the docs for the keyword counterparts or open and run
  "jejetaggame.abs"

Credits:

 -  LWJGL for AnyaBasic's graphics and sound backend   
    * www.lwjgl.org
 
 -  ConTEXT Project
    * www.contexteditor.org

 -  Mehmet Emin Coskun for Contra. 
    * mehmetcoskun@fastmail.com.
    * https://github.com/mehmetcnet/contra
    * https://leanpub.com/pic

 -  Bob Nystrom for Jasic.
    * www.stuffwithstuff.com
    * https://github.com/munificent/jasic

 -  Andre Victor T. Vicentini for FreeBasic.
    * www.freebasic.net

 -  Matthias Mann for his nice PNG decoder.

 -  Alexis Munsayac for some really important inputs while testing the language. 

 -  Jonelle H. Castañeda for the trick to force the console to open on windows.
 
 -  Jhun Leo Belen and Vincent Kyle Eusebio for the jejemon conversions.
    
 -  Wilmar Calderon and Gerald Cruz for beta testing.
 
Greets:
- Jon Petrosky(Plasma) for hosting my site at www.phatcode.net
- Dave Stanley(Dr.D)
- Ryan Lloyd
- Richard Wright
- Adigun A. Polack
- Joe Antoon
- Ebben Feagan
- Eric Carr
- Piptol
- Shockwave
- Mario Zechner(Badlogic.com)
- James Robert Osborne
- Angelo Motolla
- Hex Disaster
- Ruben Rodriguez
- Dean Janjic (Lachie)
- Vin Sagun
- John Carlo Franco
- Argelo Royce P. Bautista
- John Derick Mejia
- Vriz Alejo
- Ruel Rule
- Ramon Lansangan
- Romell de Torres
- Marlo Barrientos
- All the guys at Programmers,Developers Facebook Group
- All the guys at FreeBASIC.net
- All the guys at Java-gaming.org
- All the guys at dbfinteractive.com


How to Compile:

You need to add the jar files in the /libs folder as modules and
add references to their native bindings. The native binaries are those 
DLL, SO and LIB files found in the directory where AnyaBasic.jar is located.

