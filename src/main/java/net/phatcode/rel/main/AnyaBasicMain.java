package net.phatcode.rel.main;

/********************************************************************
 *  net.phatcode.rel.main.AnyaBasicMain.java
 *  Entry point/main class
 * 
 *  Richard Eric Lope BSN RN
 *  http://rel.phatcode.net
 *  Started: March 06, 2016
 *  Ended: Ongoing
 *  
 * License MIT: 
 * Copyright (c) 2016 Richard Eric Lope 

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. (As clarification, there is no
 * requirement that the copyright notice and permission be included in binary
 * distributions of the Software.)

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 *******************************************************************/


public class AnyaBasicMain
{

	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.out.println("Usage: AnyaBASIC.jar net.phatcode.rel.main.AnyaBasicMain <source.abs> <debug>");
			System.exit(1);
		}

		boolean debug = false;
		if (args.length > 1)
		{
			if( args[1].equals("debug") ) debug = true;
		}
		net.phatcode.rel.main.Interpreter interpreter = new net.phatcode.rel.main.Interpreter();
		interpreter.run(args[0], debug);



//        String[] source = {
//                "start",
//                    "var angle = 0",
//                    "var frame = 0",
//                    "var dx = 2.5",
//                    "var dy = 2.0",
//                    "var px = 400",
//                    "var py = 300",
//                    "var ballsX[] = {random(400),random(400),random(400),random(400)}",
//                    "var ballsY[] = {random(400),random(400),random(400),random(400)}",
//                    "var ballsDx[] = {-4+random(8),-4+random(8),-4+random(8),-4+random(8)}",
//                    "var ballsDy[] = {-4+random(8),-4+random(8),-4+random(8),-4+random(8)}",
//
//                    "function drawSpokes()",
//                    "start",
//                        "for( var i = 0 to 360 step 5)",
//                        "start",
//                            "var j = i * 3.141516 / 180",
//                            "x1 = 400 + cos(angle + j)* 1200",
//                            "y1 = 300 + sin(angle + j)* 1200",
//                            "x2 = 400 - cos(angle + j)* 1200",
//                            "y2 = 300 - sin(angle + j)* 1200",
//                            "drawLine(x1, y1, x2, y2, 1, 0, 0, 1)",
//                        "end",
//                    "end",
//
//                    "function drawAnimatedBall(dabX,dabY)",
//                    "start",
//                        "drawEllipse(dabX,dabY,50,50,0),+",
//                    "end",
//
//
//                    "function updateBouncingBalls()",
//                    "start",
//                        "for( var i = 0 to 3 step 1 )",
//                        "start",
//                            "ballsX[i] = ballsX[i] + ballsDx[i]",
//                            "if( ballsX[i] > 800 ) then ballsDx[i] = -ballsDx[i]",
//                            "if( ballsX[i] < 0 ) then ballsDx[i] = -ballsDx[i]",
//                            "ballsY[i] = ballsY[i] + ballsDy[i]",
//                            "if( ballsY[i] > 600 ) then ballsDy[i] = -ballsDy[i]",
//                            "if( ballsY[i] < 0 ) then ballsDy[i] = -ballsDy[i]",
//                        "end",
//                    "end",
//
//                    "function drawBouncingBalls()",
//                    "start",
//                        "updateBouncingBalls()",
//                        "for( var i = 0 to 3 step 1 )",
//                        "start",
//                            "drawAnimatedBall(ballsX[i],ballsY[i])",
//                        "end",
//                    "end",
//
//                    "function drawAnimatedBalls()",
//                    "start",
//                        "var ballX = sin(frame*0.02) + cos(frame*0.07)",
//                        "var ballY = cos(frame*0.05) + cos(frame*0.04) + cos(frame*0.06)",
//                        "drawAnimatedBall(400-ballX*200,300-ballY*75)",
//                        "drawAnimatedBall(400+ballX*200,300-ballY*75)",
//                        "drawAnimatedBall(400-ballX*200,300+ballY*75)",
//                        "drawAnimatedBall(400+ballX*200,300+ballY*75)",
//                    "end",
//
//                    "function drawBoxes()",
//                    "start",
//                        "for( var i = 0 to 400 step 25)",
//                        "start",
//                            "var x1 = 400 - i",
//                            "var y1 = 300 - i",
//                            "var x2 = 400 + i",
//                            "var y2 = 300 + i",
//                            "drawLine(x1, y1, x2, y2, 0, abs(sin((frame+i)*0.05)), abs(sin((frame+i)*0.5)), 1),+",
//                        "end",
//                    "end",
//
//                    "function drawCorners()",
//                    "start",
//                        "setColor(1,1,1,1)",
//                        "drawEllipse(150,100,100,150,45),+",
//                        "drawEllipse(800-150,100,100,150,360-45,abs(sin((frame)*0.05)), abs(sin((frame)*0.5)),0,1 ),+",
//                        "drawLine(50, 450, 300, 550, 1, 0, 0, 1),+,+",
//                        "setColor(0,1,0,1)",
//                        "drawEllipse(800-150,600-100,100,100,0)",
//                    "end",
//
//                    "screen(800,600)",
//                    "var imageArray[10]",
//                    "var bgImageArray[3]",
//
//                    "loadimage(\"E:/Images4Coding/anyabasic.png\", imageArray, 0)",
//                    "loadimage(\"E:/Images4Coding/megaman0013.png\", imageArray, 1)",
//                    "loadimage(\"E:/Images4Coding/players.png\", imageArray, 2)",
//                    "loadimage(\"E:/Images4Coding/bg.png\", bgImageArray, 2)",
//                    "getSubImage(210, 140, 295, 309,imageArray[2]),imageArray,3",
//
//                    "getSubImage(0, 0, 255, 511,bgImageArray[2]),bgImageArray,0",
//                    "getSubImage(256, 0, 511, 511,bgImageArray[2]),bgImageArray,1",
//
//
//                    "while( !(KeyDown(1)) && !WindowClosed() )",
//                    "start",
//                        "#  this is a comment #",
//                        "frame = frame + 1",
//                        "cls(0,1,0,0.1)",
//                        "angle = angle + 0.01 # for rotation #",
//                        "drawCorners()",
//                        "drawBoxes()",
//                        "drawSpokes()",
//                        "if( keyDown(200) == 1 ) then py = py - dy",
//                        "if( keyDown(208) == 1 ) then py = py + dy",
//                        "if( keyDown(203) == 1 ) then px = px - dx",
//                        "if( keyDown(205) == 1 ) then px = px + dx",
//                        "setColor(1,0,1,0.5)",
//                        "drawEllipse(px,py,70,70,0),+",
//                        "setColor(0,1,1,1)",
//                        "drawString(0,0, int(timer()) )",
//                        "drawString(100,550,1,1,0,1,\"RELMINATOR\")",
//                        "drawAnimatedBalls()",
//                        "setColor(1,1,1,1)",
//                        "drawImage(100,200),0,imageArray[0]",
//                        "drawImage(100,300,1,0,0,0.7),3,imageArray[0]",
//                        "setColor(1,1,1,1)",
//                        "drawTransformedImage(400,300,frame*4,2,3),3,imageArray[1]",
//                        "drawTransformedImage(400,300,-frame*4,3,2, 0,1,0,1),3,imageArray[1]",
//                        "drawTransformedImage(mouseX(),mouseY(),0,1,1, 1,1,1,1),0,imageArray[1]",
//                        "setColor(0,1,1,0.5)",
//                        "drawBouncingBalls()",
//                        "setColor(1,1,1,1)",
//                        "drawImage(10,20),0,imageArray[3]",
//                        //"setBlendMode(1)",
//                        "drawFancyLine(100,100,600,400,30,1)",
//                        "setBlendMode(0)",
//                        "drawScrolledImage(0,0,1,1.5,-frame*0.01,-frame*0.01),0,bgImageArray[0]",
//                        "delay(14)",
//                        "screenFlip()",
//                    "end",
//                "print(round(100.9234))",
//                "exit(0)",
//                "end"
//
//        };



