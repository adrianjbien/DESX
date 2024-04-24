package com.example.desx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class View {
    private Operations operations = new Operations();
    private Converter converter = new Converter();
    private TextField firstKeyContent = new TextField();
    private TextField secondKeyContent = new TextField();
    private TextField thirdKeyContent = new TextField();
    private TextArea message = new TextArea();
    private TextField result = new TextField();
    private Primitives primitives = new Primitives();
    private DaoFile daoFile = new DaoFile();

    public VBox setKeyArea() {
        VBox vBox = new VBox();
        Label firstKeyText = new Label("First key");
        Label secondKeyText = new Label("Second key");
        Label thirdKeyText = new Label("Third key");
        Button generateKeyButton = new Button("Generate keys");


        generateKeyButton.setOnAction(e -> {
            // HEX
            firstKeyContent.setText(converter.binToHex(this.generateKeyButtonOnClick(false)).toLowerCase());
            secondKeyContent.setText(converter.binToHex(this.generateKeyButtonOnClick(false)).toLowerCase());
            thirdKeyContent.setText(converter.binToHex(this.generateKeyButtonOnClick(false)).toLowerCase());
        });

        vBox.getChildren().addAll(firstKeyText, firstKeyContent, secondKeyText, secondKeyContent,thirdKeyText, thirdKeyContent, generateKeyButton);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    public VBox setMessageArea() {
        VBox vBox = new VBox();
        Label messageText = new Label("Message to encipher");
        Label blank = new Label();
        HBox hBox = new HBox();
        Button cipher = new Button("Encode");
        Button decipher = new Button("Decode");

        cipher.setOnAction(e ->
                result.setText(converter.byteStringToHex(converter.convertByteToString(this.solve(true, converter.convertStringToByte(converter.asciiToBinaryString(message.getText())))))));

        decipher.setOnAction(e ->
                result.setText(converter.BytesToAscii(converter.convertByteToString(this.solve(false, converter.convertStringToByte(converter.hexToBin(message.getText().toUpperCase())))))));

        hBox.getChildren().addAll(cipher, decipher);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(blank, messageText, message, hBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    public VBox setResultArea() {
        VBox vBox = new VBox();
        Label resultText = new Label("Result");
        Label blank = new Label();

        vBox.getChildren().addAll(blank, resultText, result);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    public VBox setFileArea() {
        VBox vBox = new VBox();
        Label FileText = new Label("File to encipher");
        Label blank = new Label();
        Label blank1 = new Label();
        Label blank2 = new Label();
        Button encipher = new Button("Choose file to encipher");
        Button decipher = new Button("Choose file to decipher");

        encipher.setOnAction(e -> {
            String path = this.getFilePath();
            try {
                daoFile.writeFile(converter.convertBytesToFile(this.solve(true, converter.convertFileToBytes(daoFile.readFile(path)))), path);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        decipher.setOnAction(e -> {
            String path = this.getFilePath();
            try {
                daoFile.writeFile(converter.convertBytesToFile(this.solve(false, converter.convertFileToBytes(daoFile.readFile(path)))), path);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        vBox.getChildren().addAll(blank1, FileText, blank, encipher, blank2, decipher);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    private byte[] solve(boolean encoding, byte[] wholeMessage) {
        // HEX
        byte[] firstKey = converter.convertStringToByte(converter.hexToBin(firstKeyContent.getText().toUpperCase()));
        byte[] secondKey = converter.convertStringToByte(converter.hexToBin(secondKeyContent.getText().toUpperCase()));
        byte[] thirdKey = converter.convertStringToByte(converter.hexToBin(thirdKeyContent.getText().toUpperCase()));

        byte[][] blocks;
        StringBuilder temp = new StringBuilder();

        blocks = operations.divideDataIntoBlocks(wholeMessage);
        if (encoding) {
            for (int i = 0; i < blocks.length; i++) {
                wholeMessage = operations.des(true,converter,firstKey,secondKey,thirdKey,blocks[i],primitives);
                temp.append(converter.convertByteToString(wholeMessage));
            }
        } else {
            for (int i = 0; i < blocks.length; i++) {
                wholeMessage = operations.des(false,converter,firstKey,secondKey,thirdKey,blocks[i],primitives);
                temp.append(converter.convertByteToString(wholeMessage));
            }
        }
        return converter.convertStringToByte(temp.toString());
    }

    public String generateKeyButtonOnClick(boolean forDES) {
        byte[] key = operations.getKey(forDES);
        return converter.convertByteToString(key);
    }

    private String getFilePath() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        return file.getAbsolutePath();
    }
}