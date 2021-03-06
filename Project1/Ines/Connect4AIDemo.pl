/*6X7 board*/
start_program :- program([
            [' ',' ',' '], 
            [' ',' ',' '],
            [' ',' ',' ']
        ], 'X').

program(Board, Player) :- draw_board(Board),
                        check_is_full(Board) -> writeln('End Game. Nobody Won!');
                        (
                           (
                           Player = 'X' -> 
                              (
                                 writeln('AI turn!'),
                                 minimax((Player, Board), (Player, NewBoard), Val)
                              );
                              (
                                 take_input(Column),
                                 check_valid_move(Column, Board),
                                 place_piece(Column, Player, Board, NewBoard)
                              )
                           ),
                           (
                              check_win(NewBoard, Player, Column) -> 
                                 (
                                    draw_board(NewBoard),
                                    writef('Player %w Won!', [Player])
                                 )
                                 ;(
                                    Player = 'X' ->
                                       program(NewBoard, 'O')
                                       ;program(NewBoard, 'X')
                                 )
                           )
                        ).
                


take_input(I) :- writeln('Write Column followed by a .'),
                read(I),
                nl.
check_valid_move(Column, Board) :- integer(Column), between(1,3,Column) -> true; program(Board).

check_win(Board, Player, Column) :- check_rows(Board, Player);
                                    check_columns(Board, Player, Column);
                                    check_diagonals(Board, 1, Player);
                                    (reverse(Board, NewBoard),
                                    check_diagonals(NewBoard, 1, Player)).


check_diagonals(Board,Column,Player) :-  check_diagonal(Board, Column, Player, 0);
                                        (Column1 is Column +1, Column1<3,check_diagonals(Board, Column1, Player)).
check_diagonals([H|T], Column, Player) :- check_diagonal(H, Column, Player, 0);
                                        check_diagonals(T, Column, Player).

check_diagonal(_,_,_,3).
check_diagonal([H|T], Column, Player, Counter) :- nth1(Column, H, Y), 
                                                Y \= Player, 
                                                Column1 is Column +1, 
                                                check_diagonal(T, Column1, Player, 0).
check_diagonal([H|T], Column, Player, Counter) :- nth1(Column, H, Player),
                                                    Column1 is Column+1,
                                                    Counter1 is Counter+1,
                                                    check_diagonal(T, Column1, Player, Counter1).
                                            


check_columns(Board, Player, Column) :- check_columns1(Board, Player, Column, 0).
check_columns1(_,_,_,3).
check_columns1([H|T], Player, Column,_) :- nth1(Column, H, Y),
                                            Y \= Player,
                                            check_columns1(T, Player, Column,0).
check_columns1([H|T], Player, Column, Counter) :- Counter1 is Counter+1, 
                                                nth1(Column, H, Player), 
                                                check_columns1(T, Player, Column, Counter1).

check_rows([H|T], Player) :- check_row(H, Player, 0).
check_rows([H|T], Player) :- check_rows(T, Player).
check_row(_,_,3).
check_row([H|T], Player, _) :- H \= Player, 
                                check_row(T, Player, 0).
check_row([Player|T], Player, Counter) :- Counter1 is Counter + 1, 
                                            check_row(T, Player, Counter1).


/*checks whether top row is full*/
check_is_full([H|_]) :- \+member(' ', H).

place_piece(Column, Piece,Board,Board) :- Board = [H|T], nth1(Column, H, Y), Y \= ' ',!.
place_piece(Column, Piece,Board, NBoard) :- place_piece1(Column, Piece, Board, NBoard).

place_piece1(Column, Piece, [H|[]] , [X|[]]) :- replace(H, Column, Piece, X),!.
place_piece1(Column, Piece, [H,S|T], [X,S|T]) :- nth1(Column,S,Y),
                                                Y \= ' ',
                                                replace(H, Column, Piece, X),!.
place_piece1(Column, Piece, [H|T], [H|R]) :- nth1(Column,H,' '),
                                             place_piece1(Column, Piece, T , R).
                                            

/*returns a new array X at position with the new piece in place*/
replace(A, Position, Piece, X) :- replace1(A, Position, 1, Piece, X ).
replace1([_|T], N, N , Piece, [Piece|T]).
replace1([H|T], Position, N , Piece, [H|R] ) :- N1 is N+1, replace1(T, Position, N1, Piece, R).
                                            
draw_board([]).
draw_board(B) :-  writeln(''),
                  writeln('   1     2    3   '),
                  draw_board1(B).

draw_board1([]) :- writeln('+-----+-----+-----+'). 
draw_board1([H|T]) :- draw_line(H), 
                    draw_board1(T).


draw_line(A) :- writeln('+-----+-----+-----+'),
                 writef('|  %w  |  %w  |  %w  |', A),
               writeln('').
               
                
                
/*
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

*/

% minimax( Pos, BestSucc, Val):
%   Pos is a position, Val is its minimax value;
%   best move from Pos leads to position BestSucc
minimax( Pos, BestSucc, Val) :-
   moves( Pos, PosList), !,      % Legal moves in Pos produce PosList
   best( PosList, BestSucc, Val)
   ; % Or
   staticval( Pos, Val).         % Pos has no successors: evaluate statically
   
best( [Pos], Pos, Val) :-
   minimax( Pos, _, Val), !.


best( [(Player,Board1) | PosList], BestPos, BestVal) :-
   (Player = 'X' -> NewPlayer = 'O';NewPlayer = 'X'),
   minimax( (NewPlayer,Board1), _, Val1),
   best( PosList, Pos2, Val2),
   betterof( Pos1, Val1, Pos2, Val2, BestPos, BestVal).


betterof( Pos0, Val0, Pos1, Val1, Pos0, Val0) :- % Pos0 better than Pos1
	min_to_move( Pos0),     % MIN to move in Pos0
	Val0 > Val1, !          % MAX prefers the greater value
	; % Or 
	max_to_move( Pos0),     % MAX to move in Pos0
	Val0 < Val1, !.         % MIN prefers the lesser value


betterof( Pos0, Val0, Pos1, Val1, Pos1, Val1). % Otherwise Pos1 better than Pos0


moves((Player,Board), PostList) :- findall(NPos, possible_play((Player,Board), NPos), PostList), PostList \= [].


possible_play((Player,Board), (NewPlayer,NewBoard)) :- member(X, [1,2,3]),
                                                   place_piece(X, Player, Board, NewBoard),
                                                   Board \= NewBoard.
                                                   

min_to_move((Player, _)) :- Player = 'O'.
max_to_move((Player, _)) :- Player = 'X'.

staticval(Pos, Val) :- check_win(Board, Player, Column) -> Val = 1; Val = -1.



