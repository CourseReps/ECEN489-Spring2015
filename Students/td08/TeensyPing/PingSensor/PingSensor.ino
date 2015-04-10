#include <Ping.h>

Ping ping = Ping(17);

void setup(){
  Serial.begin(115200);
}

void loop(){
  ping.fire();
  //Serial.print("Microseconds: ");
  //Serial.print(ping.microseconds());
  //Serial.print(" | Inches ");
  Serial.println(ping.inches());
  //Serial.print(" | Centimeters: ");
  //Serial.print(ping.centimeters());
  //Serial.println();
  delay(500);
}
