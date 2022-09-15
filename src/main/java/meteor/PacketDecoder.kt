package meteor

import com.google.common.base.Stopwatch
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import meteor.deobfuscators.IF_BUTTON
import meteor.deobfuscators.MENU_ACTION
import meteor.deobfuscators.RuneLiteApiClientPacketsClass
import meteor.deobfuscators.RuneScapeApiClientPacketsClass
import meteor.deobfuscators.InitClientPackets
import net.runelite.asm.ClassGroup
import net.runelite.deob.Deobfuscator
import net.runelite.deob.ObfuscatedClientPacket
import net.runelite.deob.util.JarUtil
import java.io.File
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Paths

class PacketDecoder {
    companion object {

        val gamepack = File("./gamepacks/208.jar")
        var classes: ClassGroup? = null
        val clientPackets = HashMap<Int, ObfuscatedClientPacket>()
        val mappedClientPackets = HashMap<Int, ObfuscatedClientPacket>()
        val logger = Logger("main")

        @JvmStatic
        fun main(args: Array<String>) {

            var revisions = 0

            classes = JarUtil.load(gamepack)

            println("Loaded $revisions revisions")

            if (classes == null)
                throw RuntimeException("Invalid gamepack path")

            // Creates Objects with name / opcode / size
            run(classes!!, InitClientPackets())

            run(classes!!, IF_BUTTON())

            run(classes!!, MENU_ACTION())

            var totalClientPackets = 0


            for (c in clientPackets) {
                totalClientPackets++
                if (c.value.structure.isNotEmpty())
                    mappedClientPackets[c.key] = c.value
            }

            val gson: Gson = GsonBuilder()
                .setPrettyPrinting()
                .create()

            // create a writer
            val writer: Writer = Files.newBufferedWriter(Paths.get("./data/clientPackets.json"))

            // convert user object to JSON file
            gson.toJson(clientPackets, writer)

            // close writer
            writer.close()

            val unmappedClientPackets = (totalClientPackets - mappedClientPackets.size)

            logger.info("ClientPackets: ${mappedClientPackets.size}/$totalClientPackets ($unmappedClientPackets missing)")

            RuneLiteApiClientPacketsClass.create()
            RuneScapeApiClientPacketsClass.create()
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