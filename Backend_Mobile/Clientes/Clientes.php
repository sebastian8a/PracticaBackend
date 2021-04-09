<?php

require '../Database.php';

class Clientes {

   function __construct() {

   }

   public static function insertClient($nombre, $apellido, $edad, $correo) {

      try{

         $pdo = Database::getInstance() -> getDb();
         $pdo -> beginTransaction();

         $comando = "INSERT INTO TCLIENTES (Nombre, Apellido, Edad, Correo) VALUES (?,?,?,?) ";

         $sentencia = $pdo -> prepare($comando);
         $sentencia -> execute(array($nombre, $apellido, $edad, $correo));

         $pdo -> commit();

         return 1;

      }catch(Exception $e){
         $pdo -> rollBack();
         return $e -> getMessage();
      }
   }


}

?>