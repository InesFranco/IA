/*
Predicados para apresentar o tabuleiro na consola de prolog
estes predicados não efetuam mudanças no tabuleiro


								divisão das linhas da tabela         mostrar a linha	*/
draw([R1,R2,R3,R4,R5,R6]):-writeln('\n  1   2   3   4   5   6   7'),writeln('+---+---+---+---+---+---+---+'),
								showLine(R6),writeln('+---+---+---+---+---+---+---+'),
								showLine(R5),writeln('+---+---+---+---+---+---+---+'),
								showLine(R4),writeln('+---+---+---+---+---+---+---+'),
								showLine(R3),writeln('+---+---+---+---+---+---+---+'),
								showLine(R2),writeln('+---+---+---+---+---+---+---+'),
								showLine(R1),writeln('+---+---+---+---+---+---+---+').

/*             						apresentação dos conteudos da linha			*/
showLine([A,B,C,D,E,F,G]):-writef('| %w | %w | %w | %w | %w | %w | %w |\n',[A,B,C,D,E,F,G]).

/*
testes de representação

draw([[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' ']
]).

*/


/* 
game logic

game(B,P):-draw(board(B)),play(B,P),nofinish(B,P).

game().
*/

game():-B=[
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ',' ',' ',' ']
],draw(B),X=42,player(X,P),game(B,X/P/'-').

game(_,_/P/'W'):-writef('O player %w ganhou',[P]).
game(_,0/_/_):-writeln('Empate').
game(B,Estado):-play(B,NB,Estado,EstAux,Cord),checkWin(NB,Estado,EstAux,NEstado,Cord),draw(NB),game(NB,NEstado).




play(B,NB,Estado,NEstado,Cord):-read(Pos),place(B,Pos,NB,Estado,NEstado,1,Cord).


place([],_,[],Estado,Estado,_,_):-writeln('jogada invalida').
place([L|R],Pos,[NL|R],X/P/_,NEstado,N,(N,Pos)):-placeAux(L,Pos,P,NL,1),novoEstado(X/P/_,NEstado),writef('line = %w and col = %w\n',[N,Pos]).
place([L|R],Pos,[L|NR],Estado,NEstado,N,Cord):-K is N + 1,place(R,Pos,NR,Estado,NEstado,K,Cord).

placeAux([X|R],Pos,P,[P|R],Pos):-!,X=' '.
placeAux([X|R],Pos,P,[X|NR],N):-K is N+1,placeAux(R,Pos,P,NR,K).

novoEstado(X/_/_,K/NP/'-'):-K is X - 1, player(K,NP).

player(P,'X'):-0 is (P mod 2).
player(_,'O').




checkWin(NB,_/P/_,_,_/P/'W',(L,_)):-horizontal(NB,P,1,L).
checkWin(NB,_/P/_,_,_/P/'W',(_,C)):-vertical(NB,P,1,C).
checkWin(NB,_/P/_,_,_/P/'W',_):-diagonal1(NB,P).
checkWin(NB,_/P/_,_,_/P/'W',_):-diagonal2(NB,P).
checkWin(_,_,NEstado,NEstado,_).

horizontal([L|_],P,N,N):-line4(L,P).
horizontal([_|R],P,N,M):-K is N + 1,horizontal(R,P,K,M).
vertical([[X1|_],[X2|_],[X3|_],[X4|_],[X5|_],[X6|_]],P,N,N):-line4([X1,X2,X3,X4,X5,X6],P).
vertical([[_|R1],[_|R2],[_|R3],[_|R4],[_|R5],[_|R6]],P,N,M):-K is N + 1,vertical([R1,R2,R3,R4,R5,R6],P,K,M).
/*diagonal1(B,P):-dList(B,L),line4(L,P). */


diagonal1([[X1|_],[_,X2|_],[_,_,X3|_],[_,_,_,X4|_],[_,_,_,_,X5|_],[_,_,_,_,_,X6|_]],P):-line4([X1,X2,X3,X4,X5,X6],P).
diagonal1([[_|R1],[_|R2],[_|R3],[_|R4],[_|R5],[_|R6]],P):-diagonal1([R1,R2,R3,R4,R5,R6],P).
diagonal2([[_,_,_,_,_,X6|_],[_,_,_,_,X5|_],[_,_,_,X4|_],[_,_,X3|_],[_,X2|_],[X1|_]],P):-line4([X1,X2,X3,X4,X5,X6],P).
diagonal2([[_|R1],[_|R2],[_|R3],[_|R4],[_|R5],[_|R6]],P):-diagonal2([R1,R2,R3,R4,R5,R6],P).


line4([P,P,P,P|_],P).
line4([_,X1,X2,X3,X4|R],P):-line4([X1,X2,X3,X4|R],P).