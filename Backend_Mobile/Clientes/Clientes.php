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

   public static function getClients() {

      $consulta = "SELECT * FROM TCLIENTES";
      try {
          $comando = Database::getInstance() -> getDb() -> prepare($consulta);
          $comando -> execute();

          return $comando -> fetchAll(PDO::FETCH_ASSOC);

      } catch (PDOException $e) {
          return false;
      }
   }

   public static function updateClient($id, $nombre, $apellido, $edad, $correo) {

      try{

          $pdo = Database::getInstance() -> getDb();
          $pdo -> beginTransaction();

          $comando = "UPDATE TCLIENTES SET Nombre = ?, Apellido = ?, Edad = ?, Correo = ? WHERE id = ? ";

          $sentencia = $pdo -> prepare($comando);
          $sentencia -> execute(array($nombre, $apellido, $edad, $correo, $id));

          $pdo -> commit();

          return 1;

      }catch(Exception $e){
          $pdo -> rollBack();
          return $e -> getMessage();
      }
  }

  public static function deleteClient($id){

      try{

          $pdo = Database::getInstance() -> getDb();

          $pdo -> beginTransaction();

          $statement = $pdo -> prepare("DELETE FROM TCLIENTES WHERE Id = ?");
          $statement -> execute(array($id));

          $pdo -> commit();

          return 1;

      }catch(Exception $e){
          $pdo -> rollBack();
          return $e -> getMessage();
      }
  }


}

?>