package com.authxero.discrash;

import com.authxero.discrash.executor.CommandExecutorService;
import com.authxero.discrash.storage.StorageProperties;
import com.authxero.discrash.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DiscrashApplication {


	public static void main(String[] args) {
		if(args.length >= 1){
			try
			{
				int port = Integer.parseInt(args[0]);
				if(port > 0 && port < 65535){
					System.setProperty("server.port", Integer.toString(port));
				}
				else
				{
					System.out.println(args[0] + " is not a valid port! Defaulting to 8080");
				}
			}
			catch (NumberFormatException e)
			{
				System.out.println(args[0] + " is not a valid port! Defaulting to 8080");
			}
		}
		if(args.length == 2){
			if(args[1].equals("true")){
				CommandExecutorService.isDebug = true;
			}
		}
		if(args.length == 6){
			if(args[2].equals("true")){
				System.setProperty("server.ssl.key-store", args[3]);
				System.setProperty("server.ssl.key-store-password", args[5]);
				System.setProperty("server.ssl.key-store-type", "PKCS12");
				System.setProperty("server.ssl.key-alias", args[4]);
				System.setProperty("server.ssl.key-password", args[5]);
				System.out.println("Running with SSL on port "+args[0]);
			}
		}
		SpringApplication.run(DiscrashApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
