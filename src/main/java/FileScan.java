import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileScan {
    private volatile String result;
    private String fileName;
    private BufferedReader reader;
    private int min;
    private int max;
    private float median;
    private float mean;
    private int[] numbers;
    private List<Integer> ascendingSequence;
    private List<Integer> descendingSequence;

    public FileScan(String fileName) {
        this.fileName = fileName;
    }

    public void scan() {
        try {
            read();
            if(result != null)
                return;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Min: ").append(min).append("\n");
            stringBuilder.append("Max: ").append(max).append("\n");
            stringBuilder.append("Average: ").append(mean).append("\n");
            stringBuilder.append("Median: ").append(median).append("\n");
            stringBuilder.append("Ascending Sequence: ").append(ascendingSequence).append("\n");
            stringBuilder.append("Descending Sequence: ").append(descendingSequence).append("\n");
            result = stringBuilder.toString();

        } catch (IOException | InterruptedException e) {
            result = "Some exception\n" + e.getMessage();
            throw new RuntimeException(e);
        }
    }


    private void read() throws IOException, InterruptedException {
        long sum = 0;
        int counter = 0;
        String str;
        ascendingSequence = new ArrayList<>();
        descendingSequence = new ArrayList<>();
        List<Integer> currentAscendingSequence = new ArrayList<>();
        List<Integer> currentDescendingSequence = new ArrayList<>();


        reader = new BufferedReader(new FileReader(fileName));
        while (reader.readLine() != null)
            counter++;
        reader.close();

        if (counter == 0) {
            result = "File is empty";
            return;
        }

        numbers = new int[counter];
        reader = new BufferedReader(new FileReader(fileName));
        str = reader.readLine();
        int currentNumber;
        try {
            currentNumber = Integer.parseInt(str);
        }catch (NumberFormatException e){
            result = "File contains some not numbers elements (letters, empty strings etc.)";
            return;
        }
        sum += currentNumber;
        numbers[0] = currentNumber;

        for (int i = 1; i < counter; i++) {
            str = reader.readLine();
            try {
                currentNumber = Integer.parseInt(str);
            }catch (NumberFormatException e){
                result = "File contains some not numbers elements (letters, empty strings etc.)";
                return;
            }
            sum += currentNumber;
            numbers[i] = currentNumber;

            if (numbers[i] >= numbers[i - 1]){
                currentAscendingSequence.add(numbers[i - 1]);
                if(i == counter - 1)
                    currentAscendingSequence.add(numbers[i]);
            }
            else if (currentAscendingSequence.size() > ascendingSequence.size()) {
                currentAscendingSequence.add(numbers[i - 1]);
                if(i == counter - 1)
                    currentAscendingSequence.add(numbers[i]);
                ascendingSequence = currentAscendingSequence;
                currentAscendingSequence = new ArrayList<>();
            } else currentAscendingSequence = new ArrayList<>();

            if (numbers[i] <= numbers[i - 1]) {
                currentDescendingSequence.add(numbers[i - 1]);
                if(i == counter - 1)
                    currentDescendingSequence.add(numbers[i]);
            }
            else if (currentDescendingSequence.size() > descendingSequence.size()) {
                currentDescendingSequence.add(numbers[i - 1]);
                if(i == counter - 1)
                    currentDescendingSequence.add(numbers[i]);
                descendingSequence = currentDescendingSequence;
                currentDescendingSequence = new ArrayList<>();
            } else currentDescendingSequence = new ArrayList<>();

        }
        reader.close();

        if(ascendingSequence.size() < currentAscendingSequence.size())
            ascendingSequence = currentAscendingSequence;
        if(descendingSequence.size() < currentDescendingSequence.size())
            descendingSequence = currentDescendingSequence;

        Arrays.sort(numbers);
        mean = (float) sum / counter;

        if (counter % 2 == 0) {
            median = (float) (numbers[counter / 2] + numbers[(counter / 2) - 1]) / 2;
        } else {
            median = numbers[counter / 2];
        }

        min = numbers[0];
        max = numbers[counter - 1];
    }

    public String getResult() {
        return result;
    }
}
