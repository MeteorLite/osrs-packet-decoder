package meteor

import com.google.common.base.Stopwatch
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import meteor.deobfuscators.Buffer
import meteor.deobfuscators.client.*
import meteor.util.RuneLiteApiClientPacketsClass
import meteor.util.RuneScapeApiClientPacketsClass
import meteor.util.RuneLiteApiBufferClass
import meteor.util.RuneScapeApiBufferClass
import net.runelite.asm.ClassGroup
import net.runelite.deob.Deobfuscator
import net.runelite.deob.util.JarUtil
import java.io.File
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Paths

class PacketDecoder {
    companion object {

        val gamepack = File("./gamepacks/210.jar")
        var classes: ClassGroup? = null
        val clientPackets = HashMap<Int, ObfuscatedClientPacket>()
        val mappedClientPackets = HashMap<Int, ObfuscatedClientPacket>()
        val bufferMethods = ArrayList<BufferMethod>()
        val logger = Logger("main")

        @JvmStatic
        fun main(args: Array<String>) {
            classes = JarUtil.load(gamepack)

            if (classes == null)
                throw RuntimeException("Invalid gamepack path")

            val classes = classes!!

            // Creates Objects with name / opcode / size
            run(classes, InitClientPackets())

            run(classes, IF_BUTTON())

            run(classes, MENU_ACTION())

            run(classes, TIMERS_REFLECTION())

            run(classes, HARDCODED())

            run(classes, Buffer())

            var expectedClientPackets = 0


            for (c in clientPackets) {
                expectedClientPackets++
                if (c.value.structure.isNotEmpty())
                    mappedClientPackets[c.key] = c.value
            }

            val gson: Gson = GsonBuilder()
                .setPrettyPrinting()
                .create()

            var writer: Writer = Files.newBufferedWriter(Paths.get("./data/json/clientPackets.json"))
            gson.toJson(clientPackets, writer)
            writer.close()

            writer = Files.newBufferedWriter(Paths.get("./data/json/buffer.json"))
            gson.toJson(bufferMethods, writer)
            writer.close()

            val missingClientPackets = (expectedClientPackets - mappedClientPackets.size)
            expectedClientPackets = 60
            if (HARDCODED.ran)
                expectedClientPackets += 3

            logger.info("Buffer: ${bufferMethods.size}/36")
            logger.info("ClientPackets: ${mappedClientPackets.size}/$expectedClientPackets ($missingClientPackets not implemented)")
            RuneLiteApiClientPacketsClass.create()
            RuneScapeApiClientPacketsClass.create()
            RuneLiteApiBufferClass.create()
            RuneScapeApiBufferClass.create()
        }

        private fun run(group: ClassGroup, deob: Deobfuscator) {
            val stopwatch = Stopwatch.createStarted()
            deob.run(group)
            stopwatch.stop()
            logger.info("${deob.javaClass.simpleName} took $stopwatch")
        }

        fun getClientPacket(name: String): ObfuscatedClientPacket? {
            for (clientPacket in clientPackets) {
                if (clientPacket.value.name == name)
                    return clientPacket.value
            }
            return null
        }
    }

}