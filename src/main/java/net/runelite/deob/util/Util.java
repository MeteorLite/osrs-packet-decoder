package net.runelite.deob.util;

public class Util {
    public static final int OBFUSCATED_NAME_MAX_LEN = 3;
    public static boolean isObfuscated(String name)
    {
        if (name.length() <= OBFUSCATED_NAME_MAX_LEN)
        {
            return !name.equals("run") && !name.equals("add");
        }
        return name.startsWith("method")
                || name.startsWith("vmethod")
                || name.startsWith("field")
                || name.startsWith("class")
                || name.startsWith("__");
    }
}
