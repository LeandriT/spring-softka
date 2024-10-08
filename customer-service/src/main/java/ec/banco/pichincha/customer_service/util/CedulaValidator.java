package ec.banco.pichincha.customer_service.util;

public class CedulaValidator {

    public static boolean isValidCedula(String cedula) {
        // Validar longitud
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        try {
            int[] cedulaDigits = cedula.chars().map(Character::getNumericValue).toArray();

            // Validar el código de provincia (debe estar en el rango 01-24)
            int provinceCode = cedulaDigits[0] * 10 + cedulaDigits[1];
            if (provinceCode < 1 || provinceCode > 24) {
                return false;
            }

            // Calcular el dígito de verificación
            int[] weights = {2, 1};
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                int product = cedulaDigits[i] * weights[i % 2];
                sum += (product > 9) ? (product - 9) : product;
            }

            int expectedDigit = (10 - (sum % 10)) % 10;

            // Comparar el dígito de verificación calculado con el proporcionado
            return expectedDigit == cedulaDigits[9];
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
