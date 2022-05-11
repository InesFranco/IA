class Tile(val type: TileType) {

    var hasPlayer = false
    var hasBox = false

    fun printChar() {
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
}