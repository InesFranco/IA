# Programa em prolog
## Logica de jogo

Foi criada uma estrutura level (numero do nivel, estado do tabuleiro) para facilitar o uso do estado do tabuleiro para
iniciar o jogo(torna o inicio do jogo menos verboso).   
O estado do tabuleiro é composto por um array de arrays que define as paredes do mapa, uma vez que no mapa existe muitas 
paredes e a iteracao por listas unidimensionais torna-se muito lento, então faz-se indexação através das coordenadas nas 
listas. A informação do player é armazenada em fora do tabuleiro para tornar os calculos mais faceis, sendo o player as
coordenadas, facilitando os cálculos. As caixas e os objetivos também foram colocados em arrays de coordeadas para
facilitar os calculos da euristica, uma vez que estes tornam os calculos muito mais rapidos.   

O jogo também tem uma componente visual que tem como objetivo mostrar o estado do jogo de forma adequada ao utilizador.   
O jogo apresenta dois modos de jogo: 
* a modo de jogo com input do utilizador
* o modo de jogo que resolve o mapa através do algoritmo a-star

Existe o predicado checkEmpty recebe as coordenadas do jogador e o vetor de movimento, verifica se a casa do tabuleiro 
não é uma parede e caso não seja uma parade retorna as coordenadas para onde o player se pretende mover.
O makeMove é chamado após o predicado checkEmpty e tem como objetivo verificar se é possivel mover o player para a 
posição pretendida, mexendo o player, movendo-o caso seja possivel ou movendo o player e a caixa caso esta se encontre 
diante do player e também seja possivel movê-la.

O predicado goal tem como objetivo verificar se as caixas já se encontram todas nos objetivos.

## Algoritmo A-star 
Usamos o algoritmo disponibilizado no git no ficheiro astar.pl e criamos os predicados que faltavam para poder usar o 
algoritmo.   
Foi necessario o predicado s que recebe o estado atual, gera o novo estado e tem custo de 1 (no sokoban todas as jogadas
têm o mesmo custo).   
A função h é a euristica que recebendo o estado retorna a distancia de manhattan das caixas aos objetivos. Para a 
calcular esta distancia foi criado um predicado para calcular o objetivo mais proximo para tentar minimizar a distância.

-----
# Programa em kotlin
## Logica de jogo

Usamos a mesma logica para armazenar o estado do tabuleiro que usamos em prolog e para conseguirmos usar estes como um 
objeto criamos a classe Data que contem o array de arrays de tile onde o tile é o tipo que representa o conteudo do 
array, o par de Int, Int que é as coordenadas do player e os array de pares de Int, Int dos objetivos e das caixas e tem
um campo para guardar qual foi o movimento anterior que é util para gerar optimizações da solução final.
Esta classe também tem alguns metodos para facilitar o movimento do jogador e das caixas e um metodo para criar uma 
copia do objeto sem ter partilha de referencias.

O jogo tem 3 modos de jogo:
* o modo com input do utilizador
* o modo de jogo que resolve o mapa através do algorito SA (Simulated Annealing)
* o modo de jogo que resolve o mapa através do algorito GA (Genetic algorithm)

O metodo makeMove recebe o vetor do movimento e o estado do tabuleiro e atualiza o estado do tabuleiro com o movimento,
o metodo checkWin verifica se o estado do tabuleiro é tem todas as caixas nos objetivos, o metodo printBoard apresenta
 o estado do tabuleiro de forma apelativa ao utilizador e o metodo optimizeSolution recebe um array de estados do 
tabuleiro e retorna um array optimizado de solução(remove movimentos que se anulam e não geram mudanças nas caixas do 
tabuleiro).

No modo de input do player tem o metodo input que recebebe o input do utilizador e transforma-o um vetor. Após a 
optenção do vetor é chamado o makeMove, este procedimento repete-se até ao estado do jogo verificar o metodo checkWin.

## Agoritmo SA (Simulated Annealing)
Para este modo adaptamos o codigo disponibilizado em matlab para kotlin ficando esse codigo na classe sa.SA. O metodo sa
 retorna um objeto da classe SAOut que contem um a temperatura, o numero de avaliações, o custo, a temperatura maxima, a
 temperatura minima, o R que é a variação de temperatura a cada ciclo, o K que é o numero de repetições, a solução e o 
ojeto que tem as informações dos estados gerados. Foi criada uma classe (classe U) para guardar o custo e a solução.
O metodo getInitialSolution gera a solução inicial, o metodo getRandomNeigh gera um estado vizinho ao estado atual, o 
metodo evalFunc gera o custo da solução atual e o metodo isOptimum indica se a solução atual é optima.
Foi criado um enumerado para indicar o modo. 

O metodo getRandomNeigh verifica se a solução gerada tem alguma caixa bloqueada e caso isso aconteça ele retorna uma 
solução que tenha corrigido este problema, ou seja, este dá rollback da solução até ter a certeza de que a caixa deixa 
de estar numa rota que a irá bloquear.  

O metodo evalFunc retorna um custo que além de considerar a distancia da caixa ao objetivo e a distancia do sokoban a 
caixa ainda considera o tamanho da solução para potenciar soluções com menos movimentos.

## Algoritmo GA (Genetic Algorithm)
Para este modo adaptamos o codigo disponibilizado em matlab para kotlin ficando esse codigo na classe sa.SA. O metodo sa
retorna um objeto da classe GAOut que contem o numero de avaliações, o fit, o Tmax que é numero maximo de gerações, o 
tamanho da população, a probabilidade de crossover, a probabilidade de mutação, o individuo e a solução. Foi criada uma 
classe (classe Gen) para guardara o codigo genetico, o tamanho do codigo genetico e a solução gerada pelo codigo 
genetico do individuo.   

O metodo getBestFitness retorna o fit e o indice do individuo mais apto, o metodo getInitialPopulation gera a população 
inicial com os genes gerados forma aleatoria, usando o metodo getInicialSolution, o metodo evaluatePopulation retorna o 
fit de toda a população calculado a partir do metodo evalFunc (funciona de forma igual ao usado no algoritmo SA), usa o 
mesmo metodo isOptimum do algoritmo SA e também foi criado o metodo updateSolution para atualizar a solução de acordo 
com o codigo genetico. Foi criada a classe FitI para poder armazenar o fit e o indice de um individuo no mesmo objeto.  

Foram criados os metodos select, cross e mutation para usar no algotimo GA. Estes metodos têm como objetivo simular as 
condições de evolução dos individuos. O metodo select é um torneio binario que gera batalhas entre os individuos da 
população e aquele que é menos apto é substituido pelo mais apto dos dois. O metodo cross é um crossover dos genes dos 
dois individuos de acordo com a probabilidade de crossover. O metodo mutation simula a mutação dos genes de alguns 
individuos selecionados a partir da probabilidade de mutação.