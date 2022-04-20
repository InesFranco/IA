% definir o tamanho da linha como dinamico para permitir diferentes tamanhos de linha
:-dynamic(win_dim/1).

/*
	Componente visual
		do jogo
*/

% apresenta o tabuleiro e o indice das colunas
draw(B):-header(B,Division),reverse(B,I),showLines(I,Division).

% cria a os indices e devolve a linha horizintal(de separacao)
header([L|_],Division):-length(L,X),columnum(1,X),division(0,X,'+',Division),writeln(Division).
% indica os indices das colunas
columnum(X,X):-writef('  %w \n',[X]).
columnum(N,X):-writef('  %w ',[N]),K is N + 1,columnum(K,X).

% cria a linha de divisao
division(X,X,Division,Division).
division(N,X,A,Division):-atom_concat(A,'---+',A1),K is N + 1,division(K,X,A1,Division).

% apresenta as linhas do tabuleiro
showLines([],_).
showLines([L|R],Division):-showLine(L),writeln(Division),showLines(R,Division).

% apresenta a linha passada como parametro
showLine([]):-writeln('|').
showLine([E|R]):-writef('| %w ',[E]),showLine(R).





/*
	   logica do jogo
	(inicializacao do jogo)
*/

% padrao do 4 em linha
game:-gamedim(6,7,4).

% padrao do jogo do galo
gameAI:-gamedimAI(3,3,3).

% predicado que permite configurar o jogo(dimensao do tabuleiro e de linha)
gamedim(L,C,N):-build_board(L,C,B),draw(B),calc_tiles(B,X),asserta(win_dim(N)),game(B,X/'X'/'-').

% predicado que permite configurar o jogo(dimensao do tabuleiro e de linha) com AI
gamedimAI(L,C,N):-build_board(L,C,B),draw(B),calc_tiles(B,X),asserta(win_dim(N)),gameAI(B,X/'X'/'-').

% contrutor do tabuleiro de jogo
build_board(L,C,B):-make_line(1,C,[' '],Line),make_board(1,L,[Line],B).

%construtor das linhas
make_line(X,X,L,L).
make_line(N,X,Aux,L):-K is N + 1,make_line(K,X,[' '|Aux],L).

%construtor de tabuleiro(auxiliar)
make_board(X,X,B,B).
make_board(N,X,[Line|R],B):-K is N + 1,make_board(K,X,[Line,Line|R],B).

% calcula o total de jogadas(casas do tabuleiro)
calc_tiles([L|R],X):-length(L,X1),length([L|R],X2),X is X1 * X2.




/*
	   logica do jogo
	   (jogabilidade)
*/

% jogabilidade e apresentacao do jogo(jogar e mostrar tabuleiro/fim do jogo)
game(_,_/_/'T'):-writeln('Empate'),retract(win_dim(_)).
game(_,_/_/P):-writef('O player %w ganhou',[P]),retract(win_dim(_)).
game(B,Estado):-play(B,NB,Estado,NEstado),draw(NB),game(NB,NEstado).




% recebe uma board e retorna o indice das posicoes possiveis(comeca em 1)
possible_play(Board,PosList):-reverse(Board,[L|_]),empty_pos(L,1,[],PosList).
empty_pos([],_,PosList,PosList).
empty_pos([' '|R],N,Aux,PosList):-K is N + 1,!,empty_pos(R,K,[N|Aux],PosList).   
empty_pos([_|R],N,Aux,PosList):-K is N + 1,!,empty_pos(R,K,Aux,PosList).

% le a input do jogador e verifica se a jogada e possivel(so retorna se a jogada for valida)
input_play(Board,Pos):-possible_play(Board,PP),read(Pos),member(Pos,PP).
input_play(Board,Pos):-writeln('jogada invalida'),input_play(Board,Pos).

% jogada do player e atualizacao do estado
play(B,NB,Estado,NEstado):-input_play(B,Pos),place(B,Pos,NB,Estado,EstAux,1,Cord),checkWin(NB,Estado,EstAux,NEstado,Cord).

% coloca a peca do jogador no tabuleiro
place([L|R],Pos,[NL|R],X/P/_,NEstado,N,(N,Pos)):-placeAux(L,Pos,P,NL,1),novoEstado(X/P/_,NEstado),!.
place([L|R],Pos,[L|NR],Estado,NEstado,N,Cord):-K is N + 1,place(R,Pos,NR,Estado,NEstado,K,Cord).
placeAux([X|R],Pos,P,[P|R],Pos):-!,X=' '.
placeAux([X|R],Pos,P,[X|NR],N):-K is N+1,placeAux(R,Pos,P,NR,K).

% atualiza o total de casas livres e troca o jogador
novoEstado(1/_/_,_/_/'T').
novoEstado(X/P/_,K/NP/'-'):-K is X - 1, player(P,NP).
player('O','X').
player('X','O').




% condicoes de fim de jogo
checkWin(NB,_/P/_,_,_/_/P,(L,_)):-horizontal(NB,P,1,L),!.
checkWin(NB,_/P/_,_,_/_/P,(_,C)):-vertical(NB,P,1,C),!.
checkWin(NB,_/P/_,_,_/_/P,Cord):-diagonal(NB,P,Cord),!.
checkWin(NB,_/P/_,_,_/_/P,(L,C)):-length(NB,X),reverse(NB,I),La is X - L + 1,diagonal(I,P,(La,C)),!.
checkWin(_,_,NEstado,NEstado,_).

% verifica a linha
horizontal([L|_],P,N,N):-!,line(L,P).
horizontal([_|R],P,N,M):-K is N + 1,horizontal(R,P,K,M).

