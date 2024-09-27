package bantads.airline.utils;

import org.springframework.stereotype.Component;

@Component
public class ValidateCPF {

    public boolean validateCPF(String num) {

        int[] vet = new int[11];
        int[] dvs = new int[2];
        int som, a, b, f, m, r;
        boolean aux = true;

        // convert cpf string to a numeric array:
        for (a = 0; a < 11; a++) {
            vet[a] = Character.getNumericValue(num.charAt(a));
        }
        // Verifies if all digits are the same:
        a = 0;
        do {
            a++;
        } while ((a < 10) && (vet[0] == vet[a]));

        if (a == 10) {
            aux = false; // all digits are the same
        } else {
            // calculates the two last digits from 'CPF'
            f = 8;
            m = 9;
            a = 0;
            do {
                som = 0;
                a++;
                f++;
                m++;
                for (b = 0; b < f; b++) {
                    som += vet[b] * (m - b);
                }
                r = som % 11;
                dvs[a - 1] = 11 - r;
                if (r < 2) {
                    dvs[a - 1] = 0;
                }
                if (vet[m - 1] != dvs[a - 1]) {
                    aux = false;
                }
            } while (a < 2 && aux);
        }
        return aux;
    }
}
