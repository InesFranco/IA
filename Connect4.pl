/*6X7 board*/
program() :- Board = 
        [
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' '],
            [' ',' ',' ',' ',' ',' ',' '], 
            [' ',' ',' ',' ',' ',' ',' ']
        ],
    draw_banner(), 
    draw_board(Board),
    read(Column),
    write(Column),
    /*place_piece(Column, Board),*/
    program().
    
place_piece(C, B) :- print("hello"). 

draw_board([]).
draw_board(B) :-    writeln("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |"),
                    writeln(" ___________________________"),
                    draw_board1(B).

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
                 



