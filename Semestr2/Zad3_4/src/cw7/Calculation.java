package cw7;

public class Calculation {
    public static double operationResult(double first, double second, char which){

        switch (which){
            case '+':
                return  first +  second;
            case '-':
                return  first -  second;
            case '*':
                return  first *  second;
            case '/':
                return  first /  second;
        }
        return 0;
    }
}
