package message;

import room.GameState
import java.io.Serializable
import java.util.*

abstract class GameMessage() : Serializable

interface GameStateUpdater {
    fun updateGameState(gameState: GameState, clientId: UUID) : GameState
}

class Position(val x: Int, val y: Int) : Serializable {
    override fun toString(): String {
        return "Position(x=$x, y=$y)"
    }
}

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