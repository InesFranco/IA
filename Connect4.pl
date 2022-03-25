/*6X7 board*/
program(Board) :- draw_banner(), 
                draw_board(Board),
                take_input(Column),
                place_piece(Column, 'X', Board, NewBoard),
                program(NewBoard).
    


take_input(I) :- writeln("Write Column followed by a '.'"),
                read(I),
                writeln("").

place_piece(C, P, B, NB) :- place_piece1(C, 1, P, B, NB).
place_piece1(C, C, P, [[' '|T]|R] , [[P|T]|R]).
place_piece1(C, N, P, [[H|T]|R], [[H|T1]|R]) :- N1 is N + 1
                                    ,place_piece1(C, N1, P, [T|R], [T1|R]).

draw_board([]).
draw_board(B) :-    writeln("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |"),
                    writeln(" ___________________________"),
                    draw_board1(B).

draw_board1([]).
draw_board1([H|T]) :- draw_line(H), 
                    draw_board1(T).


draw_line(A) :- writef("| %w | %w | %w | %w | %w | %w | %w |", A),
                writeln(""),
                writeln("|___|___|___|___|___|___|___|").
                
                

draw_banner() :- writeln("       ___________"),
                 writeln("      |.---------.|"),
                 writeln("      ||  *   *  ||"),
                 writeln("      ||    o    ||"),
                 writeln("      ||         ||"),
                 writeln("      |'---------'|"),
                 writeln("        )__ ____("),
                 writeln("       [=== -- o ]--."),
                 writeln("     __'---------'__ L "),
                 writeln("    [::::::::::: :::] )"),
                 writeln("                     [|] "),
                 writeln("").
                 

/*Board = 
        program([
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '],
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' ']
        ]).*/

