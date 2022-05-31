package com.authxero.discrash.helpers;

import com.authxero.discrash.executor.CommandExecutorService;
import com.authxero.discrash.executor.FireAndForgetExecutor;

public class Helper {
    public static CommandExecutorService ces = new CommandExecutorService();
    public static FireAndForgetExecutor ffe = new FireAndForgetExecutor();
}
