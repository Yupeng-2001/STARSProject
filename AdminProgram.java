import java.util.Date;
import java.io.File;
import java.io.IOException;

public class AdminProgram
{
    public String Current_User = "Admin";
    public MainProgram main;
    public CourseManager courseManager;

    AdminProgram(MainProgram Main, CourseManger CourseManager)
    {
        main = Main;
        courseManager = CourseManager;
    }



    void EditAccessPeriod(Datetime newPeriod)
    {
        main.AccessPeriod = newPeriod;
    }

    void AddaStudent(String name, String matric, String gender, String nation)
    {
        Student newStudent = new Student(name, matric, gender, nation)
        FileOutputStream fos = new FileOutputStream(name+".txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(newStudent);
        oos.close();
    }

    void CheckAvailableSlot(int index)
    {
        Course current_course = courseManager.GetCourse(index);
        int Slot = current_course.indexes[index].vaccancy;
        System.out.println("there are ",Slot," vacancies for index ",index);
    }


    void PrintStudentByIndex(int index)
    {
        Course current_course = courseManager.GetCourse(index);
        StudentList = current_course.indexes[index].printStudnetList();
    }

    void PrintStudentByCourse(String CourseName)
    {
        //not sure how i could find all the indexes
    }
}