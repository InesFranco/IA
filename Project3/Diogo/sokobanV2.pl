% consultar o codigo do pop
:-consult('pop.pl').
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

game:-getLevel(Estado-B),draw(Estado-B),game(Estado-B),!.

game(Estado-B):-goal(Estado-B),draw(Estado-B),writeln('win'),!.
game((Px,Py)/BoxList/O-B):-getDir(Dx,Dy),checkEmpty(B,Px,Py,Dx,Dy,X,Y),nl,makeMove(X,Y,Dx,Dy,B,BoxList,NBoxList),
						   draw((X,Y)/NBoxList/O-B),game((X,Y)/NBoxList/O-B),!.
						   
gameAI:-getLevel(Estado-B),gameAI(Estado-B),!,retractall(box(_)),retractall(right(_,_)),retractall(down(_,_)).

gameAI(Estado-B):-merge(Estado-B,NB), toState(NB, 0, 0, [], S),getNeighbor(B,0),getObj(Estado,Obj),!,plan(S, Obj, P), show_pop(P),writeSolution(P,Estado-B).

writeSolution(pop(Actions, _, _, _),Estado-B) :-
	instantiate_times(Actions),               % Instantiate times of actions for readability
	setof(T:A, member(A:T, Actions), Sorted), % Sort actions according to times
	nl, writeSolutionAux(Sorted,Estado-B).    % Write schedule
writeSolutionAux([_:push(_,_,(X1,Y1),(X2,Y2),_)],_/BoxList/O-B):-draw((X1,Y1)/BoxList/O-B),Dx is X2-X1, Dy is Y2-Y1,
				makeMove(X2,Y2,Dx,Dy,B,BoxList,NBoxList),sleep(0.5),draw((X2,Y2)/NBoxList/O-B),!.
writeSolutionAux([_:go(_,(X1,Y1),(X2,Y2))|R],_/BoxList/O-B):-draw((X1,Y1)/BoxList/O-B),Dx is X2-X1, Dy is Y2-Y1,
				makeMove(X2,Y2,Dx,Dy,B,BoxList,NBoxList),sleep(0.5),writeSolutionAux(R,_/NBoxList/O-B).
writeSolutionAux([_:push(_,_,(X1,Y1),(X2,Y2),_)|R],_/BoxList/O-B):-draw((X1,Y1)/BoxList/O-B),Dx is X2-X1, Dy is Y2-Y1,
				makeMove(X2,Y2,Dx,Dy,B,BoxList,NBoxList),sleep(0.5),writeSolutionAux(R,_/NBoxList/O-B).

getObj(_/_/Objs,Obj):-getObjAux(Objs,0,[],Obj).

getObjAux([],_,Obj,Obj).
getObjAux([C|R],X,Aux,Obj):-X1 is X + 1,atom_concat(b,X,Box), getObjAux(R,X1,[at(Box,C)|Aux],Obj).

getNeighbor([List1,List2|R],Y):-Y1 is Y + 1, getNeighborAux(List1, List2,0,Y,Y1),getNeighbor([List2|R],Y1).
getNeighbor([List],Y):-getNeighborAux(List, 0, Y).

getNeighborAux([' ',' '|R1],[' '|R2],X,Y,Y1):-X1 is X + 1, assertz(right((X,Y),(X1,Y))),assertz(down((X,Y),(X,Y1))), getNeighborAux([' '|R1],R2,X1,Y,Y1).
getNeighborAux([' ',_|R1],[' ',_|R2],X,Y,Y1):-X2 is X + 2, assertz(down((X,Y),(X,Y1))), getNeighborAux(R1,R2,X2,Y,Y1).
getNeighborAux([' ',' '|R1],[_|R2],X,Y,Y1):-X1 is X + 1, assertz(right((X,Y),(X1,Y))), getNeighborAux([' '|R1],R2,X1,Y,Y1).
getNeighborAux([_|R1],[_|R2],X,Y,Y1):-X1 is X + 1, getNeighborAux(R1,R2,X1,Y,Y1).
getNeighborAux([],[],_,_,_).

getNeighborAux([' ',' '|R1],X,Y):-X1 is X + 1, assertz(right((X,Y),(X1,Y))), getNeighborAux([' '|R1],X1,Y).
getNeighborAux([_|R1],X,Y):-X1 is X + 1, getNeighborAux(R1,X1,Y).
getNeighborAux(_,_,_).

toState([], _, _, Out, Out).
toState([List|R], Y, B, Aux, Out):-toStateAux(List, (0, Y), B, NB, Aux, Aux1), Y1 is Y + 1, toState(R, Y1, NB, Aux1, Out).

