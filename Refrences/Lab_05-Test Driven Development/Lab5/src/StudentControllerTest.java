import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class StudentControllerTest {
	private StudentController list;
	private Student s1;
	private Student s2;
	private Student s3;
	private Student s4;
	private ArrayList<Student> students;

	@Before
	public void setUp() throws Exception {
		s1 = new Student("1", "izhar");
		s2 = new Student("2", "naael");
		s3 = new Student("1", "hezi");
		s4 = new Student("4", "avi");
		students = new ArrayList<Student>();
		students.add(s1);
		list = new StudentController(students);
	}

	@Test
	public void testAddStudent() {
		ArrayList<Student> expected = students;
		expected.add(s2);
		ArrayList<Student> result = list.addStudent(s2);
		assertTrue(expected.equals(result));
	}

	@Test
	public void testRemoveStudent() {
		ArrayList<Student> expected = new ArrayList<Student>();
		ArrayList<Student> result = list.removeStudent(s1);
		assertTrue(expected.equals(result));
	}

	@Test
	public void testEditStudentName() {
		ArrayList<Student> expected = students;
		expected.set(0, s3);
		ArrayList<Student> result = list.editStudentName(0, "waleed");
		assertTrue(expected.equals(result));
	}

	@Test
	public void testEditStudentid() {
		ArrayList<Student> expected = students;
		expected.set(0, s4);
		ArrayList<Student> result = list.editStudentID(0, "4");
		assertTrue(expected.equals(result));
	}

}