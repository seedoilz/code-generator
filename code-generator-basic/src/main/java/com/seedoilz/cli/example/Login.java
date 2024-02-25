package com.seedoilz.cli.example;

import com.seedoilz.cli.utils.OptionUtil;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

class Login implements Callable<Integer> {
    @Option(names = {"-u", "--user"}, description = "User name")
    String user;

    @Option(names = {"-p", "--password"}, arity = "0..1", description = "Passphrase", interactive = true)
    String password;

    @Option(names = {"-cp", "--checkPassword"}, description = "Check Password", interactive = true)
    String checkPassword;

    public Integer call() throws Exception {
        // 打印出密码
        System.out.println("密码是：" + password);
        return 0;
    }

    public static void main(String[] args) {
        args = new String[]{"-u", "user123", "-p"};
        new CommandLine(new Login()).execute(OptionUtil.processInteractiveOptions(Login.class, args));
    }
}