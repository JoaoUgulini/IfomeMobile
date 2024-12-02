<?php

$servidor = 'localhost';
$banco      = 'bdifome';
$usuario  = 'root';
$senha    = '';

$conexao = mysqli_connect($servidor, $usuario, $senha, $banco);

$json = file_get_contents('php://input');
$obj = json_decode($json);

$texto1=$obj->nome;	
$texto2=$obj->senha;
$texto3=$obj->endereco;
$texto4=$obj->fone;
$texto5=$obj->cidade;

$sql ="INSERT INTO usuario(nome, senha, endereco, fone,cidade) VALUES ('".$texto1."','".$texto2."','".$texto3."','".$texto4."','".$texto5."')";
mysqli_query($conexao,$sql);

?>

