# Desktop RemoteOK App
> Este projeto visa a cria��o do aplicativo Desktop RemoteOK, voltado para desktops da plataforma Windows. O programa mostrar� as oportunidades de trabalho remoto anunciados no site https://remoteok.io/, sendo atualizado  sempre que houver uma nova oportunidade anunciada, sendo poss�vel favoritar e acompanhar as oportunidades que mais lhe agradarem.

![JAVA Version][java-image]



![](../header.png)

## Requisitos funcionais

* :heavy_check_mark: Dever� ser criada uma aplica��o desktop que mostre as oportunidades de trabalhos remotos em formato de lista;

* :heavy_check_mark: Quando o usu�rio encontrar uma oportunidade interessante, deve poder compartilhar via e-mail;

* :heavy_check_mark: O aplicativo ser� composto dos seguintes components:
Duas abas: Lista de todos os jobs / Favoritos;

* :heavy_check_mark: O usu�rio poder� favoritar ou desfavoritar uma aplica��o clicando no �cone de cora��o;

* :heavy_check_mark: Quando uma oportunidade da lista estiver favoritada o icone de cora��o dever� ter a cor vermelha s�lida, do contr�rio dever� ser vazada, ou seja, sem preenchimento;

* :heavy_check_mark: A lista de favoritos deve ser salva em arquivo para que possa ser recuperado cada vez que o usu�rio entrar no aplicativo;

* :heavy_check_mark: Colocar a aplica��o para funcionar no Systray do SO.
 


## Instala��o

### Requisitos:

* Java 8 *JDK 1.8*

* Git

* IDE (Netbeans)

### Netbeans:

* Fa�a um clone do reposit�rio com o comando `git clone https://github.com/chelo84/remote-ok-desktop.git` 
 na pasta que desejar;
* Abra o projeto na IDE em Arquivo -> Abrir projeto... -> remote-ok-desktop -> Abrir projeto;
* Voc� ter� que adicionar as bibliotecas externas para poder rodar o projeto, para isso clique com o
bot�o direito no projeto e v� em propriedades -> Bibliotecas -> Adicionar JAR/Pasta -> v� at� a pasta
do projeto e entre na pasta Libs -> selecione todos os arquivos desta pasta e clique *Abrir* e ent�o clique em Ok;
* Pronto, est� tudo configurado, apenas aperte no bot�o de rodar o projeto no Netbeans.

#### Login
Nome de usu�rio: remoteok

Senha: remoteok



## Exemplo de uso

Filtre os jobs utilizando a caixa de texto no topo e apertando enter.



## Hist�rico de lan�amentos

* 1.0.0
    * Lan�amento da vers�o final.


## Equipe de desenvolvimento

```sh
Andr� Rosas Agostinho
Marcelo Ricardo Lacroix
Marco Aur�lio Floriani
```



[java-image]: https://img.shields.io/badge/java-8.0-orange.svg
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/seunome/seuprojeto/wiki