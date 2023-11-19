How to Compile:

I used Eclipse so it may be different depending on what IDE you use.
1. File -> Import Existing projects into wokspace.
2. Project -> Properties -> Java Build Path -> 
	Libraries tab -> Add Jars.
3. Add all the jars in the /libs folder.
		* jinput.jar
		* lwjgl.jar
		* lwjgl_util.jar
		* pngdecoder-1.0.jar
4. Export as a "Runnable Jar"

Note: AnyaBasic.jar needs to be in the same directory as the native files (DLL, SO, dylib)



changes:

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

04-15-16
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
* Break statement

04-19-16
* Types/Structs

04-20-16
* Array of Structs
* VAR so that definition and assignment is properly delineated for safety
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

04-28-16
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

05-15-16
* Associative arrays Map< String, value >
* ContainsKey(mapName, keyName)

05-24-16
* Multiple ELSEIF construct

11-14-23
* OpenTextFile(path)
* SaveTextFile(path, content)

11-15-23
*FileExists(path)

11-19-23
* Added "workspace" commandline argument(used in the IDE)


Note to self(because updating this project after 7 years and looking at the code felt like having a lobotomy):
1. When adding keywords/statements(e.g. SaveTextFile (filename, content)
	* Make a statement class that implements Statement
	* Make a parslet(Parslets.java) to parse the code
	* Lexer -> add a case for the keyword
	* Parser -> add case to identifyKeyword() function
	* Parser -> make an execute funtion if it's long enough. Otherwise execute directly from Statement class.

2. When adding intrinsic functions(e.g. var c = OpenTextFile(filename)
	* Parser -> add case to executeFunction()
	* Parser -> make a function, that returns an Expression, that does the job of the keyword or just do it inside he case statement if it's short enough.
	