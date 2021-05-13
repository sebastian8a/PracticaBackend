<?php

require '../Database.php';

class Alumnos {

   function __construct() {

   }

   public static function insertAlumno($nombre, $image_name) {

      try{

         $pdo = Database::getInstance() -> getDb();
         $pdo -> beginTransaction();

         $comando = "INSERT INTO TALUMNOS (Nombre, Archivo) VALUES (?,?) ";

         $sentencia = $pdo -> prepare($comando);
         $sentencia -> execute(array($nombre, $image_name));

         $pdo -> commit();

         return 1;

      }catch(Exception $e){
         $pdo -> rollBack();
         return $e -> getMessage();
      }
   }

   public static function getAlumnos() {

      $consulta = "SELECT * FROM TALUMNOS";
      try {
          $comando = Database::getInstance() -> getDb() -> prepare($consulta);
          $comando -> execute();

          return $comando -> fetchAll(PDO::FETCH_ASSOC);

      } catch (PDOException $e) {
          return false;
      }
   }


}

?>