/* UART Example, any character received on either the real
   serial port, or USB serial (or emulated serial to the
   Arduino Serial Monitor when using non-serial USB types)
   is printed as a message to both ports.

   This example code is in the public domain.
*/

// set this to the hardware serial port you wish to use
int right_antenna = 6;
int left_antenna = 4;

void setup() {
	Serial.begin(9600);
        pinMode(left_antenna, OUTPUT);   
        pinMode(right_antenna, OUTPUT);           
}

void loop() {
        char incomingByte;
        
	if (Serial.available() > 0) {
		incomingByte = Serial.read();
		Serial.println(incomingByte);
                if(incomingByte == '0'){
                  digitalWrite(left_antenna,LOW);
                }
                if(incomingByte == '1'){
                  digitalWrite(left_antenna,HIGH);
                }                
                if(incomingByte == '2'){
                  digitalWrite(right_antenna,LOW);
                }                
                if(incomingByte == '3'){
                  digitalWrite(right_antenna,HIGH);
                }
	}
}

