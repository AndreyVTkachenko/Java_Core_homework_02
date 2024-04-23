/*
Переработать метод проверки победы, логика проверки победы должна работать для поля 5х5 и
количества фишек 4. Очень желательно не делать это просто набором условий для каждой из
возможных ситуаций! Используйте вспомогательные методы, используйте циклы!

Доработать искусственный интеллект, чтобы он мог блокировать ходы игрока.
 */

import java.util.Random;
import java.util.Scanner;

public class Main {


    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static int WIN_COUNT;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = ' ';
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static char[][] field;


    public static void main(String[] args) {

        System.out.println("Введите размеры поля через пробел:");
        fieldSizeX = scanner.nextInt();
        fieldSizeY = scanner.nextInt();
        System.out.println("Введите количество фишек для победы:");
        WIN_COUNT = scanner.nextInt();
        while (WIN_COUNT > fieldSizeX || WIN_COUNT > fieldSizeY) {
            System.out.println("Ошибка: количество фишек для победы не может превышать размерности поля. Повторите ввод.");
            WIN_COUNT = scanner.nextInt();
        }

        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurnV2();
                printField();
                if (checkState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }

    }

    /**
     * Инициализация объектов игры
     */
    static void initialize() {
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    static void printField() {
        System.out.print("+");
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print("-" + (x + 1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }


    /**
     * Ход игрока (человека)
     */
    static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("Введите координаты хода X и Y\n(до " + fieldSizeX + " по X и до " + fieldSizeY + " по Y) через пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }


    /**
     * Проверка, является ли ячейка игрового поля пустой
     *
     * @param x координаты ячейки в ряду
     * @param y координаты ячейки в столбце
     * @return true если ячейка пуста
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка валидности координат хода
     *
     * @param x координаты ячейки в ряду
     * @param y координаты ячейки в столбце
     * @return true если не вышли за пределы поля
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }


    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn() {
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }


    /**
     * Улучшенный ход игрока (компьютера)
     */
    static void aiTurnV2() {

        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) {
                    field[x][y] = DOT_HUMAN;
                    if (checkWinV2(DOT_HUMAN, WIN_COUNT)) {
                        field[x][y] = DOT_AI;
                        return;
                    }
                    field[x][y] = DOT_EMPTY;
                }
            }
        }

        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) {
                    field[x][y] = DOT_HUMAN;
                    if (checkWinV2(DOT_HUMAN, WIN_COUNT - 1)) {
                        field[x][y] = DOT_AI;
                        return;
                    }
                    field[x][y] = DOT_EMPTY;
                }
            }
        }

        aiTurn();
    }


    /**
     * Проверка на ничью
     *
     * @return true если заполнены все ячейки поля
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {

            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * TODO: Переработать в рамках домашней работы
     * Метод проверки победы
     *
     * @param dot фишка игрока
     * @return true если нашлось совпадение с выигрышным вариантом
     */
    static boolean checkWin(char dot) {
        // Проверка победы по горизонталям
        if (field[0][0] == dot && field[0][1] == dot && field[0][2] == dot) return true;
        if (field[1][0] == dot && field[1][1] == dot && field[1][2] == dot) return true;
        if (field[2][0] == dot && field[2][1] == dot && field[2][2] == dot) return true;

        // Проверка победы по вертикалям
        if (field[0][0] == dot && field[1][0] == dot && field[2][0] == dot) return true;
        if (field[0][1] == dot && field[1][1] == dot && field[2][1] == dot) return true;
        if (field[0][2] == dot && field[1][2] == dot && field[2][2] == dot) return true;

        // Проверка победы по диагоналям
        if (field[0][0] == dot && field[1][1] == dot && field[2][2] == dot) return true;
        if (field[0][2] == dot && field[1][1] == dot && field[2][0] == dot) return true;

        return false;
    }

    /**
     * Улучшенный метод проверки победы
     *
     * @param dot фишка игрока
     * @param WIN_COUNT количество фишек для победы
     * @return true если нашлось совпадение с выигрышным вариантом
     */
    static boolean checkWinV2(char dot, int WIN_COUNT) {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (check1(x, y, dot, WIN_COUNT) || check2(x, y, dot, WIN_COUNT) ||
                        check3(x, y, dot, WIN_COUNT) || check4(x, y, dot, WIN_COUNT)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean check1(int x, int y, char dot, int WIN_COUNT) {
        int count = 0;
        for (int i = 0; i < WIN_COUNT; i++) {
            if (x + i < fieldSizeX && field[x + i][y] == dot) {
                count++;
            } else {
                break;
            }
        }
        return count == WIN_COUNT;
    }

    static boolean check2(int x, int y, char dot, int WIN_COUNT) {
        int count = 0;
        for (int i = 0; i < WIN_COUNT; i++) {
            if (y + i < fieldSizeY && field[x][y + i] == dot) {
                count++;
            } else {
                break;
            }
        }
        return count == WIN_COUNT;
    }

    static boolean check3(int x, int y, char dot, int WIN_COUNT) {
        int count = 0;
        for (int i = 0; i < WIN_COUNT; i++) {
            if (x + i < fieldSizeX && y + i < fieldSizeY && field[x + i][y + i] == dot) {
                count++;
            } else {
                break;
            }
        }
        return count == WIN_COUNT;
    }

    static boolean check4(int x, int y, char dot, int WIN_COUNT) {
        int count = 0;
        for (int i = 0; i < WIN_COUNT; i++) {
            if (x + i < fieldSizeX && y - i >= 0 && field[x + i][y - i] == dot) {
                count++;
            } else {
                break;
            }
        }
        return count == WIN_COUNT;
    }


    /**
     * Проверка состояния игры
     *
     * @param dot фишка игрока
     * @param s победный слоган
     * @return true если есть пустые ячейки и никто из игроков не победил
     */
    static boolean checkState(char dot, String s) {
        if (checkWinV2(dot, WIN_COUNT)) {
            System.out.println(s);
            return true;
        } else if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }
}
