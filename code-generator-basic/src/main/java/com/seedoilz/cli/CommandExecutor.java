package com.seedoilz.cli;

import com.seedoilz.cli.command.ConfigCommand;
import com.seedoilz.cli.command.GenerateCommand;
import com.seedoilz.cli.command.ListCommand;
import picocli.CommandLine;

/**
 * 命令执行器
 */
@CommandLine.Command(name = "code", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable {
    private final CommandLine commandLine;

    // 代码块，节省开销，提升性能
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }

    @Override
	public void run() {
        // 不输入子命令时，给出友好提示
        System.out.println("请输入具体命令，或者输入 --help 查看命令提示");
    }

    /**
     * 执行命令
     *
     * @param args
     * @return
     */
    public Integer doExecute(String[] args) {
        return commandLine.execute(args);
    }
}
