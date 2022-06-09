import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Solution {
    private static short masSize = 4;   // Размер массива
    private static int[] vesRace;       // Массив для хранения стоимости перемещения рассы

    /*
    Возвращает минимальные затраты существа на перемещения из стартовой позиции (левый верхний угол) в конечную (правый нижний угол).
    Аргументы метода:
    > field - строка, длинной 16 символов, описывающая клетки игрового поля 4*4;
    > race - строка, содержащая расу существа.
    */
    public static int getResult(String field, String race){
        init(race);

//        Для удобства преобразования буквенного вида поля к числовому
        HashMap<Character, Integer> tempHashMap = new HashMap<>();
        tempHashMap.put('S',vesRace[0]);
        tempHashMap.put('W',vesRace[1]);
        tempHashMap.put('T',vesRace[2]);
        tempHashMap.put('P',vesRace[3]);

//        Массив для преобразования строки в поле 4х4
        char bufChar[][] = new char[masSize][masSize];
//        Массив, хранящий поле 4х4 в виде чисел
        int charToInt[][] = new int[masSize][masSize];
//        Массив для хранения веса перемещения
        int resMas[][] = new int[masSize][masSize];

//        Преобразует строку field в массив 4х4
        for(int i = 0; i < 16; i += masSize){
            bufChar[i/masSize] = field.substring(i, i+masSize).toCharArray();
        };

        for(int i = 0; i < masSize; i++){
            for(int j = 0; j < masSize; j++){
                charToInt[i][j] = tempHashMap.get(bufChar[i][j]);
            }
        }

        for(int i = 0; i < masSize; i++) {
            int cellLeft;   // вес перемещения при движении слева
            int cellUp;     // вес перемещения при движении сверху
            for (int j = 0; j < masSize; j++) {
                cellLeft = cellUp = 0;  // Обнуляем переменные перед проверкой ячейки.

                // Если слева есть ячейка
                if(j != 0) cellLeft = charToInt[i][j] + resMas[i][j-1];

                // Если справа есть ячейка
                if(i != 0) cellUp = charToInt[i][j] + resMas[i-1][j];

                if(cellLeft == 0) {     // Слева нет ячейки. Записываем результат при движении сверху
                    resMas[i][j] = cellUp;
                }
                else if(cellUp == 0){   // Сверху нет ячейки. Записываем результат при движении слева
                    resMas[i][j] = cellLeft;
                }
                else {  // Записывает наименьший (из двух направлений) вес перемещения
                    resMas[i][j] = cellLeft > cellUp ? cellUp : cellLeft;
                }
            }
        }
        return resMas[3][3];    // Возвращает значение правой нижней ячейки
    }

//    Функция чтения данных из файла
    private static void init(String race){
        vesRace = new int[masSize];
        int tempInt = 0;    // Промещуточная перменная для хранения преобразованной строки в число
        String vesString = "";  // Полученная строка с весом перемещения

        // Используем try-with-resources, чтобы открытый поток BufferedReader был закрыт автоматически
        try (BufferedReader reader = new BufferedReader(new FileReader("raceAndVes.txt"))) {
            String line;
            // Цикл выполняется, пока не будет найдено совпадение по рассе или не будет достигнут конец файла
            while ((line = reader.readLine()) != null && vesString.length() == 0) {
                // Если есть совпадение по рассе, то читается стоимость перемещения существа
                if(line.equals(race)) {
                    vesString = reader.readLine();
                }
            }
        }
        catch (IOException e) {
            System.out.println("Файл не найден.");
            e.printStackTrace();
        }

        // Преобразуем строку в число и помещаем вес в массив
        tempInt = Integer.parseInt(vesString);
        for(int j = 3; j > -1; j--){
            vesRace[j] = tempInt % 10;
            tempInt /= 10;
        };
    }
}

/*
    Перемещение происходит по следующим правилам:
    > существо может перемещаться только вверх/вниз и влево/вправо;
    > стоимость перемещения по клеткам зависит от типа клетки и от расы существа в соответствии с таблицей ниже.

    Таблица стоимости перемещения существ по различным типам клеток:
                | Болото | Вода | Кусты | Равнина |
    -----------------------------------------------
    Человек     |    5   |   2  |   3   |    1    |
    Болотник    |    2   |   2  |   5   |    2    |
    Леший       |    3   |   3  |   2   |    2    |

    Кодирование типа игровых клеток:
    > Болото - "S"
    > Вода - "W"
    > Кусты - "T"
    > Равнина - "P"

    Кодирование существ:
    > Человек - "Human"
    > Болотник - "Swamper"
    > Леший - "Woodman"

    Стоимость перемещения по стартовой клетке не входит в затраты, а по конечной - входит.
*/