% verifica a coluna
vertical(B,P,C,C):-vertAux1(B,L),!,line(L,P).
vertical(B,P,N,C):-vertAux2(B,NB),K is N + 1,vertical(NB,P,K,C).

vertAux1([],[]).
vertAux1([[X|_]|R],[X|L]):-vertAux1(R,L).

vertAux2([],[]).
vertAux2([[_|L]|R1],[L|R2]):-vertAux2(R1,R2).

%verifica a diagonal
diagonal(B,P,(L,C)):-X is L - C + 1, X > 0,diagonalAux(B,1,(X,1),Line),!,line(Line,P).
diagonal(B,P,(L,C)):-X is C - L + 1, X > 1,diagonalAux(B,1,(1,X),Line),!,line(Line,P).

diagonalAux([H|T],L,(L,C),D):-length(H,Len),getDiagonal([H|T],C,D,Len).
diagonalAux([_|R],N,(L,C),D):-K is N + 1,diagonalAux(R,K,(L,C),D).

% obtem a diagonal (recursivamente)
getDiagonal([],_,[],_).
getDiagonal(_,C,[],Len):-C > Len.
getDiagonal([L|R],C,[E|D],Len):-getCol(L,1,C,E),Ca is C+1,getDiagonal(R,Ca,D,Len).

% obtem o valor da coluna
getCol([E|_],C,C,E).
getCol([_|R],N,C,E):-K is N + 1,getCol(R,K,C,E).

% verifica a linha(condicao de vitoria)
line(L,P):-win_dim(X),lineX(L,P,0,X).

lineX(_,_,X,X).
lineX([P|R],P,N,X):-K is N + 1,lineX(R,P,K,X).
lineX([_|R],P,_,X):-lineX(R,P,0,X).





/*
		MinMax
		  Bot
*/

% jogabilidade e apresentacao do jogo(jogar e mostrar tabuleiro/fim do jogo)
gameAI(_,_/_/'T'):-writeln('Empate'),retract(win_dim(_)).
gameAI(_,_/_/'X'):-writeln('O Bot ganhou'),retract(win_dim(_)).
gameAI(_,_/_/'O'):-writeln('O player ganhou'),retract(win_dim(_)).
gameAI(B,X/'X'/'-'):-writeln('AI turn'),playAI(B,NB,X/'X'/'-',NEstado),draw(NB),gameAI(NB,NEstado).
gameAI(B,X/'O'/'-'):-play(B,NB,X/'O'/'-',NEstado),draw(NB),gameAI(NB,NEstado).


% jogada da IA e atualizacao do estado
playAI(B,NB,Estado,NEstado):-alphabeta(Estado-B,0,0,NEstado-NB,Val).



% The alpha-beta algorithm

alphabeta( Pos, Alpha, Beta, GoodPos, Val) :-
	moves( Pos, PosList), !,
	boundedbest( PosList, Alpha, Beta, GoodPos, Val)
	; % Or
	staticval( Pos, Val).       % Static value of Pos
	

boundedbest( [Pos | PosList], Alpha, Beta, GoodPos, GoodVal) :-
	alphabeta( Pos, Alpha, Beta, _, Val),
	goodenough( PosList, Alpha, Beta, Pos, Val, GoodPos, GoodVal).


goodenough( [], _, _, Pos, Val, Pos, Val) :- !.   % No other candidate


goodenough( _, Alpha, Beta, Pos, Val, Pos, Val) :-
	min_to_move( Pos), Val > Beta, !   % Maximizer attained upper bound
	; % Or
	max_to_move( Pos), Val < Alpha, !. % Minimizer attained lower bound


goodenough( PosList, Alpha, Beta, Pos, Val, GoodPos, GoodVal) :-
	newbounds( Alpha, Beta, Pos, Val, NewAlpha, NewBeta),     % Refine bounds
	boundedbest( PosList, NewAlpha, NewBeta, Pos1, Val1),
	betterof( Pos, Val, Pos1, Val1, GoodPos, GoodVal).


newbounds( Alpha, Beta, Pos, Val, Val, Beta) :-
	min_to_move( Pos), Val > Alpha, !.         % Maximizer increased lower bound



newbounds( Alpha, Beta, Pos, Val, Alpha, Val) :-
	max_to_move( Pos), Val < Beta, !.          % Minimizer decreased upper bound
	
	
	
newbounds( Alpha, Beta, _, _, Alpha, Beta). % Otherwise bounds unchanged


betterof( Pos, Val, Pos1, Val1, Pos, Val) :-   % Pos better than Pos1
	min_to_move( Pos), Val > Val1, !
	; % Or
	max_to_move( Pos), Val < Val1, !.


betterof( _, _, Pos1, Val1, Pos1, Val1). % Otherwise Pos1 better

   
% gera a lista com todas as jogadas possiveis
moves(X/P/'-'-B,PosList):-possible_play(B,PP),findall(NEstado-NB,move(B,X/P/'-',NB,NEstado,PP),PosList),PosList\=[].

move(B,Estado,NB,NEstado,PP):-member(Pos,PP),place(B,Pos,NB,Estado,EstAux,1,Cord),checkAI(NB,Estado,EstAux,NEstado,Cord).

checkAI(NB,Estado,EstAux,NEstado,Cord):-checkWin(NB,Estado,EstAux,NEstado,Cord).


max_to_move(_/'X'/'-'-_).
min_to_move(_/'O'/'-'-_).


staticval(_/_/'X'-_, 1).
staticval(_/_/'O'-_, -1).
staticval(_,0).