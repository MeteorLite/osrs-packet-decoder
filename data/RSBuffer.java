package net.runelite.api;

public interface Buffer extends Node
{
byte[] getPayload();
int getOffset();

@Import("writeInt")
@Override
void writeInt(int i);

@Import("method7916")
@Override
void writeIntLE(int i);

@Import("method7971")
@Override
void writeIntIME(int i);

@Import("method7972")
@Override
void writeIntME(int i);

@Import("readInt")
@Override
int readInt();

@Import("method7973")
@Override
int readInt();

@Import("method7974")
@Override
int readInt();

@Import("method8135")
@Override
int readInt();

}