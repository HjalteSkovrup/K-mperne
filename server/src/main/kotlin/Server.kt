import message.*
import room.GameState
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread





fun main(args: Array<String>) {
    val server = ServerSocket(9999)
    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()
        var clientId = UUID.randomUUID();
        println("Client connected: ${client.inetAddress.hostAddress}")

        thread { ClientHandler(client, clientId).run() }
    }

}



