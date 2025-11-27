package com.example.Pasteleria_Mil_Sabores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class PasteleriaMilSaboresApplication {

	public static void main(String[] args) {
		// Configurar TNS_ADMIN para Oracle Wallet antes de iniciar Spring
		configureOracleWallet();
		SpringApplication.run(PasteleriaMilSaboresApplication.class, args);
	}

	private static void configureOracleWallet() {
		// Buscar la wallet en diferentes ubicaciones posibles
		String[] possiblePaths = {
			"src/main/resources/Wallet_basesitadepasteles1000",
			"target/classes/Wallet_basesitadepasteles1000",
			"Wallet_basesitadepasteles1000"
		};

		String userDir = System.getProperty("user.dir");
		
		for (String relativePath : possiblePaths) {
			Path walletPath = Paths.get(userDir, relativePath);
			if (walletPath.toFile().exists() && walletPath.resolve("tnsnames.ora").toFile().exists()) {
				String walletLocation = walletPath.toAbsolutePath().toString().replace("\\", "/");
				System.setProperty("oracle.net.tns_admin", walletLocation);
				System.setProperty("TNS_ADMIN", walletLocation);
				System.out.println("Oracle Wallet configurada en: " + walletLocation);
				return;
			}
		}
		
		System.err.println("ADVERTENCIA: No se encontro la carpeta Wallet_basesitadepasteles1000");
	}
}