toStateAux([], _, B, B, Out, Out).
toStateAux([' '|R], (X, Y), B, NB, Aux, Out):-X1 is X + 1, toStateAux(R, (X1, Y), B, NB, [clear((X, Y))|Aux], Out).
toStateAux(['o'|R], (X, Y), B, NB, Aux, Out):-X1 is X + 1, toStateAux(R, (X1, Y), B, NB, [clear((X, Y))|Aux], Out).
toStateAux(['@'|R], (X, Y), B, NB, Aux, Out):-X1 is X + 1, toStateAux(R, (X1, Y), B, NB, [at(s, (X, Y))|Aux], Out).
toStateAux(['B'|R], (X, Y), B, NB, Aux, Out):-X1 is X + 1, atom_concat(b,B,Box), assertz(box(Box)), B1 is B + 1, toStateAux(R, (X1, Y), B1, NB, [at(Box, (X, Y))|Aux], Out).
toStateAux([_|R], (X, Y), B, NB, Aux, Out):-X1 is X + 1, toStateAux(R, (X1, Y), B, NB, Aux, Out).

getLevel(Lvl):-repeat,read(Code),level(Code, Lvl).

% selecionar a coordenada
getDir(X,Y):-repeat,get_single_char(Code),dir(Code,X,Y).

% teclas com direcoes de como o player se pode mexer
dir(6,1,0).
dir(2,-1,0).
dir(14,0,1).
dir(16,0,-1).

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
		ao pop
*/

:- op(100, fx, ~).     % Operator for condições negadas


% Precondition of action: sokoban S goes from cell SPos to NewSPos
%
can(go(S, SPos, NewSPos), [at(S, SPos), clear(NewSPos)]) :-
	sokoban(S), adjacent(SPos,NewSPos).
can(push(S, B, SPos, NewSPos, NewBPos), [at(S, SPos), at(B,NewSPos), clear(NewBPos)]) :-	
	sokoban(S), box(B), newBoxPos(SPos, NewSPos ,NewBPos).

newBoxPos((SPX,Y), (NSPX,Y), (NBPX,Y)):-right((SPX,Y),(NSPX,Y)),right((NSPX,Y),(NBPX,Y)).
newBoxPos((SPX,Y), (NSPX,Y), (NBPX,Y)):-right((NSPX,Y),(SPX,Y)),right((NBPX,Y),(NSPX,Y)).
newBoxPos((X,SPY), (X,NSPY), (X,NBPY)):-down((X,SPY),(X,NSPY)),down((X,NSPY),(X,NBPY)).
newBoxPos((X,SPY), (X,NSPY), (X,NBPY)):-down((X,NSPY),(X,SPY)),down((X,NBPY),(X,NSPY)).

% Effects of action go(Robot, CellA, CellB):
% After the action at(Robot, CellB) and clear(CellA) become true,
% and at(Robot, CellA) and clear(CellB) become false
%
effects(go(S, SPos, NewSPos), [at(S, NewSPos), clear(SPos), ~at(S, SPos), ~clear(NewSPos)]).
effects(push(S, B, SPos, NewSPos, NewBPos), [at(S, NewSPos), at(B, NewBPos), clear(SPos), ~at(S, SPos), ~at(B,NewSPos), ~clear(NewBPos)]).
sokoban(s).


adjacent((X1,Y), (X2,Y)):-right((X1,Y), (X2,Y));right((X2,Y),(X1,Y)).
adjacent((X,Y1), (X,Y2)):-down((X,Y1), (X,Y2));down((X,Y2),(X,Y1)).  % Cells A and B are adjacent in the grid
	

% inconsistent(G1, G2): goals G1 and G2 inconsistent
%
inconsistent(G, ~G).  % Negated goals always inconsistent

inconsistent(~G, G).

inconsistent(at(E, C1), at(E, C2)) :-
	C1 \== C2.      % Entity cannot be at different places at the same time

inconsistent(at(_, C), clear(C)). % A cell cannot be both clear and occupied at a time

inconsistent(clear(C), at(_, C)).

inconsistent(at(E1, C), at(E2,C)) :- % Two entities cannot be in the same cell at a time
	E1 \== E2.



% adicionar os factos das caixas de forma automatica
:-dynamic(box/1).
:-dynamic(right/2).
:-dynamic(down/2).




/*
	conponente de niveis 
		   do jogo
*/

level(0,Estado-B):-B=[
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC'],
	['\u25FC',' ',' ',' ',' ',' ','\u25FC'],
	['\u25FC',' ',' ',' ',' ',' ','\u25FC'],
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC']
	],Estado=(1,1)/[(2,1)]/[(5,1)].
	
level(1,Estado-B):-B=[
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC'],
	['\u25FC',' ',' ',' ',' ','\u25FC'],
	['\u25FC',' ',' ',' ',' ','\u25FC'],
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC']
	],Estado=(1,1)/[(2,1),(2,2)]/[(4,1),(1,2)].
	
level(2,Estado-B):-B=[
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC'],
	['\u25FC',' ',' ',' ',' ','\u25FC'],
	['\u25FC','\u25FC',' ',' ',' ','\u25FC'],
	['\u25FC',' ',' ',' ',' ','\u25FC'],
	['\u25FC',' ',' ',' ',' ','\u25FC'],
	['\u25FC','\u25FC','\u25FC','\u25FC','\u25FC','\u25FC']
	],Estado=(1,1)/[(2,3)]/[(4,4)].

level(3,Estado-B):-B=[
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


	
	