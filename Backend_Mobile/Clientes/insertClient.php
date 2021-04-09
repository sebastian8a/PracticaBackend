<?php

require 'Clientes.php';

if (isset($_SERVER['HTTP_ORIGIN'])) {
    header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
    header('Access-Control-Allow-Credentials: true');
    header('Access-Control-Max-Age: 86400');   
}

if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
 
    if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_METHOD']))
        header("Access-Control-Allow-Methods: GET, POST, OPTIONS");         

    if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']))
        header("Access-Control-Allow-Headers: {$_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']}");

    exit(0);
}
    
if ($_SERVER['REQUEST_METHOD'] == 'POST') {

   header('Content-type:application/json;charset=utf-8');

   $body = json_decode(file_get_contents("php://input"), true);

   $nombre = $body['Nombre'];
   $apellido = $body['Apellido'];
   $edad = $body['Edad'];
   $correo = $body['Correo'];

   $retorno = Clientes::insertClient($nombre, $apellido, $edad, $correo);

   if ($retorno == 1) {
      print json_encode(
         array(
               'estado' => 1,
               'mensaje' => 'Creación exitosa')
      );
   } else {
      print json_encode(
         array(
               'estado' => 2,
               'mensaje' => $retorno)
      );
   }
}
