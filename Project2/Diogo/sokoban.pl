% definir o tamanho da linha como dinamico para permitir diferentes tamanhos de linha
%:-consult('alpha_beta.pl').

/*
	Componente visual
		do jogo
*/

% apresenta o tabuleiro e o indice das colunas
draw:-B=[
	[' ',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ','\u25FC',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ','\u25FC',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ','\u25FC','\u25FC','\u25FC',' ',' ',' ','\u25FC','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ','\u25FC',' ',' ',' ',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	['\u25FC','\u25FC','\u25FC',' ','\u25FC',' ','\u25FC','\u25FC',' ','\u25FC',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC'],
	['\u25FC',' ',' ',' ','\u25FC',' ','\u25FC','\u25FC',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ',' ',' ',' ','\u25FC'],
	['\u25FC',' ',' ',' ',' ','B',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','o','\u25FC'],
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ','\u25FC','\u25FC','\u25FC',' ','\u25FC','P','\u25FC','\u25FC',' ',' ',' ',' ','\u25FC'],
	[' ',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC'],
	[' ',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ']
],showLines(B).

% apresenta as linhas do tabuleiro
showLines([]).
showLines([L|R]):-showLine(L),showLines(R).

% apresenta a linha passada como parametro
showLine([]):-nl.
showLine([E|R]):-writef('%w',[E]),showLine(R).


/*
	   logica do jogo
	(inicializacao do jogo)
*/

% direçoes de como o player se pode mexer
dir(1,0).
dir(-1,0).
dir(0,1).
dir(0,-1).


% considerasse Estado como Player/[Box|BoxList], sendo Player e Box coordenadas (x,Y)
s((Px,Py)/BoxList-B,(X,Y)/NBoxList-B,1):-dir(Dx,Dy),checkEmpty(B,Px,Py,Dx,Dy,X,Y),makeMove(X,Y,Dx,Dy,B,BoxList,NBoxList).

% calcula a posicao e verifica se esta vazia
checkEmpty(B,Px,Py,Dx,Dy,X,Y):-X is Px + (Dx),Y is Py + (Dy),nth0(Y,B,Line),!,nth0(X,Line,' ').

% o move e possivel se a casa estiver vazia ou se for uma caixa que se possa mexer
makeMove(X,Y,_,_,_,BoxList,BoxList):-_ is 0,\+member((X,Y),BoxList),!.
makeMove(Bx,By,Dx,Dy,B,BoxList,NBoxList):-checkEmpty(B,Bx,By,Dx,Dy,X,Y),\+member((X,Y),BoxList),select((Bx,By),BoxList,(X,Y),NBoxList),!.



% indica se ja esta no estado final(todas as caixas nos objetivos)
goal(_/[]-_).
goal(_/[(X,Y)|BoxList]-B):-nth0(Y,B,Line),nth0(X,Line,'o'),goal(_/BoxList-B).