import java.util.*;

class Course {
    String code, title, description, schedule;
    int capacity;
    List<Student> registeredStudents;

    public Course(String code, String title, String description, int capacity, String schedule) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.registeredStudents = new ArrayList<Student>();
    }

    public boolean isSlotAvailable() {
        return registeredStudents.size() < capacity;
    }

    public void registerStudent(Student s) {
        if (isSlotAvailable() && !registeredStudents.contains(s)) {
            registeredStudents.add(s);
        }
    }

    public void removeStudent(Student s) {
        registeredStudents.remove(s);
    }

    public int getAvailableSlots() {
        return capacity - registeredStudents.size();
    }

    public String toString() {
        return code + ": " + title + "\nDescription: " + description + 
               "\nSchedule: " + schedule + 
               "\nCapacity: " + capacity + 
               "\nAvailable Slots: " + getAvailableSlots() + "\n";
    }
}

class Student {
    String id, name;
    List<Course> registeredCourses;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.registeredCourses = new ArrayList<Course>();
    }

    public void registerCourse(Course course) {
        if (!registeredCourses.contains(course) && course.isSlotAvailable()) {
            registeredCourses.add(course);
            course.registerStudent(this);
            System.out.println("Successfully registered for " + course.code);
        } else {
            System.out.println("Cannot register. Either already registered or course is full.");
        }
    }

    public void dropCourse(Course course) {
        if (registeredCourses.contains(course)) {
            registeredCourses.remove(course);
            course.removeStudent(this);
            System.out.println("Dropped course " + course.code);
        } else {
            System.out.println("You are not registered in this course.");
        }
    }

    public void showRegisteredCourses() {
        if (registeredCourses.isEmpty()) {
            System.out.println("No registered courses.");
            return;
        }
        System.out.println("Registered Courses:");
        for (Course c : registeredCourses) {
            System.out.println("- " + c.code + ": " + c.title);
        }
    }
}

public class CourseManagementApp {
    static Scanner sc = new Scanner(System.in);
    static List<Course> courseList = new ArrayList<Course>();
    static Map<String, Student> studentMap = new HashMap<String, Student>();

    public static void main(String[] args) {
        initializeCourses();

        while (true) {
            System.out.println("\n--- Course Management Menu ---");
            System.out.println("1. View All Courses");
            System.out.println("2. Register Student");
            System.out.println("3. Register for a Course");
            System.out.println("4. Drop a Course");
            System.out.println("5. View Student's Registered Courses");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    displayCourses();
                    break;
                case 2:
                    registerStudent();
                    break;
                case 3:
                    courseRegistration();
                    break;
                case 4:
                    courseRemoval();
                    break;
                case 5:
                    viewStudentCourses();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void initializeCourses() {
        courseList.add(new Course("CS101", "Intro to CS", "Basics of Computer Science", 3, "Mon-Wed 10:00-11:30"));
        courseList.add(new Course("MA101", "Calculus", "Differential and Integral Calculus", 2, "Tue-Thu 12:00-13:30"));
        courseList.add(new Course("PHY101", "Physics I", "Mechanics and Waves", 2, "Mon-Wed 14:00-15:30"));
    }

    public static void displayCourses() {
        System.out.println("\nAvailable Courses:");
        for (Course c : courseList) {
            System.out.println(c);
        }
    }

    public static void registerStudent() {
        System.out.print("Enter student ID: ");
        String id = sc.nextLine();
        if (studentMap.containsKey(id)) {
            System.out.println("Student already registered.");
            return;
        }
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        Student s = new Student(id, name);
        studentMap.put(id, s);
        System.out.println("Student registered successfully.");
    }

    public static Student getStudent() {
        System.out.print("Enter your student ID: ");
        String id = sc.nextLine();
        if (!studentMap.containsKey(id)) {
            System.out.println("Student not found. Register first.");
            return null;
        }
        return studentMap.get(id);
    }

    public static void courseRegistration() {
        Student student = getStudent();
        if (student == null) return;

        displayCourses();
        System.out.print("Enter course code to register: ");
        String code = sc.nextLine().toUpperCase();

        for (Course c : courseList) {
            if (c.code.equals(code)) {
                student.registerCourse(c);
                return;
            }
        }
        System.out.println("Course not found.");
    }

    public static void courseRemoval() {
        Student student = getStudent();
        if (student == null) return;

        student.showRegisteredCourses();
        System.out.print("Enter course code to drop: ");
        String code = sc.nextLine().toUpperCase();

        for (Course c : courseList) {
            if (c.code.equals(code)) {
                student.dropCourse(c);
                return;
            }
        }
        System.out.println("Course not found.");
    }

    public static void viewStudentCourses() {
        Student student = getStudent();
        if (student != null) {
            student.showRegisteredCourses();
        }
    }
}
