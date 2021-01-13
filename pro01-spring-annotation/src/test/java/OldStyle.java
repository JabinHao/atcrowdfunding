import com.atguigu.entity.Student;
import com.atguigu.spring.MyAnnotationConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OldStyle {


    @Test
    public void testOldStyle() {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyAnnotationConfiguration.class);

        Student student = applicationContext.getBean(Student.class);
        System.out.println(student);
        applicationContext.close();
    }

}
