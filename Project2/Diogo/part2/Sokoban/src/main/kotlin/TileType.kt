enum class TileType(val char: Char) {   // enumerado com os lavores possiveis da tile
    EMPTY_SPACE(' '),
    WALL('X'),
    GOAL('o'),
    PLAYER('@'),
    BOX('B')
}