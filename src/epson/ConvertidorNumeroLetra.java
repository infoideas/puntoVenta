// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 23/05/2019 10:35:51 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ConvertidorNumeroLetra.java

package epson;

import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ConvertidorNumeroLetra
{

    public ConvertidorNumeroLetra()
    {
    }

    public String convertNumberToLetter(String number)
        throws NumberFormatException
    {
        return convertNumberToLetter(Double.parseDouble(number));
    }

    public String convertNumberToLetter(double doubleNumber)
        throws NumberFormatException
    {
        StringBuilder converted = new StringBuilder();
        String patternThreeDecimalPoints = "#.00";
        DecimalFormat format = new DecimalFormat(patternThreeDecimalPoints);
        format.setRoundingMode(RoundingMode.UP);
        String formatedDouble = format.format(doubleNumber);
        if(doubleNumber > 999999999D)
            throw new NumberFormatException("El numero es mayor de 999'999.999, no es posible convertirlo");
        if(doubleNumber < 0.0D)
            throw new NumberFormatException("El numero debe ser positivo");
        String splitNumber[] = String.valueOf(doubleNumber).replace('.', '#').split("#");
        int millon = Integer.parseInt((new StringBuilder()).append(String.valueOf(getDigitAt(splitNumber[0], 8))).append(String.valueOf(getDigitAt(splitNumber[0], 7))).append(String.valueOf(getDigitAt(splitNumber[0], 6))).toString());
        if(millon == 1)
            converted.append("**UN MILLON ");
        else
        if(millon > 1)
            converted.append("**").append(convertNumber(String.valueOf(millon))).append("MILLONES ");
        System.out.print("Ok3");
        int miles = Integer.parseInt((new StringBuilder()).append(String.valueOf(getDigitAt(splitNumber[0], 5))).append(String.valueOf(getDigitAt(splitNumber[0], 4))).append(String.valueOf(getDigitAt(splitNumber[0], 3))).toString());
        if(millon >= 1)
        {
            if(miles == 1)
                converted.append(convertNumber(String.valueOf(miles))).append("MIL ");
            else
            if(miles > 1)
                converted.append(convertNumber(String.valueOf(miles))).append("MIL ");
        } else
        {
            if(miles == 1)
                converted.append("**UN MIL ");
            if(miles > 1)
                converted.append("**").append(convertNumber(String.valueOf(miles))).append("MIL ");
        }
        int cientos = Integer.parseInt((new StringBuilder()).append(String.valueOf(getDigitAt(splitNumber[0], 2))).append(String.valueOf(getDigitAt(splitNumber[0], 1))).append(String.valueOf(getDigitAt(splitNumber[0], 0))).toString());
        if(miles >= 1 || millon >= 1)
        {
            if(cientos >= 1)
                converted.append(convertNumber(String.valueOf(cientos)));
        } else
        {
            if(cientos == 1)
                converted.append("**UN ");
            if(cientos > 1)
                converted.append("**").append(convertNumber(String.valueOf(cientos)));
        }
        System.out.print((new StringBuilder()).append("Valor5: ").append(formatedDouble).toString());
        if(millon + miles + cientos == 0)
            converted.append("**CERO ");
        String valor = splitNumber[1];
        if(valor.length() == 1)
            converted.append(splitNumber[1]).append("0").append("/100 ");
        else
            converted.append(splitNumber[1]).append("/100 ");
        converted.append(" PESOS**");
        return converted.toString();
    }

    private String convertNumber(String number)
    {
        if(number.length() > 3)
            throw new NumberFormatException("La longitud maxima debe ser 3 digitos");
        if(number.equals("100"))
            return "CIEN ";
        StringBuilder output = new StringBuilder();
        if(getDigitAt(number, 2) != 0)
            output.append(CENTENAS[getDigitAt(number, 2) - 1]);
        int k = Integer.parseInt((new StringBuilder()).append(String.valueOf(getDigitAt(number, 1))).append(String.valueOf(getDigitAt(number, 0))).toString());
        if(k <= 20)
            output.append(UNIDADES[k]);
        else
        if(k > 30 && getDigitAt(number, 0) != 0)
            output.append(DECENAS[getDigitAt(number, 1) - 2]).append("Y ").append(UNIDADES[getDigitAt(number, 0)]);
        else
            output.append(DECENAS[getDigitAt(number, 1) - 2]).append(UNIDADES[getDigitAt(number, 0)]);
        return output.toString();
    }

    private int getDigitAt(String origin, int position)
    {
        if(origin.length() > position && position >= 0)
            return origin.charAt(origin.length() - position - 1) - 48;
        else
            return 0;
    }

    private static final String UNIDADES[] = {
        "", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", 
        "DIEZ ", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE", 
        "VEINTE"
    };
    private static final String DECENAS[] = {
        "VENTI", "TREINTA ", "CUARENTA ", "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ", "CIEN "
    };
    private static final String CENTENAS[] = {
        "CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "
    };

}
