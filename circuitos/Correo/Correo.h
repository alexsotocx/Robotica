#include <16F887.h>
#device adc=8

#FUSES NOWDT                 	//No Watch Dog Timer
#FUSES INTRC_IO              	//Internal RC Osc, no CLKOUT
#FUSES NOPUT                 	//No Power Up Timer
#FUSES NOMCLR                	//Master Clear pin used for I/O
#FUSES NOPROTECT             	//Code not protected from reading
#FUSES NOCPD                 	//No EE protection
#FUSES NOBROWNOUT            	//No brownout reset
#FUSES NOIESO                	//Internal External Switch Over mode disabled
#FUSES NOFCMEN               	//Fail-safe clock monitor disabled
#FUSES NOLVP                 	//No low voltage prgming, B3(PIC16) or B5(PIC18) used for I/O
#FUSES NODEBUG               	//No Debug mode for ICD
#FUSES NOWRT                 	//Program memory not write protected
#FUSES BORV40                	//Brownout reset at 4.0V

#use delay(int=4000000)

