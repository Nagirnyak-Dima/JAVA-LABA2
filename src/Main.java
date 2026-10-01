import java.util.Scanner;
// ЗАВДАННЯ 1 та 2
class Line {
    private double a;
    private double b;
    private double c;

    public Line(double a, double b, double c) {
        if (a == 0 && b == 0) {
            throw new IllegalArgumentException("Коефіцієнти 'a' та 'b' не можуть бути одночасно рівними 0!");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean containsPoint(double x, double y) {
        return Math.abs(a * x + b * y + c) < 1e-9;
    }

    public double[] getIntersection(Line other) {
        double delta = this.a * other.b - other.a * this.b;

        if (Math.abs(delta) < 1e-9) {
            return null;
        }

        double x = (this.b * other.c - other.b * this.c) / delta;
        double y = (other.a * this.c - this.a * other.c) / delta;

        return new double[]{x, y};
    }

    @Override
    public String toString() {
        return String.format("%.2fx + %.2fy + %.2f = 0", a, b, c);
    }
}

// ЗАВДАННЯ 3
class Drib {
    private int numerator;
    private int denominator;

    public Drib(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Знаменник не може дорівнювати 0!");
        }
        this.numerator = numerator;
        this.denominator = denominator;
        simplify();
    }

    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private void simplify() {
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        int commonDivider = gcd(numerator, denominator);
        if (commonDivider > 1) {
            this.numerator /= commonDivider;
            this.denominator /= commonDivider;
        }
    }

    public Drib add(Drib other) {
        int num = this.numerator * other.denominator + other.numerator * this.denominator;
        int den = this.denominator * other.denominator;
        return new Drib(num, den);
    }

    public Drib subtract(Drib other) {
        int num = this.numerator * other.denominator - other.numerator * this.denominator;
        int den = this.denominator * other.denominator;
        return new Drib(num, den);
    }

    public Drib multiply(Drib other) {
        int num = this.numerator * other.numerator;
        int den = this.denominator * other.denominator;
        return new Drib(num, den);
    }

    public Drib divide(Drib other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("Ділення на нуль неможливе!");
        }
        int num = this.numerator * other.denominator;
        int den = this.denominator * other.numerator;
        return new Drib(num, den);
    }

    @Override
    public String toString() {
        if (denominator == 1) return String.valueOf(numerator);
        return numerator + "/" + denominator;
    }
}
// МЕНЮ
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nМЕНЮ ПРОГРАМИ");
            System.out.println("1.(Завдання 1 та 2)");
            System.out.println("2.(Завдання 3)");
            System.out.println("0.Вихід");
            System.out.print("Виберіть пункт: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> runLineTask();
                case "2" -> runDribTask();
                case "0" -> {
                    System.out.println("Завершення роботи. До побачення!");
                    return;
                }
                default -> System.out.println("Некоректний вибір! Спробуйте ще раз.");
            }
        }
    }

    private static void runLineTask() {
        System.out.println("\n--- ЗАВДАННЯ 1 / 2: ПРЯМА НА ПЛОЩИНІ ---");
        try {
            System.out.println("Введіть коефіцієнти першої прямої (a1, b1, c1):");
            System.out.print("a1 = "); double a1 = Double.parseDouble(scanner.nextLine());
            System.out.print("b1 = "); double b1 = Double.parseDouble(scanner.nextLine());
            System.out.print("c1 = "); double c1 = Double.parseDouble(scanner.nextLine());
            Line line1 = new Line(a1, b1, c1);

            System.out.println("\nПеревірка належності точки першій прямій (" + line1 + "):");
            System.out.print("x = "); double px = Double.parseDouble(scanner.nextLine());
            System.out.print("y = "); double py = Double.parseDouble(scanner.nextLine());

            if (line1.containsPoint(px, py)) {
                System.out.printf("Точка (%.2f, %.2f) НАЛЕЖИТЬ прямій %s%n", px, py, line1);
            } else {
                System.out.printf("Точка (%.2f, %.2f) НЕ належить прямій %s%n", px, py, line1);
            }

            System.out.println("\nВведіть коефіцієнти другої прямої (a2, b2, c2):");
            System.out.print("a2 = "); double a2 = Double.parseDouble(scanner.nextLine());
            System.out.print("b2 = "); double b2 = Double.parseDouble(scanner.nextLine());
            System.out.print("c2 = "); double c2 = Double.parseDouble(scanner.nextLine());
            Line line2 = new Line(a2, b2, c2);

            double[] intersection = line1.getIntersection(line2);
            if (intersection != null) {
                System.out.printf("Точка перетину прямих:%n  Пряма 1: %s%n  Пряма 2: %s%n  Координати: (x = %.4f, y = %.4f)%n",
                        line1, line2, intersection[0], intersection[1]);
            } else {
                System.out.println("Прямі є паралельними або збігаються, точки перетину немає!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Помилка: введено некоректне числове значення!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void runDribTask() {
        System.out.println("\n--- ЗАВДАННЯ 3: ОПЕРАЦІЇ З ДРОБАМИ ---");
        try {
            System.out.println("Введіть 4 цілих числа через пробіл (чисельник1 знаменник1 чисельник2 знаменник2):");
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.trim().split("\\s+");

            if (parts.length != 4) {
                System.out.println("Помилка: потрібно ввести рівно 4 цілих числа!");
                return;
            }

            int num1 = Integer.parseInt(parts[0]);
            int den1 = Integer.parseInt(parts[1]);
            int num2 = Integer.parseInt(parts[2]);
            int den2 = Integer.parseInt(parts[3]);

            Drib d1 = new Drib(num1, den1);
            Drib d2 = new Drib(num2, den2);

            System.out.println("\nВведені (скорочені) дроби:");
            System.out.println("Дріб 1: " + d1);
            System.out.println("Дріб 2: " + d2);

            System.out.println("\nРезультати арифметичних операцій:");
            System.out.println("Додавання  (" + d1 + " + " + d2 + ") = " + d1.add(d2));
            System.out.println("Віднімання (" + d1 + " - " + d2 + ") = " + d1.subtract(d2));
            System.out.println("Множення   (" + d1 + " * " + d2 + ") = " + d1.multiply(d2));

            try {
                System.out.println("Ділення    (" + d1 + " / " + d2 + ") = " + d1.divide(d2));
            } catch (ArithmeticException e) {
                System.out.println("Ділення    (" + d1 + " / " + d2 + ") = Помилка: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("Помилка: всі значення повинні бути цілими числами!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}