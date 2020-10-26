package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EncryptController {

    @FXML private TextField txtNumber;
    @FXML private TextField txtP;
    @FXML private TextField txtQ;
    @FXML private Text txtE;
    @FXML private TextField txtM;
    @FXML private Text txtEncoded;

    private long p = 0;
    private long q = 0;
    private long n = 0;
    private int e = 0;

    /**
     * Switch the screen to step 1.
     */
    @FXML
    public void switchStep1() {
        try {
            long number = Long.parseLong(txtNumber.getText());
            long start = System. currentTimeMillis();
            long[] primes = findPrimePair(BigInteger.valueOf(number));
            long end = System. currentTimeMillis();
            long executionTime = end - start;

            if (primes == null)  return;

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("encrypt_step1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();

            EncryptController controller = fxmlLoader.getController();
            controller.setP(primes[0]);
            controller.setQ(primes[1]);
            controller.setN(number);
            controller.txtP.setText(String.valueOf(controller.getP()));
            controller.txtQ.setText(String.valueOf(controller.getQ()));

            stage.setTitle("Encryption - Step 1");
            stage.setScene(scene);
            stage.show();

            System.out.println("P is " + primes[0]);
            System.out.println("Q is " + primes[1]);
            System.out.println("Amount of time busy finding p and q: " + executionTime);
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
            System.out.println(this.p + " " + this.q);
            long phiN = (this.p - 1) * (this.q - 1);
            int e  = 0;

            // Calculate E
            for (int i = 2; i < phiN; i++) {
                if(recursiveGCD(i, phiN) == 1 && recursiveGCD(i, this.n) == 1){
                    e = i;
                    System.out.println("e is " + e);
                }
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("encrypt_step2.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();

            EncryptController controller = fxmlLoader.getController();
            controller.setN(n);
            controller.setE(e);
            controller.txtE.setText(String.format("e is: %d", e));

            stage.setTitle("Encryption - Step 2");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window - Step 2.", e);
        }
    }

    /**
     * Switch the screen to step 3.
     */
    @FXML
    public void switchStep3() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("encrypt_step3.fxml"));

            String m = txtM.getText();

            // Encrypt m, then join encrypted values by ',' and show it as string
            List<BigInteger> encrypted = encrypt(m);
            String c = encrypted
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            txtEncoded.setText("Message after encryption is:\n " + c);

            // TODO: Move txtEncoded to step 3 and display `c` there
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();
//            stage.setTitle("Encryption - Step 3");
//            stage.setScene(scene);
//            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window - Step 3.", e);
        }
    }

    public List<BigInteger> encrypt(String m) {
        // Convert each character of m to its index in the alphabet
        List<BigInteger> mAsBigInts = Arrays
                .stream(m.split(""))
                .map(String::toLowerCase)
                .map(c -> BigInteger.valueOf((int) c.charAt(0)))
                .collect(Collectors.toList());

        // Encrypt each value of mAsBigInts like: value -> ((value ^ e) % n)
        return mAsBigInts
                .stream()
                .map(value -> value.pow(e).mod(BigInteger.valueOf(n)))
                .collect(Collectors.toList());
    }

    /**
     * Attempts to find two distinct primes whose product is equal to the given value n
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

    long recursiveGCD(long a, long b) {
        if (b == 0) {
            return a;
        }
        if (a < b) {
            return recursiveGCD(b, a);
        }
        return recursiveGCD(b, a % b);
    }

    public long getP() {
        return p;
    }

    public void setP(long p) {
        this.p = p;
    }

    public long getQ() {
        return q;
    }

    public void setQ(long q) {
        this.q = q;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }
}
