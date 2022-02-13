import message.*
import room.GameState
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.math.log

private var roomQueues = ConcurrentHashMap<String,Room>()
private var clientsToRoomMap = ConcurrentHashMap<UUID, Room>()


var roomToClientsMap = ConcurrentHashMap<String, ConcurrentLinkedQueue<ClientHandler>>()

private var roomIds = ConcurrentSkipListSet<String>()
private var clients = ConcurrentSkipListSet<UUID>()


class ClientHandler(private val client: Socket, val clientId: UUID) {
    private val reader: ObjectInputStream = ObjectInputStream(client.getInputStream())
    private val writer: ObjectOutputStream = ObjectOutputStream(client.getOutputStream())
    private var running: Boolean = false



    init {
        clients.add(clientId)
    }

    fun run() {
        running = true

        while (running) {
            try {
                val message = reader.readObject() as GameMessage
                if (message is ClientDisconnectMessage){
                    shutdown()
                    continue
                }
                else if(message is ClientCreateRoomMessage){
                    if(roomIds.contains(message.roomId))
                        writer.writeObject(ErrorMessage(clientId, "Failed to create room: a room with that name already exists"))
                    else {
                        println("CLIENT : " + clientId + " CRETATED ROOM : " + message.roomId)
                        roomIds.add(message.roomId)
                        roomToClientsMap[message.roomId] = ConcurrentLinkedQueue()
                        roomToClientsMap[message.roomId]!!.add(this)

                        val room = Room(message.roomId)
                        roomQueues[message.roomId] = room
                        clientsToRoomMap[clientId] = room

                        room.messages.add(Pair(ClientConnectToRoomMessage(message.roomId),this))
                    }
                }
                else if(message is ClientConnectToRoomMessage){
                    println("CLIENT : " + clientId + " CONNECTED TO ROOM : " + message.roomId)
                    roomToClientsMap[message.roomId]!!.add(this)
                    val room = roomQueues[message.roomId]!!
                    clientsToRoomMap[clientId] = room
                    room.messages.add(Pair(message,this))
                }
                else {
                    println("RECIEVED MOVE MESSAGE : " + clientId )
                    val room = roomQueues[clientsToRoomMap[clientId]!!.roomId]!!
                    val messages = room.messages
                    messages.add(Pair(message, this))
                }

            } catch (ex: Exception) {
                shutdown()
            }

        }
    }

    fun write(message: GameState) {
        writer.writeObject(message)
        writer.reset()
    }

    private fun shutdown() {
        running = false

        val room = clientsToRoomMap.remove(clientId)
        if(room != null){
            roomToClientsMap[room.roomId]?.remove(this)
            if(roomToClientsMap[room.roomId]?.isEmpty() == true) {
                roomToClientsMap.remove(room.roomId)
                room.end()
            }
            roomIds.remove(room.roomId)
        }
        clients.remove(clientId)

        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other.javaClass !== this.javaClass) return false

        // type casting of the argument.

        // type casting of the argument.
        val client = other as ClientHandler

        return this.clientId == client.clientId

    }

    override fun hashCode(): Int {
        return Objects.hashCode(clientId)
    }

}