//    String[] source = {
//            "start",
//                "if( !(1 == 1) && (2 > 1) ) then print(\"TRUE\")",
//                "else print(\"FALSE\")",
//                "",
//            "end",
//    };

//        String[] source = {
//            "start",
//                "write(\"Type something: \")",
//                "input(a)",
//                "print(a)",
//            "end",
//        };

//    String[] source = {
//            "start",
//                "Type MyType",
//                "start",
//                    "a = 0",
//                    "b = \"rel\"",
//                "end",
//                "Type InnerType",
//                "start",
//                    "z = 0",
//                "end",
//                "var MAX = 10",
//                "MyType myTypes[MAX]",
//                "var array[MAX]",
//                "for( var i = 0 to sizeOf(array)-1 step 1 )",
//                "start",
//                    "print(array[i])",
//                "end",
//                "myTypes[5].a  = 20",
//                "print(myTypes[5].a)",
//                //"free(myTypes)",
//                //"print(myTypes[5].a)",
//                "var s = \"RELminator\"",
//                "print(subString(s,1,5))",
//                "print(valAscii(s,1))",
//                "if(s == \"RELminator\") then print(2)",
//                "MyType j",
//                "InnerType k",
//                "k.z = 10",
//                "j.a = 3",
//                "print(j.a)",
//                "MyType x",
//                "MyType y",
//                "x.a = 10",
//                "x.b = 220",
//                "y.a = 55",
//                "y.b = 130",
//                "print(x.a)",
//                "print(x.b)",
//                "x = y",
//                "pHr!nt(x.a)",
//                "print(x.b)",
//                "y.a = 155",
//                "y.b = 1130",
//                "print(x.a)",
//                "print(x.b)",
//
//            "end",
//    };

