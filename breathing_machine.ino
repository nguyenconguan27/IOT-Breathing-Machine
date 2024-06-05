// import các thư viện cần dùng
#include <ESP8266WiFi.h>
#include <Wire.h>
#include "MAX30100_PulseOximeter.h"
#include <BMP180I2C.h>
#include <Adafruit_BMP085.h>
#include <FirebaseESP8266.h>

const char *ssid = "iPhone cua Han";
const char *password = "21042002";
// khởi tạo các chân kết nối của driver l298n với esp8266
const int enA = D3;
const int in1 = D5;
const int in2 = D6;
// khỏi tạo mức áp suất lớn nhất và vỏ nhất trong bình oxy, để có thể đo dung tích của bình
const int max_pressure = 101575;
const int min_pressure = 99200;
// khai báo các mức xung cung cấp cho enA, tương ứng với các mức bơm khí của máy lần lượt là 4l/p, 6l/p, 8l/p
const int mode_arr[3] = { 80, 100, 150 };
int cnt = 0;
uint32_t ts1 = 0, ts2 = 0;
// URL kết nối với Firebase
String path = "/may_tho/1/";
unsigned long currentMillis;
// các biến điều khiển máy thở, lần lượt là trạng thái tắt/mở, chế độ điều khiển tự động/tuỳ chỉnh, lưu lượng bơm trong bình(0, 1, 2)
int status, mode, flow;
// các chỉ số nhịp tim và spo2 đo được
int heartBeat, spo2;
// biến xác thực lấy từ Firebase
#define FIREBASE_AUTH "IsSolySsZLqNJN2mwi5mfnHENu8y65ufidtBrqqn"
#define FIREBASE_HOST "breathing-machine-bf198-default-rtdb.asia-southeast1.firebasedatabase.app"
// đối tượng đo cảm biến MAX30100 từ thư viện MAX30100_PulseOximeter
PulseOximeter pox;
// đối tượng đo cảm biến BMP180 từ thư viện Adafruit_BMP085
Adafruit_BMP085 bmp;
// đối tượng kết nối Firebase 
FirebaseData firebaseData;

void onBeatDetected() {
  Serial.println("Beat!");
}

void setup() {
  Serial.begin(115200);
  // kết nối Wifi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(250);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  // định nghĩa các chân kết nối với driver điều khiển L298N
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  // khởi tạo đối tượng pox đo cảm biến nhịp tim/spo2
  if (!pox.begin()) {
    Serial.print("Initializing pulse oximeter FAILED");
  } else {
    Serial.print("Initializing pulse oximeter SUCCESS");
  }
  // khở tạo
  if (!bmp.begin()) {
    Serial.println("Initializing Adafruit_BMP085 FAILED");
    for (;;)
      ;
  }
  else {
    Serial.println("Initializing Adafruit_BMP085 SUCCESS");
  }
  Serial.println("Cảm biến MAX30100 và BMP180 đã được kết nối!");
  pox.setOnBeatDetectedCallback(onBeatDetected);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());
  // khởi tạo kết nối Firebase
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
}
// hàm đo nhịp tim và spo2
void max30100Monitor() {
  heartBeat = ceil(pox.getHeartRate());
  spo2 = pox.getSpO2();
  Serial.print("Heart rate:");
  Serial.print(heartBeat);
  //nếu trong 3 giây, 1 trong 2 chỉ số xuống mức 0(có thể do bệnh nhân xê dịch/nhấc tay) thì mới cấp nhập lên hệ thống
  if(spo2 == 0 || heartBeat == 0) {
      cnt++;
      if(cnt == 4)
        cnt = 0;
  }
  else {
    cnt = 0;
  }Serial.println(cnt);
  // cứ sau 3 giấy, nhịp tim và spo2 lại được cập nhập lên hệ thống
  if (currentMillis - ts2 >= 3 * 1000 || ts2 == 0) {
    if((spo2 != 0 && heartBeat != 0) || cnt == 3) {
      Firebase.setFloat(firebaseData, path + "nhip_tim", heartBeat);
      Firebase.setInt(firebaseData, path + "spo2", spo2);
      // biến cap_nhap trên firebase quyết định xem trên website có cập nhập các giá trị mới của bệnh nhân không
      Firebase.setInt(firebaseData, path + "cap_nhap", 1);
    }
    ts2 = currentMillis;
  }
  Serial.print("bpm / SpO2:");
  Serial.print(spo2);
  Serial.println("%");
}

// hàm điều chỉnh máy thở(bật/tắt, điều chỉnh lưu lương)
void adjustMachine() {
  // nếu máy tắt, mức điện áp cấp cho enA = 0
  if (status == 0) {
    analogWrite(enA, 0);
  } else {
    // nếu máy ở chế độ tự động, dựa vào các chỉ số của bệnh nhân đưa ra mức lưu lượng khí phù hợp
    if (mode == 1) {
      if (spo2 > 95) {
        analogWrite(enA, mode_arr[0]);
      } else if (spo2 >= 94) {
        analogWrite(enA, mode_arr[1]);
      } else {
        analogWrite(enA, mode_arr[2]);
      }
    } else {
      analogWrite(enA, mode_arr[flow]);
    }
  }
}
// hàm đo dung tích trong bình oxy
void bmp180Monitor() {
  float pressure = bmp.readPressure();
  Serial.print(" °C - Áp suất: ");
  Serial.print(pressure);
  Serial.println(" hPa");
  int cap = round(1.0 * (pressure - min_pressure) / (max_pressure - min_pressure) * 100);
  Firebase.setInt(firebaseData, path + "dung_luong", cap <= 100 ? cap : 100);
}
// hàm lấy dữ liệu tử Firebase
int firebasegetData(String field) {
  if (Firebase.getInt(firebaseData, path + field)) {
    int value = firebaseData.intData();
    return value;
  }
  return 0;
}

void loop() {
  currentMillis = millis();
  // cập nhập lại dữ liệu của MAX30100
  pox.update();
  if ((currentMillis - ts1 >= 1000) || ts1 == 0) {
    pox.shutdown();
    // trước khi gọi các api của Firebase cần phải đóng đối tượng pox lại(nếu không sẽ gây ra lỗi)
    max30100Monitor();
    status = firebasegetData("trang_thai");
    mode = firebasegetData("che_do");
    flow = firebasegetData("luu_luong");
    bmp180Monitor();
    pox.resume();
    Serial.print("trang thai: ");
    Serial.println(status);
    Serial.print("che do: ");
    Serial.println(mode);
    Serial.print("luu luong: ");
    Serial.println(flow);
    ts1 = currentMillis;
    yield();
  }
  adjustMachine();
}