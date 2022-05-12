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
                game_loop((Board, [12/9, 6/7, 18/8])).                       %initial positions of player, box and goal

start_game_AI() :-  board(Board),
                    InitialState = (Board, [6/8, 6/7, 18/8]),
                    draw_board(InitialState),
                    bestfirst(InitialState, Solution),
                    draw_path(Solution).


dir('w', 0,-1).
dir('s', 0, 1).
dir('d', 1, 0).
dir('a',-1, 0).


draw_path([H|T]) :- draw_board(H), draw_path(T).

game_loop(State) :- draw_board(State),
                    read(Key),
                    dir(Key, X, Y),
                    move(State, X, Y, NewState),
                    game_loop(NewState).

draw_board(State) :- State = (Board,[PlayerX/PlayerY, BoxX/BoxY,_]),
                    draw_board1(Board, PlayerX, PlayerY, BoxX, BoxY, 1).

draw_board1([],_,_,_,_,_).
draw_board1(Board, PlayerX, PlayerY, BoxX, BoxY, N) :- Board = [H|T],
                                                        (
                                                            (N = PlayerY)->(
                                                                replace_nth1(H, PlayerX, ' ', 'P', NewLine),
                                                                draw_line(NewLine)
                                                            );
                                                            (N = BoxY)->(
                                                                replace_nth1(H, BoxX, ' ', 'B', NewLine),
                                                                draw_line(NewLine)
                                                            );
                                                            draw_line(H)
                                                        ),
                                                        N1 is N+1,
                                                        draw_board1(T, PlayerX, PlayerY, BoxX, BoxY, N1).


draw_line(A) :- writef('| %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w | %w |\n', A).

%checks if there is wall at position (X,Y) of board
check_wall(Board, X, Y) :- nth1(Y,Board,Row), nth1(X,Row,'X').

calculate_new_pos((Board, [PlayerX/PlayerY, BoxX/BoxY, _]), X, Y, Nx, Ny, Nbx, Nby) :- Ny is PlayerY + Y, Nx is PlayerX+X, 
                                                                                        BoxY = Ny, BoxX = Nx,
                                                                                        Nby is BoxY + Y, 
                                                                                        Nbx is BoxX + X,
                                                                                        Nby < 12, Nby > 0, Nbx > 0, Nbx < 19,
                                                                                        \+check_wall(Board, Nbx, Nby),!.

calculate_new_pos((Board, [PlayerX/PlayerY, BoxX/BoxY, _]), X, Y, Nx, Ny, BoxX, BoxY) :- Ny is PlayerY + Y, Nx is PlayerX+X, 
                                                                                        Ny < 12, Ny > 0, Nx > 0, Nx < 19,
                                                                                        \+check_wall(Board, Nx, Ny).
                                                                                    


move(Node, X, Y, (Board, [Npx/Npy,Nbx/Nby,GoalX/GoalY])) :- Node = (Board, [_,_,GoalX/GoalY]), calculate_new_pos(Node,X, Y, Npx, Npy, Nbx, Nby),!. 
move(Node,_,_,Node).

s(Node, Successor, 1) :-  dir(_,X, Y), 
                        move(Node,X,Y, Successor).
                        

h(State,H):- State = (_,[PlayerX/PlayerY,BoxX/BoxY, GoalX/GoalY]), 
            H is (BoxY-GoalY) + (BoxX-GoalX) + (BoxY-PlayerY)+(BoxX-PlayerX).



goal((_,[_,X1/Y1,X1/Y1])).