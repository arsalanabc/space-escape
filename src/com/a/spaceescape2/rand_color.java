package com.a.spaceescape2;

import java.lang.reflect.Array;
import java.util.Random;

import android.R.array;



public class rand_color {
	
 

public static  float[] rand_color_code(){
	
	//float[] array = {0.09804f,1f,0.6274f};
	float[] array = null;
		//{new Random().nextFloat(),//random hue, color
		//	new Random(10).nextFloat(),//random hue, color
		//	new Random().nextFloat()//random hue, color
		//	};
	
	switch(new Random().nextInt(14)){
	
	case 1: // red
		float[] red = {255,0,0};
		array = red;
		break;
		
	case 2: 
		float[] Lime = {0,255,0};
		array = Lime;
		break;
	case 3: 
		float[] Blue = {0,0,255};
		array = Blue;
		break;	
	case 4: 
		float[] Yellow = {255,255,0};
		array = Yellow;
		break;	
	case 5: 
		float[] Cyan = {0,255,255};
		array = Cyan;
		break;		
	
	case 6: 
		float[] Magenta = {255,0,255};
		array = Magenta;
		break;
	
	case 7: 
		float[] Silver = {192,192,192};
		array = Silver;
		break;
	case 8: 
		float[] Maroon = {128,0,0};
		array = Maroon;
		break;
		
	case 9: 
		float[] Olive = {128,128,0};
		array = Olive;
		break;	
	case 10: 
		float[] Green = {0,128,0};
		array = Green;
		break;	
		
	case 11: 
		float[] Purple = {128,0,128};
		array = Purple;
		break;
	case 12: 
		float[] Teal = {0,128,128};
		array = Teal;
		break;
	case 13: 
		float[] rand = {new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255)};
		array = rand;
		break;
		

		
	case 0: //because random has a 0
		float[] Navy = {0,0,128};
		array = Navy;
		break;

	
	}
	
	/*
	 * 
	 * maroon	#800000	(128,0,0)
 	dark red	#8B0000	(139,0,0)
 	brown	#A52A2A	(165,42,42)
 	firebrick	#B22222	(178,34,34)
 	crimson	#DC143C	(220,20,60)
 	red	#FF0000	(255,0,0)
 	tomato	#FF6347	(255,99,71)
 	coral	#FF7F50	(255,127,80)
 	indian red	#CD5C5C	(205,92,92)
 	light coral	#F08080	(240,128,128)
 	dark salmon	#E9967A	(233,150,122)
 	salmon	#FA8072	(250,128,114)
 	light salmon	#FFA07A	(255,160,122)
 	orange red	#FF4500	(255,69,0)
 	dark orange	#FF8C00	(255,140,0)
 	orange	#FFA500	(255,165,0)
 	gold	#FFD700	(255,215,0)
 	dark golden rod	#B8860B	(184,134,11)
 	golden rod	#DAA520	(218,165,32)
 	pale golden rod	#EEE8AA	(238,232,170)
 	dark khaki	#BDB76B	(189,183,107)
 	khaki	#F0E68C	(240,230,140)
 	olive	#808000	(128,128,0)
 	yellow	#FFFF00	(255,255,0)
 	yellow green	#9ACD32	(154,205,50)
 	dark olive green	#556B2F	(85,107,47)
 	olive drab	#6B8E23	(107,142,35)
 	lawn green	#7CFC00	(124,252,0)
 	chart reuse	#7FFF00	(127,255,0)
 	green yellow	#ADFF2F	(173,255,47)
 	dark green	#006400	(0,100,0)
 	green	#008000	(0,128,0)
 	forest green	#228B22	(34,139,34)
 	lime	#00FF00	(0,255,0)
 	lime green	#32CD32	(50,205,50)
 	light green	#90EE90	(144,238,144)
 	pale green	#98FB98	(152,251,152)
 	dark sea green	#8FBC8F	(143,188,143)
 	medium spring green	#00FA9A	(0,250,154)
 	spring green	#00FF7F	(0,255,127)
 	sea green	#2E8B57	(46,139,87)
 	medium aqua marine	#66CDAA	(102,205,170)
 	medium sea green	#3CB371	(60,179,113)
 	light sea green	#20B2AA	(32,178,170)
 	dark slate gray	#2F4F4F	(47,79,79)
 	teal	#008080	(0,128,128)
 	dark cyan	#008B8B	(0,139,139)
 	aqua	#00FFFF	(0,255,255)
 	cyan	#00FFFF	(0,255,255)
 	light cyan	#E0FFFF	(224,255,255)
 	dark turquoise	#00CED1	(0,206,209)
 	turquoise	#40E0D0	(64,224,208)
 	medium turquoise	#48D1CC	(72,209,204)
 	pale turquoise	#AFEEEE	(175,238,238)
 	aqua marine	#7FFFD4	(127,255,212)
 	powder blue	#B0E0E6	(176,224,230)
 	cadet blue	#5F9EA0	(95,158,160)
 	steel blue	#4682B4	(70,130,180)
 	corn flower blue	#6495ED	(100,149,237)
 	deep sky blue	#00BFFF	(0,191,255)
 	dodger blue	#1E90FF	(30,144,255)
 	light blue	#ADD8E6	(173,216,230)
 	sky blue	#87CEEB	(135,206,235)
 	light sky blue	#87CEFA	(135,206,250)
 	midnight blue	#191970	(25,25,112)
 	navy	#000080	(0,0,128)
 	dark blue	#00008B	(0,0,139)
 	medium blue	#0000CD	(0,0,205)
 	blue	#0000FF	(0,0,255)
 	royal blue	#4169E1	(65,105,225)
 	blue violet	#8A2BE2	(138,43,226)
 	indigo	#4B0082	(75,0,130)
 	dark slate blue	#483D8B	(72,61,139)
 	slate blue	#6A5ACD	(106,90,205)
 	medium slate blue	#7B68EE	(123,104,238)
 	medium purple	#9370DB	(147,112,219)
 	dark magenta	#8B008B	(139,0,139)
 	dark violet	#9400D3	(148,0,211)
 	dark orchid	#9932CC	(153,50,204)
 	medium orchid	#BA55D3	(186,85,211)
 	purple	#800080	(128,0,128)
 	thistle	#D8BFD8	(216,191,216)
 	plum	#DDA0DD	(221,160,221)
 	violet	#EE82EE	(238,130,238)
 	magenta / fuchsia	#FF00FF	(255,0,255)
 	orchid	#DA70D6	(218,112,214)
 	medium violet red	#C71585	(199,21,133)
 	pale violet red	#DB7093	(219,112,147)
 	deep pink	#FF1493	(255,20,147)
 	hot pink	#FF69B4	(255,105,180)
 	light pink	#FFB6C1	(255,182,193)
 	pink	#FFC0CB	(255,192,203)*/
	return array;
}// end rand_color_code




	

}
