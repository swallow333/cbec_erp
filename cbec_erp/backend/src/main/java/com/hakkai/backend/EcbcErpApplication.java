package com.hakkai.backend;

import com.hakkai.backend.util.ComputerInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.io.IOException;

@SpringBootApplication //boot class
@MapperScan("com.hakkai.backend.mapper") //All interfaces generate the corresponding implementation class after compile
@ServletComponentScan //Scan Servlet、Filter、Listener
@EnableScheduling //enable scheduled task
public class EcbcErpApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(EcbcErpApplication.class, args); //Receives the application's main class and command-line arguments
        Environment environment = context.getBean(Environment.class); //Provides methods for accessing application environment properties
        System.out.println("启动成功，后端服务API地址：http://" + ComputerInfo.getIpAddr() + ":" + environment.getProperty("server.port") + "/backend/doc.html"); //Gets the IP address of the computer
        System.out.println("您还需启动前端服务，启动命令：npm run dev，测试用户：cbec，密码：123456");
    }

}
