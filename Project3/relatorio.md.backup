## Algoritmo de Reconhecimento de imagem

Estudou-se o algoritmo de reconhecimento de digitos explorado no livro "Make your own Neural network". 

A rede neuronal tem três camadas: Camada de Input, Camada escondida e camada de Output.  A camada de Input tem 784 nós que correspondem ao tamanho da imagem do dígito (28x28), a camada escondida tem 200 nós e a camada de Output tem 10 nós pois os dígitos vão de 0 a 9.


Entre cada nó existem pesos que vão ser ajustados há medida que se treina a rede neuronal. 

Foi importado o ficheiro .csv de treino que contém milhares de imagens de dígitos de 0 a 9 escritos à mão que foram codificados num array de valores greyscale. 
Para cada linha deste ficheiro é chamado o método Train da classe Neural Network. O método Train vê os outputs gerados com os pesos ainda por ajustar e compara o output com o valor esperado que vem no ficheiro. Ao comparar o valor esperado com o valor real gerado chega-se ao erro. Este erro é propagado da camada de output para a escondida e da camada escondida para a de input e serve para ajustar os pesos ao longo da rede neuronal. O erro é repartido pelos nós tendo em conta o peso, ou seja, um nó com um peso mais baixo é apenas ajustado uma porção mais pequena. 
Para que os pesos sejam ajustados de forma mais incremental é usado um atenuador chamado learning rate.

O método Query é chamado quando queremos questionar a rede neuronal quando esta estiver treinada. Assim podemos passar os input que vão ser as nossas imagens de dígito num formato 28x28 codificadas num array de valores greyscale. Depois fazemos a multiplicação da matriz de inputs com a matriz de pesos e passamos isto por uma função de activação. O mesmo é feito da camada secreta para a camada de output. Nos Outputs vamos ter uma matriz com as probabilidade para cada dígito. 


### Resultados

Após treinar-se a Rede Neuronal foram produzidas duas imagens com o formato desejado com os dígitos 1 e 4. No caso do 1 a rede neuronal apontou para um 7 pois a imagem do 1 criada tinha caracteristicas ambiguas. 

  