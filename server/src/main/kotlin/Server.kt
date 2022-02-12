import java.net.ServerSocket
import java.util.*
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