//    String[] source = {
//            "start",
//                "Type MyType",
//                "start",
//                    "a = 0",
//                    "b = \"rel\"",
//                "end",
//                "var array2d[10][20]",
//                "array2d[2][3] = \"rel\"",
//                "print(array2d[2][3])",
//                "print(array2d[2][0])",
//                "MyType z[30][40]",
//                "Mytype f",
//                "z[1][1] = f",
//                "z[2][4].a = 45",
//                "z[2][4].b = \"Anya\"",
//                "print(z[2][4].a)",
//                "print(z[2][4].b)",
//                "print(sizeof(z))",
//                "print(30*40)",
//                "print(sizeof(z,0))",
//                "print(sizeof(z,1))",
//                "var dork[50] ",
//                "print(sizeof(dork))",
//
//            "end",
//    };

//        String[] source = {
//                "start",
//                "Type MyType",
//                "start",
//                "a = 0",
//                "b = \"rel\"",
//                "end",
//                "function add(x,y)",
//                "start",
//                "print(x)",
//                "print(y)",
//                "return x + y",
//                "end",
//
//                "function printType( zype)",
//                "start",
//                "print(zype.a)",
//                "print(zype.b)",
//                "end",
//
//                "var x = 10",
//                "var y = 20",
//
//
//                "var z = add(100, 200)",
//                "print(z)",
//                "print(x)",
//                "print(y)",
//
//                "myType t[12][20]",
//                "t[1][2].a = 123455",
//                "t[1][2].b = \"anya\"",
//                "printType(t[1][2])",
//
//
//                "end",
//        };

