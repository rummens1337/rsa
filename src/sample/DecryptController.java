package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DecryptController {

    @FXML
    private TextField txtN;
    @FXML
    private TextField txtE;
    @FXML
    private TextField txtC;
    @FXML
    private TextField txtMsg;
    @FXML
    private Text txtD;

    private long n = 0;
    private long e = 0;
    private long d = 0;

    /**
     * Handles all logic for screen 'Step 1.' and switches to next screen.
     */
    @FXML
    public void switchStep1() {
        try {
            // Retrieve input from user interface.
            this.n = Long.parseLong(txtN.getText());
            this.e = Long.parseLong(txtE.getText());

            // Set all scene variables and get resources.
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("decrypt_step1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();
            DecryptController controller = fxmlLoader.getController();

            // Set variables for next screen
            calculateD(this.n, this.e);
            controller.setN(this.n);
            controller.setE(this.e);
            controller.setD(this.d);
            controller.txtD.setText(String.format("d is: %d", this.d));

            // Switch to next screen.
            stage.setTitle("Decryption - Step 1");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window - Step 1.", e);
        }
    }

    /**
     * Handles all logic for screen 'Step 2.' and switches to next screen.
     */
    @FXML
    public void switchStep2() {
        try {
            // Set all scene variables and get resources.
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("decrypt_step2.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();

            // Decrypt the cypher from input of the user interface and print to screen.
            String[] cypherArray = txtC.getText().split(",");
            String decrypted = decrypt(cypherArray);
            DecryptController controller = fxmlLoader.getController();
            controller.txtMsg.setText(decrypted);

            // Switch to next screen.
            stage.setTitle("Decryption - Step 2");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window - Step 2.", e);
        }
    }

    /**
     * Calculate decryption key based on n and e.
     *
     * @param n The number n (product of two primes).
     * @param e The encryption key e
     */
    private void calculateD(long n, long e) {
        BigInteger E = new BigInteger(String.valueOf(e));
        long[] primes = findPrimePair(BigInteger.valueOf(n));
        long phiN = (primes[0] - 1) * (primes[1] - 1);
        BigInteger PHIN = new BigInteger(String.valueOf(phiN));

        this.d = Long.parseLong(E.modInverse(PHIN).toString());
    }

    /**
     * Decipher input string array, based on given d and n.
     *
     * @param cypherArray The array of cyphers (characters)
     * @return The deciphered string.
     */
    private String decrypt(String[] cypherArray) {
        StringBuilder sb = new StringBuilder();

        for (String cypher : cypherArray) {

            BigInteger D = new BigInteger(String.valueOf(this.d));
            BigInteger N = new BigInteger(String.valueOf(this.n));
            BigInteger C = new BigInteger(String.valueOf(cypher));
            BigInteger character = C.modPow(D, N);

            sb.append((char) character.intValue());
        }

        return sb.toString();
    }

    /**
     * Attempts to find two distinct primes whose product is equal to the given value n
     *
     * @param n Desired product
     * @return Array containing two primes whose product is equal to n, or null if no such primes could be found
     */
    static long[] findPrimePair(BigInteger n) {
        for (BigInteger p = BigInteger.valueOf(2L); (p.compareTo(n) < 0); p = p.nextProbablePrime()) {
            if (n.mod(p).equals(BigInteger.ZERO)) {
                BigInteger q = n.divide(p);

                if (!p.equals(q) && q.isProbablePrime(100)) {
                    return new long[]{p.longValue(), q.longValue()};
                }
            }
        }

        System.out.println("No two distinct primes could be found with a product of " + n);

        return null;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public long getE() {
        return e;
    }

    public void setE(long e) {
        this.e = e;
    }

    public long getD() {
        return d;
    }

    public void setD(long d) {
        this.d = d;
    }
}
