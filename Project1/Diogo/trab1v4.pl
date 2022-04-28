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

game:-B=[
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


place([],_,[],Estado,Estado,_,'-'):-writeln('jogada invalida').
place([L|R],Pos,[NL|R],X/P/_,NEstado,N,(N,Pos)):-placeAux(L,Pos,P,NL,1),novoEstado(X/P/_,NEstado).
place([L|R],Pos,[L|NR],Estado,NEstado,N,Cord):-K is N + 1,place(R,Pos,NR,Estado,NEstado,K,Cord).

placeAux([X|R],Pos,P,[P|R],Pos):-!,X=' '.
placeAux([X|R],Pos,P,[X|NR],N):-K is N+1,placeAux(R,Pos,P,NR,K).



novoEstado(X/_/_,K/NP/'-'):-K is X - 1, player(K,NP).



player(P,'X'):-0 is (P mod 2).
player(_,'O').




checkWin(NB,_/P/_,_,_/P/'W',(L,_)):-horizontal(NB,P,1,L).
checkWin(NB,_/P/_,_,_/P/'W',(_,C)):-vertical(NB,P,1,C).
checkWin(NB,_/P/_,_,_/P/'W',Cord):-diagonal(NB,P,Cord).
checkWin([L1,L2,L3,L4,L5,L6],_/P/_,_,_/P/'W',(L,C)):-La is 7 - L,diagonal([L6,L5,L4,L3,L2,L1],P,(La,C)).
checkWin(_,_,NEstado,NEstado,_).



horizontal([L|_],P,N,N):-line4(L,P).
horizontal([_|R],P,N,M):-K is N + 1,horizontal(R,P,K,M).



vertical([[X1|_],[X2|_],[X3|_],[X4|_],[X5|_],[X6|_]],P,C,C):-line4([X1,X2,X3,X4,X5,X6],P).
vertical([[_|R1],[_|R2],[_|R3],[_|R4],[_|R5],[_|R6]],P,N,C):-K is N + 1,vertical([R1,R2,R3,R4,R5,R6],P,K,C).



diagonal(B,P,(L,C)):-X is L - C, X >= 0,L1 is 1+X,diagonalAux(B,1,(L1,1),Line),line4(Line,P).
diagonal(B,P,(L,C)):-X is C - L + 1, X > 1,diagonalAux(B,1,(1,X),Line),line4(Line,P).

diagonalAux(B,L,(L,C),D):-getDiagonal(B,C,D).
diagonalAux([_|R],N,(L,C),D):-K is N + 1,diagonalAux(R,K,(L,C),D).

getDiagonal([],_,[]).
getDiagonal(_,C,[]):-C > 7.
getDiagonal([L|R],C,[E|D]):-getCol(L,1,C,E),Ca is C+1,getDiagonal(R,Ca,D).

getCol([E|_],C,C,E).
getCol([_|R],N,C,E):-K is N + 1,getCol(R,K,C,E).



line4([P,P,P,P|_],P).
line4([_,X1,X2,X3,X4|R],P):-line4([X1,X2,X3,X4|R],P).