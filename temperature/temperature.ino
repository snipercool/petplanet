#include <ArduinoJson.h>
#include <FirebaseArduino.h>                                                // firebase library
#include <ESP8266WiFi.h>                                                    // esp8266 library
#include <DHT.h>                                                            // dht11 temperature and humidity sensor library
#include <WiFiUdp.h>
#include <NTPClient.h>

#define WIFI_SSID "telenet-028CF"                                             // input your home or public wifi name 
#define WIFI_PASSWORD "kjmhkn7JcaHj"                                    //password of wifi ssid

#define FIREBASE_HOST "petplanet-63aa0.firebaseio.com"                          // the project name address from firebase id
#define FIREBASE_AUTH "zTRlZs2UFagtJv5ThqTOScznt0MFTexoanO58iD3"            // the secret key generated from firebase
 
#define DHTPIN 2                                                           // what digital pin we're connected to
#define DHTTYPE DHT11                                                       // select dht type as DHT 11 or DHT22
DHT dht(DHTPIN, DHTTYPE);                                                     

//Date
const long utcOffsetInSeconds = 3600;
char daysOfTheWeek[7][12] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", utcOffsetInSeconds);

void setup() {
  Serial.begin(9600);
  delay(1000);                
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);                                     //try to connect with wifi
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());                                            //print local IP address
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);                              // connect to firebase
  dht.begin();                                                               //Start reading dht sensor
  timeClient.begin();
}

void loop() {
  //Daytime
  timeClient.update();
  Serial.print(daysOfTheWeek[timeClient.getDay()]);
  
  float h = dht.readHumidity();                                              // Reading temperature or humidity takes about 250 milliseconds!
  float t = dht.readTemperature();                                           // Read temperature as Celsius (the default)
    
  if (isnan(h) || isnan(t)) {                                                // Check if any reads failed and exit early (to try again).
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }
  
  Serial.print("Humidity: ");  Serial.print(h);
  String fireHumid = String(h) + String("%");                                         //convert integer humidity to string humidity 
  Serial.print("%  Temperature: ");  Serial.print(t);  Serial.println(" °C ");
  String fireTemp = String(t) + String(" °C");                                                     //convert integer temperature to string temperature
  
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  root["temperature"] = t;
  root["day"] = daysOfTheWeek[timeClient.getDay()];
  Firebase.push("/sensors/temperature", root);
  root.printTo(Serial);

  delay(600000); //every 10 minutes
}
