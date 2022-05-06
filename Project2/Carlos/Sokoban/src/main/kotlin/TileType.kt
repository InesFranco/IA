enum class TileType(val char: Char) {
    EMPTY_SPACE(' '),
    WALL('X'),
    GOAL('o'),
    PLAYER('@'),
    BOX('B')
}