:-consult('astar.pl').


% 19 columns 11 lines
board([
    [' ', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', 'X', 'X', 'X', ' ', ' ', ' ', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    ['X', 'X', 'X', ' ', 'X', ' ', 'X', 'X', ' ', 'X', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', 'X'],
    ['X', ' ', ' ', ' ', 'X', ' ', 'X', 'X', ' ', 'X', 'X', 'X', 'X', 'X', ' ', ' ', ' ', ' ', 'X'],
    ['X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O', 'X'],
    ['X', 'X', 'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', 'X', ' ', 'X', 'X', ' ', ' ', ' ', ' ', 'X'],
    [' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'],
    [' ', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ']
    ]).
    
% Player, Box, Goal


replace_nth1(List, Index, OldElem, NewElem, NewList) :-
   % predicate works forward: Index,List -> OldElem, Transfer
   nth1(Index,List,OldElem,Transfer),
   % predicate works backwards: Index,NewElem,Transfer -> NewList
   nth1(Index,NewList,NewElem,Transfer).

start_game() :- board(Board), 
                game_loop((Board, [5/8, 6/8, 18/8])).                       %initial positions of player, box and goal

start_game_AI() :-  board(Board),
                    InitialState = (Board, [12/9, 6/8, 18/8]),
                    draw_board(InitialState),
                    bestfirst(InitialState, Solution),
                    reverse(Solution, RSolution),
                    draw_path(RSolution).


dir('w', 0,-1).
dir('s', 0, 1).
dir('d', 1, 0).
dir('a',-1, 0).


draw_path([H|T]) :- draw_board(H), sleep(1), draw_path(T).

game_loop(State) :- goal(State)->write('is goal');
                    (
                        draw_board(State),
                        read(Key),
                        dir(Key, X, Y),
                        move(State, X, Y, NewState),
                        game_loop(NewState)
                    ).

draw_board(State) :- State = (Board,[PlayerX/PlayerY, BoxX/BoxY,_]),
                    draw_board1(Board, PlayerX, PlayerY, BoxX, BoxY, 1).

draw_board1([],_,_,_,_,_).
draw_board1(Board, PlayerX, PlayerY, BoxX, BoxY, N) :- Board = [H|T],
                                                        (
                                                            (N = BoxY, N = PlayerY) ->(
                                                                replace_nth1(H, BoxX,_, 'B', NewLineBox),
                                                                replace_nth1(NewLineBox, PlayerX,_,'\u2603', NewLine),
                                                                draw_line(NewLine)
                                                            );
                                                            (N = PlayerY)->(
                                                                replace_nth1(H, PlayerX, ' ', '\u2603', NewLine),
                                                                draw_line(NewLine)
                                                            );
                                                            (N = BoxY)->(
                                                                replace_nth1(H, BoxX,_, '\u2683', NewLine),
                                                                draw_line(NewLine)
                                                            );
                                                            draw_line(H)
                                                        ),
                                                        N1 is N+1,
                                                        draw_board1(T, PlayerX, PlayerY, BoxX, BoxY, N1).


draw_line(A) :- writef('| %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w |\n', A).

%checks if there is wall at position (X,Y) of board
check_wall(Board, X, Y) :- nth1(Y,Board,Row), nth1(X,Row,'X').

calculate_new_pos((Board, [PlayerX/PlayerY, BoxX/BoxY, _]), X, Y, BoxX, BoxY, Nbx, Nby) :- 
                                                                                    BoxXAux is PlayerX + X,
                                                                                    BoxYAux is PlayerY + Y,
                                                                                    BoxX = BoxXAux,
                                                                                    BoxY = BoxYAux, 
                                                                                    Nby is BoxY + Y, 
                                                                                    Nbx is BoxX + X,
                                                                                    \+check_wall(Board, Nbx, Nby),!.

calculate_new_pos((Board, [PlayerX/PlayerY, BoxX/BoxY, _]), X, Y, Nx, Ny, BoxX, BoxY) :- Ny is PlayerY + Y, 
                                                                                        Nx is PlayerX+X, 
                                                                                        \+check_wall(Board, Nx, Ny).
                                                                                    


move((Board, [PlayerX/PlayerY,BoxX/BoxY,GoalX/GoalY]), X, Y, (Board, [Npx/Npy,Nbx/Nby,GoalX/GoalY])) :- calculate_new_pos((Board, [PlayerX/PlayerY,BoxX/BoxY,_]),X, Y, Npx, Npy, Nbx, Nby).
                                                                                                        


s(Node, Successor, 1) :-  dir(_,X, Y), 
                        move(Node,X,Y, Successor).
/*
s(([
    [' ', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', 'X', 'X', 'X', ' ', ' ', ' ', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    [' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '],
    ['X', 'X', 'X', ' ', 'X', ' ', 'X', 'X', ' ', 'X', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', 'X'],
    ['X', ' ', ' ', ' ', 'X', ' ', 'X', 'X', ' ', 'X', 'X', 'X', 'X', 'X', ' ', ' ', ' ', ' ', 'X'],
    ['X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O', 'X'],
    ['X', 'X', 'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', 'X', ' ', 'X', 'X', ' ', ' ', ' ', ' ', 'X'],
    [' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'],
    [' ', ' ', ' ', ' ', 'X', 'X', 'X', 'X', 'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ']
    ], [12/9, 6/7, 18/8]), Successor, 1).                    
*/


h(State,H):- State = (_,[PlayerX/PlayerY, BoxX/BoxY, GoalX/GoalY]), 
            H is ((GoalY-BoxY) + (GoalX-BoxX) + (PlayerY-BoxY)+(PlayerX-BoxX)).
            



goal((_,[_,X1/Y1,X1/Y1])):-!.


