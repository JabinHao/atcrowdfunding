import com.atguigu.entity.Student;
import com.atguigu.spring.MyAnnotationConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({MyAnnotationConfiguration.class})
public class StudentTest {

    @Autowired
    public Student student;

    @Test
    public void testBean() {
        student.setName("rui");
        student.setId(12015108);
        System.out.println(student);
    }
}
