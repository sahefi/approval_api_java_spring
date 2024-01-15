package approval_api.approval_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"approval_api.approval_api"})
public class ApprovalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApprovalApiApplication.class, args);
	}

}
