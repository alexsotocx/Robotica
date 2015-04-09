#include <main.h>




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
