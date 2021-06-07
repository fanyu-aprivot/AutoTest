import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/6 19:59
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.course")
@EnableSwagger2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
