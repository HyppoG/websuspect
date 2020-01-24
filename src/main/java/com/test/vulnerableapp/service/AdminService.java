package com.test.vulnerableapp.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

	private final String command = "lshw";

	public String getSystemInfo() {
		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			StringBuilder results = new StringBuilder();
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				results.append(line).append("\n");
			}
			return results.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Operation Failed";
		}
	}

}