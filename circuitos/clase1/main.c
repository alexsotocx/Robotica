#include <16F887.h>
#device adc=8

#FUSES NOWDT                    //No Watch Dog Timer
#FUSES INTRC_IO                 //Internal RC Osc, no CLKOUT
#FUSES NOPUT                    //No Power Up Timer
#FUSES NOMCLR                   //Master Clear pin used for I/O
#FUSES NOPROTECT                //Code not protected from reading
#FUSES NOCPD                    //No EE protection
#FUSES NOBROWNOUT               //No brownout reset
#FUSES NOIESO                   //Internal External Switch Over mode disabled
#FUSES NOFCMEN                  //Fail-safe clock monitor disabled
#FUSES NOLVP                    //No low voltage prgming, B3(PIC16) or B5(PIC18) used for I/O
#FUSES NODEBUG                  //No Debug mode for ICD
#FUSES NOWRT                    //Program memory not write protected
#FUSES BORV40                   //Brownout reset at 4.0V

#use delay(int=4000000)


void main()
{

   setup_adc_ports(sAN1|sAN2|VSS_VDD);
   setup_adc(ADC_CLOCK_INTERNAL);
   setup_timer_0(RTCC_INTERNAL|RTCC_DIV_1);

   //TODO: User Code
   
   int value1;
   int value2;
   
   
   
   while(1) {
      set_adc_channel(1);
      value1= read_adc();
   
      set_adc_channel(2);
      value2= read_adc();
      
      if(value1>128){
         output_high(PIN_D0);
         output_low(PIN_D1);
         output_high(PIN_D2);
         output_low(PIN_D3);
      } else if(value1<128) {
         output_low(PIN_D0);
         output_high(PIN_D1);
         output_low(PIN_D2);
         output_high(PIN_D3);
      } else {
         output_low(PIN_D0);
         output_low(PIN_D1);
         output_low(PIN_D2);
         output_low(PIN_D3);
      }
   }
}
