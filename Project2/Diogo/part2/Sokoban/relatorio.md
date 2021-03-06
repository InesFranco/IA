# Programa em prolog
## Logica de jogo

Foi criada uma estrutura *level* (número do nivel, estado do tabuleiro) para tornar o inicio do jogo menos verboso.   
O estado do tabuleiro é composto por um array de arrays que definem o conteúdo de cada célula do mapa. A informação do player, das caixas e dos objectivos são armazenadas fora do tabuleiro para tornar a heuristica mais fácil de calcular. A informação consiste no caso do player apenas nas suas coordenadas e no caso das caixas e dos objetivos em arrays de coordeadas.


Existe uma componente visual que tem como objetivo mostrar o estado do jogo de forma adequada ao utilizador.   
O jogo apresenta dois modos: 

* Modo de jogo com input do utilizador
* Modo de jogo que resolve o mapa através do algoritmo a-star


O predicado *checkEmpty* recebe as coordenadas do jogador e o vetor de movimento, verifica se a casa do tabuleiro não é uma parede e, caso não seja, retorna as coordenadas para onde o player pretende mover-se.
*makeMove* é chamado após o predicado *checkEmpty* e tem como objetivo verificar se é possivel mover o player para a posição pretendida. Caso seja possível, move-o. Se houver uma caixa adiante do jogador move-a também.

O predicado *goal* verifica se as caixas já se encontram todas nos objetivos.
___
## Algoritmo A-star 
Partindo do código disponibilizado pelo professor, foram adicionados os predicados em falta.   
O predicado *s* que recebe o estado atual, gera o novo estado e tem custo de 1 sempre.
O predicado *h* consiste na heurística do programa que recebe o estado e retorna a distância de *Manhattan* das caixas aos objetivos. Para calcular esta distância foi criado um predicado para calcular a distância ao objetivo mais próximo para tentar minimizar a distância total.

-----
# Programa em kotlin
## Logica de jogo

Foi aplicada a mesma lógica para armazenar o estado do tabuleiro que o programa em prolog. 

A classe Data contem :

* board : Array de Arrays do tipo *Tile* onde o *Tile*  representa o conteúdo do tabuleiro
* player: Pair<Int,Int> que corresponde ás coordenadas do jogador 
* boxes: Array de Pair<Int,Int> que corresponde ás coordenadas das caixas 
* goals: Array de Pair<Int,Int> que corresponde ás coordenadas dos objectivos
* move: Campo que guarda o movimento anterior sendo este útil para optimizar posteriormente a solução final.

Esta classe tem também alguns métodos para facilitar o movimento do jogador e das caixas e um método para criar uma copia do objeto sem ter partilha de referências.

O programa tem 3 modos de jogo:

* o modo com input do utilizador
* o modo de jogo que resolve o mapa através do algorito SA (Simulated Annealing)
* o modo de jogo que resolve o mapa através do algorito GA (Genetic algorithm)


Os métodos importantes são:

* *makeMove* : Recebe o vetor do movimento e o estado do tabuleiro atualizando-o com o novo movimento
* *checkWin* : Verifica se o estado do tabuleiro tem todas as caixas nos objetivos.
* *optimizeSolution* : Recebe um array de estados do tabuleiro e retorna um array optimizado da solução, removendo movimentos que se anulam ou que não geram mudanças nas caixas do 
tabuleiro.




## Algoritmo SA (Simulated Annealing)
O método *sa* retorna um objeto da classe SAOut que contém a temperatura, o numero de avaliações, o custo, a temperatura maxima, a temperatura minima, a variação de temperatura a cada ciclo, o numero de repetições por temperatura, a solução e o 
ojeto que contém a informação dos estados gerados. 
Foi também criada uma classe (classe U) para guardar o custo e a solução.

* *getInitialSolution* : Gera a solução inicial
* *getRandomNeigh* : Gera um estado vizinho ao estado atual verificando se a solução gerada tem alguma caixa bloqueada, caso tenha, faz rollback da solução até ter a certeza que a caixa deixa de estar bloqueada. 
* *evalFunc* : Gera um custo que, além de considerar a distancia da caixa ao objetivo e a distancia do sokoban á caixa, considera ainda o tamanho da solução para potenciar soluções com menos movimentos.
* *isOptimum* : Indica se a solução atual é optima ou seja, se todas as caixas se encontram no seu devido objectivo.



## Algoritmo GA (Genetic Algorithm)
O método *ga* retorna um objeto da classe GAOut que contém :

* Número de avaliações, 
* Fit
* Tmax : número maximo de gerações
* Tamanho da população
* Probabilidade de crossover
* Probabilidade de mutação
* Indivíduo
* Solução

Foi criada a classe *Gen* para guardar o código genético, o seu tamanho e a solução que gerou. 


* getBestFitness :  Retorna o fit e o índice do indivíduo mais apto
* getInitialPopulation : Gera a população inicial com os genes gerados de forma aleatória
* evaluatePopulation : Retorna o *fit* de toda a população calculado a partir do metodo evalFunc que funciona de forma semelhante ao usado no algoritmo SA. 
* isOptimum : Semelhante ao algoritmo SA 
* updateSolution :  Atualiza a solução de acordo com o codigo genetico. 

Foi criada a classe Fit para poder armazenar o fit e o índice de um indivíduo no mesmo objeto.  


Os métodos select, cross e mutation têm como objetivo simular as 
condições de evolução dos individuos:

* select:  Torneio binário que gera batalhas entre os individuos da população,substituindo os individuos menos aptos pelos individuos mais capazes
* cross: Crossover dos genes dos dois individuos de acordo com a probabilidade de crossover. 
* mutation: Simula a mutação dos genes de alguns individuos selecionados a partir da probabilidade de mutação.


### Algoritmo SA(Optimização)


Foi explorada uma variação do algoritmo de Simulated Annealing. Nesta versão o estado contém um caminho que vai ser alterado por cada iteração. 
Existe a possibilidade de concretizar três tipos de operações sobre o caminho:

* ADD : Adiciona no fim do caminho um movimento random
* REMOVE : Retira uma posição aleatória do caminho
* ALTER : Remove uma parte do caminho. Esta instrução é extremamente importante para evitar entradas em ciclos porque dá a possibilidade de reconstruir um caminho onde a caixa ficou presa. Também é uma forma de procurar caminhos que possam ser mais curtos.

Estas operações retornam um estado com o novo caminho que vai ser avaliado pelo método *evalFunc*. Este método apenas chama o método *evaluatePath* que vai percorrer o caminho e, caso este seja inválido, retorna infinito positivo. Um caminho é inválido se passar por uma parede ou se a caixa ficar presa num canto. Para favorecer caminhos mais curtos foi adicionado um modificador que multiplica pelo tamanho do caminho.


Quando for encontrada uma solução o algoritmo retorna o estado final que contém o caminho percorrido. Como o algoritmo Simulated annealing não é o mais indicado para o tipo de problema este caminho final continua a ter movimentos não ótimos. Porém observou-se que com esta versão otimizada os caminhos gerados estavam entre os 40 e os 200 movimentos enquanto que com a versão original os movimentos estavam entre os 100 e os 1000. 

