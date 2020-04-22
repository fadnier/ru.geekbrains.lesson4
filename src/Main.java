
import java.util.Random;
import java.util.Scanner;

public class Main {
    static final int SIZE = 5;
    static final int DOTS_TO_WIN = 4;

    static final char DOT_X = 'X';
    static final char DOT_O = 'O';
    static final char DOT_EMPTY = '.';

    static char[][] map;

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) {
        initMap();
        printMap();

        while (true) {
            humanTurn();
            printMap();
            if(checkWin(DOT_X)){
                System.out.println("Ты Супер победитель!");
                break;
            }

            if(isFull()){
                System.out.println("Ничья...");
                break;
            }

            aiTurn();
            printMap();
            if(checkWin(DOT_O)){
                System.out.println("ИИ нынче очень развито, компьютер победил!");
                break;
            }
            if(isFull()){
                System.out.println("Ничья...");
                break;
            }
        }
    }

    public static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    public static void printMap() {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.printf("%s ", map[i][j]);
            }
            System.out.println();
        }
    }

    public static void humanTurn() {
        int x;
        int y;

        do {
            System.out.println("input koord X Y");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(y, x));
        map[y][x] = DOT_X;

    }

    public static boolean isCellValid(int y, int x) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) {
            return false;
        }
        return map[y][x] == DOT_EMPTY;
    }

    public static void aiTurn() {
        int x;
        int y;

        int[] searchWin = aiSearchWin(DOT_O);
        int[] searchBlock = aiSearchWin(DOT_X);
        int[] searchPosition = aiSearchPosition();
        if(searchWin[0]==1) {
            y = searchWin[1];
            x = searchWin[2];
        } else
        if(searchBlock[0]==1) {
            y = searchBlock[1];
            x = searchBlock[2];
        } else
        if(searchPosition[0]>=1) {
            y = searchPosition[1];
            x = searchPosition[2];
        } else
            do {
                y = random.nextInt(SIZE);
                x = random.nextInt(SIZE);
            } while (!isCellValid(y, x));
        map[y][x] = DOT_O;

    }

    public  static int[] aiSearchPosition() {
        int[][] mapPosition = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                mapPosition[i][j] = 0;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                //Проверка 1 победной линии
                if((i+1-DOTS_TO_WIN) >= 0 && (j-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i-k][j+k] == DOT_X) break;
                        if ((k+1) == DOTS_TO_WIN) {
                            for (int l = 0; l < DOTS_TO_WIN; l++) {
                                if(map[i-l][j+l] == DOT_EMPTY) {
                                    mapPosition[i-l][j+l] ++;
                                }
                            }
                        }
                    }
                }
                //Проверка второй победной линии
                if((j-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i][j+k] == DOT_X) break;
                        if ((k+1) == DOTS_TO_WIN) {
                            for (int l = 0; l < DOTS_TO_WIN; l++) {
                                if(map[i][j+l] == DOT_EMPTY){
                                    mapPosition[i][j+l] ++;
                                }
                            }
                        }
                    }
                }
                //Проверка третьей победной линии
                if((i-1+DOTS_TO_WIN) < SIZE && (j-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i+k][j+k] == DOT_X) break;
                        if ((k+1) == DOTS_TO_WIN) {
                            for (int l = 0; l < DOTS_TO_WIN; l++) {
                                if(map[i+l][j+l] == DOT_EMPTY) {
                                    mapPosition[i+l][j+l] ++;
                                }
                            }
                        }
                    }
                }
                //Проверка четвертой победной линии
                if((i-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i+k][j] == DOT_X) break;
                        if ((k+1) == DOTS_TO_WIN) {
                            for (int l = 0; l < DOTS_TO_WIN; l++) {
                                if(map[i+l][j] == DOT_EMPTY) {
                                    mapPosition[i+l][j] ++;
                                }
                            }
                        }
                    }
                }
            }
        }

        int[] searchPosition = new int[]{0,0,0};

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (mapPosition[i][j] > searchPosition[0]) {
                    searchPosition = new int[]{mapPosition[i][j],i,j};
                }
            }
        }

        return searchPosition;
    }

    public static int[] aiSearchWin(char c) {
        int[] coordBlock = new int[]{0,0,0};
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                //Проверка 1 победной линии
                if((i+1-DOTS_TO_WIN) >= 0 && (j-1+DOTS_TO_WIN) < SIZE) {
                    int skip = 0;
                    int cordSkipX = 0;
                    int cordSkipY = 0;
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i-k][j+k] != c && (skip == 1 || map[i-k][j+k] != DOT_EMPTY)) {
                            break;
                        } else {
                            if(map[i-k][j+k] == DOT_EMPTY) {
                                skip++;
                                cordSkipX = i - k;
                                cordSkipY = j + k;
                            }
                        }
                        if ((k+1) == DOTS_TO_WIN && skip == 1) {
                            return coordBlock = new int[]{1,cordSkipX,cordSkipY};
                        }
                    }
                }
                //Проверка второй победной линии
                if((j-1+DOTS_TO_WIN) < SIZE) {
                    int skip = 0;
                    int cordSkipX = 0;
                    int cordSkipY = 0;
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i][j+k] != c && (skip == 1 || map[i][j+k] != DOT_EMPTY)) {
                            break;
                        } else {
                            if(map[i][j+k] == DOT_EMPTY) {
                                skip++;
                                cordSkipX = i;
                                cordSkipY = j+k;
                            }
                        }
                        if ((k+1) == DOTS_TO_WIN && skip==1) {

                            return coordBlock = new int[]{1,cordSkipX,cordSkipY};
                        }
                    }
                }
                //Проверка третьей победной линии
                if((i-1+DOTS_TO_WIN) < SIZE && (j-1+DOTS_TO_WIN) < SIZE) {
                    int skip = 0;
                    int cordSkipX = 0;
                    int cordSkipY = 0;
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i+k][j+k] != c && (skip == 1 || map[i+k][j+k] != DOT_EMPTY)) {
                            break;
                        } else {
                            if(map[i+k][j+k] == DOT_EMPTY) {
                                skip++;
                                cordSkipX = i + k;
                                cordSkipY = j + k;
                            }
                        }
                        if ((k+1) == DOTS_TO_WIN && skip==1) {
                            return coordBlock = new int[]{1,cordSkipX,cordSkipY};
                        }
                    }
                }
                //Проверка четвертой победной линии
                if((i-1+DOTS_TO_WIN) < SIZE) {
                    int skip = 0;
                    int cordSkipX = 0;
                    int cordSkipY = 0;
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i+k][j] != c && (skip == 1 || map[i+k][j] != DOT_EMPTY)) {
                            break;
                        } else {
                            if(map[i+k][j] == DOT_EMPTY) {
                                skip++;
                                cordSkipX = i + k;
                                cordSkipY = j;
                            }
                        }
                        if ((k+1) == DOTS_TO_WIN && skip==1) {
                            return coordBlock = new int[]{1,cordSkipX,cordSkipY};
                        }
                    }
                }
            }
        }
        return coordBlock;
    }

    public static boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkWin(char c){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                //Проверка 1 победной линии
                if((i+1-DOTS_TO_WIN) >= 0 && (j-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i-k][j+k] != c) break;
                        if ((k+1) == DOTS_TO_WIN) return true;
                    }
                }
                //Проверка второй победной линии
                if((j-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i][j+k] != c) break;
                        if ((k+1) == DOTS_TO_WIN) return true;
                    }
                }
                //Проверка третьей победной линии
                if((i-1+DOTS_TO_WIN) < SIZE && (j-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i+k][j+k] != c) break;
                        if ((k+1) == DOTS_TO_WIN) return true;
                    }
                }
                //Проверка четвертой победной линии
                if((i-1+DOTS_TO_WIN) < SIZE) {
                    for (int k = 0; k < DOTS_TO_WIN; k++) {
                        if(map[i+k][j] != c) break;
                        if ((k+1) == DOTS_TO_WIN) return true;
                    }
                }
            }
        }

        return false;
    }

}
