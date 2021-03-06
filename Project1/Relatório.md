## Como correr o programa

O predicado **game** corre a versão do tabuleiro original(6x7).

O predicado **gameAI** corre a versão com a Inteligência Artificial com a dimensão 3x3.

O predicado **gamedim** e o **gamedimAI** constroem um tabuleiro com linhas e colunas do tamanho passado.



## Lógica do Jogo

Estado = X/P/W
onde,

X = número de casas livres

P = Jogador atual

W = Condição de paragem(Alguém ganhou ou o tabuleiro está cheio)


Tabuleiro cheio é empate.
T é empate, X ou O de acordo com o player que ganhou

O predicado **possible_play** retorna as colunas que ainda têm posições livres. 

## Alpha Beta

o predicado moves chama o **possible_play** para encontrar as colunas ainda livres. De seguida utiliza o findall sobre o predicado move e de seguida verifica que nao resultou numa lista vazia.

O predicado move recebe as colunas disponiveis e, por backtrack, gera todas as jogadas possiveis.

O predicado **max_to_move** e **min_to_move** sucedem se o jogador atual for X ou O respetivamente.


O predicado **staticval** atribui uma pontuação aos tabuleiro final de cada ramo. 


## Conclusões

Observou-se que o algoritmo minimax é consideravelmente mais lento que o alpha-beta prunning visto que o último é uma optimização do primeiro.


___


**Alunos**
	
* Diogo Amorim, nº 47248
* Carlos Alves, nº 45835
* Inês Franco, nº 44860




