package com.example.calculadora;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javax.script.ScriptEngineManager;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
public class Controlador {
    @FXML
    private TextField pantalla;

    public Controlador() { // Establecemos en el Controlador el motor de Cálculo que vamos a usar para las operaciones.
        ScriptEngineManager manager = new ScriptEngineManager();
        manager.getEngineByName("js");
    }

    public void error (String mensaje){ //Metodo para mostrar mensaje de error.
        pantalla.setText(mensaje);
    }

    public void borrarTodo() {
        pantalla.clear();
    } // Metodo para borrar la pantalla.

    public void borrarUltimo(){ // Metodo para borrar el ultimo digito mostrado.
        if (!pantalla.getText().isEmpty()){
            pantalla.setText(pantalla.getText(0, pantalla.getLength()-1));
        }
    }

    public void clickDigito(MouseEvent mouseEvent){ // Metodo para mostrar el dígito pulsado por pantalla
        Button boton = (Button) mouseEvent.getSource();
        pantalla.appendText(boton.getText());
    }

    public void cambiarCaracter(MouseEvent mouseEvent){ // Metodo para intercambiar caracteres.
        Button button = (Button) mouseEvent.getSource();
        boolean operadorExiste = false;
        if (!pantalla.getText().isEmpty()){
            char ultimoCaracter = pantalla.getText().charAt(pantalla.getLength()-1);
            String caracteres = "/X-+%.";
            operadorExiste = caracteres.indexOf(ultimoCaracter) != -1;
            }
            if (operadorExiste){
                borrarUltimo();
                pantalla.appendText(button.getText());
            } else {
                pantalla.appendText(button.getText());
            }
    }

    public void resultado() { // Metodo para mostrar el resultado.
        String funcion = pantalla.getText().replace("X", "*");

        try {
            if (pantalla.getText().contains("/0")) {
                error("Error No se puede dividir entre 0");
                throw new ArithmeticException();
            } else if (pantalla.getText().isEmpty()) {
                throw new IllegalArgumentException();
            } else {
                Expression expression = new ExpressionBuilder(funcion).build();
                ValidationResult validation = expression.validate();
                if (!validation.isValid()){
                    error("Error");
                } else {
                    double resultadoFinal = expression.evaluate();
                    pantalla.setText(String.valueOf(resultadoFinal));
                }
            }
        } catch (ArithmeticException divisionCero) {
            error("No se puede Dividir entre cero");
        } catch (IllegalArgumentException sinNumeros) {
            error("Introduzca Numeros");
        }
    }
}