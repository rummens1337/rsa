package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EncryptController {

    @FXML private TextField txtNumber;
    @FXML private TextField txtP;
    @FXML private TextField txtQ;
    private int p = 0;
    private int q = 0;
    private int n = 0;

    /**
     * Switch the screen to step 1.
     */
    @FXML
    public void switchStep1() {
        try {
            int number = Integer.parseInt(txtNumber.getText());
            long start = System. currentTimeMillis();
            int[] primes = findPrimePair(number);
            long end = System. currentTimeMillis();
            long executionTime = end - start;

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("encrypt_step1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();
            EncryptController controller = fxmlLoader.getController();
            controller.setP(primes[0]);
            controller.setQ(primes[1]);
            controller.setN(number);
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
            int phiN = (this.p - 1) * (this.q - 1);
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
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();
            stage.setTitle("Encryption - Step 3");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window - Step 3.", e);
        }
    }

    // Generate all prime numbers less than n.
    static boolean SieveOfEratosthenes(int n, boolean isPrime[]) {
        // Initialize all entries of boolean
        // array as true. A value in isPrime[i]
        // will finally be false if i is Not a
        // prime, else true bool isPrime[n+1];
        isPrime[0] = isPrime[1] = false;
        for (int i = 2; i <= n; i++)
            isPrime[i] = true;

        for (int p = 2; p * p <= n; p++) {
            // If isPrime[p] is not changed,
            // then it is a prime
            if (isPrime[p]) {
                // Update all multiples of p
                for (int i = p * p; i <= n; i += p)
                    isPrime[i] = false;
            }
        }
        return false;
    }

    // Prints a prime pair with given sum
    static int[] findPrimePair(int n) {
        // Generating primes using Sieve
        boolean[] isPrime = new boolean[n + 1];
        int[] result = new int[2];
        SieveOfEratosthenes(n, isPrime);

        // Traversing all numbers to find first
        // pair
        for (int i = 0; i < n; i++) {
            if (isPrime[i] && isPrime[n - i]) {
                result[0] = i;
                result[1] = (n - i);
                return result;
            }
        }

        return result;
    }

    int recursiveGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        if (a < b) {
            return recursiveGCD(b, a);
        }
        return recursiveGCD(b, a % b);
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
