import java.util.Random;
import java.util.Scanner;

public class PerfectSolution {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static int WIN_COUNT;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = ' ';
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static char[][] field;


    public static void main(String[] args) {

        System.out.println("Введите размеры поля через пробел:");
        fieldSizeX = SCANNER.nextInt();
        fieldSizeY = SCANNER.nextInt();
        System.out.println("Введите количество фишек для победы:");
        WIN_COUNT = SCANNER.nextInt();
        while (WIN_COUNT > fieldSizeX || WIN_COUNT > fieldSizeY) {
            System.out.println("Ошибка: количество фишек для победы не может превышать размерности поля. Повторите ввод.");
            WIN_COUNT = SCANNER.nextInt();
        }

        while (true) {
            initField();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (gameChecks(DOT_HUMAN, WIN_COUNT, "Вы победили!")) break;
                aiTurn();
                printField();
                if (gameChecks(DOT_AI, WIN_COUNT, "Победил компьютер!")) break;
            }
            System.out.println("Сыграем ещё раз?");
            if (!SCANNER.next().equalsIgnoreCase("Y"))
                break;
        }

    }

    /**
     * Инициализация объектов игры
     */
    static void initField() {
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
    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++)
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        System.out.println();

        for (int i = 0; i < fieldSizeY; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSizeX; j++)
                System.out.print(field[i][j] + "|");
            System.out.println();
        }

        for (int i = 0; i <= fieldSizeX * 2 + 1; i++)
            System.out.print("-");
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
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }


    /**
     * Проверка, является ли ячейка игрового поля пустой
     * @param x координата ячейки
     * @param y координата ячейки
     * @return результат проверки
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка валидности ячейки
     * @param x координата ячейки
     * @param y координата ячейки
     * @return результат проверки
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Ход компьютера
     */
    private static void aiTurn() {

        // Побеждает ли компьютер в текущем ходе (при выигрышной комбинации WIN_COUNT)?
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY){
                    field[y][x] = DOT_AI;
                    if (checkWin(DOT_AI, WIN_COUNT))
                        return;
                    else
                        field[y][x] = DOT_EMPTY;
                }
            }
        }

        // Побеждает ли игрок на текущий момент при выигрышной комбинации WIN_COUNT - 1?
        boolean f = checkWin(DOT_HUMAN, WIN_COUNT - 1);
        // Теперь, снова пройдем по всем свободным ячейкам игрового поля, если игрок уже побеждает при
        // выигрышной комбинации WIN_COUNT - 1, компьютер попытается закрыть последнюю выигрышную ячейку.
        // Если игрок НЕ побеждает при выигрышной комбинации WIN_COUNT - 1, компьютер будет действовать
        // на опережение, попытается заранее "подпортить" человеку выигрышную комбинацию.
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY){
                    field[y][x] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN, WIN_COUNT - (f ? 0 : 1))) {
                        field[y][x] = DOT_AI;
                        return;
                    }
                    else
                        field[y][x] = DOT_EMPTY;
                }
            }
        }

        // Ни человек, ни компьютер не выигрывают, значит, компьютер ставит фишку случайным образом
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));
        field[y][x] = DOT_AI;
    }


    /**
     * Проверка на ничью
     * @return результат проверки
     */
    private static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * Проверка победы игрока
     * @param dot фишка игрока (человек или компьютер)
     * @param winCount кол-во фишек для победы
     * @return
     */
    static boolean checkWin(char dot, int winCount) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == dot)
                    if (checkXY(y, x, 1, winCount) ||
                            checkXY(y, x, -1, winCount) ||
                            checkDiagonal(y, x, -1, winCount) ||
                            checkDiagonal(y, x, 1, winCount))
                        return true;
            }
        }
        return false;
    }

    /**
     * Проверка выигрыша игрока (человек или компьютер) горизонтали + вправо/вертикали + вниз
     * @param x начальная координата фишки
     * @param y начальная координата фишки
     * @param dir направление проверки (-1 => горизонтали + вправо/ 1 => вертикали + вниз)
     * @param win выигрышная комбинация
     * @return результат проверки
     */
    static boolean checkXY(int x, int y, int dir, int win) {
        char c = field[x][y]; // получим текущую фишку (игрок или компьютер)
        // Пройдем по всем ячейкам от начальной координаты (например 2,3) по горизонтали вправо и по вертикали вниз
        // (в зависимости от значения параметра dir)
        /*  +-1-2-3-4-5-
            1|.|.|.|.|.|
            2|.|.|.|.|.|
            3|.|X|X|X|X|
            4|.|X|.|.|.|
            5|.|X|.|.|.|
            ------------
        */
        for (int i = 1; i < win; i++)
            if (dir > 0 && (!isCellValid(x + i, y) || c != field[x + i][y])) return false;
            else if (dir < 0 && (!isCellValid(x, y + i) || c != field[x][y + i])) return false;
        return true;
    }

    /**
     * Проверка выигрыша игрока (человек или компьютер) по диагонали вверх + вправо/вниз + вправо
     * @param x начальная координата фишки
     * @param y начальная координата фишки
     * @param dir направление проверки (-1 => вверх + вправо/ 1 => вниз + вправо)
     * @param win кол-во фишек для победы
     * @return результат проверки
     */
    static boolean checkDiagonal(int x, int y, int dir, int win) {
        char c = field[x][y]; // получим текущую фишку (игрок или компьютер)
        // Пройдем по всем ячейкам от начальной координаты (например 3,3) по диагонали вверх и по диагонали вниз
        // (в зависимости от значения параметра dir)
        /*  +-1-2-3-4-5-
            1|.|.|.|.|X|
            2|.|.|.|X|.|
            3|.|.|X|.|.|
            4|.|.|.|X|.|
            5|.|.|.|.|X|
            ------------
        */
        for (int i = 1; i < win; i++)
            if (!isCellValid(x + i, y + i*dir) || c != field[x + i][y + i*dir]) return false;
        return true;
    }


    /**
     * Метод проверки состояния игры
     * @param dot фишка игрока (человек/компьютер)
     * @param win выигрышная комбинация
     * @param s победное сообщение
     * @return результат проверки
     */
    private static boolean gameChecks(char dot, int win, String s) {
        if (checkWin(dot, win)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }
}
