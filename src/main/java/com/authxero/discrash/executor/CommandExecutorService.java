package com.authxero.discrash.executor;

import java.io.IOException;

public class CommandExecutorService implements CustomExecutorService {

    private final String commandType;
    public static boolean isDebug = false;
    public CommandExecutorService() {
        this.commandType = getSystemCommandType();
    }

    public String getSystemCommandType() {
        String os = System.getProperty("os.name");
        return os.contains("Windows") ? "cmd" : "bash";
    }

    public String getCommandType() {
        return commandType;
    }

    @Override
    public void exec(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String identifier = commandType.equals("cmd") ? "/" : "-";
            String[] splitCommand = command.split(" ");
            String[] argArr = new String[splitCommand.length + 2];
            argArr[0] = this.getCommandType();
            argArr[1] = identifier + "c";
            System.arraycopy(splitCommand, 0, argArr, 2, splitCommand.length);
            if(this.getCommandType().equals("bash")){
                processBuilder.command(splitCommand);
            }
            else{
                processBuilder.command(argArr);
            }
            processBuilder.command(argArr);
            ProcessBuilder.Redirect type = isDebug ? ProcessBuilder.Redirect.INHERIT : ProcessBuilder.Redirect.DISCARD;
            processBuilder.redirectError(type).redirectOutput(type).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Process fireAndReturnProcess(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String identifier = commandType.equals("cmd") ? "/" : "-";
            String[] splitCommand = command.split(" ");
            String[] argArr = new String[splitCommand.length + 2];
            argArr[0] = this.getCommandType();
            argArr[1] = identifier + "c";
            System.arraycopy(splitCommand, 0, argArr, 2, splitCommand.length);
            if(this.getCommandType().equals("bash")){
                processBuilder.command(splitCommand);
            }
            else{
                processBuilder.command(argArr);
            }
            ProcessBuilder.Redirect type = isDebug ? ProcessBuilder.Redirect.INHERIT : ProcessBuilder.Redirect.DISCARD;
            return processBuilder.redirectError(type).redirectOutput(type).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
