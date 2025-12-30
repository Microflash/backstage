package com.example.jwt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

public class RSAKeyGenerator {

	static final KeyPairGenerator KEY_PAIR_GENERATOR;
	static final PEMEncoder PEM_ENCODER = PEMEncoder.of();

	static {
		try {
			KEY_PAIR_GENERATOR = KeyPairGenerator.getInstance("RSA");
			KEY_PAIR_GENERATOR.initialize(2048);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	void main() throws IOException {
		var keyPair = KEY_PAIR_GENERATOR.generateKeyPair();
		writeKeyPair(keyPair, Paths.get("src/main/resources"));
	}

	static void writeKeyPair(KeyPair keyPair, Path parent) throws IOException {
		var publicKeyPath = Paths.get(parent.toString(), "public.pem");
		Files.writeString(publicKeyPath, PEM_ENCODER.encodeToString(keyPair.getPublic()));
		var privateKeyPath = Paths.get(parent.toString(), "private.pem");
		Files.writeString(privateKeyPath, PEM_ENCODER.encodeToString(keyPair.getPrivate()));
	}
}
