% definir o tamanho da linha como dinamico para permitir diferentes tamanhos de linha
:-consult('astar.pl').
/*
	Componente visual
		do jogo
*/

% apresenta o tabuleiro e o indice das colunas
draw(Estado-B):-merge(Estado-B,NB),showLines(NB).

% apresenta as linhas do tabuleiro
showLines([]).
showLines([L|R]):-showLine(L),showLines(R).

% apresenta a linha passada como parametro
showLine([]):-nl.
showLine([E|R]):-writef('%w',[E]),showLine(R).


% serve para reunir toda a informacao no tabuleiro
merge((X,Y)/BoxList/O-B,NB):-replaceTile(X,Y,0,'@',B,BAux1),replaceList('o',O,BAux1,BAux2),replaceList('B',BoxList,BAux2,NB).

replaceList(_,[],NB,NB).
replaceList(E,[(X,Y)|List],B,NB):-replaceTile(X,Y,0,E,B,BAux),replaceList(E,List,BAux,NB).

replaceTile(X,Y,Y,E,[L|R],[NL|R]):-replaceAux(X,0,E,L,NL).
replaceTile(X,Y,N,E,[L|R],[L|NR]):-K is N + 1,replaceTile(X,Y,K,E,R,NR).

replaceAux(X,X,E,[_|R],[E|R]).
replaceAux(X,N,E,[C|R],[C|NR]):-K is N + 1,replaceAux(X,K,E,R,NR).

/*
	   logica do jogo
	(inicializacao do jogo)
*/

game:-level(1,Estado-B),draw(Estado-B),game(Estado-B).

game(Estado-B):-goal(Estado-B),draw(Estado-B),writeln('win'),!.
game((Px,Py)/BoxList/O-B):-getDir(Dx,Dy),checkEmpty(B,Px,Py,Dx,Dy,X,Y),nl,makeMove(X,Y,Dx,Dy,B,BoxList,NBoxList),
						   draw((X,Y)/NBoxList/O-B),game((X,Y)/NBoxList/O-B).
						   
gameAI:-level(1,Estado-B),gameAI(Estado-B).

gameAI(Estado-B):-bestfirst(Estado-B,Solution),reverse(Solution,Inverted),printSolution(Inverted).
printSolution([]):-writeln('Solved').
printSolution([Estado-B|R]):-draw(Estado-B),sleep(0.5),printSolution(R).

% selecionar a coordenada
getDir(X,Y):-repeat,get_single_char(Code),char_code(Key,Code),dir(Key,X,Y).

% teclas com direcoes de como o player se pode mexer
dir('d',1,0).
dir('a',-1,0).
dir('s',0,1).
dir('w',0,-1).

% calcula a posicao e verifica se esta vazia
checkEmpty(B,Px,Py,Dx,Dy,X,Y):-X is Px + (Dx),Y is Py + (Dy),nth0(Y,B,Line),!,nth0(X,Line,' '),!.

% o move e possivel se a casa estiver vazia ou se for uma caixa que se possa mexer
makeMove(X,Y,_,_,_,BoxList,BoxList):- \+member((X,Y),BoxList),!.
makeMove(Bx,By,Dx,Dy,B,BoxList,NBoxList):-checkEmpty(B,Bx,By,Dx,Dy,X,Y),\+member((X,Y),BoxList),select((Bx,By),BoxList,(X,Y),NBoxList),!.

% indica se ja esta no estado final(todas as caixas nos objetivos)
goal(_/[]/_-_).
goal(_/[(X,Y)|BoxList]/O-B):-member((X,Y),O),goal(_/BoxList/O-B).


/*
	componeste dedicada 
		ao a-star
*/

% considerasse Estado como Player/[Box|BoxList], sendo Player e Box coordenadas (x,Y)
s((Px,Py)/BoxList/O-B,(X,Y)/NBoxList/O-B,1):-dir(_,Dx,Dy),checkEmpty(B,Px,Py,Dx,Dy,X,Y),makeMove(X,Y,Dx,Dy,B,BoxList,NBoxList).

% custo euristico
h(_/BoxList/O-_,H):-hAux(BoxList,O,0,H).

hAux([],[],H,H).
hAux([Box|R],O,K,H):-shortest(Box,O,C,NO),N is K + C,hAux(R,NO,N,H),!.

% recebe uma caixa, a lista de objetivos e o custo mais curto atual,retorna a distancia mais curta e a lista de objetivos restantes
shortest(_,[],inf,[]).
shortest((X1,Y1),[(X2,Y2)|RO],C,NO):-shortest((X1,Y1),RO,C2,AO),C1 is abs(X1-X2) + abs(Y1-Y2), minPath(C1-RO,C2-[(X2,Y2)|AO],C-NO).

minPath(C1-O1,C2-_,C1-O1):-C1 < C2.
minPath(_-_,C2-O2,C2-O2).



/*
	conponente de niveis 
		   do jogo
*/

level(1,Estado-B):-B=[
	[' ',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ','\u25FC',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ',' ',' ','\u25FC',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ','\u25FC','\u25FC','\u25FC',' ',' ',' ','\u25FC','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	[' ',' ','\u25FC',' ',' ',' ',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' '],
	['\u25FC','\u25FC','\u25FC',' ','\u25FC',' ','\u25FC','\u25FC',' ','\u25FC',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC'],
	['\u25FC',' ',' ',' ','\u25FC',' ','\u25FC','\u25FC',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ',' ',' ',' ','\u25FC'],
	['\u25FC',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','\u25FC'],
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ','\u25FC','\u25FC','\u25FC',' ','\u25FC',' ','\u25FC','\u25FC',' ',' ',' ',' ','\u25FC'],
	[' ',' ',' ',' ','\u25FC',' ',' ',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC'],
	[' ',' ',' ',' ','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC',' ',' ',' ',' ',' ',' ',' ',' ']
	],Estado=(11,8)/[(5,7)]/[(17,7)].