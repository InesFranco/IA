
/*6X7 board*/


program() :- Board = 
        [
            [
                (1, 1, yespiece), 
                (1, 2, nopiece), 
                (1, 3, nopiece), 
                (1, 4, nopiece),
                (1, 5, nopiece),
                (1, 6, nopiece),
                (1, 7, nopiece)
            ], 
            [
                (2, 1, nopiece), 
                (2, 2, nopiece), 
                (2, 3, nopiece), 
                (2, 4, nopiece),
                (2, 5, nopiece),
                (2, 6, nopiece),
                (2, 7, nopiece)
            ], 
            [
                (3, 1, nopiece), 
                (3, 2, nopiece), 
                (3, 3, nopiece), 
                (3, 4, nopiece),
                (3, 5, nopiece),
                (3, 6, nopiece),
                (3, 7, nopiece)
            ], 
            [
                (4, 1, nopiece), 
                (4, 2, nopiece), 
                (4, 3, nopiece), 
                (4, 4, nopiece),
                (4, 5, nopiece),
                (4, 6, nopiece),
                (4, 7, nopiece)
            ], 
            [
                (5, 1, nopiece), 
                (5, 2, nopiece), 
                (5, 3, nopiece), 
                (5, 4, nopiece),
                (5, 5, nopiece),
                (5, 6, nopiece),
                (5, 7, nopiece)
            ], 
            [
                (6, 1, nopiece), 
                (6, 2, nopiece), 
                (6, 3, nopiece), 
                (6, 4, nopiece),
                (6, 5, nopiece),
                (6, 6, nopiece),
                (6, 7, nopiece)
            ]
        ],
    draw_banner(), 
    draw_board(Board).
    

draw_board([]).
draw_board([H|T]) :- write("|"), draw_board_inner(H), draw_board(T).
draw_board_inner([]) :- write("|\n"), write("|\n").
draw_board_inner([H|T]) :- H = (_,_, yespiece) -> write("|-x-|"), draw_board_inner(T);write("|---|"), draw_board_inner(T).


draw_banner() :- write("       ___________\n"),
                 write("      |.---------.|\n"),
                 write("      ||  *   *  ||\n"),
                 write("      ||    o    ||\n"),
                 write("      ||         ||\n"),
                 write("      |'---------'|\n"),
                 write("        )__ ____(\n"),
                 write("       [=== -- o ]--.\n"),
                 write("     __'---------'__ L \n"),
                 write("    [::::::::::: :::] )\n"),
                 write("     ''''''''''''''' [|] \n"),
                 write("\n").
                 



