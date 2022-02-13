import message.ClientConnectToRoomMessage
import message.ClientPositionMessage
import message.GameMessage
import message.GameStateUpdater
import room.GameState
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.schedule
import java.util.concurrent.*
import kotlin.collections.HashMap
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.concurrent.thread

class Room(val roomId: String){
    val messages = LinkedBlockingQueue<Pair<GameMessage,ClientHandler>>()
    private val roomThread = RoomThread(messages, roomId)
    private val thread  = thread{ roomThread.run()}


    fun end(){
        roomThread.stop()
        thread.join()
    }

    class RoomThread(private val messages: LinkedBlockingQueue<Pair<GameMessage,ClientHandler>>, private val roomId: String ) : Runnable {
        @Volatile
        private var gameState = GameState(HashMap())
        @Volatile
        private var shouldContinue = true

        override fun run() {
            println("STARTING ROOM : " + roomId)
            val timer =Timer("NameOfMyTimer", true)

            timer.scheduleAtFixedRate(TimeUnit.MILLISECONDS.toMillis(0),TimeUnit.MILLISECONDS.toMillis(15)) {
                val clients = roomToClientsMap[roomId]!!

                clients.forEach{
                    it.write(gameState)
                }
            }

            while (shouldContinue){
                val data = messages.poll()
                if(data != null){
                    val client = data.second;
                    val message = data.first;
                    if(message is ClientConnectToRoomMessage){
                        gameState = gameState.addPlayer(client.clientId)
                    } else if(message is GameStateUpdater){
                        gameState = message.updateGameState(gameState, client.clientId)
                    }
                }
            }
        }

        fun stop() {
            shouldContinue = false
        }
    }
}
