package message;

import room.GameState
import room.RoomId
import java.io.Serializable
import java.util.*

abstract class GameMessage() : Serializable

interface GameStateUpdater {
    fun updateGameState(gameState: GameState, clientId: UUID) : GameState
}

class Position(x: Int, y: Int) : Serializable

class ClientCreateRoomMessage(val roomId: String) : GameMessage()

class ClientConnectToRoomMessage(val roomId: String) : GameMessage()

class ClientDisconnectMessage() : GameMessage()

class ErrorMessage(clientId: UUID, message: String) : GameMessage()

class ClientPositionMessage(val position: Position ) : GameMessage(), GameStateUpdater {
    override fun updateGameState(gameState: GameState, clientId: UUID): GameState {
        return gameState.updatePosition(position, clientId)
    }

}

class ClientJumpMessage() : GameMessage(), GameStateUpdater {
    override fun updateGameState(gameState: GameState, clientId: UUID): GameState {
        TODO("Not yet implemented")
    }
}