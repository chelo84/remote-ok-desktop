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

### Detalhes T�cnicos
#### Desenvolvido utilizando as seguintes principais tecnologias

![java-image]
 
![miglayout-image]
 
![lombok-image]
 
![unirest-image]


### Requisitos

* Java 8 *JDK 1.8*

* Git

* IDE (Netbeans)

### Netbeans

* Fa�a um clone do reposit�rio com o comando `git clone https://github.com/chelo84/remote-ok-desktop.git` 
 na pasta que desejar;
* Abra o projeto na IDE em Arquivo -> Abrir projeto... -> remote-ok-desktop -> Abrir projeto;
* Voc� ter� que adicionar as bibliotecas externas para poder rodar o projeto, para isso clique com o
bot�o direito no projeto e v� em propriedades -> Bibliotecas -> Adicionar JAR/Pasta -> v� at� a pasta
do projeto e entre na pasta Libs -> selecione todos os arquivos desta pasta e clique *Abrir* e ent�o clique em Ok;
* Pronto, est� tudo configurado, apenas aperte no bot�o de rodar o projeto no Netbeans.

##### Login
Nome de usu�rio: remoteok

Senha: remoteok



## Exemplo de uso

Ache os jobs que procura

Adicione jobs nos favoritos para que possa encontr�-los com mais facilidade mais tarde

Compartilhe os jobs por e-mail

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
[miglayout-image]: https://img.shields.io/badge/MigLayout-3.5.5-red.svg
[lombok-image]: https://img.shields.io/badge/Lombok--brightgreen.svg
[jackson-image]: https://img.shields.io/badge/Jackson-2.9.8-lightblue.svg
[unirest-image]: https://img.shields.io/badge/Unirest-1.4.9-blue.svg