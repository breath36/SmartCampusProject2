# Akıllı Kampüs Ders Programı Planlama Sistemi (Smart Campus Schedule)

Bu proje, üniversite kampüslerinde derslik ve öğretim elemanlarının zaman çakışmaları yaşanmadan otomatik olarak ders programına yerleştirilmesini sağlayan, nesne yönelimli programlama (OOP) ilkelerine ve SOLID prensiplerine tam uygun olarak geliştirilmiş bir konsol uygulamasıdır.

## 🚀 Proje Özellikleri ve Yetenekler
- **Çakışmasız Otomatik Planlama:** Aynı derslik veya aynı öğretim elemanı için aynı zaman diliminde (TimeSlot) çakışma oluşmasını engelleyen akıllı motor.
- **Kalıcı Veri Saklama:** Verilerin JSON formatında diske kaydedilmesini ve okunmasını sağlayan yapı.
- **Modüler Mimari:** Tek dosya veya katmanlı yapıda kolayca çalıştırılabilir konsol arayüzü.

---

## 🏗️ Nesne Yönelimli Programlama (OOP) ve Tasarım Unsurları

Projede OOP'nin 4 temel şartı eksiksiz uygulanmıştır:
1. **Soyutlama (Abstraction):** `User` ve `Room` sınıfları `abstract` olarak tanımlanarak ortak şablonlar oluşturulmuştur.
2. **Kalıtım (Inheritance):** `Instructor` sınıfı `User` sınıfından; `LectureHall` ve `ComputerLab` sınıfları ise `Room` sınıfından türetilmiştir.
3. **Çok Biçimlilik (Polymorphism):** `Room` sınıfındaki `getRoomDetails()` soyut metodu alt sınıflarda kendine özgü şekilde `override` edilmiştir.
4. **Kapsülleme (Encapsulation):** Sınıf içerisindeki kritik değişkenler `private` tutulmuş, kontrollü erişim için `getter/setter` metotları kullanılmıştır.

### 📐 SOLID Prensipleri (4 Adet)
- **Single Responsibility Principle (SRP):** `JsonFileRepository` sadece veri saklama ile, `ScheduleEngine` ise sadece yerleştirme algoritmasıyla ilgilenir.
- **Open/Closed Principle (OCP):** Sisteme yeni bir oda tipi (`SeminarRoom`) eklenmek istendiğinde mevcut kodlar değiştirilmez, sadece `Room` sınıfından yeni bir sınıf türetilir.
- **Interface Segregation Principle (ISP):** Sınıflar ve metotlar gereksiz bağımlılıklardan arındırılmıştır.
- **Dependency Inversion Principle (DIP):** Üst seviye modüller somut detaylara değil, soyutlamalara bağımlıdır.

### 🎨 Tasarım Kalıpları (Design Patterns - 2 Adet)
1. **Factory Pattern (`RoomFactory`):** İhtiyaca göre dinamik olarak farklı tipte derslik nesneleri (`LectureHall`, `ComputerLab`) üretir.
2. **Repository Pattern (`JsonFileRepository`):** Veritabanı ve dosya yönetimini soyutlayarak veri erişimini standartlaştırır.

---

## 📂 10 Anlamlı Sınıf Mimarisi

1. `User` (Abstract Base Class)
2. `Instructor` (Derived Class)
3. `Room` (Abstract Base Class)
4. `LectureHall` (Derived Class)
5. `ComputerLab` (Derived Class)
6. `Course` (Entity Class)
7. `TimeSlot` (Value Object)
8. `ScheduleRecord` (Matching Entity)
9. `JsonFileRepository` (Data Access Class)
10. `ScheduleEngine` (Core Algorithm Class)

---

## 🛠️ Kurulum ve Çalıştırma

Projeyi bilgisayarınızda çalıştırmak için aşağıdaki adımları izleyebilirsiniz:

1. Depoyu klonlayın:
   ```bash
   git clone [https://github.com/KULLANICI_ADINIZ/SmartCampusSchedule.git](https://github.com/KULLANICI_ADINIZ/SmartCampusSchedule.git)
