package com.quartzy.qapi.command;

@FunctionalInterface
public interface Executor{
    int run(CommandExecutorInfo info);
}