//    String[] source = {
//            "start",
//            "var frames = 64",
//            "var PI = 3.14158265358979",
//            "var gravity = 9.806 * 4",
//
//            "var linkedBody[40]",
//            "var bodyX[40]",
//            "var bodyY[40]",
//            "var vectorX[40]",
//            "var vectorY[40]",
//            "var bodySize[40]",
//            "var bodies = 0",
//            "function createBody( x, y, velocity, angle, size )",
//            "start",
//            "bodyX[bodies] = x",
//            "bodyY[bodies] = y",
//            "vectorX[bodies] = velocity * cos( angle )",
//            "vectorY[bodies] = velocity * sin( angle )",
//            "bodySize[bodies] = size",
//            "bodies = bodies + 1",
//            "return bodies - 1",
//            "end",
//
//            "function drawBody (body)",
//            "start",
//            "setcolor( 1, 1, 1, 0.4 )",
//            "drawEllipse( bodyX[body], bodyY[body], bodySize[body], bodySize[body], 0 ) +",
//            "setcolor( 1, 1, 1, 1 )",
//            "drawEllipse( bodyX[body], bodyY[body], bodySize[body], bodySize[body], 0 )",
//            "end",
//
//            "function moveBody (mbody)",
//            "start",
//            "var linked = linkedBody[mbody]",
//            "if( linked != mbody ) then",
//            "start",
//            "var targetAng = atan2( bodyY[linked] - bodyY[mbody], bodyX[linked] - bodyX[mbody] )",
//            "vectorX[mbody] = vectorX[mbody] + gravity * cos( targetAng )",
//            "vectorY[mbody] = vectorY[mbody] + gravity * sin( targetAng )",
//            "end",
//
//            "bodyX[mbody] = bodyX[mbody] + vectorX[mbody] * ( 1 / frames )",
//            "bodyY[mbody] = bodyY[mbody] + vectorY[mbody] * ( 1 / frames )",
//            "end",
//
//            "function linkBody ( lbody, tobody)",
//            "start",
//            "linkedBody[lbody] = tobody",
//            "end",
//
//            "var center = createBody( 400, 300, 0, 0, 32 )",
//            "for( var i = 1 to sizeOf( linkedBody ) - 1 step 1)",
//            "start",
//            "var tmp = createBody( random( 800 ), random( 600 ), 100 + random( 300 ), random( PI * 2 ), 8 )",
//            "linkBody( tmp, center )",
//            "end",
//
//            "screen( 800, 600 )",
//            "while( !(KeyDown(1)) && !windowclosed() )",
//            "start",
//            "cls()",
//            "for( var i = 0 to sizeOf( linkedBody ) - 1 step 1)",
//            "start",
//            "moveBody( i )",
//            "drawBody( i )",
//            "end",
//            //"print(mouseButtonDown(0))",
//            "delay( 1000 / frames )",
//            "screenflip()",
//            "end",
//            "exit( 0 )",
//            "end",
//        };

//String[] source = {
//        "start",
//            "type BarType",
//            "start",
//                "x  = 0",
//            "end",
//            "BarType bar",
//            "function doBar(x)",
//            "start",
//                "bar.x = x",
//            "end",
//            "function foo(x,y)",
//            "start",
//                "if(x > 2) then",
//                "start",
//                    "print(x)",
//                    "print(y)",
//                "end",
//                  "print(x)",
//            "end",
//            "foo(10,20)",
//            "doBar(11)",
//            "BarType b",
//            "b.x = 5",
//            "BarType c",
//            "c = b",
//            "c.x = 15",
//            "print(b.x)",
//            "print(c.x)",
//            "var f = \"relkanoid\"",
//            "print(stringLength(f))",
//            "soundInit()",
//            "soundLoad( \"assets/bg.wav\", 1)",
//            "soundPlay(\"assets/bg.wav\")",
//            "var ia",
//            "input(ia)",
//            "soundStop(\"assets/bg.wav\")",
//            "input(ia)",
//            "exit(0)",
//        "end",
//
//};

