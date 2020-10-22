package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DecryptController {

    @FXML private TextField txtN;
    @FXML private TextField txtE;
    @FXML private TextField txtC;
    @FXML private Text txtD;

    private int n = 0;
    private int e = 0;
    private int d = 0;

    /**
     * Switch the screen to step 1.
     */
    @FXML
    public void switchStep1() {
        try {
            int n = Integer.parseInt(txtN.getText());
            int e = Integer.parseInt(txtE.getText());

            System.out.printf("n: %d, e: %d\n", n, e);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("decrypt_step1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();

            DecryptController controller = fxmlLoader.getController();

            int d = calculateD(n, e);
            controller.setN(n);
            controller.setE(e);
            controller.setD(d);

            controller.txtD.setText(String.format("d is: %d", d));

            stage.setTitle("Decryption - Step 1");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window - Step 1.", e);
        }
    }

    /**
     * Switch the screen to step 2.
     */
    @FXML
    public void switchStep2() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("decrypt_step2.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();

            List<Integer> c = Arrays
                    .stream(txtC.getText().split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            System.out.println("C: " + Arrays.toString(c.toArray()));

            String decrypted = decrypt(c, d, n);
            System.out.println("Decrypted: " + decrypted);

            // TODO: When decrypt() works correctly, show decrypted message

//            stage.setTitle("Decryption - Step 2");
//            stage.setScene(scene);
//            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window - Step 2.", e);
        }
    }

    private int calculateD(int n, int e) {
        // TODO: Get p and q properly
        int p = 5;
        int q = 13;
        int phiN = (p-1) * (q-1);

        // Find a value for which ((<value> * e) % phiN) == 1
        for (int i = 1;; i++) {
            System.out.printf("[i=%d]: %d\n", i, (e * i) % phiN);

            if ((e * i) % phiN == 1) {
                return i;
            }
        }
    }

    private String decrypt(List<Integer> c, int d, int n) {
        // TODO: Does not decrypt properly yet
        return c.stream()
                .map(value -> (int) (Math.pow(value, d) % n))
                .map(value -> Character.toString((char) value.intValue()))
                .collect(Collectors.joining(","));
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }
}
