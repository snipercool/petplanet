#include <ArduinoJson.h>
#include <FirebaseArduino.h>                                                // firebase library
#include <ESP8266WiFi.h>                                                    // esp8266 library
#include <ESP8266HTTPClient.h>
#include <DHT.h>                                                            // dht11 temperature and humidity sensor library
#include <WiFiUdp.h>
#include <NTPClient.h>

#define WIFI_SSID "NAME_OF_WIFI"                                             // input your home or public wifi name 
#define WIFI_PASSWORD "PASSWORD"                                    //password of wifi ssid

#define FIREBASE_HOST "PROJECT_NAME"                          // the project name address from firebase id
#define FIREBASE_AUTH "SECRET_KEY"            // the secret key generated from firebase
 
#define DHTPIN 2                                                           // what digital pin we're connected to
#define DHTTYPE DHT11                                                       // select dht type as DHT 11 or DHT22
DHT dht(DHTPIN, DHTTYPE);                                                     

//Date
const long utcOffsetInSeconds = 3600;
char daysOfTheWeek[7][12] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", utcOffsetInSeconds);

//Weather outside
const String LOCATION = "Mechelen,be";
const String URL = "http://api.openweathermap.org/data/2.5/weather?q=";
const String API_KEY_QUERY = "API_KEY";
const String OPTIONS = "&mode=json&units=metric";

const String FULL_URL = URL + LOCATION + API_KEY_QUERY + OPTIONS;

float tempOutstide;

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
  if (WiFi.status() == WL_CONNECTED) 
  {
    HTTPClient http; //Object of class HTTPClient
    http.begin(FULL_URL); //Specify the URL and certificate.
    int httpCode = http.GET();
    Serial.print("Searching for API.");

    /*
      < 0 there is a connection error
      > 0 api found
    */
    if (httpCode > 0) 
    {
      const size_t bufferSize = JSON_OBJECT_SIZE(2) + JSON_OBJECT_SIZE(3) + JSON_OBJECT_SIZE(5) + JSON_OBJECT_SIZE(8) + 370;
      DynamicJsonBuffer jsonBuffer(bufferSize);
      String jsonString = http.getString();

      // FIND FIELDS IN JSON TREE
      JsonObject& root = jsonBuffer.parseObject(jsonString);
      JsonObject& main = root["main"];
      if (!root.success()) {
        Serial.println("parseObject() failed");
        return;
      }

      //Getting and displaying the temperature
      tempOutstide = main["temp"];
      Serial.print("The temperature is: ");
      Serial.println(tempOutstide);
    }
    else{
      Serial.print("Error in HTTP request");
    }
    http.end(); //Close connection
  }
  
  //Daytime
  timeClient.update();
  
  float h = dht.readHumidity();                                              // Reading temperature or humidity takes about 250 milliseconds!
  float tempInside = dht.readTemperature();                                           // Read temperature as Celsius (the default)
    
  if (isnan(h) || isnan(tempInside)) {                                                // Check if any reads failed and exit early (to try again).
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }
  
  Serial.print("%  Temperature: ");  Serial.print(tempInside);  Serial.println(" °C ");
  String fireTemp = String(tempInside) + String(" °C");                                                     //convert integer temperature to string temperature
  
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  root["temperatureOutstide"] = tempOutstide;
  root["temperatureInside"] = tempInside;
  root["day"] = daysOfTheWeek[timeClient.getDay()];
  Firebase.push("/sensors/temperature", root);
  root.printTo(Serial);

  delay(600000); //every 10 minutes
}
