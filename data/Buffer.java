package net.runelite.api;

public interface Buffer extends Node
{
byte[] getPayload();
int getOffset();

void writeInt(int i);
void writeIntLE(int i);
void writeIntIME(int i);
void writeIntME(int i);
int readInt();
int readIntLE();
int readIntIME();
int readIntME();
}