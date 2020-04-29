# Changelog

# 03-08-16 (0.1.0)
Initial release.

# 04-04-16
* Ditched the old line based-interpreter
* Added interfaces for AST
* Recoded the parser
* Added AST

# 04-05-16
* re-implemented while:loop
* one statement per block only

# 04-06-16
* nested AST.  Main AST ->while :: while AST -> blockAST
* nested loops
* IF-ELSE construct
* REPEAT loop
* FOR loop
* Support for single line IF-ELSE, FOR and REPEAT statements

# 04-07-16
* Single variable Functions
* Multi variable Functions
* User defined Sub/Function ( can only be called as a statement and parameters are not working yet )

# 04-08-16
* Java2D graphics backend
* OpenGL graphics backend 'cuz setcolor() refused to work in Java2D
* Graphics primitives
* Keyboard support

# 04-10-16
* Refactored code
* Graphics Print
* Graphics Image
* Timer

# 04-11-16
* format(string, number) function
* User defined functions as part of an expression
* Function parameters

# 04-12-16
* String variables
* String concatenation
* String comparisons
* Arrays(both definition, assignment and access)
* Swap function

# 04-13-16
* NOT, AND, OR
* windowClosed
* New syntax for DrawImage
* DrawTransformedImage

# 04-14-16
* Documentation
* New syntax for DrawLine

# 0.2.0 04-15-16
* Comments
* Fixed the array[multi-expression] bug

# 04-16-16
* GetSubImage()
* int() cast
* BoxIntersects()
* CircleIntersects()
* SizeOf()

# 04-17-16
* Better error reporting

# 04-18-16
* Break statemment

# 04-19-16
* Structs

# 04-20-16
* Array of Structs
* VAR so that definition and assignment is properly delineated for safety
* Better runtime error handling

# 0.3.0 04-21-16
* Cleaned up how Functions are handled (Definition, Execution and Expression)
* Boolean Conditions now support parentheses
* Better runtime error handling

# 04-21-16
* Cleaned up how Functions are handled (Definition, Execution and Expression)
* Boolean Conditions now support parentheses
* Input statement

# 04-22-16
* var max = 10    var array[max]  support
* SetBlendMode()  for glowing sprites
* DrawFancyLine()
* Free()
* DrawScrolledImage()
* SubString()
* ValAscii()

# 04-27-16 (got a bit lazy...  LOL)
* 2D array support for both vars and user types
* sizeOf(arrayName, dimension)
* Function params are local now.

# 04-28-16(0.4.3)
* StringLength(stringname)
* Screen() can have an extra 3rd argument to set vsynch(1 - On, 0 - off)
* ScreenFlip() can have a single param to set synch value(60 if absent)
* SoundInit(), SoundPlay(), SoundPause(), SoundStop()

# 05-05-16
* val()
* isDigit()
* isLetter()
* lowerCase()
* upperCase()

# 05-10-16
* Functions as paramenters and arguments
* functions can now return a Type, an Array, or an Array of Types.
* split(string) function
* GFX and Sound contexts are destroyed when there's a runtime error.

# 05-15-16(0.5.1)
* Associative arrays Map< String, value >
* ContainsKey(mapName, keyName)
