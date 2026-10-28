import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SmartCampusApp {

    // ==========================================
    // 1. SOYUTLAMA & KAPSÜLLEME (User & Instructor)
    // ==========================================
    public static abstract class User {
        private String name;
        private String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public abstract String getRoleDetails();
    }

    public static class Instructor extends User {
        private String department;

        public Instructor(String name, String email, String department) {
            super(name, email);
            this.department = department;
        }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        @Override
        public String getRoleDetails() {
            return "Öğretim Elemanı: " + getName() + " (" + department + ")";
        }
    }

    // ==========================================
    // 2. KALITIM & ÇOK BİÇİMLİLİK (Room, LectureHall, ComputerLab)
    // ==========================================
    public static abstract class Room {
        private String roomCode;
        private int capacity;

        public Room(String roomCode, int capacity) {
            this.roomCode = roomCode;
            this.capacity = capacity;
        }

        public String getRoomCode() { return roomCode; }
        public int getCapacity() { return capacity; }

        // Çok biçimlilik (Polymorphism)
        public abstract String getRoomDetails();
    }

    public static class LectureHall extends Room {
        private boolean hasProjector;

        public LectureHall(String roomCode, int capacity, boolean hasProjector) {
            super(roomCode, capacity);
            this.hasProjector = hasProjector;
        }

        @Override
        public String getRoomDetails() {
            return "Anfi [Kod: " + getRoomCode() + ", Kapasite: " + getCapacity() + ", Projeksiyon: " + (hasProjector ? "Var" : "Yok") + "]";
        }
    }

    public static class ComputerLab extends Room {
        private int workStationCount;

        public ComputerLab(String roomCode, int capacity, int workStationCount) {
            super(roomCode, capacity);
            this.workStationCount = workStationCount;
        }

        @Override
        public String getRoomDetails() {
            return "Bilgisayar Lab [Kod: " + getRoomCode() + ", Kapasite: " + getCapacity() + ", İstasyon: " + workStationCount + "]";
        }
    }

    // ==========================================
    // 3. TASARIM KALIBI: FACTORY PATTERN (RoomFactory)
    // ==========================================
    public static class RoomFactory {
        public static Room createRoom(String type, String code, int capacity, int extra) {
            if ("LectureHall".equalsIgnoreCase(type)) {
                return new LectureHall(code, capacity, extra == 1);
            } else if ("ComputerLab".equalsIgnoreCase(type)) {
                return new ComputerLab(code, capacity, extra);
            }
            throw new IllegalArgumentException("Geçersiz derslik tipi!");
        }
    }

    // ==========================================
    // 4. ENTITY SINIFLARI (Course, TimeSlot, Schedule)
    // ==========================================
    public static class Course {
        private String courseCode;
        private String courseName;
        private int weeklyHours;

        public Course(String courseCode, String courseName, int weeklyHours) {
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.weeklyHours = weeklyHours;
        }

        public String getCourseCode() { return courseCode; }
        public String getCourseName() { return courseName; }
        public int getWeeklyHours() { return weeklyHours; }
    }

    public static class TimeSlot {
        private String day;
        private String timeRange;

        public TimeSlot(String day, String timeRange) {
            this.day = day;
            this.timeRange = timeRange;
        }

        public String getDay() { return day; }
        public String getTimeRange() { return timeRange; }

        @Override
        public String toString() {
            return day + " " + timeRange;
        }
    }

    public static class ScheduleRecord {
        private Course course;
        private Room room;
        private Instructor instructor;
        private TimeSlot timeSlot;

        public ScheduleRecord(Course course, Room room, Instructor instructor, TimeSlot timeSlot) {
            this.course = course;
            this.room = room;
            this.instructor = instructor;
            this.timeSlot = timeSlot;
        }

        public Course getCourse() { return course; }
        public Room getRoom() { return room; }
        public Instructor getInstructor() { return instructor; }
        public TimeSlot getTimeSlot() { return timeSlot; }

        @Override
        public String toString() {
            return "[" + timeSlot + "] " + course.getCourseName() + " | Oda: "  + room.getRoomCode() + " | Eğitmen: " + instructor.getName();
        }
    }

    // ==========================================
    // 5. KALICI VERİ SAKLAMA (JsonFileRepository - SRP)
    // ==========================================
    public static class JsonFileRepository {
        private final String filePath;

        public JsonFileRepository(String fileName) {
            this.filePath = Paths.get(System.getProperty("user.dir"), fileName).toString();
        }

        public void saveData(String content) {
            try {
                Files.writeString(Path.of(filePath), content);
            } catch (IOException e) {
                System.err.println("Dosya yazma hatası: " + e.getMessage());
            }
        }

        public String loadData() {
            try {
                Path path = Path.of(filePath);
                if (!Files.exists(path)) return "";
                return Files.readString(path);
            } catch (IOException e) {
                return "";
            }
        }
    }

    // ==========================================
    // 6. ALGORİTMA MOTORU (ScheduleEngine)
    // ==========================================
    public static class ScheduleEngine {
        private List<ScheduleRecord> schedules = new ArrayList<>();

        public boolean assignSchedule(Course course, Room room, Instructor instructor, TimeSlot timeSlot) {
            // Çakışma Kontrolü (Aynı oda veya aynı eğitmen aynı saatte başka derste mi?)
            for (ScheduleRecord record : schedules) {
                if (record.getTimeSlot().getDay().equals(timeSlot.getDay()) &&
                        record.getTimeSlot().getTimeRange().equals(timeSlot.getTimeRange())) {

                    if (record.getRoom().getRoomCode().equals(room.getRoomCode())) {
                        System.out.println("ÇAKIŞMA: " + room.getRoomCode() + " nolu oda bu saatte dolu!");
                        return false;
                    }
                    if (record.getInstructor().getName().equals(instructor.getName())) {
                        System.out.println("ÇAKIŞMA: " + instructor.getName() + " bu saatte başka bir derste!");
                        return false;
                    }
                }
            }

            schedules.add(new ScheduleRecord(course, room, instructor, timeSlot));
            return true;
        }

        public List<ScheduleRecord> getSchedules() {
            return schedules;
        }
    }

    // ==========================================
    // 7. ANA ÇALIŞTIRMA (MAIN METODU & DEMO)
    // ==========================================
    public static void main(String[] args) {
        System.out.println("=== AKILLI KAMPÜS DERS PROGRAMI SİSTEMİ ===");

        // Nesne üretimi (Factory & Polymorphism)
        Room hall1 = RoomFactory.createRoom("LectureHall", "D101", 100, 1);
        Room lab1 = RoomFactory.createRoom("ComputerLab", "LAB-A", 40, 35);

        System.out.println(hall1.getRoomDetails());
        System.out.println(lab1.getRoomDetails());

        // Veri Tanımlamaları
        Instructor instructor1 = new Instructor("Ahmet Yılmaz", "ahmet@kampus.edu", "Bilgisayar Müh.");
        Course course1 = new Course("BIL101", "Nesne Yönelimli Programlama", 4);
        TimeSlot slot1 = new TimeSlot("Pazartesi", "09:00 - 11:00");

        // Algoritma ve Çakışma Yönetimi
        ScheduleEngine engine = new ScheduleEngine();
        boolean success1 = engine.assignSchedule(course1, hall1, instructor1, slot1);
        System.out.println("1. Atama Başarılı mı? " + success1);

        // Çakışma Testi (Aynı odaya aynı saatte başka ders atama denemesi)
        Course course2 = new Course("MAT101", "Matematik I", 3);
        boolean success2 = engine.assignSchedule(course2, hall1, instructor1, slot1);
        System.out.println("2. Çakışma Testi Başarılı mı (False olmalı)? " + success2);

        // Kalıcı Veri Saklama Testi (JSON Repository)
        JsonFileRepository repo = new JsonFileRepository("ders_programi_data.json");
        repo.saveData("{\"status\": \"saved\", \"totalSchedules\": " + engine.getSchedules().size() + "}");
        System.out.println("Kalıcı depodan okunan veri: " + repo.loadData());
    }
}