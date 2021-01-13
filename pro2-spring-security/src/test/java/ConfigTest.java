import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ConfigTest {

    @Test
    public void testEncoder() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = "123456";
        System.out.println(passwordEncoder.encode("123456"));

    }

}
