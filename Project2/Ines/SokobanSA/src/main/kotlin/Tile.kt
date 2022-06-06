class Tile(val type: TileType) {    // compusicao do tabuleiro

    var hasPlayer = false
    var hasBox = false

    fun printChar() {               // printa o char associado ao tile
        if (hasPlayer) {
            print(TileType.PLAYER.char)
            return
        }
        if (hasBox) {
            print(TileType.BOX.char)
            return
        }
        print(type.char)
    }

    fun copy(): Tile {             // para impedir problemas de referencias durante a copia das tiles
        val ret = Tile(type)
        ret.hasBox = hasBox
        ret.hasPlayer = hasPlayer
        return ret
    }
}