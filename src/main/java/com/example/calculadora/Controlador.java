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
    private char[] caracteres = {'/','X','-','+','%','.'};
    private boolean errorCalculo = false;
    public Controlador() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
    }
    public void error (String mensaje){
        pantalla.setText(mensaje);
        errorCalculo = true;
    }
    public void borrarTodo(MouseEvent mouseEvent) {
        pantalla.clear();
    }
    public void borrarUltimo(){
        if (!pantalla.getText().isEmpty()){
            pantalla.setText(pantalla.getText(0, pantalla.getLength()-1));
            errorCalculo = false;
        }
    }
    public void clickDigito(MouseEvent mouseEvent){
        Button boton = (Button) mouseEvent.getSource();
        pantalla.appendText(boton.getText());
        errorCalculo = false;
    }
    public void cambiarCaracter(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        boolean operadorExiste = false;
        if (!pantalla.getText().isEmpty()){
            char ultimoCaracter = pantalla.getText().charAt(pantalla.getLength()-1);
            for (char c : caracteres) {
                if (c == ultimoCaracter){
                    operadorExiste = true;
                }
            }
            if (operadorExiste){
                borrarUltimo();
                pantalla.appendText(button.getText());
            } else {
                pantalla.appendText(button.getText());
            }
            errorCalculo = false;
        }
    }
    public void resultado() {
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