//    String[] source = {
//            "start",
//                "var a = \"1.2345\"",
//                "var b = \"5.0\"",
//                "print(val(a) + val(b))",
//                "var low = \"rel\"",
//                "var hi = \"ANYA\"",
//                "var mix = \"RelMinatoR\"",
//                "print(hi + lowerCase(hi))",
//                "print(low + upperCase(low))",
//                "print(mix + lowerCase(mix))",
//                "print(mix + upperCase(mix))",
//                "print(isDigit(\"1\"))",
//                "print(isDigit(\".\"))",
//                "print(isDigit(\"a\"))",
//                "print(isLetter(\".\"))",
//                "print(isLetter(\"b\"))",
//                "print(isLetter(\"5\"))",
//
//            "end",
//    };

//        String[] source = {
//                "start",
//                "Type MyType",
//                "start",
//                "  a = 10",
//                "  b = \"rel\"",
//                "end",
//                "Function rType()",
//                "start",
//                "   MyType a",
//                "   a.a = 123",
//                "   a.b = 466565",
//                "   return a",
//                "end",
//                "Function rArray()",
//                "start",
//                "   var array[100]",
//                "   array[50] = 1337",
//                "   array[51] = 42",
//                "   array[99] = \"Luko-luko si Anya\"",
//                "   return array",
//                "end",
//                "Function rArray2D()",
//                "start",
//                "   var array2D[10][20]",
//                "   array2D[5][5] = \"2D Array\"",
//                "   array2D[1][2] = 10",
//                "   array2D[3][7] = \"Hokagirl si Anya\"",
//                "   return array2D",
//                "end",
//                "Function rArrayType2D()",
//                "start",
//                "   MyType typeArray2D[10][20]",
//                "   TypeArray2D[5][5].a = \"2D TypeArray\"",
//                "   TypeArray2D[1][2].b = 14344",
//                "   TypeArray2D[3][7].a = \"Gwapo ang Tatay ni Anya\"",
//                "   return TypeArray2D",
//                "end",
//                "MyType z",
//                "z = rType()",
//                "print(z.a)",
//                "print(z.b)",
//                "var y = rArray()",
//                "print(y[0])",
//                "print(y[50])",
//                "print(y[51])",
//                "print(y[99])",
//                "var x = rArray2D()",
//                "print(x[7][7])",
//                "print(x[5][5])",
//                "print(x[1][2])",
//                "print(x[3][7])",
//                "MyType j",
//                "j = rArrayType2D()",
//                "print(j[7][7].a)",
//                "print(j[7][7].b)",
//                "print(j[5][5].a)",
//                "print(j[5][5].b)",
//                "print(j[1][2].a)",
//                "print(j[1][2].b)",
//                "print(j[3][7].a)",
//                "print(j[3][7].b)",
//                "j[3][7].b = \"Aldub!\"",
//                "print(j[3][7].b)",
//                "var s = \"The quick brown fox.\"",
//                "var as = split(s,\" \")",
//                "for( var i = 0 to sizeOf(as)-1 step 1)",
//                "   print(as[i])",
//                "end",
//        };

//    String[] source = {
//            "start",
//            "   Type Vec2D",
//            "   start",
//            "       x = 20",
//            "       y = 30",
//            "   end",
//            "   Vec2D v",
//            "   v.x = 45",
//            "   v.y = 23",
//            "   Var a[10][10]",
//            "   a[1][1] = \"AnyaBasic\"",
//            "   var map{:}",
//            "   map{\"rel\"} = 999",
//            "   map{\"anya\"} = v",
//            "   map{\"Lilytte\"} = a",
//            "   var x ",
//            "   x = map{\"rel\"}",
//            "   x = map{\"anya\"}",
//            "   print(x.x)",
//            "   print(x.y)",
//            "   var y = map{\"Lilytte\"}",
//            "   print(y[1][1])",
//            "   print(containsKey(map,\"anya\"))",
//            "   print(sizeOf(map))",
//            "end",
//    };
//
//        String code = "";
//    for( String aSource : source )
//    {
//        code += aSource + " " + "";
//    }
//    Interpreter interpreter = new Interpreter(code);
//    interpreter.run();

}
	

}
