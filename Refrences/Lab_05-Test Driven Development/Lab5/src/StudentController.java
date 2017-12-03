import java.util.ArrayList;

public class StudentController {
	private ArrayList<Student> students;

	public StudentController(ArrayList<Student> students) {
		super();
		this.students = students;
	}
	
	public ArrayList<Student> addStudent(Student s) {
		if(s.getId()!=null && s.getName()!=null)
			this.students.add(s);
		return this.students;
	}
	
	public ArrayList<Student> removeStudent(Student s) {
		this.students.remove(s);
		return this.students;
	}
	
	public ArrayList<Student> editStudentName(int i, String new_name) {
		if(new_name!=null) {
			Student s = this.students.get(i);
			s.setName(new_name);
			this.students.set(i, s);
		}
		return this.students;
	}
	
	public ArrayList<Student> editStudentID(int i, String new_id) {
		if(new_id!=null) {
			Student s = this.students.get(i);
			s.setName(new_id);
			this.students.set(i, s);
		}
		return this.students;
	}
}
