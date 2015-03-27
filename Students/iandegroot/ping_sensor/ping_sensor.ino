#include <Ping.h>

Ping ping = Ping(13);

void setup(){
  Serial.begin(115200);
}
int val;

void loop(){

  ping.fire();

  Serial.println(ping.inches());

  delay(250);
}
