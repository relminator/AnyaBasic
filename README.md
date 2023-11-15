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

Changelog:

0.1.0 (03-08-16)
Initial release.

04-04-16
* Ditched the old line based-interpreter
* Added interfaces for AST
* Recoded the parser
* Added AST

04-05-16
* re-implemented while:loop
* one statement per block only

04-06-16
* nested AST.  Main AST ->while :: while AST -> blockAST
* nested loops
* IF-ELSE construct
* REPEAT loop
* FOR loop
* Support for single line IF-ELSE, FOR and REPEAT statements

04-07-16
* Single variable Functions
* Multi variable Functions
* User defined Sub/Function ( can only be called as a statement and parameters are not working yet )

04-08-16
* Java2D graphics backend
* OpenGL graphics backend 'cuz setcolor() refused to work in Java2D
* Graphics primitives
* Keyboard support

04-10-16
* Refactored code
* Graphics Print
* Graphics Image
* Timer

04-11-16
* format(string, number) function
* User defined functions as part of an expression
* Function parameters

04-12-16
* String variables
* String concatenation
* String comparisons
* Arrays(both definition, assignment and access)
* Swap function

04-13-16
* NOT, AND, OR
* windowClosed
* New syntax for DrawImage
* DrawTransformedImage

04-14-16
* Documentation
* New syntax for DrawLine

0.2.0 04-15-16
* Comments
* Fixed the array[multi-expression] bug

04-16-16
* GetSubImage()
* int() cast
* BoxIntersects()
* CircleIntersects()
* SizeOf()

04-17-16
* Better error reporting

04-18-16
* Break statemment

04-19-16
* Structs

04-20-16
* Array of Structs
* VAR so that definition and assignment is properly delineated for safety
* Better runtime error handling

0.3.0 04-21-16
* Cleaned up how Functions are handled (Definition, Execution and Expression)
* Boolean Conditions now support parentheses
* Better runtime error handling

04-21-16
* Cleaned up how Functions are handled (Definition, Execution and Expression)
* Boolean Conditions now support parentheses
* Input statement

04-22-16
* var max = 10    var array[max]  support
* SetBlendMode()  for glowing sprites
* DrawFancyLine()
* Free()
* DrawScrolledImage()
* SubString()
* ValAscii()

04-27-16 (got a bit lazy...  LOL)
* 2D array support for both vars and user types
* sizeOf(arrayName, dimension)
* Function params are local now.

04-28-16(0.4.3)
* StringLength(stringname)
* Screen() can have an extra 3rd argument to set vsynch(1 - On, 0 - off)
* ScreenFlip() can have a single param to set synch value(60 if absent)
* SoundInit(), SoundPlay(), SoundPause(), SoundStop()

05-05-16
* val()
* isDigit()
* isLetter()
* lowerCase()
* upperCase()

05-10-16
* Functions as paramenters and arguments
* functions can now return a Type, an Array, or an Array of Types.
* split(string) function
* GFX and Sound contexts are destroyed when there's a runtime error.

05-15-16(0.5.1)
* Associative arrays Map< String, value >
* ContainsKey(mapName, keyName)

05-24-16
* Multiple ELSEIF construct

11-14-23
* OpenTextFile(path)
* SaveTextFile(path, content)


Note to self(because updating this project after 7 years and looking at the code felt like having a lobotomy):
1. When adding keywords/statements(e.g. SaveTextFile (filename, content)
	* Make a statement class that implements Statement
	* Make a parslet(Parslets.java) to parse the code
	* Add keyword to lexer
	* Parser -> add case to identifyKeyword() function
	* Parser -> make an execute funtion if it's long enough. Otherwise execute directly from Statement class.

2. When adding intrinsic functions(e.g. var c = OpenTextFile(filename)
	* Parser -> add case to executeFunction()
	* Parser -> make a function, that returns an Expression, that does the job of the keyword or just do it inside he case statement if it's short enough.
	