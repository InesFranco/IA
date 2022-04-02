/*
Predicados para apresentar o tabuleiro na consola de prolog
estes predicados não efetuam mudanças no tabuleiro


								divisão das linhas da tabela         mostrar a linha	*/
draw(B):-header(B,Division),invert(B,I),showLines(I,Division).
					
header([L|_],Division):-length(L,X),columnum(1,X),division(0,X,'+',Division),writeln(Division).
columnum(X,X):-writef('  %w \n',[X]).
columnum(N,X):-writef('  %w ',[N]),K is N + 1,columnum(K,X).

division(X,X,Division,Division).
division(N,X,A,Division):-atom_concat(A,'---+',A1),K is N + 1,division(K,X,A1,Division).

showLines([],_).
showLines([L|R],Division):-showLine(L),writeln(Division),showLines(R,Division).

showLine([]):-writeln('|').
showLine([E|R]):-writef('| %w ',[E]),showLine(R).

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

game:-gamedim(6,7).

gamedim(L,C):-build_board(L,C,B),draw(B),tiles(B,X),player(X,P),game(B,X/P/'-').

build_board(L,C,B):-make_line(1,C,[' '],Line),make_board(1,L,[Line],B).

make_line(X,X,L,L).
make_line(N,X,Aux,L):-K is N + 1,make_line(K,X,[' '|Aux],L).

make_board(X,X,B,B).
make_board(N,X,[Line|R],B):-K is N + 1,make_board(K,X,[Line,Line|R],B).

game(_,_/P/'W'):-writef('O player %w ganhou',[P]).
game(_,0/_/_):-writeln('Empate').
game(B,Estado):-play(B,NB,Estado,EstAux,Cord),checkWin(NB,Estado,EstAux,NEstado,Cord),draw(NB),game(NB,NEstado).


tiles([L|R],X):-length(L,X1),length([L|R],X2),X is X1 * X2.


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
checkWin(NB,_/P/_,_,_/P/'W',(L,C)):-length(NB,X),La is X + 1 - L,invert(NB,I),diagonal(I,P,(La,C)).
checkWin(_,_,NEstado,NEstado,_).



horizontal([L|_],P,N,N):-line4(L,P).
horizontal([_|R],P,N,M):-K is N + 1,horizontal(R,P,K,M).



vertical(B,P,C,C):-vertAux1(B,L),line4(L,P).
vertical(B,P,N,C):-vertAux2(B,NB),K is N + 1,vertical(NB,P,K,C).

vertAux1([],[]).
vertAux1([[X|_]|R],[X|L]):-vertAux1(R,L).

vertAux2([],[]).
vertAux2([[_|L]|R1],[L|R2]):-vertAux2(R1,R2).


diagonal(B,P,(L,C)):-X is L - C, X >= 0,L1 is 1+X,diagonalAux(B,1,(L1,1),Line),line4(Line,P).
diagonal(B,P,(L,C)):-X is C - L + 1, X > 1,diagonalAux(B,1,(1,X),Line),line4(Line,P).

diagonalAux(B,L,(L,C),D):-getDiagonal(B,C,D).
diagonalAux([_|R],N,(L,C),D):-K is N + 1,diagonalAux(R,K,(L,C),D).

getDiagonal([],_,[]).
getDiagonal([L|R],C,[E|D]):-getCol(L,1,C,E),Ca is C+1,getDiagonal(R,Ca,D).

getCol([E|_],C,C,E).
getCol([_|R],N,C,E):-K is N + 1,getCol(R,K,C,E).



line4([P,P,P,P|_],P).
line4([_,X1,X2,X3,X4|R],P):-line4([X1,X2,X3,X4|R],P).



invert(L,I):-invAux(L,[],I).

invAux([],I,I).
invAux([X|R],Aux,I):-invAux(R,[X|Aux],I).