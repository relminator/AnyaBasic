todo:
1. Value Function (would make function calls easier)(done)
2. Local variables in functions (Value functions would have their own symbol table)
3. Var keyword (done)



04-05-2016 (fixed 04-16-16 by adding nested ASTs)
// While statement
// bug!!  Code here is fine
// However, when condition is false
// block statement still gets executed
// Weird thing is,
// the code executed is not from the While node but the main AST node itself and
// the current token at this juncture is AFTER the blockstatement
// which should be the case.
// possible fix:
// identifyKeyword adds the statement to the main AST so make another
// function to mimic the call but don't add the statement to the AST.
// pseudo:
// block has it's own AST nested from the main AST via the while statement
// add all the statements inside the condition there
// execute the nested AST from  Statementwhile


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

