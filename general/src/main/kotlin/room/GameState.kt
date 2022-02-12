package room;
import message.Position
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class Player(val clientId : UUID, val color: String, val position: Position)

class GameState(val state : HashMap<UUID, Player>) : Serializable {

    fun addPlayer(clientId : UUID) : GameState{
        state[clientId] = Player(clientId, "red", Position(20,20))
        return GameState(state)
    }

    fun updatePosition(position: Position, clientId : UUID) : GameState{
        val oldState = state[clientId]!!
        state[clientId] = Player(clientId, oldState.color, position)
        return GameState(state)
    }
}
