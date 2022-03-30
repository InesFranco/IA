/*6X7 board*/


start_program() :- program([
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '],
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' ']
        ], 'X').

program(Board, Player) :- draw_banner(), 
                draw_board(Board),
                take_input(Column),
                check_valid_move(Column, Board),
                place_piece(Column, Player, Board, NewBoard),
                (check_win(NewBoard, Player) -> 
                    draw_end_banner(),
                    draw_board(NewBoard),
                    writef("Player %w Won!", [Player])
                    ;(Player = 'X' -> 
                        program(NewBoard, 'O')
                        ;program(NewBoard, 'X'))
                ).
                


take_input(I) :- writeln("Write Column followed by an '.'"),
                read(I),
                writeln("").

check_valid_move(Column, Board) :- integer(Column), between(1,7,Column) -> true; program(Board).

check_win([], _) :- false.
check_win([H|_], Player) :- check_rows(H, Player, 0).
check_win([H|T], Player) :- \+check_rows(H, Player,0),check_win(T, Player).


check_rows(_,_,4).
check_rows([H|T], Player, _) :- H \= Player, 
                                check_rows(T, Player, 0).
check_rows([Player|T], Player, Counter) :- Counter1 is Counter + 1, 
                                        check_rows(T, Player, Counter1).


place_piece(Column, Piece, [H|[]] , [X|[]]) :- replace(H, Column, Piece, X). 
place_piece(Column, Piece, [H,S|T], [X,S|T]) :- nth1(Column,S,Y),
                                                Y \= ' ',
                                                replace(H, Column, Piece, X).
place_piece(Column, Piece, [H|T], [H|R]) :- nth1(Column,H,' '),
                                            place_piece(Column, Piece, T , R).
                                            

/*returns a new array X at position with the new piece in place*/
replace(A, Position, Piece, X) :- replace1(A, Position, 1, Piece, X ).
replace1([_|T], N, N , Piece, [Piece|T]).
replace1([H|T], Position, N , Piece, [H|R] ) :- N1 is N+1, replace1(T, Position, N1, Piece, R).
                                            
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
                 writeln("      ||    v    ||"),
                 writeln("      ||         ||"),
                 writeln("      |'---------'|"),
                 writeln("        )__ ____("),
                 writeln("       [=== -- o ]--."),
                 writeln("     __'---------'__ L "),
                 writeln("    [::::::::::: :::] )"),
                 writeln("                     [|] "),
                 writeln("").

draw_end_banner() :- writeln("       ___________"),
                    writeln("      |.---------.|"),
                    writeln("      || > ___ < ||"),
                    writeln("      || (_____) ||"),
                    writeln("      ||         ||"),
                    writeln("      |'---------'|"),
                    writeln("        )__ ____("),
                    writeln("       [=== -- o ]--."),
                    writeln("     __'---------'__ L "),
                    writeln("    [::::::::::: :::] )"),
                    writeln("                     [|] "),
                    writeln("").