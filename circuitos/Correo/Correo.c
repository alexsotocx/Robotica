#include <Correo.h>
#use rs232(baud=9600, xmit=PIN_C6,rcv=PIN_C7)
// Velocidad,pin transmision, pin recepcion

char SD,Datos[4];
int i;

//Interrupción por recepcion 
//Cada vez que recibe un 7E el sistema se interrumpe y almacena
//el resto de la cadena por separado
#INT_RDA
void serial(){
//Obtenemos el primer dato
SD=getc();
//Preguntamos si es el inicio de la trama de comunicación
if(SD=0x7E){
   //Obtenemos el resto de los datos
     for(i=0; i<4; ++i){
      Datos[i]=getc();
     }
   }
}

void main()
{
   //Permitir interrupciones en el microcontrolador
   enable_interrupts(GLOBAL);
   enable_interrupts(INT_RDA);
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
   SET_TRIS_D( 0b00000000 );
   
   //Condiciones iniciales para que el carrito no salga disparado
   Datos[2]=50;
   Datos[3]=50;

   //TODO: User Code
   int value1,value2,PWM1,PWM2;  
   int duty1,duty2;
   
   while(1){   
   /*
   set_adc_channel(1);
   delay_us(20);
   value1 = read_adc();
   
   set_adc_channel(2);
   delay_us(20);
   value2 = read_adc();
   */
   value1=Datos[2];
   value2=Datos[3];
/////////////////////////////
//se necesitara luego
/////////////////////////////
//Pasa 0-100 => 0-255;
 PWM1=(value1*(255.0/100));
 PWM2=(value2*(255.0/100));
///////////////////////////// 
  /*  PWM1=value1;
      PWM2=value2;
  */
//Analizar los 6 casos propuestos a continuacion y revisar 
//como funcionan en la simulacion
  
//avanza hacia adelante
   if(PWM1>128 && PWM2>120 && PWM2<136 ){
   
   output_low(PIN_D1); // apagar primero antes de prender otro
   output_high(PIN_D0); 
   output_low(PIN_D3);
   output_high(PIN_D2);
   
   //calculo de la velocidad de cada motor
   duty1=(PWM1-128)*2;
   duty2=(PWM1-128)*2;
   set_pwm1_duty(duty1);  
   set_pwm2_duty(duty2);  
   } 
   
//Avanza a la derecha   
   if(PWM1>=128 && PWM2>=136){
   
   output_low(PIN_D1); // apagar primero antes de prender otro
   output_high(PIN_D0); 
   output_low(PIN_D3);
   output_high(PIN_D2);
   
   //calculo de la velocidad de cada motor
   duty1=PWM2-136+PWM1-128;
   duty2=PWM1-128 ;
   set_pwm1_duty(duty1);  
   set_pwm2_duty(duty2); 
   }
       
//Avanza a la izquierda  
   if(PWM1>=128 && PWM2<=120){
   
   output_low(PIN_D1); // apagar primero antes de prender otro
   output_high(PIN_D0); 
   output_low(PIN_D3);
   output_high(PIN_D2);
   
   //calculo de la velocidad de cada motor
   duty1=(PWM1-128);
   duty2=(120-PWM2+PWM1-128);
   set_pwm1_duty(duty1);  
   set_pwm2_duty(duty2);        
   } 
   
//retrocede
   if(PWM1<128 && PWM2>120 && PWM2<136 ){   
   
   output_low(PIN_D0); // apagar primero antes de prender otro
   output_high(PIN_D1);  
   output_low(PIN_D2);
   output_high(PIN_D3);
   
   //calculo de la velocidad de cada motor
   duty1=(127-PWM1)*2;
   duty2=(127-PWM1)*2;
   set_pwm1_duty(duty1);  
   set_pwm2_duty(duty2);  
   }   
 
//retrocede a la izquierda  
   if(PWM1<128 && PWM2<=120){
   
   output_low(PIN_D0); // apagar primero antes de prender otro
   output_high(PIN_D1);  
   output_low(PIN_D2);
   output_high(PIN_D3);
   
   //calculo de la velocidad de cada motor
   duty1=128-PWM1;
   duty2=120-PWM2+128-PWM1;
   set_pwm1_duty(duty1);  
   set_pwm2_duty(duty2); 
   
   }
  
//retrocede a la derecha 
   if(PWM1<128 && PWM2>=136){
   
   output_low(PIN_D0); // apagar primero antes de prender otro
   output_high(PIN_D1);  
   output_low(PIN_D2);
   output_high(PIN_D3);
   
   //calculo de la velocidad de cada motor
   duty1=PWM2-136+128-PWM1;
   duty2=128-PWM1;
   set_pwm1_duty(duty1);  
   set_pwm2_duty(duty2); 
   
   }
   
 }
 
}
