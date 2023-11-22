package com.example.calculadora;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
public class Controlador {
    @FXML
    private TextField pantalla;
    private String operacionPendiente = "";
    private String caracteres ="/X-+%.";
    private boolean errorCalculo = false;

    public Controlador() { // Establecemos en el Controlador el motor de Cálculo que vamos a usar para las operaciones.
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js"); // En este caso JavaScript.
    }

    public void error (String mensaje){ //Metodo para mostrar mensaje de error.
        pantalla.setText(mensaje);
        errorCalculo = true;
    }

    public void borrarTodo(MouseEvent mouseEvent) {
        pantalla.clear();
    } // Metodo para borrar la pantalla.
    public void borrarUltimo(){ // Metodo para borrar el ultimo digito mostrado.
        if (!pantalla.getText().isEmpty()){
            pantalla.setText(pantalla.getText(0, pantalla.getLength()-1));
            errorCalculo = false;
        }
    }

    public void clickDigito(MouseEvent mouseEvent){ // Metodo para mostrar el dígito pulsado por pantalla
        Button boton = (Button) mouseEvent.getSource();
        pantalla.appendText(boton.getText());
        errorCalculo = false;
    }

    public void cambiarCaracter(MouseEvent mouseEvent){ // Metodo para intercambiar caracteres.
        Button button = (Button) mouseEvent.getSource();
        boolean operadorExiste = false;
        if (!pantalla.getText().isEmpty()){
            char ultimoCaracter = pantalla.getText().charAt(pantalla.getLength()-1);
            operadorExiste = caracteres.indexOf(ultimoCaracter) != -1;
            }
            if (operadorExiste){
                borrarUltimo();
                pantalla.appendText(button.getText());
            } else {
                pantalla.appendText(button.getText());
            }
            errorCalculo = false;
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
                    operacionPendiente = String.valueOf(resultadoFinal);
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