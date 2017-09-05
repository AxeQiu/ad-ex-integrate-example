package me.qiuyue.tech;


//Spring stuff
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

//exceptions
import java.io.IOException;


@Configuration
public class App {


    public static void main(String[] args)
    throws IOException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
        PowershellUtil psutil = (PowershellUtil)ctx.getBean(PowershellUtil.class);
        psutil.executeRegularCommand("ls ~", "~", "~");
    }

    @Bean
    @Qualifier("ps")
    public PowershellProcessProvider getPowershellProcessProvider() {
        return new PowershellExchangeProvider();
    }

    @Bean
    @Qualifier("psutil")
    @Autowired
    public PowershellUtil getPowershellUtil(PowershellProcessProvider ps)
    throws IOException {
        return new PowershellUtil(ps);
    }

}
