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
   setup_spi(SPI_SS_DISABLED);
   setup_timer_0(RTCC_INTERNAL|RTCC_DIV_4);
   setup_timer_1(T1_DISABLED);
   setup_timer_2(T2_DIV_BY_16,255,1);
   setup_ccp1(CCP_PWM);
   setup_ccp2(CCP_PWM);
   set_pwm1_duty(0);
   set_pwm2_duty(0);
   setup_comparator(NC_NC_NC_NC);// This device COMP currently not supported by the PICWizard
   SET_TRIS_D(0b00000000);

   //TODO: User Code
   
   int value1; //seekbar1
   int value2; //seekbar2
   int pwm1, pwm2;
   
   
   while(1) {
      set_adc_channel(1);
      value1= read_adc();
      pwm1 = 100.0/255*value1;
   
      set_adc_channel(2);
      value2= read_adc();
      pwm2 = 100.0/255*value2;
      
      
      set_pwm1_duty(value1);
      set_pwm2_duty(value2);
      
      if (pwm1 > 50) {
         output_high(PIN_D0);
         output_low(PIN_D1);
         output_high(PIN_D3);
         output_low(PIN_D4);
      } else if (pwm1 < 50) {
         output_low(PIN_D0);
         output_high(PIN_D1);
         output_low(PIN_D3);
         output_high(PIN_D4);
      }
   }

}
