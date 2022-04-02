/*
Predicados para apresentar o tabuleiro na consola de prolog
estes predicados não efetuam mudanças no tabuleiro


								divisão das linhas da tabela         mostrar a linha	*/
draw([R1,R2]):-writeln('\n  1   2'),writeln('+---+---+'),
								showLine(R2),writeln('+---+---+'),
								showLine(R1),writeln('+---+---+').

/*             						apresentação dos conteudos da linha			*/
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

game:-B=[
	[' ',' '],
	[' ',' ']
],draw(B),X=4,player(X,P),game(B,X/P/'-').

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
checkWin(NB,_/P/_,_,_/P/'W',(L,C)):-length(NB,X),La is X + 1 - L,invert(NB,I),diagonal(I,P,(La,C)).
checkWin(_,_,NEstado,NEstado,_).



horizontal([L|_],P,N,N):-line2(L,P).
horizontal([_|R],P,N,M):-K is N + 1,horizontal(R,P,K,M).



vertical([[X1|_],[X2|_]],P,C,C):-line2([X1,X2],P).
vertical([[_|R1],[_|R2]],P,N,C):-K is N + 1,vertical([R1,R2],P,K,C).



diagonal(B,P,(L,C)):-X is L - C, X >= 0,L1 is 1+X,diagonalAux(B,1,(L1,1),Line),line2(Line,P).
diagonal(B,P,(L,C)):-X is C - L + 1, X > 1,diagonalAux(B,1,(1,X),Line),line2(Line,P).

diagonalAux(B,L,(L,C),D):-getDiagonal(B,C,D).
diagonalAux([_|R],N,(L,C),D):-K is N + 1,diagonalAux(R,K,(L,C),D).

getDiagonal([],_,[]).
getDiagonal(_,C,[]):-C > 7.
getDiagonal([L|R],C,[E|D]):-getCol(L,1,C,E),Ca is C+1,getDiagonal(R,Ca,D).

getCol([E|_],C,C,E).
getCol([_|R],N,C,E):-K is N + 1,getCol(R,K,C,E).



line2([P,P|_],P).



invert(L,I):-invAux(L,[],I).

invAux([],I,I).
invAux([X|R],Aux,I):-invAux(R,[X|Aux],I).