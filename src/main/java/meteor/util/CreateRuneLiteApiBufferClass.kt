package meteor.util

import meteor.BufferMethod
import meteor.PacketDecoder
import java.io.File

object RuneLiteApiBufferClass {
    val classHeader = "package net.runelite.api;\n" +
            "\n" +
            "public interface Buffer extends Node\n" +
            "{\n" +
            "byte[] getPayload();\n" +
            "int getOffset();\n"

    val classFooter = "}"


    fun create() {
        var classText = classHeader
        classText += "\n"
        classText += "void writeInt(int i);\n"
        classText += "void writeIntLE(int i);\n"
        classText += "void writeIntIME(int i);\n"
        classText += "void writeIntME(int i);\n"
        classText += "int readInt();\n"
        classText += "int readIntLE();\n"
        classText += "int readIntIME();\n"
        classText += "int readIntME();\n"
        classText += classFooter
        File("./data/Buffer.java").writeText(classText)
    }
}