package com.seedoilz.maker.generator;

import java.io.*;
import java.util.Map;

public class JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        String mavenCommand = "mvn clean package -DskipTests=true";

        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));
        Map<String, String> environment = processBuilder.environment();
        System.out.println(environment);

        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null){
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        System.out.println("命令执行结束" + exitCode);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("/Users/seedoilz/Codes/code-generator/code-generator-basic");
    }
}
